(ns programming-clojure.ch01-welcome
  (:require [clojure.repl :refer :all]
            [clojure.edn :as edn]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Down the Rabbit Hole!

(defn average
  [numbers]
  (/ (apply + numbers) (count numbers)))
;; => #'programming-clojure.ch01-welcome/average


(average [60 80 100 400])
;; => 160

(println (average [60 80 100 400]))
;; 160
;; => nil


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; The Reader

;; (doc read)
;; (doc read-string)
;; (doc clojure.edn/read-string)

(read-string "42")
;; => 42
(edn/read-string "42")
;; => 42

(read-string "(+ 1 2)")
;; => (+ 1 2)

;; reader printer duality.

(pr-str [1 2 3])
;; => "[1 2 3]"
(read-string "[1 2 3]")
;; => [1 2 3]

(read-string (print-str [1 2 3]))
;; => [1 2 3]


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Scalar literals as accepted by the reader:

;; Strings

"hello there"
;; => "hello there"


"multi-line strings
 are very handy"
;; => "multi-line strings\n are very handy"


;; Boolean
true ;; => true
(read-string "true") ;; => true

nil ;; => nil
(read-string "nil") ;; => nil


;;; Characters

\c ;; => \c
(read-string "\\c") ;; => \c
(class \c) ;; => java.lang.Character

\u00ff  ;; => \ÿ

\o41  ;; => \!

;; and ...
\space
\newline
\formfeed
\return
\backspace
\tab

;;; Keywords - evaluate to themselves
:key
;; => :key

(def person {:name "Sandra Cruz"
             :city "Portland, ME"})
;; => #'programming-clojure.ch01-welcome/person

(:city person)
;; => "Portland, ME"

;; always prefixed with a colon '/' denotes a `namespaced keyword`
;; :: always expands to current namespace.
;; namespace keywords figure prominently with multimethods,  isa? hierarchies,
;; and specs.

(def pizza {:name "Ramuto's"
            :location "Claremont, NH"
            ::location "43.3734,-72.3365"})

pizza
{:name "Ramuto's",
 :location "Claremont, NH",
 :programming-clojure.ch01-welcome/location "43.3734,-72.3365"}

(:programming-clojure.ch01-welcome/location pizza)
;; => "43.3734,-72.3365"

;; "named" values
(name :programming-clojure.ch01-welcome/location)
;; => "location"

(namespace :programming-clojure.ch01-welcome/location)
;; => "programming-clojure.ch01-welcome"

(namespace :location)
;; => nil


;;; Symbols - like keywords but evaluate to values.

(average [60 80 100 400])
;; => 160

(class average)
;; => programming_clojure.ch01_welcome$average


;;; Numbers

42 ;; => 42
0xff ;; => 255
040 ;; => 32
(class 0xff );; => java.lang.Long

3.14 ;; => 3.14
6.02214e23 ;; => 6.02214E23
(class 3.14) ;; => java.lang.Double

24N ;; => 24N
(class 34N) ;; => clojure.lang.BigInt

0.01M ;; => 0.01M
(class 0.01M) ;; => java.math.BigDecimal

22/7 ;; => 22/7
(class 22/7) ;; => clojure.lang.Ratio


;;; Regular expressions

(class #"(p|h)ail")
;; => java.util.regex.Pattern

(re-seq #"(...) (...)" "foo bar")
;; => (["foo bar" "foo" "bar"])

;; Clojure's regex syntax does not require escaping backslashes.

(re-seq #"(\d+)-(\d+)" "1-3")  ; would be "(\\d+)-(\\d+)" in Java
;; => (["1-3" "1" "3"])


;;; Comments

;; Single-line comments (;)
;; From-level using the `#_` reader macro.
(read-string "(+ 1 2 #_(* 2 2) 8)")
;; => (+ 1 2 8)

(+ 1 2 #_(* 2 2) 8)
;; => 11

(comment
  "this stuff is not seen by the compiler"
  (defn add-3 [num] (+ 3 num))
  )

;; Commas are white space
(= [1 2 3] [1, 2, 3])
;; => true

;; mostly useful in map literal definitions


;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Collection Literals

'(a b :name 12.5)                       ; list
['a 'b :name 12.5]                      ; vector
{:name "Chas" :age 31}                  ; map
#{1 2 3}                                ; set

;; '() list reader syntax so list isn't interpreted as a function call

;;; Reader syntax throughout the book.


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Namespaces

;; Clojure works through the Read->Evaluate->Print process
;; The evaluation of symbols is affected by namespaces

;; vars are a mutable storage location that can hold any value.
;; vars are defined using `def` special form which acts in the current namespace.
;; vars should only be defined a the REPL or in a Clojure file,
;; never in a program. (vars are not variables)

(def x 1)
;; => #'programming-clojure.ch01-welcome/x

x
;; => 1

;; we can redefine vars for interactive development
(def x "hello")
;; => #'programming-clojure.ch01-welcome/x

x
;; => "hello"

*ns*
;; => #namespace[programming-clojure.ch01-welcome]

(ns foo)
;; => nil

*ns*
;; => #namespace[foo]

programming-clojure.ch01-welcome/x
;; => "hello"

;; x ;; not in the foo namspace
;; 1. Caused by java.lang.RuntimeException
;; Unable to resolve symbol: x in this context

;; java.lang is imported into all Clojure namspaces

String
;; => java.lang.String
Integer
;; => java.lang.Integer
java.util.List
;; => java.util.List
java.net.Socket
;; => java.net.Socket

;; so is clojure.core
filter
;; => #function[clojure.core/filter]
