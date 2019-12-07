(ns inventory.core
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as st]))

(defn find-by-title
  "Search for a book by title
  Where title is a string and books is a collection
  of book maps, each of which mush have a :title entry"
  [title books]
  (some #(when (= (:title %) title) %) books))


(defn number-of-copies
  "Return the number of copies in inventory of the
  given title, where title is a string and books is a collection"
  [title books]
  (:copies (find-by-title title books)))


;;; This is the data you are looking for

{:title "Getting Clojure" :author "Olsen" :copies 1000000}

(defn book? [x]
  (and
   (map? x)
   (string? (:author x))
   (string? (:title x))
   (pos-int? (:copies x))))

(book? {:title "Getting Clojure" :author "Olsen" :copies 1000000})
;; => true

;; spec testing
(s/valid? number? 44) ;; => true
(s/valid? number? :hello) ;; => false

;; `s/and` - complex specs
(def n-gt-10 (s/and number? #(> % 10)))

(s/valid? n-gt-10 1)  ;; => false
(s/valid? n-gt-10 10) ;; => false
(s/valid? n-gt-10 11) ;; => true

(def n-gt-10-lt-100
  (s/and number? #(> % 10) #(< % 100)))

;; macth this `s/or` that
(def n-or-s (s/or :a-number number? :a-string string?))

(s/valid? n-or-s "Hello!") ;; => true
(s/valid? n-or-s 99)   ;; => true
(s/valid? n-or-s 'foo) ;; => false

;; building arbitrary specs out of other specs
(def n-gt-10-or-s (s/or :greater-10 n-gt-10 :a-symbol symbol?))


;;; Spec'ing collections

;; Something like '("Alice" "In" "Wonderland").
(def coll-of-strings (s/coll-of string?))

(s/valid? coll-of-n-or-sh '("Alice" "In" "Wonderland")) ;; => true

;; Or a collection of numbers or strings, perhapps ["Emma" 1815 "Jaws" 1974]
(def coll-of-n-or-s (s/coll-of n-or-s))

(s/valid? coll-of-n-or-s ["Emma" 1815 "Jaws" 1974]) ;; => true

;; `s/cat` this sould follow that
(def s-n-s-n (s/cat :s1 string? :n1 number? :s2 string? :n2 number?))

(s/valid? s-n-s-n ["Emma" 1885 "Jaws" 1974]) ;; => true

;; `s/cat` requires descriptive keywords.

;; specs for maps using keys functions.
;; spec for book map.
(def book-s
  (s/keys :req-un [:inventory.core/title
                   :inventory.core/author
                   :inventory.core/copies]))

(s/valid? book-s {:title "Emma" :author "Austin" :copies 10}) 
;; => true

(s/valid? book-s {:title "Arabian Nights" :copies 17})
;; => false - missing author

(s/valid? book-s {:title "2001" :author "Clarke" :copies 1 :published 1968 })
;; => true  - extra keys are ok.


;;; Registering specs

(s/def :inventory.core/book
  (s/keys
   :req-un
   [:inventory.core/title :inventory.core/author :inventory.core/copies]))

(s/valid? :inventory.core/book {:title "Dracula" :author "Stoker" :copies 10})
;; => true

;; keyword namespace shortcut
(s/def ::book (s/keys :req-un [::title ::author ::copies]))


;;; spec'ing maps again

(s/def ::title string?)
(s/def ::author string?)
(s/def ::copies int?)

(s/def ::book (s/keys :req-un [::title ::author ::copies]))


;;; Why? Why? Why?

(s/valid? ::book {:author :austen :title :emma})
;; => false

;; `s/explain` `s/conform`

(s/explain n-gt-10 44) ;; => nil

(s/explain n-gt-10 4)
;; 4 - failed: (> % 10)

(s/explain ::book {:author :austen :title :emma})
;;:austen - failed: string? in: [:author] at: [:author] spec: :inventory.core/author
;; :emma - failed: string? in: [:title] at: [:title] spec: :inventory.core/title
;; {:author :austen, :title :emma} - failed: (contains? % :copies) spec: :inventory.core/book

(s/conform s-n-s-n ["Emma" 1885 "Jaws" 1974])
;; => {:s1 "Emma", :n1 1885, :s2 "Jaws", :n2 1974}

(s/conform number? 1968);; => 1968


;;; Function specs

;; An inventory is a collection of books.
(s/def :inventory.core/inventory
  (s/coll-of ::book))

(defn find-by-title-1
  [title inventory]
  {:pre [(s/valid? ::title title)
         (s/valid? ::inventory inventory)]}
  (some #(when (= (:title %) title) %) inventory))

;; Register a spec for the find-by-title function

(s/fdef find-by-title
  :args (s/cat :title ::title
               :inventory ::inventory))

;; (st/instrument 'inventory.core/find-by-title)

;; (find-by-title "Emma" ["Emma" "2001" "Jaws"])


;; Spec-driven tests

(defn book-blurb [book]
  (str "The best selling book " (:title book) " by " (:author book)))

(defn check-return [{:keys [args ret]}]
  (let [author (-> args :book :author)]
    (not (neg? (.indexOf ret author)))))

(s/fdef book-blurb
  :args (s/cat :book ::book)
  :ret (s/and string? (partial re-find #"The best selling"))
  :fn check-return)

;; (st/check 'inventory.core/book-blurb)

;; => ({:spec #object[clojure.spec.alpha$fspec_impl$reify__2524 0x75a11e1d "clojure.spec.alpha$fspec_impl$reify__2524@75a11e1d"],
;; :clojure.spec.test.check/ret {:result true, :pass? true, :num-tests 1000, :time-elapsed-ms 564, :seed 1575755867306}, :sym inventory.core/book-blurb})
