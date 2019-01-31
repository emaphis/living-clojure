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
;; `rest` returns and empty sequence, while `next` returns `nil`.
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

;; identity
;; (= (next ...)
;;    (seq (rest ...)))

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

;; using built in functions using composition
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

;; you can also use assoc to append to vectors \ with the caveat that you need to
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


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Indexed
;;  `nth`
;; indices are the new pointers - low level
;; corollary - IndexOutOfBoundsException is the new core-dump.

;; comparing `nth` and `get` on vectors
(nth [:a :b :c] 2) ;; => :c
(get [:a :b :c] 2) ;; => :c

;; (nth [:a :b :c] 3)
;; 1. Unhandled java.lang.IndexOutOfBoundsException
(get [:a :b :c] 3) ;; => nil

;; (nth [:a :b :c] -1)
;; 1. Unhandled java.lang.IndexOutOfBoundsException
(get [:a :b :c] -1) ;; => nil

;; but you can provide a default return value
(nth [:a :b :c] -1 :not-found) ;; => :not-found
(get [:a :b :c] -1 :not-found) ;; => :not-found

;; different meanings:  `nth` only works with numerical indices and works on
;; many things that can be numerically indexed: vectors, lists, sequences,
;; Java arrays, Java lists, strings, and regular expression matches.
;; `get` is more general, it works on any associative type and treats numberical
;; indices as keys.

;; `get` is more resilient. Returns `nil` when key is not found and returns `nil`
;; when the subject is not supported.
(get 42 0) ;; => nil
;; (nth 42 0)
;; 1. Unhandled java.lang.UnsupportedOperationException
;; nth not supported on this type: Long


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Stack

;; Last-in, first-out semantics (LIFO)
;; * `conj` for pushing a value onto the stack (conveniently reusing the
;;   collection-generalized operation)
;; * `pop`  for obtaining the stack with its top value removed
;; * `peek` for obtaining the value on the top of the stack

;; lists and vectors can be used as stacks

