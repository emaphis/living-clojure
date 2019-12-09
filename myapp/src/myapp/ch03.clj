(ns myapp.ch03)

;;;; Functional Programming

;;; Immutability

;;; Referential Transparency


;;; First-class Functions

;; passing a function

(defn cap [s]
  (clojure.string/upper-case s))

(defn greeting [f s]
  (prn (f s)))

(greeting cap "hi there")

;; returning a function - `partial`

(defn sum [x y]
  (+ x y))

(def add-on-five (partial sum 5)) ; return a function

(add-on-five 10)
;; => 15

;;; `complement` `apply` `map` `reduce` `filter` `comp`

;; `complement`

((complement empty?) "") 
;; => false


;; `apply`

(apply str ["a" "b" "c" "d"])
;; => "abcd"

(apply + [1 2 3 4])
;; => 10


;; `map`

(map inc [1 2 3])
;; => (2 3 4)

(map
 (fn [[_ v]] (inc v))
 {:a 1 :b 2 :c 3})
;; => (2 3 4)

(map
 (fn [[k v]] [k (inc v)])
 {:a 1 :b 2 :c 3})
;; => ([:a 2] [:b 3] [:c 4])

(into {}
      (map
       (fn [[k v]] [k (inc v)])
       {:a 1 :b 2 :c 3}))
;; => {:a 2, :b 3, :c 4}


;; `reduce`

(reduce + [1 2 3 4])
;; => 10

(reduce
 (fn [acc elemet]
   (assoc acc (first elemet) (last elemet)))
 {}
 [[:a :b] [:c :d]])
;; => {:a :b, :c :d}


;; `filter`

(filter even? (range 10))
;; => (0 2 4 6 8)


;;; `comp`

((comp clojure.string/upper-case (partial apply str) reverse) "hello")
;; => "OLLEH"

;; long form version

(clojure.string/upper-case
 (apply str
        (reverse "hello")))
;; => "OLLEH"

;; longform as a function
(defn reverse-and-upcase [s]
  (clojure.string/upper-case (apply str (reverse s))))

(reverse-and-upcase "hello")
;; => "OLLEH"

(map (comp keyword str) [1 2 3])
;; => (:1 :2 :3)

;; not using `comp`
(map #(keyword (str %)) [1 2 3])
;; => (:1 :2 :3)


;;; Partial Application


;;; Recursive Iteration

;; example `loop/recur`
(loop [i 10]
  (if (= i 0)
    (prn "finished")
    (recur (do (prn i) (dec i)))))

;; stack exhaustion with recursion
(defn count-down [x]
  (if (= x 0)
    (prn "finished")
    (count-down (do (prn x) (dec x)))))

(count-down 10)
;; (count-down 100000) ; stack overflow

;; (count-down 6000)

(defn count-down [x]
  (if (= x 0)
    (prn "finished")
    #(count-down (do (prn x) (dec x)))))

(count-down 10)
(trampoline (count-down 10))
;; (trampoline (count-down 100000))

;;; Composablility
