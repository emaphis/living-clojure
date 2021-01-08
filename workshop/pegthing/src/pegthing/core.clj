(ns pegthing.core
  (:require [clojure.set :as set]
            [clojure.string :as s])
  (:gen-class))

(declare successful-move prompt-move game-over query-rows)

;;; 1. Creating a new board

{1  {:pegged true, :connections {6 3, 4 2}},
 2  {:pegged true, :connections {9 5, 7 4}},
 3  {:pegged true, :connections {10 6, 8 5}},
 4  {:pegged true, :connections {13 8, 11 7, 6 5, 1 2}},
 5  {:pegged true, :connections {14 9, 12 8}},
 6  {:pegged true, :connections {15 10, 13 9, 4 5, 1 3}},
 7  {:pegged true, :connections {9 8, 2 4}},
 8  {:pegged true, :connections {10 9, 3 5}},
 9  {:pegged true, :connections {7 8, 2 5}},
 10 {:pegged true, :connections {8 9, 3 6}},
 11 {:pegged true, :connections {13 12, 4 7}},
 12 {:pegged true, :connections {14 13, 5 8}},
 13 {:pegged true, :connections {15 14, 11 12, 6 9, 4 8}},
 14 {:pegged true, :connections {12 13, 5 9}},
 15 {:pegged true, :connections {13 14, 6 10}},
 :rows 5}

(defn tri*
  "Generates lazy sequence of triangular numbers"
  ([] (tri* 0 1))
  ([sum n]
   (let [new-sum (+ sum n)]
     (cons new-sum (lazy-seq (tri* new-sum (inc n)))))))

;; the actual sequence
(def tri (tri*))

#_(take 8 tri)
;; => (1 3 6 10 15 21 28 36)


