(ns programming-clojure.ch02-functional
  (:require [clojure.repl :refer :all]
            [clojure.edn :as edn]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; What does functional programming even mean.
;;  * Preference for working with immutable `values`:
;;    - Use of immutable data-structures that satisfy simple abstractions
;;    - Functions are first class values.
;;  * Declarative processing of data over imperative control structures
;;  * Natural, incremental composition of functions, higher-order functions
;;    immutable data-structures.


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Comparing Values to Mutable Objects

;; see src/java/Statefulinteger.java
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
(= five 5)
;; => true
(.setInt five 6)
(= five six)
;; => true  ;; What !!

;; another consequence of mutable objects
(defn print-number
  [n]
  (println (.intValue n))               ; side effect
  (.setInt n 42))                       ; insane side effect

(print-number five)
;; 6
;; => nil
(= five six)
;; => false
(= five (StatefulInteger. 42))
;; => true - oops 1

;; Mutable object can lead to strange results.

;;; the safety of values.
;; using a vector as a key
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
;;     Reactor ...
;;  * Utilities for erecting flimsy immutable facades in front of mutable
;;    data-structures.
;;  * Documentation and questionable advice. - deadlocks, race conditions.


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; First-Class and Higher-Order Functions
;;  Functions as datatypes.

;; call_twice - OK if `f` has side-effects
(defn call-twice [f x]
  (f x)
  (f x))

(call-twice println 123)
;; 123
;; 123


;; Clojure functions are just functions - not members of classes

;; Members of namespaces and not classes
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

;; evaluation stream.
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
(reduce
 (fn [mp vc]
   (assoc mp vc (* vc vc)))
 {}                                     ; initial value
 [1 2 3 4])
;; => {1 1, 2 4, 3 9, 4 16}


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Applying Ourselves Partially

;; `Function application` is the invocation of a function with a sequence
;; of arguments rather then using syntactical invocation.

;; function application by `apply`.

(apply hash-map [:a 5 :b 6])
;; => {:b 6, :a 5}

(def args [2 -2 10])
(apply * 0.5 3 args)
;; => -60.0

(def only-strings (partial filter string?))
(only-strings ["a" 5 "b" 6])
;; => ("a" "b")

(def add-3 (partial + 3))
(add-3 4)
;; => 7

;; `partial` versus function literals.

(#(filter string? %) ["a" 5 "b" 6])
;; => ("a" "b")

;; However, function literals do not limit you to defining only the
;; initial arguments to the function.

(#(filter % ["a" 5 "b" 6]) string?)
;; => ("a" "b")

(#(filter % ["a" 5 "b" 6]) number?)
;; => (5 6)

;; But you are forced to fully specify the arguments.

;; (#(map *) [1 2 3] [4 5 6] [7 8 9])
;; 1. Unhandled clojure.lang.ArityException
;; Wrong number of args (3) passed to:

(#(map * % %2 %3) [1 2 3] [4 5 6] [7 8 9])
;; => (28 80 162)

;; (#(map * % %2 %3) [1 2 3] [4 5 6])
;; 1. Unhandled clojure.lang.ArityException
;; Wrong number of args (2) passed to:

(#(apply map * %&) [1 2 3] [4 5 6] [7 8 9])
;; => (28 80 162)

(#(apply map * %&) [1 2 3])
;; => (1 2 3)

((partial map * ) [1 2 3] [4 5 6] [7 8 9])
;; => (28 80 162)

((partial #(filter string? %)) ["a" 5 "b" 6])
;; => ("a" "b")


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Composition of Function(ality)

;; A function of a negation of a sum of given numbers returned
;; as a string.
(defn negated-sum-str
  [& numbers]
  (str (- (apply + numbers))))

(negated-sum-str 10 12 3.4)
;; => "-25.4"

;; `comp` used for clarity.
(def negated-sum-str (comp str - +))

(negated-sum-str 10 12 3.4)
;; => "-25.4"

;; but functions must be composed in correct order for types.

;; ((comp + - str) 5 10)
;; 1. Unhandled java.lang.ClassCastException
;; class java.lang.String cannot be cast to class java.lang.Number

;; CamelCase to keyword converter.

(require '[clojure.string :as str])

(def camel->keyword (comp keyword
                          str/join
                          (partial interpose \-)
                          (partial map str/lower-case)
                          #(str/split % #"(?<=[a-z])(?=[A-Z])")))

(camel->keyword "CamelCase")
;; => :camel-case
(camel->keyword "lowerCamelCase")
;; => :lower-camel-case

;; More composition: Returns an idiomatic Clojure map given as sequence
;; of key/value pairs that use CamelCase-style keys

(def camel-pairs->map
  (comp (partial apply hash-map)
        (partial map-indexed (fn [i x]
                               (if (odd? i)
                                 x
                                 (camel->keyword x))))))

(camel-pairs->map ["CamelCase" 5 "lowerCamelCase" 3])
;; => {:camel-case 5, :lower-camel-case 3}


;;; writing  Higher-Order Functions

;; A HOF that returns a function that adds a given number to its argument
(defn adder
  [n]
  (fn [x] (+ n x)))

((adder 5) 18)
;; => 23

(def add-3 (adder 3))

(add-3 4) ;; => 7

;; A HOF that returns a function that doubles the result of calling the
;; provided function
(defn doubler
  [f]
  (fn [& args]
    (* 2 (apply f args))))

(def double-+ (doubler +))

(double-+ 1 2 3)
;; => 12

;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Building a Primitive Logging System with
;;; Composable Higher-Order Functions

(defn print-logger
  [writer]
  #(binding [*out* writer]
     (println %)))

(def *out*-logger (print-logger *out*))

(*out*-logger "hello")
;; hello
;; => nil

;; create and retain a StringWriter.
(def writer (java.io.StringWriter.))
(def retained-logger (print-logger writer))

;; Calling our logging function doesn't write to stdout.
(retained-logger "hello")
;; => nil

;;... because it's been println'ed to out StringWriter instead.
(str writer)
;; => "hello\r\n"

(require 'clojure.java.io)

(defn file-logger
  [file]
  #(with-open [f (clojure.java.io/writer file :append true)]
     ((print-logger f) %)))

;; Let's see how we're doing:
(def log->file (file-logger "messages.log"))
;; => #'programming-clojure.ch02-functional/log->file

(log->file "hello log")
;; => nil

;; logging to multiple destinations.
(defn multi-logger
  [& logger-fns]
  #(doseq [f logger-fns]
     (f %)))

(def log (multi-logger
          (print-logger *out*)
          (file-logger "messages.log")))

(log "hello log again")
;; "hello log again
;; => nil

;; logging middle-ware: include a `time-stamp` with each log message

(defn timestamped-logger
  [logger]
  #(logger (format "[%1$tY-%1$tm-%1$te %1$tH:%1$tM:%1$tS] %2$s" (java.util.Date.) %)))

(def log-timestamped (timestamped-logger
                      (multi-logger
                       (print-logger *out*)
                       (file-logger "messages.log"))))

(log-timestamped "goodbye log, new")
;; [2019-01-17 23:54:37] goodbye log, new
;; => nil
