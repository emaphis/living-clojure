(ns myapp.chap06)

;;;; Functional things

;;; Functions are Values

(def dracula {:title "Dracula"
              :author "Stoker"
              :price 1.99
              :genre :horror})

;; test for price
(defn cheap? [book]
  (when (<= (:price book) 9.99)
    book))

(defn pricy? [book]
  (when (> (:price book) 9.99)
    book))

(cheap? dracula)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}
(pricy? dracula) ;; => nil

;; test for genre:
(defn horror? [book]
  (when (= (:genre book) :horror)
    book))

(defn adventure? [book]
  (when (= (:genre book) :adventure)
    book))

(horror? dracula)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}
(adventure? dracula) ;; => nil

;; complex solution for combined functios
(defn cheap-horror [book]
  (when (and (cheap? book)
             (horror? book))
    book))

(defn pricy-adventure? [book]
  (when (and (pricy? book)
             (adventure? book))
    book))

;; functions can be bound to other symbols
(def reasonably-priced? cheap?)

(reasonably-priced? dracula) 
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}

;; passing functions by value!!
(defn run-with-dracula [f]
  (f dracula))

(run-with-dracula pricy?)
;; => nil
(run-with-dracula horror?)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}

(defn both? [first-predicate-f second-predicate-f book]
  (when (and (first-predicate-f book)
             (second-predicate-f book))
    book))

(both? cheap? horror? dracula)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}
(both? pricy? adventure? dracula) ;; => nil


;;; Functions on the Fly

(fn [n] (* 2 n))

;; you can pass functions by value
(println "A function:" (fn [n] (* 2 n)))

;; bind it to a symbol
(def double-it (fn [n] (* 2 n)))

(double-it 10) ;; => 20
((fn [n] (* 2 n)) 10) ;; => 20

;; same thing as `cheap`

(fn [book]
  (when (<= (:price book) 9.99)
    book))

;; function that produces functions!

(defn cheaper-f [max-price]
  (fn [book]
    (when (<= (:price book) max-price)
      book)))

;; bargain hunting functions
(def real-cheap? (cheaper-f 1.00))
(def kind-of-cheap? (cheaper-f 1.99))
(def marginally-cheap? (cheaper-f 5.99))

(real-cheap? dracula) ;; => nil
(kind-of-cheap? dracula)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}
(marginally-cheap? dracula)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}

(defn both-f [predicate-f-1 predicate-f-2]
  (fn [book]
    (when (and (predicate-f-1 book)
               (predicate-f-2 book))
      book)))

(def cheap-horror? (both-f cheap? horror?))
(def real-cheap-adventure? (both-f real-cheap? adventure?))
(def real-cheap-horror? (both-f real-cheap? horror?))

;; another level of meta:
(def cheap-horror-possesion?
  (both-f cheap-horror
          (fn [book] (= (:title book) "Possession"))))


;;; A Functional Toolkit

;; apply

(+ 1 2 3 4) ;; => 10

(def the-function +)
(def args [1 2 3 4])

(apply the-function args) ;; => 10

;; converting from one kind of value to another.

(def v ["The number " 2 " best selling " "book."])
v ;; => ["The number " 2 " best selling " "book."] 

(apply str v) ;; => "The number 2 best selling book."
(apply list v) ;; => ("The number " 2 " best selling " "book.")
(apply vector (apply list v)) ;; => ["The number " 2 " best selling " "book."]

;; partial

(inc 1) ;; => 2

(defn my-inc-1 [n] (+ 1 n)) ;; fill in one of the arguments of `+`

(def my-inc (partial + 1))

;; using partial to define cheapness functions

(defn cheaper-than [max-price book]
  (when (<= (:price book) max-price)
    book))

(def real-cheap-1? (partial cheaper-than 1.00))
(def kind-of-cheap-1? (partial cheaper-than 1.99))
(def marginally-cheap-1? (partial cheaper-than 5.99))

(real-cheap-1? dracula) ;; => nil
(kind-of-cheap-1? dracula)
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}
(marginally-cheap-1? dracula) 
;; => {:title "Dracula", :author "Stoker", :price 1.99, :genre :horror}

;; `complement` - opposite day...

(defn adventure-1? [book]
  (when (= (:genre book) :adventure)
    book))

(defn not-adventure? [book] (not (adventure-1? book)))

(def not-adventure-1? (complement adventure-1?))

(not-adventure? dracula) ;; => true
(not-adventure-1? dracula) ;; => true

;; `every-pred` replaces `both-f`

(def cheap-horror-1? (every-pred cheap? horror?))

(def cheap-horror-possesion-1?
  (every-pred
   cheap?
   horror?
   (fn [book] (= (:title book) "Possession"))))


;;; Function Literals

;; guts of `adventure?`
#(when (= (:genre %1) :adventure) %1)

;; double
#(* 2 %1)

;; sum three
#(+ %1 %2 %3)
