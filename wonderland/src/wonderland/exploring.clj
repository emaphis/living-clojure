(ns wonderland.exploring
  (:require [clojure.string :as str]
            [clojure.repl :as r]))

(quote (1 2 3))
;; => (1 2 3)

'(1 2 3)
  ;; => (1 2 3)

  ;; sets
#{1 2 3 5}
;; => #{1 3 2 5}

;; maps
{"Lisp" "McCarthy" "Clojure" "Hickey"}
;; => {"Lisp" "McCarthy", "Clojure" "Hickey"}

;; keywords
:foo

{:Lisp "McCarthy" :Clojure "Hickey"}
;; => {:Lisp "McCarthy", :Clojure "Hickey"}

;; records
(defrecord Book [title author])
;; => wonderland.exploring.Book

(->Book "title" "author")
;; => #wonderland.exploring.Book{:title "title", :author "author"}

;;; Strings and Charactors

"This is a\nmultiline string"

"This is also
a multiline string"

(println "another\nmultiline\nstring")
;; => nil

;; str - string function

(str 1 2 nil 3)
;; => "123"

(str \h \e \y \space \y \o \u)
;; => "hey you"

;;; booleans and nil
;; true is true and false is false.
;; nil evaluates to false.
;; only false and nil is false, everything else evaluates to true.
;; true false and nil are special values, special tokens, everything else is a symbol

;; empty list is not false in Clojure
(if () "() is true" "() is false")
;; => "() is true"

;; Zero is true
(if 0 "Zero is true" "Zero is false")
;; => "Zero is true"

;; predicates evaluate to true or false.

(true? true)
;; => true

(true? "foo")
;; => false

;; nil? (nil) and false? (false)

(zero? 0.0)
;; => true

(zero? (/ 22 7))
;; => false

