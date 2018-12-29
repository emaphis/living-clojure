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

(tail-fibo 1000)

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


;;;;;;;;;;;;;;;;;;;;;;;;
;;; Recursion revisited

;; mutual recursion

(declare my-odd? my-even?)

(defn my-odd? [n]
  (if (= n 0)
    false
    (my-even? (dec n))))

(defn my-even? [n]
  (if (= n 0)
    true
    (my-odd? (dec n))))

(map my-even? (range 10))
;; => (true false true false true false true false true false)

(map my-odd? (range 10))
;; => (false true false true false true false true false true)

;; (my-even? (* 1000 1000 1000))
;; Unhandled java.lang.StackOverflowError

;;; converting to self-recursion

(defn parity [n]
  (loop [n n par 0]
    (if (= n 0)
      par
      (recur (dec n) (- n par)))))

(map parity (range 10))
;; => (0 1 -1 2 -2 3 -3 4 -4 5)

(defn my-even? [n] (= 0 (parity n)))
(defn my-odd? [n] (= 1 (parity n)))


;;; Trampolining mutual recursion

;; (trampoline f & partial-args)

(trampoline list)
;; => ()

(trampoline + 1 2)
;; => 3

;; don't write code like this
(defn trampoline-fibo [n]
  (let [fib (fn fib [f-2 f-1 current]
              (let [f (+ f-2 f-1)]
                (if (= n current)
                  f
                  #(fib f-1 f (inc current)))))]
    (cond
      (= n 0) 0
      (= n 1) 1
      :else (fib 0N 1 2))))

(trampoline trampoline-fibo 9)
;; => 34N

(rem (trampoline trampoline-fibo 1000000) 1000)
;; => 875N



(declare my-odd? my-even?)

(defn my-odd? [n]
  (if (= n 0)
    false
    #(my-even? (dec n))))

(defn my-even? [n]
  (if (= n 0)
    true
    #(my-odd? (dec n))))

(trampoline my-even? 10000000)
;; => true

(require '[wonderland.wallingford :as wall])

(wall/replace-symbol '((a b) (((b g r) (f r)) c (d e)) b) 'b 'a)
;; => ((a a) (((a g r) (f r)) c (d e)) a)

(defn deeply-nested [n]
  (loop [n n
         result '(bottom)]
    (if (= n 0)
      result
      (recur (dec n) (list result)))))

(deeply-nested 5)
;; => ((((((bottom))))))

(deeply-nested 25)
;; => ((((((((((((((((((((((((((bottom))))))))))))))))))))))))))


;;; Memoization

;; do not use these directly
(declare m f)

(defn m [n]
  (if (zero? n)
    0
    (- n (f (m (dec n))))))

(defn f [n]
  (if (zero? n)
    1
    (- n (m (f (dec n))))))

(time (m 200))
;; => 124
;; "Elapsed time: 4087.6877 msecs"

(def m (memoize m))
(def f (memoize f))


(time (m 200))
;; => 124
;; "Elapsed time: 51.1075 msecs"

(time (m 200))
;; => 124
;; "Elapsed time: 0.0304 msecs"

;; lazy sequences + memoization

(def m-seq (map m (iterate inc 0)))
(def f-seq (map f (iterate inc 0)))

(time (nth m-seq 10000))
;; => 6180
;; "Elapsed time: 81.2914 msecs"
