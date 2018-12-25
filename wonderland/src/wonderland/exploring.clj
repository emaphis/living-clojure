(ns wonderland.exploring
  (:require [clojure.string :as str]))

(defn greeting
  "Returns a greeting of the form, 'Hello,username.'"
  [username]
  (str "Hello, " username))

(defn greeting
  "Returns a greeting of the form, 'Hello,user-name.'
  Default username is 'world'"
  ([] (greeting "world"))
  ([username]
   (str "Hello, " username)))

(defn date [person-1 person-2 & chaperones]
  (println person-1 "and" person-2
           "went out with" (count chaperones) "chaperons."))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; anonymous functions.

(defn indexable-word? [word]
  (> (count word) 2))

(filter indexable-word? (str/split "a fine day it is" #"\W+"))
;; => ("fine" "day")

;; don't pollute the name space with little used functions
(filter (fn [w] (> (count w) 2)) (str/split "a fine day it is" #"\W+"))
;; => ("fine" "day")


;; named local functions defined in 'let'
(defn indexable-words [text]
  (let [indexable-word? (fn [w] (> (count w) 2))]
    (filter indexable-word? (str/split text #"\W+"))))

(indexable-words "a fine day it is")
;; => ("fine" "day")

;; create a function at runtime
(defn make-greeter [greeting-prefix]
  (fn [username] (str greeting-prefix ", " username)))

(def hello-greeting (make-greeter "Hello"))


(def aloha-greeting (make-greeter "Aloha"))

(hello-greeting "world")
;; => "Hello, world"

(aloha-greeting "world")
;; => "Aloha, world"


((make-greeter "Howdy") "pardner")
;; => "Howdy, pardner"


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Destructuring

(defn greet-author-1 [author]
  (println "Hello," (:first-name author)))

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
  (let [[w1 w2 w3] (str/split words #"\s+")]
    (str/join  " " [w1 w2 w3 "..."])))

(ellipsize "The quick brown fox jumps over the lazy dog.")
;; => "The quick brown ..."

;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; calling java

;;; Accessing Constructors, Methods and Fields

(new java.util.Random)
;; => #object[java.util.Random 0x6abdefd8 "java.util.Random@6abdefd8"]

(def rnd (new java.util.Random))
;; => #'wonderland.exploring/rnd


(. rnd nextInt)
;; => -1923527362

(. rnd nextInt 10)
;; => 9

(. Math PI)
;; => 3.141592653589793

;; (import [& import-lists])

(import '(java.util Random Locale)
        '(java.text MessageFormat))
;; => java.text.MessageFormat

Random
;; => java.util.Random

Locale
;; => java.util.Locale

MessageFormat
;; => java.text.MessageFormat


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

;; find third occurrence of "heads"
(nth (index-filter #{:h} [:t :t :h :t :h :t :t :t :h :h])
     2)
;; => 8

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
  (.toUpperCase s))

(meta #'shout)
;; =>
;; {:tag java.lang.String,
;;  :arglists ([s]),
;;  :line 272, :column 1,
;;  :file "c:/src/Clojure/Living-Clojure/wonderland/src/wonderland/exploring.clj",
;;  :name shout,
;;  :ns #namespace[wonderland.exploring]}
