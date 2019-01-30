(ns programming-clojure.ch03-collections
  (:require [clojure.repl :refer :all]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Clojure Collections and Data Structures

;;; Maps vectors sets and lists

'(a b :name 12.5) ;; list

['a 'b :name 12.5] ;; vector - tuple

{:name "Chas" :age 31}  ;; map

#{1 2 3} ;; set

{Math/PI "~3.14"
 [:composite "key"] 42
 nil "nothing"}         ;; another map

#{{:first-name "chas" :last-name "emeric"}
  {:first-name "brian" :last-name "carper"}
  {:first-name "christophe" :last-name "grand"}}  ;; a set of maps.


;; Same datatypes exist in other languages but:
;; * They are used as in terms of abstractions, not in concrete implementations.
;; * They are immutable and persistent. Essential to Clojure's style of
;;    functional programming.


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Abstractions of Implementations.

;; It is better to have 100 functions operate over one abstraction than one
;; datastructure.

;; preliminary: operations on a vector.
(def v [1 2 3])
(conj v 4) ;; => [1 2 3 4]
(conj v 4 5) ;; => [1 2 3 4 5]
(seq v) ;; => (1 2 3)

;; `seq` always yields a sequential view over a collection.

;; same operations work on maps
(def m {:a 5 :b 6})
(conj m [:c 7]) ;; => {:a 5, :b 6, :c 7}
(seq m) ;; => ([:a 5] [:b 6])

;; ... sets
(def s #{ 1 2 3})
(conj s 10) ;; => #{1 3 2 10}
(conj s 3 4) ;; => #{1 4 3 2}
(seq s) ;; => (1 3 2)

;; ... and lists
(def lst '(1 2 3))
(conj lst 0) ;; => (0 1 2 3)
(conj lst 0 -1) ;; => (-1 0 1 2 3)
(seq lst) ;; => (1 2 3)

;; The essence of Clojure, have small approachable APIs on which auxiliary
;; functions are built. So many functions are built on `seq` and `conj`.

;; `into` is built on `seq` and `conj`.
(into v [4 5]) ;; => [1 2 3 4 5]
(into m [[:c 7] [:d 8]]) ;; => {:a 5, :b 6, :c 7, :d 8}
(into #{1 2} [2 3 4 5 3 3 2]) ;; => #{1 4 3 2 5}
(into [1] {:a 1 :b 2}) ;; => [1 [:a 1] [:b 2]]



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Interfaces (abstractions) implemented by Clojure collections
;; * Collection
;; * Sequence
;; * Associative
;; * Indexed
;; * Stack
;; * Set
;; * Sorted.


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Collection
;;  * `conj` to add an item to a collection
;;  * `seq` to get a sequence of a collection or `nil`
;;  * `count` to get the number of items of a collection
;;  * `empty` to obtain and empty instance of a collection.
;;  * `=` to determine value equality of a collection compared to one or more
;;        other collections

;; `conj` add an item to a collection
(conj '(1 2 3) 4) ;; => (4 1 2 3)
(into '(1 2 3) [:a :b :c]) ;; => (:c :b :a 1 2 3)

;;  * `empty` to obtain and empty instance of a collection.
(defn swap-pairs
  [sequential]
  (into (empty sequential)  ; create seq of the same type
        (interleave
         (take-nth 2 (drop 1 sequential))
         (take-nth 2 sequential))))

(swap-pairs (apply list (range 10)))
;; => (8 9 6 7 4 5 2 3 0 1)
(swap-pairs (apply vector (range 10)))
;; => [1 0 3 2 5 4 7 6 9 8]

;; a function that allows you to map a given function over every value in a map.
(defn map-map
  [f m]
  (into (empty m)
        (for [[k v] m]
          [k (f v)])))

(map-map inc (hash-map :z 5 :c 6 :a 0))
;; => {:z 6, :c 7, :a 1}
(map-map inc (sorted-map :z 5 :c 6 :a 0))
;; => {:a 1, :c 7, :z 6}

;; sorted in -> sorted out; unsorted in -> unsorted out.

;;  * `count` to get the number of items of a collection
(count [1 2 3]) ;; => 3
(count {:a 1 :b 2 :c 3}) ;; => 3
(count #{1 2 3}) ;; => 3
(count '(1 2 3)) ;; => 3


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Sequences - provide transversal over a collection or successive values over
;;              a computation.

;;  * `seq` produces a sequence over its argument or nil
;;  * `first` `rest` and `next` provide a way to consume sequences.
;;  * `lazy-seq` produces a 'lazy sequence' that is the result of evaluating
;;     an expression.

;; Types that are "seqable".
;; * Clojure collection types
;; * Java collections (java.util).
;; * Java maps
;; * java.lang.CharSequence including String.
;; * java.lang.Iterable
;; * Arrays
;; * `nil` or `null` returned for Java apis
;; * clojure.lang.Seqable interface.

;;;  * `seq` produces a sequence over its argument or nil
(seq "Clojure")   ;; => (\C \l \o \j \u \r \e)
(seq {:a 5 :b 6}) ;; => ([:a 5] [:b 6])
(seq (java.util.ArrayList. (range 5))) ;; => (0 1 2 3 4)
(seq (into-array ["Clojure" "Programming"])) ;; => ("Clojure" "Programming")
(seq []) ;; => nil
(seq '())  ;; => nil
(seq nil) ;; => nil

;; Many functions that work with sequences call seq on their argument(s)
;; implicitly.
(map str "Clojure") ;; => ("C" "l" "o" "j" "u" "r" "e")
(set "Programming") ;; => #{\a \g \i \m \n \o \P \r}

;;  * `first` `rest` and `next` provide a way to consume sequences.
;;     - Traversing and processing sequences
(first "Clojure") ;; => \C
(rest "Clojure") ;; => (\l \o \j \u \r \e)
(next "Clojure") ;; => (\l \o \j \u \r \e)

;; `rest` vs. `next`
(rest [1]) ;; => ()
(next [1]) ;; => nil
(rest nil) ;; => ()
(next nil) ;; => nil
(rest []) ;; => ()
(next []) ;; => nil

(def x [1])
(= (next x)
   (seq (rest x)))

(def y [])
(= (next y)
   (seq (rest y)))

;; Sequences are not iterators
(doseq [x (range 3)]
  (println x))
;; 0
;; 1
;; 2

;; but the seq is immutable

(let [r (range 3)
      rst (rest r)]
  (prn (map str rst))
  (prn (map #(+ 100 %) r))
  (prn (conj r -1) (conj rst 42)))
;; ("1" "2")
;; (100 101 102)
;; (-1 0 1 2) (42 1 2)

;; Sequences are not lists
;; * `count` of seqs carry a cost. Lists keep track of length.
;; * contents of seqs may be computed lazily.
;; * seqs may be infinite.

(let [s (range 1e6)]
  (time (count s)))
;; "Elapsed time: 660.268 msecs"
;; => 1000000

(let [s (apply list (range 1e6))]
  (time (count s)))
;; "Elapsed time: 0.0231 msecs"
;; => 1000000

;; creating seqs
;; `cons` `list*`

;; Returns a new seq where x is the first element and seq is the rest.
(cons 0 (range 1 5)) ;; => (0 1 2 3 4)
(cons :a [:b :c :d]) ;; => (:a :b :c :d)

;; `list*` is a helper for producing seqs with any number of head values,
;; followed by a sequence
(cons 0 (cons 1 (cons 2 (cons 3 (range 4 10)))))
;; => (0 1 2 3 4 5 6 7 8 9)
(list* 0 1 2 3 (range 4 10))
;; => (0 1 2 3 4 5 6 7 8 9)

;; `cons` and `list*` are most used in macros where seqs and lists are
;; equivalent and you just need to prepend a value. Also used in the next
;; step of a lazy sequence


;;  * `lazy-seq` produces a 'lazy sequence' that is the result of evaluating
;;     an expression.
;; => 1000000

(lazy-seq [1 2 3]) ;; => (1 2 3)

(defn random-ints
  "Returns a lazy seq of random integers in the range [0,limit)"
  [limit]
  (lazy-seq
   (cons (rand-int limit)
         (random-ints limit))))  ; lazy recursion

(take 10 (random-ints 50))       ; realize 10
;; => (15 35 34 33 11 32 26 27 45 38)

;; version that prints its realizations
(defn random-ints
  "Returns a lazy seq of random integers in the range [0,limit)"
  [limit]
  (lazy-seq
   (println "realizing a random number")
   (cons (rand-int limit)
         (random-ints limit))))

(def rands (take 10 (random-ints 50)))
(first rands)
;; realizing a random number
;; => 49
(nth rands 3)
;; realizing a random number
;; realizing a random number
;; realizing a random number
;; => 27
(count rands)
;; realizing a random number
;; realizing a random number
;; realizing a random number
;; realizing a random number
;; realizing a random number
;; realizing a random number
;; => 10
(count rands)
;; => 10

;; using built in functions
(repeatedly 10 (partial rand-int 50))
;; => (26 15 25 10 42 34 7 37 20 27)

;; lazy functions - `map` `for` `filter` `take` `drop`
;; data sources - `file-seq` `line-seq` `xml-seq`

;; `next` always checks next value so forces realizations of lazy sequencs
(def x (next (random-ints 50)))
;; realizing a random number
;; realizing a random number

;; `rest` blindly returns the head of next.
(def y (rest (random-ints 50)))
;; realizing a random number

;; Sequential destructuring always uses `next`.
(let [[x & rest] (random-ints 50)])
;; realizing a random number

;; `dorun` retains the computations
;; `doall` drops the contents - for side effects.

(dorun (take 5 (random-ints 50)))
;; realizing a random number
;; realizing a random number
;; realizing a random number
;; realizing a random number
;; realizing a random number

;; Code defining lazy sequences should minimize side effects.

;; pattern: given one or more data sources, you extract a sequence from it,
;; process it, and turn it back into a more appropriate data structure.
(apply str (remove (set "aeiouy")
                   "vowels are useless! or maybe not..."))
;; => "vwls r slss! r mb nt..."
;; String -> Sequence -> String

;; Head retention
(split-with neg? (range -5 5))
;; => [(-5 -4 -3 -2 -1) (0 1 2 3 4)]

;; This will cause an out of memory error because of head retention
;; (let [[t d] (split-with #(< % 12) (range 1e8))]
;;   [(count t) (count d)])

;; but reversing the order of evaluation solves the problems
;; (let [[t d] (split-with #(< % 12) (range 1e8))]
;;   [(count t) (count d)])
;; => [12 99999988]


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Associative

;; Data structures that link keys and values
;; * `assoc` which establishes new associations between keys and values within
;;    the given collection
;; * `dissoc` which drops associations for given keys from the collection
;; * `get` which looks up the value for a particular key in a collection
;; * `contains?` which is a predicate that returns true only if the collection
;;    has a value associated with the given key

(def m {:a 1 :b 2 :c 3})
(get m :b) ;; => 2
(get m :d) ;; => nil
(get m :d "not-found") ;; => "not-found"
(assoc m :d 4) ;; => {:a 1, :b 2, :c 3, :d 4}
(dissoc m :b) ;; => {:a 1, :c 3}

;; multiple entities.
(assoc m
       :x 4
       :y 5
       :z 6)
;; => {:a 1, :b 2, :c 3, :x 4, :y 5, :z 6}

(dissoc m :a :c) ;; => {:b 2}

;; vectors
(def v [1 2 3])
(get v 1) ;; => 2
(get v 10) ;; => nil
(get v 10 "not-found") ;; => "not-found"
(assoc v
       1 4
       0 -12
       2 :p)
;; => [-12 4 :p]

;; you can also use assoc to append to vectors\with the caveat that you need to
;; know what the new valuefs index will be:
(assoc v 3 10) ;; => [1 2 3 10]

;; ... sets  - values are associated with themselves
(get #{1 2 3} 2) ;; => 2
(get #{1 2 3} 4) ;; => nil
(get #{1 2 3} 4 "not-found") ;; => "not-found"

(when (get #{1 2 3} 2)
  (println "it contains `2` !"))
;; it contains `2` !


;;; * `contains?` which is a predicate that returns true only if the collection
;;     has a value associated with the given key

(contains? [1 2 3] 0) ;; => true
(contains? {:a 5 :b 6} :b) ;; => true
(contains? {:a 5 :b 6} 42) ;; => false
(contains? #{1 2 3} 1) ;; => true

;; can't search for values in a vector
(contains? [1 2 3] 3) ;; => false
(contains? [1 2 3] 2) ;; => true
(contains? [1 2 3] 0) ;; => true
;; zero indexed

;;; Beware of `nil` values.
;;  `get` returns `nil` when therefs no entry for a given key and when no
;;   default value is provided. However, `nil` is a perfectly valid value.

(get {:ethel nil} :lucy)  ;; => nil
(get {:ethel nil} :ethel) ;; => nil

;; use `find` - works very much like `get` except that, instead of returning the
;;  associated value, it returns the whole entry; or, `nil` when not found.

(find {:ethel nil} :lucy)  ;; => nil
(find {:ethel nil} :ethel) ;; => [:ethel nil]

;; `find` works very well with destructuring and conditional forms like `iflet`
;; `(or when-let)`

(if-let [e (find {:a 5 :b 6} :a)]
  (format "found %s => %s" (key e) (val e))
  "not found")
;; => "found :a => 5"

(if-let [e (find {:a 5 :b 6} :c)]
  (format "found %s => %s" (key e) (val e))
  "not found")
;; => "not found"


(if-let [[k v] (find {:a 5 :b 6} :a)]
  (format "found %s => %s" k v)
  "not found")
;; => "found :a => 5"

(if-let [[k v] (find {:a 5 :b 6} :c)]
  (format "found %s => %s" k v)
  "not found")
;; => "not found"

;; beware of `false` too.
