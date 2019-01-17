(ns programming-clojure.ch02-functional
  (:require [clojure.repl :refer :all]
            [clojure.edn :as edn]))




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Comparing Values to Mutable Objects

;; lein javac

(import StatefulInteger)

(def five (StatefulInteger. 5))
(def six (StatefulInteger. 6))

(.intValue five)
;; => 5
(.intValue six)
;; => 6
(.doubleValue five)
;; => 5.0
(= five six)
;; => false
(= five five)
;; => true
(.setInt five 6)
(= five six)
;; => true  ;; What !!

;; another consequence of mutable objects
(defn print-number
  [n]
  (println (.intValue n))
  (.setInt n 42))

(print-number five)
;; 6
;; => nil
(= five six)
;; => false
(= five (StatefulInteger. 42))
;; => true


;;; the safety of values.
(def h {[1,2] 3})
(h [1 2])
;; => 3
(conj (first (keys h)) 3)
;; => [1 2 3]
(h [1 2])
;; => 3
h
;; => {[1 2] 3}

;;; A Critical Choice
;; * Mutable objects cannot be safely passed to methods.
;; * Mutable objects cannot be reliably used in maps, entries in sets...
;;    equality an lookup semantics change over time.
;; * Mutable objects cannot be reliably cached.
;; * Mutable objects cannot be reliably used in a concurrent environment.

;;; Mutable object languages deal with these problems.
;;  * Copy constructors and deep copy methods to ensure reliable access to
;;    objects in a particular state.
;;  * patterns for tracking and managing change over time, including Observer,
;;    Reactor ...
;;  * Utilities for erecting flimsy immutable facades in front of mutable
;;    datastructures.
;;  * Documentation and questionable advice. - deadlocks, race conditions.


;;; First-Class and Higher-Order Functions
;;  Functions as datatypes.

;; call_twice
(defn call-twice [f x]
  (f x)
  (f x))

(call-twice println 123)
;; 123
;; 123


;; Clojure functions are just functions - not members of classes

(max 5 6)
;; => 6

(require 'clojure.string)

(clojure.string/lower-case "Clojure")
;; => "clojure"

;; ... so they can be passed or returned from other functions
;; ... `map` `reduce` `filter` `partial` `comp` `complement` `repeatedly` ...

;; `map` accepts a function and one or more collection and returns a sequence
;; of results of applying that function to values in the collections
;;  (map f [a b c]) => [(f a) (f b) (f c)]
;;  (map f [a b c] [x y z]) => [(f a x) (f b y) (f c z)]

(map clojure.string/lower-case ["Java" "Imperative" "Weeping"
                                "Clojure" "Learning" "Peace"])
;; => ("java" "imperative" "weeping" "clojure" "learning" "peace")

(map * [1 2 3 4] [5 6 7 8])
;; => (5 12 21 32)

;; `reduce` accepts a function of two parameters and a collection accumulating
;; a value

(reduce max [0 -3 10 48])
;; => 48

(max 0 -3)
;; => 0
(max 0 10)
;; => 10
(max 10 48)
;; => 48

;; optional initial value
(reduce + 50 '(1 2 3 4))
;; => 60

;; reduce a collection of numbers into a `map`, with numbers for keys, and
;; their squares for values:
