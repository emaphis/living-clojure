(ns myapp.chap05)

;;;; Chapter 5 - Function features

;;; One function, different parameters

(defn greet
  ([to-whom] (greet "Welcome to Blotts Books" to-whom))
  ([messge to-whom] (println messge to-whom)))

(greet "Dolly")
(greet "Hello" "Dolly")


;;; Argument with wild abandon.

(defn print-any-args [& args]
  (println "My arguments are:" args))

(print-any-args "Hello")
(print-any-args "Hello" "World")
(print-any-args 1 "two" [:three])

(defn first-argument [& args]
  (first args))

(first-argument "One" 3 :three) ;; => "One"

(defn new-first-argument [x & args] x)

(new-first-argument "One" 3 :three) ;; => "One"


;;; Multimethods

(defn dispatch-book-format [book]
  (cond
    (vector? book) :vector-book
    (contains? book :title) :standard-map
    (contains? book :book) :alternative-map))

(defmulti normalize-book dispatch-book-format)

(defmethod normalize-book :vector-book [book]
  {:title (first book) :author (second book)})

(defmethod normalize-book :standard-map [book]
  book)

(defmethod normalize-book :alternative-map [book]
  {:title (:book book) :author (:by book)})

;; Just returns the same (standard) book map.

(normalize-book {:title "War and Peace" :author "Tolstoy"})
;; => {:title "War and Peace", :author "Tolstoy"}
(normalize-book {:book "Emma" :by "Austen"})
;; => {:title "Emma", :author "Austen"}
(normalize-book ["1984" "Orwell"])
;; => {:title "1984", :author "Orwell"}

;; deal with copyrite
(defn dispatch-published [book]
  (cond
    (< (:published book) 1928) :public-domain
    (< (:published book) 1928) :old-copyright
    :else :new-copyright))

(defmulti compute-royalties dispatch-published)

(defmethod compute-royalties :public-domain [book] 0)

(defmethod compute-royalties :old-copyright [book]
  100.00)  ; whatever

(defmethod compute-royalties :new-copyright [book]
  200.00)  ; ok

;; :genre multimethod

(def books [{:title "Pride and Prejudice" :author "Austen" :genre :romance}
            {:title "World War Z" :author "Brooks" :genre :zombie}])

(defmulti book-description :genre)

(defmethod book-description :romance [book]
  (str "The heart warming new romance by " (:author book)))

(defmethod book-description :zombie [book]
  (str "The heart consuming new zombie adventure by " (:author book)))

;;; later - Opps, new genre !!

(def ppz {:title "Pride and Prejudice and Zombies"
          :author "Grahame-Smith"
          :genre :zombie-romance})

(defmethod book-description :zombie-romance [book]
  (str "The heart warming and consuming new romance by " (:author book)))


;;; Deeply Recursive
(def book-vec
  [{:title "Jaws" :copies-sold 2000000}
   {:title "Emma" :copies-sold 3000000}
   {:title "2001" :copies-sold 4000000}])

(defn sum-copies
  ([books] (sum-copies books 0))
  ([books total]
   (if (empty? books)
     total
     (sum-copies
      (rest books)
      (+ total (:copies-sold (first books)))))))

(sum-copies []) ;; => 0
(sum-copies [{:title "LOW" :copies-sold 300000}]) ;; => 300000
(sum-copies book-vec) ;; => 9000000

;; recur

(defn sum-copies-2
  ([books] (sum-copies books 0))
  ([books total]
   (if (empty? books)
     total
     (recur
      (rest books)
      (+ total (:copies-sold (first books)))))))

(sum-copies-2 []) ;; => 0
(sum-copies-2 [{:title "High" :copies-sold 400000}]) ;; => 400000
(sum-copies-2 book-vec) ;; => 9000000

;; loop expression

(defn sum-copies-3 [books]
  (loop [books books total 0]
    (if (empty? books)
      total
      (recur
       (rest books)
       (+ total (:copies-sold (first books)))))))

(sum-copies-3 []) ;; => 0
(sum-copies-3 [{:title "High" :copies-sold 400000}]) ;; => 400000
(sum-copies-3 book-vec) ;; => 9000000

;; using map higher function

(defn sum-copies-4 [books] (apply + (map :copies-sold books)))

(sum-copies-4 []) ;; => 0
(sum-copies-4 [{:title "High" :copies-sold 400000}]) ;; => 400000
(sum-copies-4 book-vec) ;; => 9000000


;;; Docstrings

(defn average
  "Return the average of `a` and `b`"
  [a b]
  (/ (+ a b) 2.0))

(defn multi-average
  "Return the average of 2 or 3 numbers"
  ([a b]
   (/ (+ a b) 2.0))
  ([a b c]
   (/ (+ a b c) 3.0)))


;;; Pre and Post Consditions

;; Publish a book using the (unseen) print-book
;; and ship-book functions

(defn print-book [book]
  (println "book: " book))

(defn ship-book [book]
  (println "ship book: " (print-book book)))

(defn publish-book-1 [book]
  (when-not (contains? book :title)
    (throw (ex-info "Books must contain :title" {:book book})))
  (print-book book)
  (ship-book book))

(defn publish-book-2 [book]
  {:pre [(:title book)]}
  (print-book book)
  (ship-book book))

(defn publish-book-3 [book]
  {:pre [(:title book) (:author book)]}
  (print-book book)
  (ship-book book))

;; post-condition

(defn publish-book-4 [book]
  {:pre  [(:title book) (:author book)]
   :post [(boolean? %)]}
  (print-book book)
  (ship-book book))
