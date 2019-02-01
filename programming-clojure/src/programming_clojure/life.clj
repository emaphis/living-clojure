(ns programming-clojure.life
  (:require [clojure.repl :as r]
            [clojure.string :as str]
            [clojure.pprint :as p]))



(defn empty-board
  "Creates a rectangular empty board of the specified width
  and height."
  [w h]
  (vec (repeat w (vec (repeat h nil)))))

(defn populate
  "Turns :on each of the cells specified as [x,y] coordinates"
  [board living-cells]
  (reduce (fn [board coordinates]
            (assoc-in board coordinates :on))
          board
          living-cells))

(def glider (populate (empty-board 6 6) #{[2 0] [2 1] [2 2] [1 2] [0 1]}))

;; `indexed-step` takes a board's state and returns a successor.

(defn neighbours
  [[x y]]
  (for [dx [-1 0 1] dy [-1 0 1] :when (not= 0 dx dy)]
    [(+ dx x) (+ dy y)]))

(defn count-neighbors
  [board loc]
  (count (filter #(get-in board %) (neighbours loc))))

(defn indexed-step
  "Yields the next state of the board, using indices to determine neighbours
   liveness, etc."
  [board]
  (let [w (count board)
        h (count (first board))]
    (loop [new-board board x 0 y 0]
      (cond
        (>= x w) new-board
        (>= y h) (recur new-board (inc x) 0)
        :else
        (let [new-liveness
              (case (count-neighbors board [x y])
                2 (get-in board [x y])
                3 :on
                nil)]
          (recur (assoc-in new-board [x y] new-liveness) x (inc y)))))))

#_(-> (iterate indexed-step glider) (nth 8) p/pprint)

;; get rid of manual iteration. Replace each `loop` by a reduce over range.
(defn indexed-step2
  [board]
  (let [w (count board)
        h (count (first board))]
    (reduce
     (fn [new-board x]
       (reduce
        (fn [new-board y]
          (let [new-liveness
                (case (count-neighbors board [x y])
                  2 (get-in board [x y])
                  3 :on
                  nil)]
            (assoc-in new-board [x y] new-liveness)))
        new-board (range h)))
     board (range w))))

;; nested reductions
(defn indexed-step3
  [board]
  (let [w (count board)
        h (count (first board))]
    (reduce
     (fn [new-board [x y]]
       (let [new-liveness
             (case (count-neighbors board [x y])
               2 (get-in board [x y])
               3 :on
               nil)]
         (assoc-in new-board [x y] new-liveness)))
     board (for [x (range h) y (range w)] [x y]))))

;; sequences replace indices
;; ... but how?
;; on one dimension
(partition 3 1 (range 5))
;; => ((0 1 2) (1 2 3) (2 3 4))

;; add padding to the original collection
(partition 3 1 (concat [nil] (range 5) [nil]))
;; => ((nil 0 1) (0 1 2) (1 2 3) (2 3 4) (3 4 nil))

(defn window
  "Returns a lazy sequence of 3-item windows centered around each item of coll."
  [coll]
  (partition 3 1 (concat [nil] coll [nil])))

;; applying this to `n` rows gives us `n` triples of 3 rows and each triple of 3
;; rows can transform in a sequence of `m` triples - "transposition".

;; a sequence of triples of triples.

(defn cell-block
  "Create a sequence of 3x3 windows from a triple of 3 sequences"
  [[left mid right]]
  (window (map vector
               (or left (repeat nil)) mid (or right (repeat nil)))))


;; simply by adding an optional pad argument.
(defn window
  "Returns a lazy sequence of 3-item windows centered
   around each item of coll, padded as necessary with
   pad or nil"
  ([coll] (window nil coll))
  ([pad coll]
   (partition 3 1 (concat [pad] coll [pad]))))


(defn cell-block
  "Create a sequence of 3x3 windows from a triple of 3 sequences"
  [[left mid right]]
  (window (map vector left mid right)))


(defn liveness
  "Returns the liveness (nil or :on) of the center cell for the next step"
  [block]
  (let [[_ [_ center _] _] block]
    (case (- (count (filter #{:on} (apply concat block)))
             (if (= :on center) 1 0))
      2 center
      3 :on
      nil)))

;; replacement for indexed-step without indices
(defn- step-row
  "Yields the next state of the center row"
  [rows-triple]
  (vec (map liveness (cell-block rows-triple))))

(defn index-free-step
  "Yields the next state of the board."
  [board]
  (vec (map step-row (window (repeat nil) board))))

(= (nth (iterate indexed-step glider) 8)
   (nth (iterate index-free-step glider) 8))
;; => true


;;; Basic rules.
;; At each step in time, the following transitions occur:
;; * Any live cell with fewer than two live neighbours dies, as if caused by
;;   underpopulation.
;; * Any live cell with two or three live neighbours lives on to the
;;   next generation.
;; * Any live cell with more than three live neighbours dies, as if
;;   by overcrowding.
;; * Any dead cell with exactly three live neighbours becomes a live cell,
;;   as if by reproduction.

;; no rows, columns or indices.
;; but neighbourhood and living cells.

(defn step
  "Yields the next state of the world"
  [cells]
  (set (for [[loc n] (frequencies (mapcat neighbours cells))
             :when (or (= n 3) (and (= n 2) (cells loc)))]
         loc)))

;; test code
#_(->> (iterate step #{[2 0] [2 1] [2 2] [1 2] [0 1]})
       (drop 8)
       first
       (populate (empty-board 6 6))
       p/pprint)



(defn stepper
  "Returns a step function for Life-like cell automata.
   neighbours takes a location and return a sequential collection
   of locations. survive? and birth? are predicates on the number
   of living neighbours."
  [neighbours birth? survive?]
  (fn [cells]
    (set (for [[loc n] (frequencies (mapcat neighbours cells))
               :when (if (cells loc) (survive? n) (birth? n))]
           loc))))



(defn rect-stepper
  "Returns a step function for standard game of life on a (bounded) rectangular
   board of specified size."
  [w h]
  (stepper #(filter (fn [[i j]] (and (< -1 i w) (< -1 j h)))
                    (neighbours %)) #{2 3} #{3}))

(defn draw
  [w h step cells]
  (let [state (atom cells)
        run (atom true)
        listener (proxy [java.awt.event.WindowAdapter] []
                   (windowClosing [_] (reset! run false)))
        pane
        (doto (proxy [javax.swing.JPanel] []
                (paintComponent [^java.awt.Graphics g]
                  (let [g (doto ^java.awt.Graphics2D (.create g)
                            (.setColor java.awt.Color/BLACK)
                            (.fillRect 0 0 (* w 10) (* h 10))
                            (.setColor java.awt.Color/WHITE))]
                    (doseq [[x y] @state]
                      (.fillRect g (inc (* 10 x)) (inc (* 10 y)) 8 8)))))
          (.setPreferredSize (java.awt.Dimension. (* 10 w) (* 10 h))))]
    (doto (javax.swing.JFrame. "Quad Life")
      (.setContentPane pane)
      (.addWindowListener listener)
      .pack
      (.setVisible true))
    (future (while @run
              (Thread/sleep 80)
              (swap! state step)
              (.repaint pane)))))

(defn rect-demo []
  (draw 30 30 (rect-stepper 30 30)
        #{[15 15] [15 17] [16 16] [15 16]}))

(rect-demo)