(defn triangular?
  "Is the number triangular? e.g. 1, 3, 6, 10, 15..."
  [n]
  (= n (last (take-while #(>= n %) tri))))

#_(triangular? 5)
;; => false
#_(triangular? 6)
;; => true


(defn row-tri
  "The triangular number at the end of row n"
  [n]
  (last (take n tri)))

#_(row-tri 1)
;; => 1
#_(row-tri 2)
;; => 3
#_(row-tri 3)
;; => 6


(defn row-num
  "Returns row number the position belongs to: pos 1 in tow 2,
  pos 2 and 3 in row 2, etc"
  [pos]
  (inc (count (take-while #(> pos %) tri))))

#_(row-num 1)
;; => 1
#_(row-num 5)
;; => 3


(defn connect
  "Form a mutual connection between two positons"
  [board max-pos pos neighbor destination]
  (if (<= destination max-pos)
    (reduce (fn [new-board [p1 p2]]
              (assoc-in new-board [p1 :connections p2] neighbor))
            board
            [[pos destination] [destination pos]])
    board))

#_(connect {} 15 1 2 4)
;; {1 {:connections {4 2}},
;;  4 {:connections {1 2}}}

#_(assoc-in {} [:cookie :monster :vocals] "Finntroll")
;; => {:cookie {:monster {:vocals "Finntroll"}}}
#_(get-in {:cookie {:monster {:vocals "Finntroll"}}} [:cookie :monster])
;; => {:vocals "Finntroll"}
#_(assoc-in {} [1 :connections 4] 2)
;; => {1 {:connections {4 2}}}


(defn connect-rigth
  [board max-pos pos]
  (let [neighbor (inc pos)
        destination (inc neighbor)]
    (if-not (or (triangular? neighbor) (triangular? pos))
      (connect board max-pos pos neighbor destination)
      board)))

(defn connect-down-left
[board max-pos pos]
(let [row (row-num pos)
      neighbor (+ row pos)
      destination (+ 1 row neighbor)]
  (connect board max-pos pos neighbor destination)))

(defn connect-down-right
  [board max-pos pos]
  (let [row (row-num pos)
        neighbor (+ 1 row pos)
        destination (+ 2 row neighbor)]
    (connect board max-pos pos neighbor destination)))

#_(connect-down-left {} 15 1)
;; {1 {:connections {4 2}},
;;  4 {:connections {1 2}}}
#_(connect-down-right {} 15 3)
;; {3 {:connections {10 6}},
;;  10 {:connections {3 6}}}


(defn add-pos
  "Pegs the position and performs connections"
  [board max-pos pos]
  (let [pegget-board (assoc-in board [pos :pegged] true)]
    (reduce (fn [new-board connections-creation-fn]
              (connections-creation-fn new-board max-pos pos))
            pegget-board
            [connect-rigth connect-down-left connect-down-right])))

#_(add-pos {} 15 1)
;; {1 {:pegged true, :connections {4 2, 6 3}},
;;  4 {:connections {1 2}},
;;  6 {:connections {1 3}}}


(defn new-board 
  "Creates a new board with the given number of rows"
  [rows]
  (let [initial-board {:rows rows}
        max-pos (row-tri rows)]
    (reduce (fn [board pos] (add-pos board max-pos pos))
            initial-board
            (range 1 (inc max-pos)))))


;;; 2. Returning a board with the result of the player’s move
;;; Moving Pegs.

(defn pegged?
  "Does the position have peg in it?"
  [board pos]
  (get-in board [pos :pegged]))

(defn remove-peg
  "Take the peg at given position out of the board"
  [board pos]
  (assoc-in board [pos :pegged] false))

(defn place-peg
  "Put a peg at given position out of the board"
  [board pos]
  (assoc-in board [pos :pegged] true))

(defn move-peg
  "Take a peg out of p1 and place it in p2"
  [board p1 p2]
  (place-peg (remove-peg board p1) p2))

(defn valid-moves
  "Return a map of all valid move for pos, where the key is the
  destination and the values is the jumped position"
  [board pos]
  (into {}
        (filter (fn [[destination jumped]]
                  (and (not (pegged? board destination))
                       (pegged? board jumped)))
                (get-in board [pos :connections]))))

(def my-board (assoc-in (new-board 5) [4 :pegged] false))
;; (valid-moves my-board 1) ; => {4 2}
;; (valid-moves my-board 6) ; => {4 5}
;; (valid-moves my-board 11) ; => {4 7}
;; (valid-moves my-board 5) ; => {}
;; (valid-moves my-board 8) ; => {}


(defn valid-move?
  "Return jumped position if the move form p1 to p2 is valid, nil
  otherwise"
  [board p1 p2]
  (get (valid-moves board p1) p2))

;; (valid-move? my-board 8 4) ; => nil
;; (valid-move? my-board 1 4) ; => 2


(defn make-move
  "Move peg from p1 to p2, removing jumped peg"
  [board p1 p2]
  (if-let [jumped (valid-move? board p1 p2)]
    (move-peg (remove-peg board jumped) p1 p2)))


(defn can-move?
  "Do any of the pegged positions have valid moves?"
  [board]
  (some (comp not-empty (partial valid-moves board))
        (map first (filter #(get (second %) :pegged) board))))


;;; 3. Representing a board textually
;;;    Rendering and Printing the Board

(def alpha-start 97)
(def alpha-end 123)
(def letters (map (comp str char) (range alpha-start alpha-end)))
(def pos-chars 3)

(def ansi-style
  {:red "[31m"
   :green "[32m"
   :blue "[34m"
   :reset "[0m"})

(defn ansi
  "Returns string that will apply ANSI style"
  [style]
  (str "\u001b" (style ansi-style)))

;; (defn ansi
;;   "Produce a string which will apply an ansi style"
;;   [style]
;;   (if (< (.indexOf (System/getProperty "os.name") "Windows") 0)
;;     (str \u001b (style ansi-styles))
;;     ""))

(defn colorize
  "Apply ansi color to text"
  [text color]
  (str (ansi color) text (ansi :reset)))


(defn render-pos
  [board pos]
  (str (nth letters (dec pos))
       (if (get-in board [pos :pegged])
         (colorize "0" :blue)
         (colorize "_" :red))))


(defn row-positions
  "Return all positions in the given row"
  [row-num]
  (range (inc (or (row-tri (dec row-num)) 0))
         (inc (row-tri row-num))))

(defn row-padding
  "String of spaces to add to the beggining of a row to center it"
  [row-num rows]
  (let [pad-length (/ (* (- rows row-num) pos-chars) 2)]
    (apply str (take pad-length (repeat " ")))))

(defn render-row
  [board row-num]
  (str (row-padding row-num (:rows board))
       (s/join " " (map (partial render-pos board)
                        (row-positions row-num)))))

(defn print-board
  [board]
  (doseq [row-num (range 1 (inc (:rows board)))]
    (println (render-row board row-num))))


;;; 4. Handling user interaction
;;;    Player Interaction


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))


