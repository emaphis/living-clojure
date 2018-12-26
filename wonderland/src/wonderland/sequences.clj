(ns wonderland.sequences
  (:require [clojure.repl :as r]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Everything is a sequence

;; (first aseq)

;; (rest aseq)

;; (cons elem aseq)

;; clojure.lang.ISeq

;; (seq coll) ;; return a seq or nil if empty

;; (next aseq) => (seq (rest aseq))

;; clarify rest/next behavoir
;; (rest ())        => ()
;; (next ())        => nil
;; (seq (rest ()))  => nil


;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Lists

(first '(1 2 3))
;; => 1

(rest '(1 2 3))
;; => (2 3)

(cons 0 '(1 2 3))
;; => (0 1 2 3)


;;; Other data structures too.

(first [1 2 3])
;; => 1

(rest [1 2 3])
;; => (2 3)

(cons 0 [1 2 3])  ;; expensive
;; => (0 1 2 3)

(class [1 2 3])
;; => clojure.lang.PersistentVector

(class (rest [1 2 3]))
;; => clojure.lang.PersistentVector$ChunkedSeq


;;;;;;;;;;;;;;;;;;;;;
;;; Maps and Sets

(first {:fname "Aaron" :lname "Bedra"})
;; => [:fname "Aaron"]

(rest {:fname "Aaron" :lname "Bedra"})
;; => ([:lname "Bedra"])

(cons [:mname  "James"] {:fname "Aaron" :lname "Bedra"})
;; => ([:mname "James"] [:fname "Aaron"] [:lname "Bedra"])

(first #{:the :quick :brown :fox})
;; => :fox

(rest #{:the :quick :brown :fox})
;; => (:the :quick :brown)

(cons :jumped #{:the :quick :brown :fox})
;; => (:jumped :fox :the :quick :brown)

;; elements don't come back in the order entered
#{:the :quick :brown :fox}
;; => #{:fox :the :quick :brown}

(sorted-set :the :quick :brown :fox)
;; => #{:brown :fox :quick :the}

{:a 1 :b 2 :c 3 :d 4 :e 5}
;; => {:a 1, :b 2, :c 3, :d 4, :e 5}


;; 'conj' and 'into' add elements to a list in natural order.
(conj '(1 2 3) :a)
;; => (:a 1 2 3)

(into '(1 2 3) '(:a :b :c))
;; => (:c :b :a 1 2 3) ; like a stack

(conj [1 2 3] :a)
;; => [1 2 3 :a]

(into [1 2 3] [:a :b :c])
;; => [1 2 3 :a :b :c]


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; The sequence library

;;; Creating sequences

;; (range start? end step?)

(range 10)
;; => (0 1 2 3 4 5 6 7 8 9)

(range 10 20)
;; => (10 11 12 13 14 15 16 17 18 19)

(range 1 25 2)
;; => (1 3 5 7 9 11 13 15 17 19 21 23)


;; (repeat n x) - repeat x n times

(repeat 5 1)
;; => (1 1 1 1 1)

(repeat 10 "x")
;; => ("x" "x" "x" "x" "x" "x" "x" "x" "x" "x")

;; (iterate f x) - applies function 'f' to x then to it's output.
;; (take n sequence) - takes the first 'n' items of a given sequence

(take 10 (iterate inc 1))
;; => (1 2 3 4 5 6 7 8 9 10)

(defn whole-numbers [] (iterate inc 1))

(take 12 (whole-numbers))
;; => (1 2 3 4 5 6 7 8 9 10 11 12)

;; (repeat n) - returns a lazy infinite sequence

(take 20 (repeat 1))
;; => (1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1)

;; (cycle coll) - cycles a collection infinitely

(take 10 (cycle (range 3)))
;; => (0 1 2 0 1 2 0 1 2 0)

;; (interleave & colls) - interleave values from given collections

(interleave (whole-numbers) ["A" "B" "C" "D" "E" "F"])
;; => (1 "A" 2 "B" 3 "C" 4 "D" 5 "E" 6 "F")

;; (interpose separator coll) - returns a coll with each element segregated by a separator
(interpose "," ["apples" "bananas" "grapes"])
;; => ("apples" "," "bananas" "," "grapes")

(apply str (interpose \, ["apples" "bananas" "grapes"]))
;; => "apples,bananas,grapes"


(use '[clojure.string :only (join)])

(join \, ["apples" "bananas" "grapes"])
;; => "apples,bananas,grapes"

;; take an arbitrary number of arguments and create a collection

;; (list & elements)
;; (vector & elements)
;; (hash-set & elements)
;; (hash-map & elements)
(set [1 2 3])
;; => #{1 3 2}

(hash-set 1 2 3)
;; => #{1 3 2}

(vec (range 3))
;; => [0 1 2]

(vector 0 1 2 3)
;; => [0 1 2 3]


;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Filtering sequences

;; (filter pred coll)

(take 10 (filter even? (whole-numbers)))
;; => (2 4 6 8 10 12 14 16 18 20)


(take 10 (filter odd? (whole-numbers)))
;; => (1 3 5 7 9 11 13 15 17 19)

;; (take-while pred coll) - form a sequence while pred is true

(take-while (complement #{\a\e\i\o\u}) "the-quick-brown-fox")
;; => (\t \h)

;; (drop-while pred coll) - drops while true

(drop-while (complement #{\a\e\i\o\u}) "the-quick-brown-fox")
;; => (\e \- \q \u \i \c \k \- \b \r \o \w \n \- \f \o \x)


;; (split-at index coll)
;; (split-with pred coll)

(split-at 5 (range 10))
;; => [(0 1 2 3 4) (5 6 7 8 9)]

(split-with #(<= % 10) (range 0 20 2))
;; => [(0 2 4 6 8 10) (12 14 16 18)]


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Sequence predicates

;; (every pred coll) - asks pred of every element

(every? odd? [1 3 5])
;; => true

(every? odd? [1 3 5 8])
;; => false

;; (some pred coll) - returns first non false value for it's predicate

(some even? [1 2 3])
;; => true

(some even? [1 3 5])
;; => nil

(some identity [nil false 1 nil 2])
;; => 1

;; (not-every? pred coll)
;; (not-any? pred coll)

(not-every? even? [1 2 3 4 5])
;; => true

(not-every? even? [2 4 6 8])
;; => false

(not-every? even? [2 3 6 8])
;; => true

(not-any? even? [1 2 3 4 5 6])
;; => false


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Transforming sequences

;; (map f coll)

(map #(+ 2 %) [1 2 3 4 5])
;; => (3 4 5 6 7)

(map #(format "<%s>%s</%s>" %1 %2 %1)
     ["h1" "h2" "h3" "h1"] ["the" "quick" "brown" "fox"])
;; => ("<h1>the</h1>" "<h2>quick</h2>" "<h3>brown</h3>" "<h1>fox</h1>")


;; (reduce f coll)

(reduce + (range 1 11))
;; => 55

(reduce * (range 1 11))
;; => 3628800


;; (sort comp? coll)
;; (sort a-fn comp? coll)

(sort [42 1 7 11])
;; => (1 7 11 42)

(sort-by #(.toString %) [42 1 7 11]) ; lexical
;; => (1 11 42 7)

(sort > [42 1 7 11])
;; => (42 11 7 1)

(sort-by :grade > [{:grade 83} {:grade 90} {:grade 77}])
;; => ({:grade 90} {:grade 83} {:grade 77})


;; for comprehension

(for [word ["the" "quick" "brown" "fox"]]
  (format "<p>%s</p>" word))
;; => ("<p>the</p>" "<p>quick</p>" "<p>brown</p>" "<p>fox</p>")

(take 10 (for [n (whole-numbers) :when (even? n)] n))
;; => (2 4 6 8 10 12 14 16 18 20)

(for [n (whole-numbers) :while (even? n)] n)
;; => ()

(for [file "ABCDEFGH" rank (range 1 9)] (format "%c%d" file rank))
;; => ("A1" "A2" "A3" "A4" "A5" "A6" "A7" "A8" "B1" "B2" "B3" "B4" "B5" "B6" "B7" "B8" "C1" "C2" "C3" "C4" "C5" "C6" "C7" "C8" "D1" "D2" "D3" "D4" "D5" "D6" "D7" "D8" "E1" "E2" "E3" "E4" "E5" "E6" "E7" "E8" "F1" "F2" "F3" "F4" "F5" "F6" "F7" "F8" "G1" "G2" "G3" "G4" "G5" "G6" "G7" "G8" "H1" "H2" "H3" "H4" "H5" "H6" "H7" "H8")

(for [rank (range 1 9) file "ABCDEFGH"] (format "%c%d" file rank))
;; => ("A1" "B1" "C1" "D1" "E1" "F1" "G1" "H1" "A2" "B2" "C2" "D2" "E2" "F2" "G2" "H2" "A3" "B3" "C3" "D3" "E3" "F3" "G3" "H3" "A4" "B4" "C4" "D4" "E4" "F4" "G4" "H4" "A5" "B5" "C5" "D5" "E5" "F5" "G5" "H5" "A6" "B6" "C6" "D6" "E6" "F6" "G6" "H6" "A7" "B7" "C7" "D7" "E7" "F7" "G7" "H7" "A8" "B8" "C8" "D8" "E8" "F8" "G8" "H8")


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Lazy and Infinite Sequences

;; see primes.clj

(use 'wonderland.primes)

(def ordinals-and-primes (map vector (iterate inc 1) primes))

(take 5 (drop 1000 ordinals-and-primes))
;; => ([1001 7927] [1002 7933] [1003 7937] [1004 7949] [1005 7951])


;;; forcing sequences

(def x (for [i (range 1 3)] (do (println i) i)))
;; => #'wonderland.sequences/x

(doall x)
;; 1
;; 2
;; => (1 2)

(def x (for [i (range 1 3)] (do (println i) i)))

(dorun x)
;; 1
;; 2
;; => nil


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Clojure make Java seq-able

;; Collections
;; Regular expressions
;; File system traveral
;; XML processing
;; Relational database results

