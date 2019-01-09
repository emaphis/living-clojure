(ns wonderland.snake
  (:import (java.awt Color Dimension)
           (javax.swing JPanel JFrame Timer JOptionPane)
           (java.awt.event ActionListener KeyListener KeyEvent))
  #_(:use wonderland.import-static))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; The Functional Model

(def width 75)
(def height 50)
(def point-size 10)
(def turn-millis 75)
(def win-length 5)
(def dirs {KeyEvent/VK_LEFT [-1 0]
           KeyEvent/VK_RIGHT [ 1 0]
           KeyEvent/VK_UP [ 0 -1]
           KeyEvent/VK_DOWN [ 0 1]})

(defn add-points [& pts]
  (vec (apply map + pts)))

(defn point-to-screen-rec [pt]
  (map #(* point-size %)
       [(pt 0) (pt 1) 1 1]))

;; (add-points [10 10] [-1 0])  ; move left by one
;; => [9 10]


(defn create-apple []
  {:location [(rand-int width) (rand-int height)]
   :color (Color. 210 50 90)
   :type :apple})

(defn create-snake []
  {:body (list [1 1])
   :dir [1 0]
   :type :snake
   :color (Color. 15 160 70)})


(defn move [{:keys [body dir] :as snake} & grow]
  (assoc snake :body (cons (add-points (first body) dir)
                           (if grow body (butlast body)))))

;; (move (create-snake))
;; {:body ([2 1]),
;;  :dir [1 0],
;;  :type :snake,
;;  :color #object[java.awt.Color 0x5e587058 "java.awt.Color[r=15,g=160,b=70]"]}

;; (move (create-snake) :grow)
;; {:body ([2 1] [1 1]),
;;  :dir [1 0],
;;  :type :snake,
;;  :color #object[java.awt.Color 0x20aea399 "java.awt.Color[r=15,g=160,b=70]"]}


(defn win? [{body :body}]
  (>= (count body) win-length))

;; (win? {:body [[1 1]]})
;; => false
;; (win? {:body [[1 1] [1 2] [1 3] [1 4] [1 5]]})
;; => true

(defn head-overlaps-body? [{[head & body] :body}]
  (contains? (set body) head))

(def lose? head-overlaps-body?)

;; (lose? {:body [[1 1] [1 2] [1 3]]})
;; => false
;; (lose? {:body [[1 1] [1 2] [1 1]]})
;; => true

(defn eats? [{[snake-head] :body} {apple :location}]
  (= snake-head apple))

;; (eats? {:body [[1 1] [1 2]]} {:location [2 2]})
;; => false
;; (eats? {:body [[2 2] [1 2]]} {:location [2 2]})
;; => true

(defn turn [snake newdir]
  (assoc snake :dir newdir))

;; (turn (create-snake) [0 -1])
;; {:body ([1 1]),
;;  :dir [0 -1],
;;  :type :snake,
;;  :color #object[java.awt.Color 0x30a7d0ec "java.awt.Color[r=15,g=160,b=70]"]}


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Mutable Model using STM

(defn reset-game [snake apple]
  (dosync (ref-set apple (create-apple))
          (ref-set snake (create-snake)))
  nil)


(comment ;; test reset
  (def test-snake (ref nil))
  (def test-apple (ref nil))

  (reset-game test-snake test-apple)

  @test-snake
  @test-apple
  )


(defn update-direction [snake newdir]
  (when newdir (dosync (alter snake turn newdir))))


(defn update-positions [snake apple]
  (dosync
   (if (eats? @snake @apple)
     (do (ref-set apple (create-apple))
         (alter snake move :grow))
     (alter snake move)))
  nil)

(comment ;; test
  (reset-game test-snake test-apple)

  ;; @test-snake
  ;; @test-apple

  (dosync (alter test-apple assoc :location [1 1]))
  ;; => {:location [1 1], :color #object[java.awt.Color 0x2de98407 "java.awt.Color[r=210,g=50,b=90]"], :type :apple}

  (update-positions test-snake test-apple)
  ;; => nil

  (:body @test-snake)
  ;; => ([2 1] [1 1])

  )
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; The Snake GUI

(defn fill-point [g pt color]
  (let [[x y width height] (point-to-screen-rec pt)]
    (.setColor g color)
    (.fillRect g x y width height)))

(defmulti paint (fn [g object & _] (:type object)))

(defmethod paint :apple [g {:keys [location color]}]
  (fill-point g location color))

(defmethod paint :snake [g {:keys [body color]}]
  (doseq [point body]
    (fill-point g point color)))


(defn game-panel [frame snake apple]
  (proxy [JPanel ActionListener KeyListener] []
    (paintComponent [g]
      (proxy-super paintComponent g)
      (paint g @snake)
      (paint g @apple))
    (actionPerformed [e]
      (update-positions snake apple)
      (when (lose? @snake)
        (reset-game snake apple)
        (JOptionPane/showMessageDialog frame "You lose!"))
      (when (win? @snake)
        (reset-game snake apple)
        (JOptionPane/showMessageDialog frame "You win!"))
      (.repaint this))
    (keyPressed [e]
      (update-direction snake (dirs (.getKeyCode e))))
    (getPreferredSize []
      (Dimension. (* (inc width) point-size)
                  (* (inc height) point-size)))
    (keyReleased [e])
    (keyTyped [e])))


(defn game []
  (let [snake (ref (create-snake))
        apple (ref (create-apple))
        frame (JFrame. "Snake")
        panel (game-panel frame snake apple)
        timer (Timer. turn-millis panel)]
    (doto panel
      (.setFocusable true)
      (.addKeyListener panel))
    (doto frame
      (.add panel)
      (.pack)
      (.setVisible true))
    (.start timer)
    [snake, apple, timer]))
