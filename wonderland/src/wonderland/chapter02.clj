(ns wonderland.chapter02)
;;; Flow and Functional Transformations

;; An expression is code than can be evaluated to a result

(first [1 2 3])
;; => 1

;; A form is a valid expression that can be evaluated

;;; Controlling the Flow with Logic

;; booleans, true, false

(class true)
;; => java.lang.Boolean


(true? true)
;; => true

(true? false)
;; => false

(false? false)
;; => true

(false? true)
;; => false

;; absence of value 'nil'
(nil? nil)
;; => true

(nil? 1)
;; => false

;; negation
(not true)
;; => false
(not false)
;; => true

;; 'nil' is logically false
(not nil)
;; => true

(not "hi")
;; => false

;; the symbol '=' is the same as Java's 'equals' method.

(= :drinkme :drinkme)
;; => true

(= :drinkme 4)
;; => false

;; collection equality is special:
(= '(:drinkme :bottle) [:drinkme :bottle])
;; => true

;; 'not=' is shortcut for (not (= ....))
(not= :drinkyme :4)
;; => true


;;; logical tests on collections

(empty? [:table :door :key])
;; => false

(empty? [])
;; => true

(empty? {})
;; => true

(empty? '())
;; => true

;; 'seq' returns a seq abstraction on a collection or 'nil' on empty

(seq [1 2 3])
;; => (1 2 3)

(class [1 2 3])
;; => clojure.lang.PersistentVector

(class (seq [1 2 3]))
;; => clojure.lang.PersistentVector$ChunkedSeq

(seq [])
;; => nil

;;; use 'seq' to test if a collection is not empty - idiomatic in Clojure

(empty? [])
;; => true

;; check not empty
(seq [])
;; => nil

;; truth properties of every item in a collection
(every? odd? [1 3 5])
;; => true

(every? odd? [1 2 3 4 5])
;; => false

;; defining our own predicates
(defn drinkable? [x]
  (= x :drinkme))

(every? drinkable? [:drinkme :drinkme])
;; => true

(every? drinkable? [:drinkme :poison])
;; => false

;; anonymous function
(every? (fn [x] (= x :drinkme)) [:drinkme :drinkme])
;; => true

(every? #(= % :drinkme) [:drinkme :drinkme])
;; => true

;; test for none elements
(not-any? #(= % :drinkme) [:drinkme :poison])
;; => false

(not-any? #(= % :drinkme) [:poison :poison])
;; => true

;; some

(some #(> % 3) [1 2 3 4 5 6])
;; => true

;; using a set as a function
(#{1 2 3 4 5} 3)
;; => 3

;; using a set and some to return the matching element
(some #{3} [1 2 3 4 5])
;; => 3

(some #{4 5} [1 2 3 4 5 6])
;; => 4


;;; flow control

(if true "it is true" "it is false")
;; => "it is true"

(if false "it is true" "it is false")
;; => "it is false"

(if nil "it is true" "it is false")
;; => "it is false"

(if (= :drinkme :drinkme)
  "Try it"
  "Don't try it")
;; => "Try it"

;; if you want to test something then remember it
(let [need-to-grow-small (> 5 3)]
  (if need-to-grow-small
    "drink bottle"
    "don't drink bottle"))
;; => "drink bottle"

;; simpler
(if-let [need-to-grow-small (> 5 3)]
  "drink bottle"
  "don't drink bottle")
;; => "drink bottle"

;; only do something if true
(defn drink [need-to-grow-small]
  (when need-to-grow-small "drink bottle"))

(drink true)
;; => "drink bottle"

(drink false)
;; => nil

;; when-let
(when-let [need-to-grow-small true]
  "drink bottle")
;; => "drink bottle"

(when-let [need-to-grow-small false]
  "drink bottle")
;; => nil

;; similar to multiple if else - case statement

(let [bottle "drinkme"]
  (cond
    (= bottle "poison") "don't touch"
    (= bottle "drinkme") "grow smaller"
    (= bottle "empty") "all gone"))
;; => "grow smaller"

;; order of clauses are important

(let [x 5]
  (cond
    (> x 10) "bigger than 10"
    (> x 4) "bigger than 4"
    (> x 3) "bigger than 3"))
;; => "bigger than 4"

;; again order of clauses are important
(let [x 5]
  (cond
    (> x 3) "bigger than 3"
    (> x 10) "bigger than 10"
    (> x 4) "bigger than 4"))
;; => "bigger than 3"

;; if none of the clauses match return 'nil'
(let [x 1]
  (cond
    (> x 10) "bigger than 10"
    (> x 4) "bigger than 4"
    (> x 3) "bigger than3"))
;; => nil

;; a default clause
(let [bottle "mystery"]
  (cond
    (= bottle "poison") "don't touch"
    (= bottle "drinkme") "grow smaller"
    (= bottle "empty") "all gone"
    :else "unknown"))
;; => "unknown"

;; :else isn't special
(let [bottle "mystery"]
  (cond
    (= bottle "poison") "don't touch"
    (= bottle "drinkme") "grow smaller"
    (= bottle "empty") "all gone"
    :default "unknown"))
;; => "unknown"

;; 'case' is a shortcut where there is only on test value and
;; can compare with '='
(let [bottle "drinkme"]
  (case bottle
    "poison" "don't touch"
    "drinkme" "grow smaller"
    "empty" "all gone"))
;; => "grow smaller"

;; 'case' returns error if no clause matches
#_(let [bottle "mystery"]
  (case bottle
    "poison" "don't touch"
    "drinkme" "grow smaller"
    "empty" "all gone"))
;; java.lang.IllegalArgumentException    No matching clause: mystery


;; a default case

(let [bottle "mystery"]
  (case bottle
    "poison" "don't touch"
    "drinkme" "grow smaller"
    "empty" "all gone"
    "unknown"))
;; => "unknown"


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Functions creating functions and other expressions

;; 'partial' somewhat like currying

(defn grow [name direction]
  (if (= direction :small)
    (str name " is growing smaller")
    (str name " is growing bigger")))

(grow "Alice" :small)
;; => "Alice is growing smaller"

(grow "Alice" :big)
;; => "Alice is growing bigger"

;; Alice specialized function

(partial grow "Alice")
;; => #function[clojure.core/partial/fn--4759]

((partial grow "Alice") :small)
;; => "Alice is growing smaller"

;;; combining multiple functions with 'comp'

(defn toggle-grow [direction]
  (if (= direction :small) :big :small))
;; => #'wonderland.chapter02/toggle-grow

(toggle-grow :big)
;; => :small

(toggle-grow :small)
;; => :big

;; which direction are we growing
(defn oh-my [direction]
  (str "Oh My! You are growing " direction))

(oh-my (toggle-grow :small))
;; => "Oh My! You are growing :big"

;; using composition
(defn surprise [direction]
  ((comp oh-my toggle-grow) direction))

(surprise :small)
;; => "Oh My! You are growing :big"

;; some practice with partial
(defn adder [x y]
  (+ x y))

(adder 3 4)
;; => 7

(def adder-5 (partial adder 5))

(adder-5 10)
;; => 15


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Destructuring

