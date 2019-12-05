(ns myapp.chap03)

;;; Chapter 3 - Maps, Keywords, and Sets

;;; Map

{"title" "Oliver Twist" "author" "Dickens" "published" 1838}
;; => {"title" "Oliver Twist", "author" "Dickens", "published" 1838}

(hash-map "title" "Oliver Twist"
          "author" "Dickens"
          "published" 1838)
;; => {"author" "Dickens", "published" 1838, "title" "Oliver Twist"}

(def book {"title" "Oliver Twist",
           "author" "Dickens",
           "published" 1838})

(get book "published");; => 1838
(book "published");; => 1838

(book "copyright");; => nil


;;; Keywords

:title ;; => :title
:author ;; => :author
:published ;; => :published
:word-count ;; => :word-count
:preface&introduction ;; => :preface&introduction
:chapter-1-and-2 ;; => :chapter-1-and-2

;; keywords and maps

(def book
  {:title "Oliver Twist" :author "Dickens" :published 1838})

(println "Title:" (book :title))
(println "By:" (book :author))
(println "Published:" (book :published))

(book :title) ;; => "Oliver Twist"
(:title book) ;; => "Oliver Twist"

;; Changing a map without changing it.

(assoc book :page-count 362)
;; => {:title "Oliver Twist", :author "Dickens", :published 1838, :page-count 362}

;; updating info
(assoc book :page-count 362 :title "War & Peace")
;; => {:title "War & Peace", :author "Dickens", :published 1838, :page-count 362}

(dissoc book :published)
;; => {:title "Oliver Twist", :author "Dickens"}

(dissoc book :title :author :published) ;; => {}

;; assoc works with vectors two

(keys book) ;; => (:title :author :published)
(vals book) ;; => ("Oliver Twist" "Dickens" 1838)

;;; Sets

(def genres #{:sci-fi :romance :mystery})

(def authors #{"Dickens" "Austen" "King"})

(contains? authors "Austen")  ;; => true
(contains? genres "Austen") ;; => false

(authors "Austen") ;; => "Austen"
(genres :historical) ;; => nil

(:sci-fi genres) ;; => :sci-fi
(:historical genres) ;; => nil

(def more-authors (conj authors "Clarke"))

;; not an error
(conj more-authors "Clarke") ;; => #{"King" "Dickens" "Clarke" "Austen"}

(disj more-authors "King") ;; => #{"Dickens" "Clarke" "Austen"}
