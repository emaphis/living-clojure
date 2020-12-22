(ns workshop.chap04
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [semantic-csv.core :as sc]))

(use 'clojure.repl)

;;; Mapping and Filtering

;;; `map`

(map inc [1 2 3])
;; => (2 3 4)


;;; Exercise 4.01: Working with map

;; 1. `map` one to one relationship with sequences
(map (fn [i] (* i 10)) [1 2 3 4 5])
;; => (10 20 30 40 50)

;; 2. measure word length
(map count ["Let's" "measure" "word" "lenght" "now"])
;; => (5 7 4 6 3)

;; 3. add a label
(map (fn [w] (str w ": " (count w))) ["Let's" "measure" "word" "length" "now"])
;; => ("Let's: 5" "measure: 7" "word: 4" "length: 6" "now: 3")


;;; `filter`

(filter keyword? ["a" :b "c" :d "e" :f "g"])
;; => (:b :d :f)


;;; Exercise 4.02: Getting Started with filter

;; 1.  `odd?`
(odd? 5)
;; => true

;; 2.
(odd? 6)
;; => false

;; 3. filter odd
(filter odd? [1 2 3 4 5])
;; => (1 3 5)

;; 4. `remove` removes objects where the predicate is true.
(remove odd? [1 2 3 4 5])
;; => (2 4)

;; 5. always return a sequence
(filter (constantly true) [1 2 3 4 5])
;; => (1 2 3 4 5)

(filter (constantly false) [1 2 3 4 5])
;; => ()

;;; other members of the filter family - `take-while`, `drop-while`

(take 3 [1 2 3 4 5])
;; => (1 2 3)

(drop 3 [1 2 3 4 5])
;; => (4 5)

(take-while #(> 10 %) [2 9 4 12 3 99 1])
;; => (2 9 4)

(drop-while #(> 10 %) [2 9 4 12 3 99 1])
;; => (12 3 99 1)


;;; Exercise 4.03: Partitioning a Sequence with take-while and drop-while

;; 1.
(def students [{:name "Eliza" :year 1994}
               {:name "Salma" :year 1995}
               {:name "Jodie" :year 1997}
               {:name "Kaitlyn" :year 2000}
               {:name "Alice" :year 2001}
               {:name "Pippa" :year 2002}
               {:name "Fleur" :year 2002}])

;; 2. a predicate
#(< (:year %) 2000)

;; 3.
(take-while #(< (:year %) 2000) students)
;; ({:name "Eliza", :year 1994}
;;  {:name "Salma", :year 1995}
;;  {:name "Jodie", :year 1997})

(drop-while #(< (:year %) 2000) students)
;; ({:name "Kaitlyn", :year 2000}
;;  {:name "Alice", :year 2001}
;;  {:name "Pippa", :year 2002}
;;  {:name "Fleur", :year 2002})


;;; Using `map` and `filter` together

(map (fn [n] (* 10 n))
     (filter odd? [1 2 3 4 5]))
;; => (10 30 50)


;;; Threading Macros

;; using temp values
(def filtered (filter odd? [1 2 3 4 5]))
(map (fn [n] (* 10 n)) filtered)
;; => (10 30 50)

;; using threading macro
(->> [1 2 3 4 5]
     (filter odd?)
     (map (fn [n] (* 10 n))))
;; => (10 30 50)

(->> [1 2 3 4 5] (filter odd?)) ;; => (1 3 5)
;; is the same as...
(filter odd? [1 2 3 4 5]) ;; => (1 3 5)


;;; Using Lazy Sequences

(range 1 6)
;; => (1 2 3 4 5)

;; saving a lazy sequence
(def our-seq (range 100))

;; realize first
(first our-seq)
;; => 0

;; realize all
(last our-seq)
;; => 99

;; `map` `filter` `remove` are lazy
;; `count` `sort` `last` are not lazy


;;; Exercise 4.04: Watching Lazy Evaluation

;; 1.
(defn our-range [limit]
  (take-while #(< % limit) (iterate inc 0)))

;; 2.
(our-range 5)
;; => (0 1 2 3 4)

;; 3. multiply by 10
(map #(* 10 %) (our-range 5))
;; => (0 10 20 30 40)

;; 4. now side effects
(map (fn [i] (print ".") (* i 10)) (our-range 5))

;; 5. store our lazy sequence.
(def by-ten (map (fn [i] (print ".") (* i 10)) (our-range 5)))

;; 6. evaluate
by-ten


(->> (range)
     (map #(* 10 %))
     (take 5))  ; doesn't run until infinity
;; => (0 10 20 30 40)


;;; Exercise 4.05: Creating Our Own Lazy Sequence

;; 1. lazy sequence
(def our-randoms (repeatedly (partial rand-int 100)))

;; 2. take some
(take 20 our-randoms)
;; => (8 85 94 22 67 59 73 76 56 80 81 74 99 57 83 7 51 53 20 55)

;; 3. wrap this in a function
(defn some-random-integers [size]
  (take size (repeatedly (fn [] (rand-int 100)))))

;; 4.
(some-random-integers 12)
;; => (54 38 88 50 17 53 92 82 42 38 6 29)


;;; Common Idioms and Patterns

;; Anonymous Functions
(def my-data [1 2 3 4 5])

;; multiply elements of `my-data` by 10
(defn mult-10 [n] (* 10 n))
(map mult-10 my-data)
;; => (10 20 30 40 50)

;; mult-10 isn't important enough to name if it's used only once
(map (fn [n] (* 10 n)) my-data)
;; => (10 20 30 40 50)

;; shorter more concise.
(map #(* 10 %) my-data)
;; => (10 20 30 40 50)

;; use a function that creates an anonymous function
(def apart (partial * 10))
(apart 5)
;; => 50

(map (partial * 10) my-data)
;; => (10 20 30 40 50)


;;; Keywords as Functions

(:my-field {:my-field 42})
;; => 42


;;; Exercise 4.06: Extracting Data from a List of Maps

;; data
(def game-users
  [{:id 9342
    :username "speedy"
    :current-points 45
    :remaining-lives 2
    :experience-level 5
    :status :active}
   {:id 9854
    :username "stealthy"
    :current-points 1201
    :remaining-lives 1
    :experience-level 8
    :status :speed-boost}
   {:id 3014
    :username "sneaky"
    :current-points 725
    :remaining-lives 7
    :experience-level 3
    :status :active}
   {:id 2051
    :username "forgetful"
    :current-points 89
    :remaining-lives 4
    :experience-level 5
    :status :imprisoned}
   {:id 1032
    :username "wandering"
    :current-points 2043
    :remaining-lives 12
    :experience-level 7
    :status :speed-boost}
   {:id 7213
    :username "slowish"
    :current-points 143
    :remaining-lives 0
    :experience-level 1
    :status :speed-boost}
   {:id 5633
    :username "smarter"
    :current-points 99
    :remaining-lives 4
    :experience-level 4
    :status :terminated}
   {:id 3954
    :username "crafty"
    :current-points 21
    :remaining-lives 2
    :experience-level 8
    :status :active}
   {:id 7213
    :username "smarty"
    :current-points 290
    :remaining-lives 5
    :experience-level 12
    :status :terminated}
   {:id 3002
    :username "clever"
    :current-points 681
    :remaining-lives 1
    :experience-level 8
    :status :active}])

;; 1.
(map (fn [player ] (:current-points player )) game-users)
;; => (45 1201 725 89 2043 143 99 21 290 681)

;; 2.
(map :current-points game-users)
;; => (45 1201 725 89 2043 143 99 21 290 681)


;;; Sets as Predicates

(def alpha-set (set [:a :b :c]))

(alpha-set :z)
;; => nil
(alpha-set :a)
;; => :a

(hash-set :a :b :c)
;; => #{:c :b :a}

(def animal-names ["turtle" "horse" "cat" "frog" "hawk" "worm"])

(remove (fn [animal-name]
          (or (= animal-name "horse")
              (= animal-name "cat")))
        animal-names)
;; => ("turtle" "frog" "hawk" "worm")

;; use set predicate
(remove #{"horse" "cat"} animal-names)
;; => ("turtle" "frog" "hawk" "worm")

;; Filtering on a Keyword with `comp` and a Set

(require '[clojure.string :as str])
(defn normalize [s] (str/trim (str/lower-case s)))

(normalize "  Ok THIS is a TEST ")
;; => "ok this is a test"

;; more concise
(def normalizer (comp str/trim str/lower-case))

(normalizer "  Some Information ")
;; => "some information"

;; (comp function-c function-b function-a)
;; same as
;; (fn [x] (function-c (function-b (function-a)))

;; building functions on the fly
(def remove-words #{"and" "an" "a" "the" "of" "is"})

(remove (comp remove-words str/lower-case str/trim) ["February" " THE " "4th"])
;; => ("February" "4th")


;;; Exercise 4.07: Using comp and a Set to Filter on a Keyword

;; 1. use old `game-users`
(map :current-points game-users)
;; => (45 1201 725 89 2043 143 99 21 290 681)

;; 2.
(def keep-statues #{:active :imprisoned :speed-boost})

;; 3. use `keep-statues` as function
(filter (fn [player] (keep-statues (:status player))) game-users)

(map :status *1)
;; => (:active :speed-boost :active :imprisoned :speed-boost :speed-boost :active :active)

(filter (comp keep-statues :status) game-users)

;; 4. Use `->>`
(->> game-users
     (filter (comp #{:active :imprisoned :speed-boost} :status))
     (map :current-points))
;; => (45 1201 725 89 2043 143 21 681)


;; Returning a List Longer than the input with `mapcat`

(def alpha-lc [ "a" "b" "c" "d" "e" "f" "g" "h" "i" "j" ])

(mapcat (fn [letter] [letter (str/upper-case letter)]) alpha-lc)
;; => ("a" "A" "b" "B" "c" "C" "d" "D" "e" "E" "f" "F" "g" "G" "h" "H" "i" "I" "j" "J")


;; Mapping with Multiple Inputs

(map (fn [a b] (+ a b)) [5 8 3 1 2] [5 2 7 9 8])
;; => (10 10 10 10 10)

(defn our-zipmap [xs ys]
  (->> (map (fn [x y] [x y]) xs ys)
       (into {})))

(our-zipmap [:a :b :c] [1 2 3])
;; => {:a 1, :b 2, :c 3}
(source zipmap)


;; multiple inputs - add a number to each item
(def meals ["breakfast" "lunch" "dinner" "midnight snack"])

(map (fn [idx meal] (str (inc idx) ". " meal)) (range) meals)
;; => ("1. breakfast" "2. lunch" "3. dinner" "4. midnight snack")

;; (inc idx) start indexes at 1

;; special function
(map-indexed (fn [idx meal] (str (inc idx) ". " meal)) meals)
;; => ("1. breakfast" "2. lunch" "3. dinner" "4. midnight snack")

;; looking ahead or behind in a collection - windowing
;; (map (fn [x next-x] (do-something x next-x))
;;      my-sequence
;;      (rest my-sequence))


;;; Exercise 4.08: Identifying Weather Trends

;; 1. weather data
(def temperature-by-day
  [18 23 24 23 27 24 22 21 21 20 32 33 30 29 35 28 25 24 28 29 30])

;; 2. expression with windowing offsets
(map (fn [today yesterday]
       (cond (> today yesterday) :warmer
             (< today yesterday) :colder
             (= today yesterday) :unchanged))
     (rest temperature-by-day)  ; look backward
     temperature-by-day)
;; => (:warmer :warmer :colder :warmer :colder :colder :colder :unchanged :colder :warmer :warmer :colder :colder :warmer :colder :colder :colder :warmer :warmer :warmer)

;;; Consuming Extracted Data with `apply`
;;   producing a summary with `min` `max` or `+` with `apply`

(apply max [3 9 6])
;; => 9
(max 3 9 6)
;; => 9
(apply + [3 9 6])
;; => 18

#_(let [a 5
        b nil
        c 18]
    (+ a b c))

;; filter and apply
(let [a 5
      b nil
      c 18]
  (apply + (filter integer? [a b c])))
;; => 23

;; fails - min always needs arguemnts
#_(apply min [])

(apply min 0 [])
;; => 0


;;; Exercise 4.09: Finding the Average Weather Temperature

;; 1. use already defined temperature-by-day
temperature-by-day
;; => [18 23 24 23 27 24 22 21 21 20 32 33 30 29 35 28 25 24 28 29 30]

;; 2. use (apply + _) to calculate average temperature
(let [total (apply + temperature-by-day)
      c (count temperature-by-day)]
  (/ total c))
;; => 26


;;; Activity 4.01: Using map and filter to Report Summary Information


(defn max-value-by-status [field status users]
  (->> users
       (filter (fn [st] (= (:status st) status)))
       (map field)
       (apply max 0)))

(defn min-value-by-status [field status users]
  (->> users
       (filter (fn [st] (= (:status st) status)))
       (map field)
       (apply min 0)))

game-users

(max-value-by-status :current-points :active game-users)
;; => 725
(min-value-by-status :current-points :active game-users)
;; => 21\
(max-value-by-status :experience-level :imprisoned game-users)
;; => 5
(min-value-by-status :experience-level :imprisoned game-users)
;; => 0


;;; Importing a Dataset from a CSV file.

;;; Exercise 4.10: Importing Data from a CSV File

(with-open [r (io/reader "c:/test/tennis-data/match_scores_1991-2016_unindexed_csv.csv")]
  (count (csv/read-csv r)))
;; => 95360

;; Real-World Laziness

;;; Exercise 4.11: Avoiding Lazy Evaluation Traps with Files

(with-open [r (io/reader "c:/test/tennis-data/match_scores_1991-2016_unindexed_csv.csv")]
  (->> (csv/read-csv r)
       (map #(nth % 7))
       (take 6)
       (doall)))  ; force lazy evaluation
;; => ("winner_name" "Nicklas Kulti" "Michael Stich" "Nicklas Kulti" "Jim Courier" "Michael Stich")


;;; Convenient CSV Parsing  - see tennis.clj
;;; Activity 4.02: Arbitrary Tennis Rivalries - see tenni.clj
