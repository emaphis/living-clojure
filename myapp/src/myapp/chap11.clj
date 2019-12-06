(ns myapp.chap11)

;;;; Lazy Sequences

;;; Sequences without end

;; Nonsense text

(def jack "All work and no play makes Jack a dull boy.")

(def text [jack jack jack jack jack jack jack jack jack jack jack])

;; lazy stream of jacks
(def repeated-text (repeat jack))

;; one instance
(first repeated-text) ;; => "All work and no play makes Jack a dull boy."
(nth repeated-text 10) ;; => "All work and no play makes Jack a dull boy."
(nth repeated-text 10202) ;; => "All work and no play makes Jack a dull boy."

;; Twenty dull boys.
(take 20 repeated-text)


;;; More Interesting Laziness
;; `cycle` `iterate`

(take 7 (cycle [1 2 3])) ;; => (1 2 3 1 2 3 1)

(def numbers (iterate inc 1))

(first numbers) ;; => 1

(nth numbers 0) ;; => 1
(nth numbers 1) ;; => 2
(nth numbers 99)  ;; => 100
(take 5 numbers) ;; => (1 2 3 4 5)


;;; Lazy friends

;; `take``map``interleave` are also lazy

(def many-nums (take 1000000000 (iterate inc 1)))

(println (take 20 (take 1000000000 (iterate inc 1))))

(def evens (map #(* 2 %) (iterate inc 1)))

(take 20 evens) ;; => (2 4 6 8 10 12 14 16 18 20 22 24 26 28 30 32 34 36 38 40)

(take 10 (interleave numbers evens))
;; => (1 2 2 4 3 6 4 8 5 10)


;;; Laziness in practice

;; Nonsense list of books for testing

(def test-books-1
  [{:author "Bob Jordan", :title "Wheel of Time, Book 1"}
   {:author "Jane Austen", :title "Wheel of Time, Book 2"}
   {:author "Chuck Dickens", :title "Wheel of Time, Book 3"}
   {:author "Leo Tolstoy", :title "Wheel of Time, Book 4"}
   {:author "Bob Poe", :title "Wheel of Time, Book 5"}
   {:author "Jane Jordan", :title "Wheel of Time, Book 6"}
   {:author "Chuck Austen", :title "Wheel of Time, Book 7"}])

;; let's generate a list
(def numbers [1 2 3])

(def trilogy (map #(str "Wheel of Time, Book " %) numbers))

trilogy
;; => ("Wheel of Time, Book 1"
;;     "Wheel of Time, Book 2"
;;     "Wheel of Time, Book 3")

;; make it lazy
(def numbers (iterate inc 1))

(def titles (map #(str "Wheel of Time, book " %) numbers))

(def trilogy (take 3 titles))

trilogy
;; => ("Wheel of Time, book 1"
;;     "Wheel of Time, book 2"
;;     "Wheel of Time, book 3")

(def first-names ["Bob" "Jane" "Chuck" "Leo"])

(take 5 (cycle first-names))
;; => ("Bob" "Jane" "Chuck" "Leo" "Bob")

(def last-names ["Jordan" "Austen" "Dickens" "Tolstoy" "Poe"])

(take 7 (cycle last-names))
;; => ("Jordan" "Austen" "Dickens" "Tolstoy" "Poe" "Jordan" "Austen")

(defn combine-names [fname lname]
  (str fname " " lname))

(combine-names (first first-names) (second last-names))
;; => "Bob Austen"

(def authors
  (map combine-names
       (cycle first-names)
       (cycle last-names)))

(take 6 authors)
;; => ("Bob Jordan" "Jane Austen" "Chuck Dickens" "Leo Tolstoy" "Bob Poe" "Jane Jordan")

(defn make-book [title author]
  {:author author :title title})

(def test-books (map make-book titles authors))

(take 4 test-books)
;; => ({:author "Bob Jordan", :title "Wheel of Time, book 1"}
;;     {:author "Jane Austen", :title "Wheel of Time, book 2"}
;;     {:author "Chuck Dickens", :title "Wheel of Time, book 3"}
;;     {:author "Leo Tolstoy", :title "Wheel of Time, book 4"})



;;; Behind the scenes

(lazy-seq [1 2 3]) ;; => (1 2 3)

(defn chatty-vector []
  (println "Here we go!")
  [1 2 3])

(chatty-vector) ;; => [1 2 3]

;; No output when we do this.
(def s (lazy-seq (chatty-vector)))

s ;; => (1 2 3)

;; This will cause print out
(first s)
;; => 1

;; Implement `repeat` `iterate` `map`
;; Note that the real `repeat` has a couple of arities
;; that we won't bother to implement here.

(defn my-repeat [x]
  (cons x (lazy-seq (my-repeat x))))

(take 4 (my-repeat 4)) ;; => (4 4 4 4)

(defn my-iterate [f x]
  (cons x (lazy-seq (my-iterate f (f x)))))

(take 5 (my-iterate inc 1)) ;; => (1 2 3 4 5)

(defn my-map [f col]
  (when-not (empty? col)
    (cons (f (first col))
          (lazy-seq (my-map f (rest col))))))

(take 6 (my-map #(* 2 %) (my-iterate inc 1)))
;; => (2 4 6 8 10 12)

;; (spit "./src/myapp/outtest.txt"  "Out test text - mark 2")
;; (slurp "./src/myapp/outtest.txt")
