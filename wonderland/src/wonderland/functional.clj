(ns wonderland.functional
  (:require [clojure.repl :as r]))

;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Laziness


;; bad idea - will blow up stack

(defn stack-consuming-fibo [n]
  (cond (= n 0) 0
        (= n 1) 1
        :else (+ (stack-consuming-fibo (- n 1))
                 (stack-consuming-fibo (- n 2)))))


;; (stack-consuming-fibo 1000000)
;; Unhandled java.lang.StackOverflowError


;; Tail recursion

(defn tail-fibo [n]
  (letfn [(fib
            [current next n]
            (if (zero? n)
              current
              (fib next (+ current next) (dec n))))]
    (fib 0N 1N n)))

(tail-fibo 15000)

;; self-recursion with recure

;; better but not great
(defn recur-fibo [n]
  (letfn [(fib
            [current next n]
            (if (zero? n)
              current
              (recur next (+ current next) (dec n))))]
    (fib 0N 1N n)))

(recur-fibo 100000)


;; Lazy sequences

(defn lazy-seq-fibo
  ([]
   (concat [0 1] (lazy-seq-fibo 0N 1N)))
  ([a b]
   (let [n (+ a b)]
     (lazy-seq
      (cons n (lazy-seq-fibo b n))))))


(take 10 (lazy-seq-fibo))
;; => (0 1 1N 2N 3N 5N 8N 13N 21N 34N)

;; (rem (nth (lazy-seq-fibo) 1000000) 1000)

;; using existing functions
(take 10 (iterate (fn [[a b]] [b (+ a b)]) [0 1]))
;; => ([0 1] [1 1] [1 2] [2 3] [3 5] [5 8] [8 13] [13 21] [21 34] [34 55])

(defn fibo []
  (map first (iterate (fn [[a b]] [b (+ a b)]) [0N 1N])))

(take 10 (fibo))
;; => (0N 1N 1N 2N 3N 5N 8N 13N 21N 34N)


;;; Coming to realization

(def lots-of-fibs (take 1000000000 (fibo)))
;; => #'wonderland.functional/lots-of-fibs

(nth lots-of-fibs 100)
;; => 354224848179261915075N

;; Don't do this, holds onto the whole datasruct

(def head-fibo (lazy-cat [0N 1N] (map + head-fibo (rest head-fibo))))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Lazier than lazy

(defn count-heads-pairs [coll]
  (loop [cnt 0 coll coll]
    (if (empty? coll)
      cnt
      (recur (if (= :h (first coll) (second coll))
               (inc cnt)
               cnt)
             (rest coll)))))


(count-heads-pairs [:h :h :h :t :h])
;; => 2

(count-heads-pairs [:h :t :h :t :h])
;; => 0

;;; data analysis
[:h :t :t :h :h :h]

;; transform into adjacent pairs to evaluate each neighbor
[[:h :t] [:t :t] [:t :h] [:h :h] [:h :h]]

(defn by-pairs [coll]
  (let [take-pair (fn [c]
                    (when (next c) (take 2 c)))]
    (lazy-seq
     (when-let [pair (seq (take-pair coll))]
       (cons pair (by-pairs (rest coll)))))))

(by-pairs [:h :t :t :h :h :h])
;; => ((:h :t) (:t :t) (:t :h) (:h :h) (:h :h))

(defn count-heads-pairs
  "Count the pairs of results that are all heads"
  [coll]
  (count (filter (fn [pair] (every? #(= :h %) pair))
                 (by-pairs coll))))


;; (partition size step? coll)  ;; more general than by-pairs

(partition 2 [:h :t :t :h :h :h])
;; => ((:h :t) (:t :h) (:h :h))

;; 1 step overlap
(partition 2 1 [:h :t :t :h :h :h])
;; => ((:h :t) (:t :t) (:t :h) (:h :h) (:h :h))

;; comp of count and filter

(def ^{:doc "Count items matching a filter"}
  count-if (comp count filter))

(count-if odd? [1 2 3 4 5])
;; => 3


(defn count-runs
  "Count run of length 'n' where 'pred' is 'true' in 'coll'"
  [n pred coll]
  (count-if #(every? pred %) (partition n 1 coll)))


(count-runs 2 #(= % :h) [:h :t :t :h :h :h])
;; => 2

(count-runs 2 #(= % :t) [:h :t :t :h :h :h])
;; => 1

(count-runs 3 #(= % :h) [:h :t :t :h :h :h])
;; => 1

(def ^{:doc "Count funs of length of two that are both heads"}
  count-heads-pairs (partial count-runs 2 #(= % :h)))

;; (partial f & partial-args) - specify function and part of the argument list


;;; Currying and partial application

;; almost a curry
(defn faux-curry [& args] (apply partial partial args))

(def add-3 (partial + 3))

(add-3 4)
;; => 7

(def add-3 ((faux-curry +) 3))
(add-3 4)
;; => 7

((faux-curry true?) (= 1 1))
;; => #function[clojure.core/partial/fn--5824]

;; if the curry were real
((faux-curry true?) (= 1 1)) ;; True

;; add extra parentheses to execute the function
(((faux-curry true?) (= 1 1)))
;; => true