;;(r/find-doc #"\?$")


;;; Functions

;; a function call is a list whose first element resolves to a function
(str "hello" " " "world")
;; => "hello world"

(string? "hello")
;; => true

(keyword? :hello)
;; => true

(symbol? 'hello)
;; => true

;; (defn name doc-string? attr-map? [params*] prepost-map? body)

(defn greeting
  "Returns a greeting of the form, 'Hello,username.'"
  [username]
  (str "Hello, " username))
;; => #'wonderland.exploring/greeting

(greeting "world")
;; => "Hello, world"

;; multi-arity functions
(defn greeting
  "Returns a greeting of the form, 'Hello,user-name.'
  Default username is 'world'"
  ([] (greeting "world"))
  ([username]
   (str "Hello, " username)))

(greeting)
;; => "Hello, world"


(defn date [person-1 person-2 & chaperones]
  (println person-1 "and" person-2
           "went out with" (count chaperones) "chaperons."))

;; multiple arity

(date "Romeo" "Juliet" "Friar Lawrence" "Nurse")
;; => nil
;; Romeo and Juliet went out with 2 chaperons.


;;; anonymous functions.

(defn indexable-word? [word]
  (> (count word) 2))

(filter indexable-word? (str/split "a fine day it is" #"\W+"))
;; => ("fine" "day")

;; don't pollute the name space with little used functions
;; (fn [params*] body)

(filter (fn [w] (> (count w) 2)) (str/split "a fine day it is" #"\W+"))
;; => ("fine" "day")

;; reader macro lambda
;; #(body)

(filter #(> (count %) 2) (str/split "a fine day it is" #"\W+"))
;; => ("fine" "day")


;; named local functions defined in 'let'
;; interesting enough to have a name but only useful in a local context
(defn indexable-words [text]
  (let [indexable-word? (fn [w] (> (count w) 2))]
    (filter indexable-word? (str/split text #"\W+"))))

(indexable-words "a fine day it is")
;; => ("fine" "day")


;; create a function dynamically at runtime
(defn make-greeter [greeting-prefix]
  (fn [username] (str greeting-prefix ", " username)))

(def hello-greeting (make-greeter "Hello"))
;; => #'wonderland.exploring/hello-greeting


(def aloha-greeting (make-greeter "Aloha"))
;; => #'wonderland.exploring/aloha-greeting

(hello-greeting "world")
;; => "Hello, world"

(aloha-greeting "world")
;; => "Aloha, world"

;; completely at runtime.
((make-greeter "Howdy") "pardner")
;; => "Howdy, pardner"


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Vars, Bindings and Namespaces

;;; vars
(def foo 10)
;; => #'wonderland.exploring/foo

foo

;; return the var bound to wonderland.exploring/foo instead of the value
(var foo)
;; => #'wonderland.exploring/foo

;; reader macro
#'foo
;; => #'wonderland.exploring/foo


;;; Bindings

;;; var bound to names and function parameter bindings

(defn triple [num] (* 3 num))
;; => #'wonderland.exploring/triple

(triple 10)
;; => 30

;; let bindings
;; (let [bindings*] exprs*)

(defn square-corners [bottom left size]
  (let [top (+ bottom size)
        right (+ left size)]
    [[bottom left] [top left] [top right] [bottom right]]))

(square-corners 10 20 30)
;; => [[10 20] [40 20] [40 50] [10 50]]


;;; Destructuring

;; some functions only need to use first name
(defn greet-author-1 [author]
  (println "Hello," (:first-name author)))
;; => #'wonderland.exploring/greet-author-1


(greet-author-1 {:last-name "Vinge" :first-name "Vernor"})
;; => nil
;; Hello, Vernor

;; makes the function more general
(greet-author-1 {:first-name "Fred" :first-book "Happy Days"})
;; => nil
;; Hello, Fred

;;; simplifying with destructuring

(defn greet-author-2 [{fname :first-name}]
  (println "Hello," fname))

(greet-author-2 {:last-name "Vinge" :first-name "Vernor"})
;; => nil
;; Hello, Vernor

(greet-author-2 {:first-name "Fred" :first-book "Happy Days"})
;; => nil
;; Hello, Fred

;; bind the first two coordinates in a three-dimensional coordinate space

(let [[x y] [1 2 3]]
  [x y])
;; => [1 2]

;; third coordinate
(let [[_ _ z] [1 2 3]]
  z)
;; => 3

;; don't do!! lol.
(let [[_ _ z] [1 2 3]]
  _)
;; => 2

;; binding whole structure
(let [[x y :as coords] [1 2 3 4 5 6]]
  (str "x: " x ", y: " y ", total dimensions " (count coords)))
;; => "x: 1, y: 2, total dimensions 6"

(defn ellipsize [words]
  "take a string and return the first three words followed by an ellipsis"
  (let [[w1 w2 w3] (str/split words #"\s+")]
    (str/join  " " [w1 w2 w3 "..."])))

(ellipsize "The quick brown fox jumps over the lazy dog.")
;; => "The quick brown ..."


;;; Namespaces

(def foo 10)
;; => #'wonderland.exploring/foo

(resolve 'foo)
;; => #'wonderland.exploring/foo

;; creating a namespace
;; (in-ns 'myapp)
;; => #namespace[myapp]

;;  (clojure.core/use 'clojure.core)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Meta data

(meta #'str)
;; =>{:added "1.0",
;; :ns #namespace[clojure.core],
;; :name str,
;; :file "clojure/core.clj",
;; :static true,
;; :column 1, :line 544,
;; :tag java.lang.String,
;; :arglists ([] [x] [x & ys]),
;; :doc "With no args, returns the empty string. With one arg x, returns\n  x.toString().  (str nil) returns the empty string. With more than\n  one arg, returns the concatenation of the str values of the args."}

(defn ^{:tag String} shout [^{:tag String} s]
  (str/upper-case s))

(meta #'shout)
;; =>
;; {:tag java.lang.String,
;;  :arglists ([s]),
;;  :line 272, :column 1,
;;  :file "c:/src/Clojure/Living-Clojure/wonderland/src/wonderland/exploring.clj",
;;  :name shout,
;;  :ns #namespace[wonderland.exploring]}

;; sort form of :tag
(defn ^String shout [^String s]
  (str/upper-case s))

(defn shout [s]
  (str/upper-case s)
  {:tag String})


;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; calling Java

;;; Accessing Constructors, Methods and Fields

;; (new classname)

(new java.util.Random)
;; => #object[java.util.Random 0x6abdefd8 "java.util.Random@6abdefd8"]

;; using '.' syntax (classname.)

(java.util.Random.)
;; => #object[java.util.Random 0x60ca526 "java.util.Random@60ca526"]


(def rnd (new java.util.Random))
;; => #'wonderland.exploring/rnd

;; calling methods
;; (. class-or-instance member-symbol & args)
;; (. class-or-instance (member-symbol & args))

;; rnd.nextInt
(. rnd nextInt)
;; => -1923527362

;; rnd.nextInt(10)
(. rnd nextInt 10)
;; => 9

;; accessing instance fields, static fields and static methods

;; Instance fields
(def pnt (java.awt.Point. 10 20)) ; var point = new java.awt.Point(10,20);
(. pnt x)
;; => 10

;; static method
(. System lineSeparator)  ;; System.lineSeparator 
;; => "\r\n"


;; static field
(. Math PI)  ; Math.PI
;; => 3.141592653589793


;; more concise syntax
;; (.method instance & args)
;; (.field instance)
;; (.-field instance) ; if method has same name as field
;; (Class/method & args)
;; Class/field

(.nextInt rnd 10)
;; => 6

(.x pnt)
;; => 10

(System/lineSeparator)
;; => "\r\n"

Math/PI
;; => 3.141592653589793


;; (import (package-symbol & class-name-symbols)*)

(import '(java.util Random Locale)
        '(java.text MessageFormat))
;; => java.text.MessageFormat

Random
;; => java.util.Random

Locale
;; => java.util.Locale

MessageFormat
;; => java.text.MessageFormat


;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Comments

;; this is a comments

(comment
  (defn ingore-me []
    ;; not done yet
    )

  (+ 3 4)
  )

;; reader macro #_(

(defn triple [num]
  #_(println "debug triple:" num)
  (* 3 num))

(triple 3)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Flow control

;; if

(defn is-small? [number]
  (if (< number 100) "yes"))

(is-small? 50) ;; => "yes"

(is-small? 50000) ;; => nil

;; else part
(defn is-small? [number]
  (if (< number 100) "yes" "no"))


(is-small? 50000) ;; => "no"

(is-small? 50)
;; => "yes"


;; side effects with do

(defn is-small? [number]
  (if (< number 100)
    "yes"
    (do
      (println "Saw a big number" number)
      "now")))


(is-small? 220)
;; => "now"
;; Saw a big number 220

(is-small? 50)
;; => "yes"


;;; Recur with loop/recur

;; (loop [bindings *] exprs*)

;; works like 'let' establishing 'bindings' and then evaluating
;; 'exprs'. 'loop' sets a 'recursion point' which can then be targeted
;; by the 'recur' special form.

(loop [result [] x 5]
  (if (zero? x)
    result
    (recur (conj result x) (dec x))))
;; => [5 4 3 2 1]

;; implicit loop
(defn countdown [result x]
  (if (zero? x)
    result
    (recur (conj result x) (dec x))))

(countdown [] 5)
;; => [5 4 3 2 1]

;; higher level versions

(into [] (take 5 (iterate dec 5)))
;; => [5 4 3 2 1]

(into [] (drop-last (reverse (range 6))))
;; => [5 4 3 2 1]

(vec (reverse (rest (range 6))))
;; => [5 4 3 2 1]


;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; no for loop.

(defn indexed [coll] (map-indexed vector coll))

(indexed "abcde")
;; => ([0 \a] [1 \b] [2 \c] [3 \d] [4 \e])


(defn index-filter [pred coll]
  (when pred
    (for [[idx elt] (indexed coll) :when (pred elt)] idx)))

(index-filter #{\a \b} "abcdbbb")
;; => (0 1 4 5 6)

(index-filter #{\a \b} "xyz")
;; => ()

(defn index-of-any [pred coll]
  (first (index-filter pred coll)))

(index-of-any #{\z \a} "zzabyycdxx")
;; => 0

(index-of-any #{\b \y} "zzabyycdxx")
;; => 3

(index-of-any #{\j \k} "zzabyycdxx")
;; => nil

;; find third occurrence of "heads"
(nth (index-filter #{:h} [:t :t :h :t :h :t :t :t :h :h])
     2)
;; => 8
