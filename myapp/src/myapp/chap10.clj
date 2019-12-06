(ns myapp.chap10)

;;;; Sequences

;;; One thing after another

;; Naive `count` implementation

(defn flavor [x]
  (cond
    (list? x) :list
    (vector? x) :vector
    (set? x) :set
    (map? x) :map
    (string? x) :string
    :else :unknown))

(defmulti my-count-1 flavor)

(defmethod my-count-1 :list [x] (,,,))
(defmethod my-count-1 :vector [x] (,,,))

;; `seq`

(def title-seq (seq ["Emma" "Oliver Twist" "Robinson Crusoe"]))
;; => #'myapp.chap10/title-seq

title-seq ;; => ("Emma" "Oliver Twist" "Robinson Crusoe")

;; sequence from a list
(seq '("Emma" "Oliver Twist" "Robinson Crusoe"))
;; => ("Emma" "Oliver Twist" "Robinson Crusoe")

;; sequence from a map
(seq {:title "Emma", :author "Austen", :published 1815})
;; => ([:title "Emma"] [:author "Austen"] [:published 1815])

;; `seq` on `seq` - noop
(seq (seq ["Emma" "Oliver Twist" "Robinson Crusoe"]))
;; => ("Emma" "Oliver Twist" "Robinson Crusoe")

;; `seq` returns `nil` on empty collections

(seq []) ;; => nil
(seq '()) ;; => nil
(seq {})  ;; => nil
(seq "") ;; => nil

;; *** use `(seq collection)` to test for empty collections ***


;;; A Universal Interface

(first (seq '("Emma" "Oliver Twist" "Robinson Crusoe"))) ;; => "Emma"

(rest (seq '("Emma" "Oliver Twist" "Robinson Crusoe")))
;; => ("Oliver Twist" "Robinson Crusoe")

(next (seq '("Emma" "Oliver Twist" "Robinson Crusoe")))
;; => ("Oliver Twist" "Robinson Crusoe")

(cons "Emma" (seq '("Oliver Twist" "Robinson Crusoe")))
;; => ("Emma" "Oliver Twist" "Robinson Crusoe")

;; *** The `seq` interface `first` `rest` `next` `cons` ***

;; `my-count` using `sew` interface

(defn my-count [col]
  (let [the-seq (seq col)]
    (loop [n 0 s the-seq]
      (if (seq s)
        (recur (inc n) (rest s))
        n))))

(my-count '()) ;; => 0
(my-count []) ;; => 0
(my-count '(1)) ;; => 1
(my-count [1 2 3]) ;; => 3
(my-count {:a "a", :b "b", :c "c"}) ;; => 3
(my-count "abcd") ;; => 4

;; `cons` `next` and `rest` always returns a sequence (or `nil`)

(rest [1 2 3]) ;; => (2 3)
(rest {:fname "Jane", :lname "Austen"}) ;; => ([:lname "Austen"])
(next {:fname "Jane", :lname "Austen"}) ;; => ([:lname "Austen"])

(cons 0 [1 2 3]) ;; => (0 1 2 3)
(cons 0 #{1 2 3}) ;; => (0 1 3 2)


;;; A Rich Toolkit ...

(def titles ["Jaws" "Emma" "2001" "Dracula"])

(sort titles) ;; => ("2001" "Dracula" "Emma" "Jaws")

(reverse titles) ;; => ("Dracula" "2001" "Emma" "Jaws")

(reverse (sort titles))
;; => ("Jaws" "Emma" "Dracula" "2001")

(def titles-and-authors ["Jaws" "Benchley" "2001" "Clarke"])

(partition 2 titles-and-authors)
;; => (("Jaws" "Benchley") ("2001" "Clarke"))

;; A vector of titles and a list of authors

(def titles ["Jaws" "2001"])
(def authors '("Benchley" "Clarke"))

(interleave titles authors)
;; => ("Jaws" "Benchley" "2001" "Clarke")

(def scary-animals ["Lions" "Tigers" "Bears"])

(interpose "and" scary-animals)
;; => ("Lions" "and" "Tigers" "and" "Bears")


;;; ... Made Richer with Functional Values

;; `filter`
(filter neg? '(1 -22 3 -99 4 5 6 -99)) ;; => (-22 -99 -99)

(def books
  [{:title "Deep Six" :price 13.99 :genre :sci-fi :rating 6}
   {:title "Dracula" :price 1.99 :genre :horror :rating 7}
   {:title "Emma" :price 7.99 :genre :comedy :rating 9}
   {:title "2001" :price 10.50 :genre :sci-fi :rating 5}])

(defn cheap? [book]
  (when (<= (:price book) 9.99)
    book))

(filter cheap? books) 
;; => ({:title "Dracula", :price 1.99, :genre :horror, :rating 7}
;;     {:title "Emma", :price 7.99, :genre :comedy, :rating 9})

(some cheap? books)
;; => {:title "Dracula", :price 1.99, :genre :horror, :rating 7}

(if (some cheap? books)
  (println "We have cheap books for sale!"))


;;; Map

(def some-numbers [1 53 811])

(def doubled (map #(* 2 %) some-numbers))

doubled ;; => (2 106 1622)

(map (fn [book] (:title book)) books)
;; => ("Deep Six" "Dracula" "Emma" "2001")

(map :title books) ;; => ("Deep Six" "Dracula" "Emma" "2001")

;; lengths of titles
(map (fn [book] (count (:title book))) books)
;; => (8 7 4 4)
(map (comp count :title) books)
;; => (8 7 4 4)

;;; `for`

(for [b books]
  (count (:title b)))
;; => (8 7 4 4)

(for [b {:a 1 :b 2 :c 3 :d 4}] b)
;; => ([:a 1] [:b 2] [:c 3] [:d 4])


;;; `reduce`

(def numbers [10 20 30 40 50])

(defn add2 [a b]
  (+ a b))

(reduce add2 0 numbers) ;; => 150

(reduce + 0 numbers) ;; => 150
;; or
(reduce + numbers) ;; => 150

(defn hi-price [hi book]
  (if (> (:price book) hi)
    (:price book)
    hi))

(reduce hi-price 0 books) ;; => 13.99


;;; Composing a Solution

;; produce the three top rated books

(reverse (sort-by :rating books))
;; => ({:title "Emma", :price 7.99, :genre :comedy, :rating 9}
;;     {:title "Dracula", :price 1.99, :genre :horror, :rating 7}
;;     {:title "Deep Six", :price 13.99, :genre :sci-fi, :rating 6}
;;     {:title "2001", :price 10.5, :genre :sci-fi, :rating 5})

(take 3 (reverse (sort-by :rating books)))
;; => ({:title "Emma", :price 7.99, :genre :comedy, :rating 9}
;;     {:title "Dracula", :price 1.99, :genre :horror, :rating 7}
;;      {:title "Deep Six", :price 13.99, :genre :sci-fi, :rating 6})

(map :title (take 3 (reverse (sort-by :rating books))))
;; => ("Emma" "Dracula" "Deep Six")

(interpose
 " // "
 (map :title (take 3 (reverse (sort-by :rating books)))))
;; => ("Emma" " // " "Dracula" " // " "Deep Six")

(defn format-top-titles [books]
  (apply
   str
   (interpose
    " // "
    (map :title (take 3 (reverse (sort-by :rating books)))))))

(format-top-titles books)
;; => "Emma // Dracula // Deep Six"


;;; Other Sources of Sequences

;; authors.txt
(require '[clojure.java.io :as io])

(defn listed-author? [author]
  (with-open [r (io/reader "authors.txt")]
    (some (partial = author) (line-seq r))))