(conj '() 1) ;; => (1)
(conj '(2 1) 3) ;; => (3 2 1)
(peek '(3 2 1)) ;; => 3
(pop '(3 2 1)) ;; => (2 1)
(pop '(2 1))  ;; => (1)
(pop '(1))  ;; => ()

(conj [] 1)   ;; => [1]
(conj [1 2] 3) ;; => [1 2 3]
(peek [1 2 3]) ;; => 3
(pop [1 2 3]) ;; => [1 2]
(pop [1]) ;; => []


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Set

;; Associative
(get #{1 2 3} 2) ;; => 2
(get #{1 2 3} 4) ;; => nil
(get #{1 2 3} 4 :not-found) ;; => :not-found

;; To be complete set requires `disj`
(disj #{1 2 3} 3)   ;; => #{1 2}
(disj #{1 2 3} 3 1) ;; => #{2}

;; sets are mostly used as a concrete type
;; clojure.set - provides the set operations....
;; `subset?` `superset?` `union` `intersection` `project`


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Sorted
;; Collections that participate in the sorted abstraction guarantee that their
;; values will be maintained in a stable ordering that is optionally defined by
;; a predicate or implementation of a special `comparator` interface.
;; * `rseq` which returns a seq of a collectionfs values in reverse, with the
;;   guarantee that doing so will return in constant time
;; * `subseq` which returns a seq of a collectionfs values that fall within a
;;   specified range of keys
;; * `rsubseq` the same as subseq, but the seq is in reversed order

(def sm (sorted-map :z 5 :x 9 :y 0 :b 2 :a 3 :c 4))
sm        ;; => {:a 3, :b 2, :c 4, :x 9, :y 0, :z 5}
(rseq sm) ;; => ([:z 5] [:y 0] [:x 9] [:c 4] [:b 2] [:a 3])
(subseq sm <= :c) ;; => ([:a 3] [:b 2] [:c 4])
(subseq sm > :b <= :y)  ;; => ([:c 4] [:x 9] [:y 0])
(rsubseq sm > :b <= :y) ;; => ([:y 0] [:x 9] [:c 4])

;; The compare function defines the default sort: ascending, supporting all
;; Clojure scalars and sequential collections, sorting lexicographically at each
;; level

;; equal
(compare 2 2) ;; => 0
;; less than
(compare "ab" "abc") ;; => -1
;; greater than
(compare ["a" "b" "c"] ["a" "b"]) ;; => 1
;; less than
(compare ["a" 2] ["a" 2 0]) ;; => -1


;;; Comparators and predicates to define ordering.

(sort < (repeatedly 10 #(rand-int 100)))
;; => (13 22 29 34 41 47 50 50 76 87)
(sort-by first > (map-indexed vector "Clojure"))
;; => ([6 \e] [5 \r] [4 \u] [3 \j] [2 \o] [1 \l] [0 \C])

;; `-` negates a sort.
(sorted-map-by compare :z 5 :x 9 :y 0 :b 2 :a 3 :c 4)
;; => {:a 3, :b 2, :c 4, :x 9, :y 0, :z 5}
(sorted-map-by (comp - compare) :z 5 :x 9 :y 0 :b 2 :a 3 :c 4)
;; => {:z 5, :y 0, :x 9, :c 4, :b 2, :a 3}

;; sort order defines equality within a sorted map or set

(defn magnitude
  [x]
  (-> x Math/log10 Math/floor))

(magnitude 100);; => 2.0
(magnitude 100000) ;; => 5.0

(defn compare-magnitude
  "magnitude comparison predicate"
  [a b]
  (- (magnitude a) (magnitude b)))

;; TODO: why doesn't this work?
((comparator compare-magnitude) 10 10000) ;; => -1
((comparator compare-magnitude) 100 10)  ;; => -1
((comparator compare-magnitude) 10 10) ;; => -1

;; using `compare-magnitude` with a sorted collection
(sorted-set-by compare-magnitude 10 1000 500)
;; => #{10 500 1000}
(conj *1 600)
;; => #{10 500 1000}
(disj *1 500)
;; => #{10 1000}
(contains? *1 1000)
;; => true

;; `compare-magnitude` can be rewritten to ensure that only equivalent numbers
;; are considered equal

(defn compare-magnitude
  [a b]
  (let [diff (- (magnitude a) (magnitude b))]
    (if (zero? diff)
      (compare a b)
      diff)))

(sorted-set-by compare-magnitude 10 1000 500)
;; => #{10 500 1000}
(conj *1 600)
;; => #{10 500 600 1000}
(disj *1 750)
;; => #{10 500 600 1000}


;; `subseq` and `rsubseq` continue to work as expected.
(sorted-set-by compare-magnitude 10 1000 500 670 1239)
;; => #{10 500 670 1000 1239}
(def ss *1)
(subseq ss > 500)
;; => (670 1000 1239)
(subseq ss > 500 <= 1000)
;; => (670 1000)
(rsubseq ss > 500 <= 1000)
;; => (1000 670)

;; One amusing use of these functions is to implement linear interpolation:
(defn interpolate
  "Takes a collection of points (as [x y] tuples), returning a function
  which is a linear interpolation between those points."
  [points]
  (let [results (into (sorted-map) (map vec points))]
    (fn [x]
      (let [[xa ya] (first (rsubseq results <= x))
            [xb yb] (first (subseq results > x))]
        (if (and xa xb)
          (/ (+ (* ya (- xb x)) (* yb (- x xa)))
             (- xb xa))
          (or ya yb))))))

;; three known points, [0 0], [10 10], and [15 5]:
(def f (interpolate [[0 0] [10 10] [15 5]]))
(map f [2 10 12])
;; => (2 10 8)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Concise collection access

;;; Collections are functions.

(get [:a :b :c] 2) ;; => :c
(get {:a 5 :b 6} :b) ;; => 6
(get {:a 5 :b 6} :c 7) ;; => 7
(get #{1 2 3} 3) ;; => 3

;; equivalent calls
([:a :b :c] 2) ;; => :c
({:a 5 :b 6} :b) ;; => 6
({:a 5 :b 6} :c 7) ;; => 7
(#{1 2 3} 3) ;; => 3

;; Indices provided for vector lookups must also be within the range of
;; the vector, just as with `nth`:

;; ([:a :b :c] -1)
;; 1. Unhandled java.lang.IndexOutOfBoundsException


;;; Collections keys are (often) functions.

(get {:a 5 :b 6} :b) ;; => 6
(get {:a 5 :b 6} :c 7) ;; => 7
(get #{:a :b :c} :d) ;; => nil

;; equivalent expressions

(:b {:a 5 :b 6}) ;; => 6
(:c {:a 5 :b 6} 7)  ;; => 7
(:b #{:a :b :c}) ;; => :b
(:d #{:a :b :c}) ;; => nil

('a {'a 1 'b 2}) ;; => 1


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Idiomatic usage.

;; using keywords as functions since this avoids null pointer exceptions
(defn get-foo
  [map]
  (:foo map))

(get-foo nil) ;; => nil

(defn get-bar
  [map]
  (map :bar))

;; (get-bar nil)
;; 1. Unhandled java.lang.NullPointerException

;; Also (coll :foo) assumes that `coll` is a function, but that is only true for
;; Clojure data structures.


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Collections and keys and higher-order functions

;; Using keywords symbols and collection are functions you can use them as
;; inputs to higher-order functions.

(map :name [{:age 21 :name "David"}
            {:gender :f :name "Suzanne"}
            {:name "Sara" :location "NYC"}])
;; => ("David" "Suzanne" "Sara")

;; `some` searches for the first value in a sequence that returns a logically
;; true from a provided predicate;

(some #{1 3 7} [0 2 4 5 6]) ;; => nil
(some #{1 3 7} [0 2 3 4 5 6 7]) ;; => 3

;; `filter` is a more general version of this

(filter :age [{:age 21 :name "David"}
              {:gender :f :name "Suzanne"}
              {:name "Sara" :location "NYC"}])
;; => ({:age 21, :name "David"})

(filter (comp (partial <= 25) :age) [{:age 21 :name "David"}
                                     {:gender :f :name "Suzanne" :age 20}
                                     {:name "Sara" :location "NYC" :age 34}])
;; => ({:name "Sara", :location "NYC", :age 34})

;; Beware of the `nil` (again)
(remove #{5 7} (cons false (range 10)))
;; => (false 0 1 2 3 4 6 8 9)

(remove #{5 7 false} (cons false (range 10)))
;; => (false 0 1 2 3 4 6 8 9)

;; prefer `contains?`
(remove (partial contains? #{5 7 false}) (cons false (range 10)))
;; => (0 1 2 3 4 6 8 9)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Data Structure Types

;;; Lists
'(1 2 3) ;; => (1 2 3)
'(1 2 (+ 1 2)) ;; => (1 2 (+ 1 2))
(list 1 2 (+ 1 2)) ;; => (1 2 3)

(list? '(1 2 3)) ;; => true


;;; Vectors
[1 2 3] ;; => [1 2 3]
(vector 1 2 3) ;; => [1 2 3]
(vec (range 5)) ;; => [0 1 2 3 4]

(vector? [1 2 3]) ;; => true


;; Vectors as tuples

;; returning multiple values
(defn euclidian-division
  [x y]
  [(quot x y) (rem x y)])

(euclidian-division 42 8) ;; => [5 2]

((juxt quot rem) 42 8) ;; => [5 2]

;; paired with destructuring

(let [[q r] (euclidian-division 53 7)]
  (str "53/7 = " q " * 7 + " r))
;; => "53/7 = 7 * 7 + 4"

;; Problems with Tuples.
;; * Tuples are not self-documenting
;; * Tuples are inflexible. You have to fill the whole tuple.
;;   They are not extendable.


;;; Sets

#{1 2 3} ;; => #{1 3 2}

;; This doesn't work
;; #{1 2 3 3}
;; 1. Caused by java.lang.IllegalArgumentException
;;  Duplicate key: 3

(hash-set 1 2 3 3) ;; => #{1 3 2}
(hash-set :a :b :c :d)  ;; => #{:c :b :d :a}

;; from other collections
(set [1 6 1 8 3 7 7]) ;; => #{7 1 6 3 8}

;; This actually works with anything that is ""seqable"", and allows for some
;; very succinct idioms given that sets are functions themselves:

(apply str (remove (set "aeiouy") "vowels are useless"))
;; => "vwls r slss"

(defn numeric? [s] (every? (set "0123456789") s))

(numeric? "123") ;; => true
(numeric? "12h") ;; => false
(numeric? [1 2 3]) ;; => false


;;; Maps

{:a 5 :b 6}

;; {:a 1 :a 5}
;; 1. Caused by java.lang.IllegalArgumentException
;;  Duplicate key: :a

(hash-map :a 5 :b 6) ;; => {:b 6, :a 5}

;; keys and vals

(def m {:a 1 :b 2 :c 3})

(keys m) ;; => (:a :b :c)
(vals m) ;; => (1 2 3)

;; short cuts for...
(map key m) ;; => (:a :b :c)
(map val m) ;; => (1 2 3)


;;; Maps as ad-hoc stucts
;;   Since map values can be of any type, they are frequently used as simple,
;;   flexible models, most often with keywords for keys to identify each field
;;   (also called slots).

(def playlist
  [{:title "Elephant", :artist "The White Stripes", :year 2003}
   {:title "Helioself", :artist "Papas Fritas", :year 1997}
   {:title "Stories from the City, Stories from the Sea",
    :artist "PJ Harvey", :year 2000}
   {:title "Buildings and Grounds", :artist "Papas Fritas", :year 2000}
   {:title "Zen Rodeo", :artist "Mardi Gras BB", :year 2002}])

;; Querying with keys
(map :title playlist)
;; => ("Elephant" "Helioself" "Stories from the City, Stories from the Sea"
;;     "Buildings and Grounds" "Zen Rodeo")

;; access with associative destructuring
(defn summarize [{:keys [title artist year]}]
  (str title " / " artist " / " year))

(map summarize playlist)
;; ("Elephant / The White Stripes / 2003"
;;  "Helioself / Papas Fritas / 1997"
;;  "Stories from the City, Stories from the Sea / PJ Harvey / 2000"
;;  "Buildings and Grounds / Papas Fritas / 2000"
;;  "Zen Rodeo / Mardi Gras BB / 2002")


;;; Other usages of maps.
;;   Maps can be used as summaries, indexes, or translation tables,
;;   like database indexes and views.

;; partitioning collections.
(group-by #(rem % 3) (range 10))
;; => {0 [0 3 6 9], 1 [1 4 7], 2 [2 5 8]}

(group-by :artist playlist)
;; {"The White Stripes" [{:title "Elephant", :artist "The White Stripes", :year 2003}],
;;  "Papas Fritas" [{:title "Helioself", :artist "Papas Fritas", :year 1997}
;;                  {:title "Buildings and Grounds", :artist "Papas Fritas", :year 2000}],
;;  "PJ Harvey" [{:title "Stories from the City, Stories from the Sea", :artist "PJ Harvey", :year 2000}],
;;  "Mardi Gras BB" [{:title "Zen Rodeo", :artist "Mardi Gras BB", :year 2002}]}

;; Indexing by two columns
;;  (group-by (juxt :col1 :col2))

;; Compute a summary of items for a given key rather than returning a vector of
;; those items.
;; Use group-by and then process each value to summarize it:

;; (into {} (for [[k v] (group-by key-fn coll)]
;;            [ (summarize v)]))


(defn reduce-by
  [key-fn f init coll]
  (reduce (fn [summaries x]
            (let [k (key-fn x)]
              (assoc summaries k (f (summaries k init) x))))
          {} coll))


;; List of purchase orders to ACME Corp, represented using plain
;; maps as gstructsh:

(def orders
  [{:product "Clock", :customer "Wile Coyote", :qty 6, :total 300}
   {:product "Dynamite", :customer "Wile Coyote", :qty 20, :total 5000}
   {:product "Shotgun", :customer "Elmer Fudd", :qty 2, :total 800}
   {:product "Shells", :customer "Elmer Fudd", :qty 4, :total 100}
   {:product "Hole", :customer "Wile Coyote", :qty 1, :total 1000}
   {:product "Anvil", :customer "Elmer Fudd", :qty 2, :total 300}
   {:product "Anvil", :customer "Wile Coyote", :qty 6, :total 900}])

;; we can easily compute order totals by customer:
(reduce-by :customer #(+ %1 (:total %2)) 0 orders)
;; => {"Wile Coyote" 7200, "Elmer Fudd" 1200}

;; get the customers for each product:
(reduce-by :product #(conj %1 (:customer %2)) #{} orders)
;; {"Clock" #{"Wile Coyote"},
;;  "Dynamite" #{"Wile Coyote"},
;;  "Shotgun" #{"Elmer Fudd"},
;;  "Shells" #{"Elmer Fudd"},
;;  "Hole" #{"Wile Coyote"},
;;  "Anvil" #{"Elmer Fudd" "Wile Coyote"}}

;; two-level breakup

(fn [order]
  [(:customer order) (:product order)])

#(vector (:customer %) (:product %))

(fn [{:keys [customer product]}]
  [customer product])

(juxt :customer :product)

(reduce-by (juxt :customer :product)
           #(+ %1 (:total %2)) 0 orders)
;; {["Wile Coyote" "Clock"] 300,
;;  ["Wile Coyote" "Dynamite"] 5000,
;;  ["Elmer Fudd" "Shotgun"] 800,
;;  ["Elmer Fudd" "Shells"] 100,
;;  ["Wile Coyote" "Hole"] 1000,
;;  ["Elmer Fudd" "Anvil"] 300,
;;  ["Wile Coyote" "Anvil"] 900}


(defn reduce-by-in
  [keys-fn f init coll]
  (reduce (fn [summaries x]
            (let [ks (keys-fn x)]
              (assoc-in summaries ks
                        (f (get-in summaries ks init) x))))
          {} coll))

(reduce-by-in (juxt :customer :product)
              #(+ %1 (:total %2)) 0 orders)
{"Wile Coyote" {"Clock" 300,
                "Dynamite" 5000,
                "Hole" 1000,
                "Anvil" 900},
 "Elmer Fudd" {"Shotgun" 800,
               "Shells" 100,
               "Anvil" 300}}

(def flat-breakup
  {["Wile Coyote" "Anvil"] 900,
   ["Elmer Fudd" "Anvil"] 300,
   ["Wile Coyote" "Hole"] 1000,
   ["Elmer Fudd" "Shells"] 100,
   ["Elmer Fudd" "Shotgun"] 800,
   ["Wile Coyote" "Dynamite"] 5000,
   ["Wile Coyote" "Clock"] 300})

(reduce #(apply assoc-in %1 %2) {} flat-breakup)
;; {"Wile Coyote" {"Anvil" 900,
;;                 "Hole" 1000,
;;                 "Dynamite" 5000,
;;                 "Clock" 300},
;;  "Elmer Fudd" {"Anvil" 300,
;;                "Shells" 100,
;;                "Shotgun" 800}}


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Transients

;; Transients vs. Persistents
;; Don't guarantee older versions.
;; Transient collections are mutable.

(def x (transient []))
(def y (conj! x 1))
(count y) ;; => 1
(count x) ;; => 1


;;; A test

(into #{} (range 5)) ;; => #{0 1 4 3 2}

;; a reimplementation.

(defn naive-into
  [coll source]
  (reduce conj coll source))

(= (into #{} (range 500))
   (naive-into #{} (range 500)))
;; => true

;; time the implementations

(time (do (into #{} (range 1e6))
          nil))
;; "Elapsed time: 526.3549 msecs"

(time (do (naive-into #{} (range 1e6))
          nil))
;; "Elapsed time: 805.8188 msecs"

(defn faster-into
  [coll source]
  (persistent! (reduce conj! (transient coll) source)))


(time (do (faster-into #{} (range 1e6))
          nil))
;; "Elapsed time: 514.1629 msecs"


(defn transient-capable?
  "Returns true if a transient can be obtained for the given collection
   i.e. tests if (transient coll) will succeed."
  [coll]
  (instance? clojure.lang.IEditableCollection coll))

;; a persistent collection used as the basis of a transient is unaffected.
(def v [1 2])
(def tv (transient v))
(conj v 3) ;; => [1 2 3]

;; turning a transient collection into a persistent one by using `persistent!`
;; makes the source transient unusable
(persistent! tv) ;; => [1 2]
;; (get tv 0)
;; 1. Unhandled java.lang.IllegalAccessError
;; Transient used after persistent! call

;; functions supported by transients
(nth (transient [1 2]) 1) ;; => 2
(get (transient {:a 1 :b 2}) :a) ;; => 1
((transient {:a 1 :b 2}) :a) ;; => 1
((transient [1 1]) 1) ;; => 1
(find (transient {:a 1 :b 2}) :a) ;; => [:a 1] - opps works

;; transients are functions too.

;; seqs must be persistent
;;  (seq (transient [1 2])) ;; doesn't work

;; `conj!` `assoc!` `dissoc!` `disj!` `pop!`

;; you must always use the results of transient functions or they may change.
(let [tm (transient {})]
  (doseq [x (range 100)]
    (assoc! tm x 0))
  (persistent! tm))
;; => {0 0, 1 0, 2 0, 3 0, 4 0, 5 0, 6 0, 7 0}

;; transients must be used in the thread created it.
(let [t (transient {:a 4})]
  @(future (get t :a)))
;; => 4
;; oops, works

;;; transients don't compose.
;;  `persistent!` will not traverse a hierarchy of nested transients you may
;;   have created, so calling `persistent!` on the top level reference will not
;;   apply to subcollections:
(persistent! (transient [(transient {})]))
;; => [#object[clojure.lang.PersistentArrayMap$TransientArrayMap 0x7347c2ee
;;     "clojure.lang.PersistentArrayMap$TransientArrayMap@7347c2ee"]]

;; transients don't have values semantics
(= (transient [1 2]) (transient [1 2])) ;; => false


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Metadata

;; Type declarations, access modifiers.
;; Java annotations.

;; Metadata can be attached to any Clojure data structure, sequence, record,
;; symbol, or reference type, and always takes the form of a map

(def a ^{:created (System/currentTimeMillis)}
  [1 2 3])

(meta a) ;; => {:created 1548906493453}

;; Boolean metadata.
(meta ^:private [1 2 3]) ;; => {:private true}
(meta ^:private ^:dynamic [1 2 3])
;; => {:dynamic true, :private true}

(def b (with-meta a (assoc (meta a)
                           :modified (System/currentTimeMillis))))
(meta b)
;; => {:created 1548906493453, :modified 1548906863399}
(def b (vary-meta a assoc :modified (System/currentTimeMillis)))
(meta b)
;; => {:created 1548906493453, :modified 1548906933487}

;; metadata doesn't change the value of data.
(= a b) ;; => true
a ;; => [1 2 3]
b ;; => [1 2 3]
(= ^{:a 5} 'any-value
   ^{:b 4} 'any-value) ;; => true

;; "modifying" a var doesn't change the metadata
(meta (conj a 500)) ;; => {:created 1548906493453}
