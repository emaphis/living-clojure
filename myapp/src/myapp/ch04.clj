(ns myapp.ch04)

;;;; Sequences

;; Triggering side-effects over a sequence
(doseq [element '(:a :b :c)]
  (prn (str (name element) "!")))
;; => nil

;; example of `for` loop
(for [element '(:a :b :c)]
  (prn (str (name element) "!")))
;; => (nil nil nil)


;;; List comprehension

;; `map` to mimic `for` example
(map
 (fn [element]
   (prn (str (name element) "!")))
 '(:a :b :c))
;; => (nil nil nil)

;; example of List Comprehension using `for` form
(for [x [1 2]
      y [7 8]]
  (do
    (prn x y)
    (+ x y)))
;; => (8 9 9 10)

;; difference of `map` with multiple collections
(map #(do
        (prn %1 %2)
        (+ %1 %2))
     [1 2]
     [7 8])
;; => (8 10)

;; list comprehension modifiers - `:let` `:while` `:when`
;; `when`
(for [x (range 5)
      :when (> (* x x) 3)]
  (* 2 2))
;; => (4 4 4)


;;; Sequence Abstraction
;; `first` `rest` `cons`

;; types
(type (seq '(:a 1 :b 2)))
;; => clojure.lang.PersistentList
(type (seq [:a 1 :b 2]))
;; => clojure.lang.PersistentVector$ChunkedSeq
(type (seq #{:a 1 :b 2}))
;; => clojure.lang.APersistentMap$KeySeq
(type (seq {:a 1 :b 2}))
;; => clojure.lang.PersistentArrayMap$Seq

;;; Lazy Sequences

;; chunks
(def v (vec (range 1 65)))
(def m (map #(do (prn %1) (str %1 "v")) v))
(first m) ; prints everything out
;; => "1v"

(nth m 10) ;; => "11v"

(nth m 33)  ; 33 - 64
;; => "34v"

;; `lazy-seq`

;; generate your own lazy sequence

(defn add-n [n coll]
  (lazy-seq
   (cons
    (+ n (first coll))
    (add-n n (rest coll)))))

(type (add-n 1 (range))) 
;; => clojure.lang.LazySeq

(take 10 (add-n 5 (range)))
;; => (5 6 7 8 9 10 11 12 13 14)

;; Simple but explicit nested lazy sequence example
(def ls
  (lazy-seq
   (do
     (prn "body executed")
     (lazy-seq (do (prn "next body executed") [:a :b :c])))))

ls
;; "body executed"
;; "next body executed"
;; => (:a :b :c)

(take 1 ls)
;; => (:a)
;; "body executed"
;; "next body executed"
