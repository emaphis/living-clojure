(ns wonderland.functional
  (:require [clojure.repl :as r]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Functional programming concepts

;; pure functions
;; persistent data structures
;; Laziness and Recursion
;; Referential Transparency.


;;; Guidelines

;; Avoid direct recursion.
;;  The JVM can't optimize recursive calls, and Clojure programs that recurse
;;  will blow their stack.

;; Use recur when you're producing scalar values or small, fixed sequences.
;;  Clojure will optimize calls that use an explicit recur.

;; When producing large or variable-sized sequences, always be lazy.
;;  (Do not recur.) Then, your callers can consume just the part of the
;;  sequence they actually need.

;; Be careful not to realize more of a lazy sequence than you need.

;; Know the sequence library.
;;  You can often write code without using recur or the lazy APIs at all.

;; Subdivide.
;;  Divide even simple-seeming problems into smaller pieces, and you'll
;;  often find solutions in the sequence library that lead to more general,
;;  reusable code.

;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Laziness

;;; Fibonacci Sequence

'(0 1 1 2 3 5 8 13 21 34)

;; simple recursion - bad idea - will blow up stack

(defn stack-consuming-fibo [n]
  (cond (= n 0) 0
        (= n 1) 1
        :else (+ (stack-consuming-fibo (- n 1))
                 (stack-consuming-fibo (- n 2)))))

(stack-consuming-fibo 9)
;; => 34

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

(tail-fibo 9)
;; => 34N

(tail-fibo 1000)
;; => 43466557686937456435688527675040625802564660517371780402481729089536555417949051890403879840079255169295922593080322634775209689623239873322471161642996440906533187938298969649928516003704476137795166849228875N

;; (tail-fibo 1000000) ; blows stack


;; self-recursion with recur

;; better but not great
(defn recur-fibo [n]
  (letfn [(fib
            [current next n]
            (if (zero? n)
              current
              (recur next (+ current next) (dec n))))]
    (fib 0N 1N n)))
;; => #'wonderland.functional/recur-fibo

(recur-fibo 9N)
;; => 34N

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

;;(rem (nth (lazy-seq-fibo) 1000000) 1000)
;; => 875N

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

;; Don't do this ...
;;(take 1000000000 (fibo))
;; ... will print a billion numbers

(set! *print-length* 10)
(take 1000000000 (fibo))
;; => (0N 1N 1N 2N 3N 5N 8N 13N 21N 34N ...)

;; now
(fibo)
;; => (0N 1N 1N 2N 3N 5N 8N 13N 21N 34N ...)


;; Don't do this, holds onto the whole datastructure
(def head-fibo (lazy-cat [0N 1N] (map + head-fibo (rest head-fibo))))

;; works OK for small numbers
(take 10 head-fibo)
;; => (0N 1N 1N 2N 3N 5N 8N 13N 21N 34N)

;; but not large
(nth head-fibo 1000000)
;; java.lang.OutOfMemoryError: GC overhead limit exceeded


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Lazier than lazy
;;  combining existing lazy functions

[:h :t :t :h :h :h]


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

;;; data analysis ...
[:h :t :t :h :h :h]

;; ... transform into adjacent pairs to evaluate each neighbor
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

;; -- or
(by-pairs [:h :t :t :h :h :h])
;; => ((:h :t) (:t :t) (:t :h) (:h :h) (:h :h))


;; comp of count and filter

(def ^{:doc "Count items matching a filter"}
  count-if (comp count filter))

(count-if odd? [1 2 3 4 5])
;; => 3

(count-if even? [1 2 3 4 5])
;; => 2


(defn count-runs
  "Count run of length 'n' where 'pred' is 'true' in 'coll'"
  [n pred coll]
  (count-if #(every? pred %) (partition n 1 coll)))


(count-runs 2 #(= % :h) [:h :t :t :h :h :h])
;; => 2

;; pairs of tails
(count-runs 2 #(= % :t) [:h :t :t :h :h :h])
;; => 1

;; three in a row
(count-runs 3 #(= % :h) [:h :t :t :h :h :h])
;; => 1


;;; Currying and partial application

(def ^{:doc "Count funs of length of two that are both heads"}
  count-heads-pairs (partial count-runs 2 #(= % :h)))

;; (partial f & partial-args) - specify function and part of the argument list

(partial count-runs 1 #(= % :h))  ; is more concise than

(fn [coll] (count-runs 1 #(= % :h)))


;; almost a curry
(defn faux-curry [& args] (apply partial partial args))

(def add-3 (partial + 3))

(add-3 4)
;; => 7

(def add-3 ((faux-curry +) 3))
(add-3 4)
;; => 7

;; faux-curry
((faux-curry true?) (= 1 1))
;; => #function[clojure.core/partial/fn--5824]

;; if the curry were real
;; ((curry true?) (= 1 1)) ;; True

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
      (recur (dec n) (- 1 par)))))

(map parity (range 10))
;; => (0 1 0 1 0 1 0 1 0 1)


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


;; mutual recursion
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

;;; Replacing recursion with Lazyness

(require '[wonderland.wallingford :as wall])

(wall/replace-symbol '((a b) (((b g r) (f r)) c (d e)) b) 'b 'a)
;; => ((a a) (((a g r) (f r)) c (d e)) a)


;; deeply nested function to blow stack
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

;; reduce printing level of deeply nested structures
(set! *print-level* 25)
;; => 25

(deeply-nested 25)
;; => (((((((((((((((((((((((((#)))))))))))))))))))))))))

(wall/replace-symbol (deeply-nested 5) 'bottom 'deepest)
;; => ((((((deepest))))))

;;(wall/replace-symbol (deeply-nested 10000) 'bottom 'deepest)
;; Unhandled java.lang.StackOverflowError

;;; to fix wrap the recursion with lazy-seq
(defn- coll-or-scalar [x & _] (if (coll? x) :collection :scalar))

(defmulti replace-symbol coll-or-scalar)

(defmethod replace-symbol :collection [coll oldsym newsym]
  (lazy-seq
   (when (seq coll)
     (cons (replace-symbol (first coll) oldsym newsym)
           (replace-symbol (rest coll) oldsym newsym)))))

(defmethod replace-symbol :scalar [obj oldsym newsym]
  (if (= obj oldsym) newsym obj))

(replace-symbol (deeply-nested 10000) 'bottom 'deepest)
;; => (((((((((((((((((((((((((#)))))))))))))))))))))))))


;;; Memoization

;; Hofstadtler Female and Male sequences
;; F(0) = 1; M(0) = 0
;; F(n) = n - M(F(n-1)), n > 0
;; M(n) = n - F(M(n-1)), n > 0


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

;; now memoize
(def m (memoize m))
(def f (memoize f))


(time (m 200))
;; => 124
;; "Elapsed time: 51.1075 msecs"

(time (m 200))
;; => 124
;; "Elapsed time: 0.0304 msecs"

(time (m 250))
;; => 155
;; "Elapsed time: 1.3578 msecs"

(time (m 250))
;; => 155
;; "Elapsed time: 0.0399 msecs"

;; but we still have a stack overflow
;; (m 10000)
;; Execution error (StackOverflowError) at wonderland.functional/m (form-init10335001488879732383.clj:436).


;; expose lazy sequences instead of functions + memoization
;; so iterate inc instead of just inc

(def m-seq (map m (iterate inc 0)))
(def f-seq (map f (iterate inc 0)))

(time (nth m-seq 10000))
;; => 6180
;; "Elapsed time: 58.0016 msecs"

(time (nth m-seq 10000))
;; => 6180
;; "Elapsed time: 1.6487 msecs"

;; Method:
;; Define a mutually recursive function in a natural way.
;; Use memoization to shortcut recursion for values that have already
;;  been calculated.
;; Expose a sequence so that dependent values are cached before they're needed.


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Eager Transformations

;; Useful if we:
;;  Construct an output collection rather than a sequence,
;;  Optimize memory and performance for transformations of large collections,
;;  Control external resources.

;;; Producing output collections

;; Applying a function to a sequence to produce an output vector.

(defn square [x] (* x x))

(defn sum-squares-seq [n]
  (vec (map square (range n))))

(sum-squares-seq 10)
;; => [0 1 4 9 16 25 36 49 64 81]

;; this holds three versions of the range, the range, the map values, the vec

;; but we can skip intermediate values with a transducer, placing the values
;; directly in the vector
;; calling map without an input collection returns a transducer.

(defn sum-squares [n]
  "'into sink transformation source' is a transducer"
  (into [] (map square) (range n)))


(sum-squares 10)
;; => [0 1 4 9 16 25 36 49 64 81]

(class (map square))
;; => clojure.core$map$fn__5847

;;; Optimizing performance

;; find all the predicate functions in our namespace loaded so far.

(defn preds-seq []
  (->> (all-ns)
       (map ns-publics)
       (mapcat vals)
       (filter #(clojure.string/ends-with? % "?"))
       (map #(str (.-sym %)))
       vec))

;; transducer version

(defn preds []
  (into []
        (comp (map ns-publics)
              (mapcat vals)
              (filter #(clojure.string/ends-with? % "?"))
              (map #(str (.-sym %))))
        (all-ns)))


(time (preds-seq))

(time (preds))

;; TODO: try criterium sometime.

(count (preds))
;; => 155


;;; Managing external resources

;; find non-blank line in a file

(defn non-blank? [s]
  (not (clojure.string/blank? s)))

(defn non-blank-lines-seq [file-name]
  (let [reader (clojure.java.io/reader file-name)]
    (filter non-blank? (line-seq reader))))

;; the reader isn't being closed.

;; eager version that closes the reader

(defn non-blank-lines [file-name]
  (with-open [reader (clojure.java.io/reader file-name)]
    (into [] (filter non-blank?) (line-seq reader))))

;; this version has a problem with large files

;; eduction - a suspended transformation that processes the entire input each
;; time - only once?

(defn non-blank-lines-eduction [reader]
  (eduction (filter non-blank?) (line-seq)))

(defn lein-count [file-name]
  (with-open [reader (clojure.java.io/reader file-name)]
    (reduce (fn [cnt el] (inc cnt)) 0 (non-blank-lines-eduction reader))))

;; So the core of Clojure is syntax, immutable collections, sequence abstraction
;; functional programming, recursion
