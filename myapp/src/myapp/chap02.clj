(ns myapp.chap02)

;;; vectors

[1 2 3 4]

[true 3 "four" 5]

[1 [true 3 "four" 5] 6]

(vector true 3 "four" 5)
;; => [true 3 "four" 5]

;; same as []
(vector)

(def novels ["Emma" "Coma" "War and Peace"])

(first novels)
;; => "Emma"
(rest novels)
;; => ("Coma" "War and Peace")
(rest (rest novels))
;; => ("War and Peace")
(rest ["One"])
;; => ()
(rest [])
;; => ()

(def year-book ["1491" "April 1865" "1984" "2001"])
(def third-book (first (rest year-book)))
third-book ;; => "April 1865"
(nth year-book 2)
;; => "1984"
(year-book 2)
;; => "1984"
(conj novels "Carrie")
;; => ["Emma" "Coma" "War and Peace" "Carrie"]
(cons "Carrie" novels)
;; => ("Carrie" "Emma" "Coma" "War and Peace")

;;; Lists

'(1 2 3)
;; =>  (1 2 3)
'(1 2 3 "four" 5 "six")
'(1 .0 2.999 "four" 5.001 "six")
'([1 2 ("a" "list" "inside" "vector")] "inside" "a" "list")

(list 1 2 3 "four" 5 "six")
;; => (1 2 3 "four" 5 "six")

(def poems '("iliad" "Odyssey" "Now We Are Six"))

(count poems)
;; => 3
(first poems)
;; => "iliad"
(rest poems)
;; => ("Odyssey" "Now We Are Six")
(nth poems 2)
;; => "Now We Are Six"

(conj poems "Jabberwocky")
;; => ("Jabberwocky" "iliad" "Odyssey" "Now We Are Six")

(def vector-poems ["Iliad" "Odyssey" "Now We Are Six"])
(conj vector-poems "Jabberwocky")
;; => ["Iliad" "Odyssey" "Now We Are Six" "Jabberwocky"]

