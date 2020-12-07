(ns workshop.chap01)

(use 'clojure.repl)

(+ 1 2)
(+ 1 2 3)
;; => 6
(* 3 4 1)
;; => 12
(/ 9 3)
;; => 3
(println "Would you like to dance")
(println (+ 1 2))
(* 2 (+ 1 2))

(+ 4 *3)

(inc 10)
;; => 11
*1
;; => 11
(inc *1)
;; => 12

(/ 1 0)
*e

(doc str)

(str "I" "will" "be" "concatenated")
(clojure.core/str "This" " works" " too.")
(doc clojure.repl)
(find-doc #"(?i)modulus")
(mod 7 3)
;; => 1
(apropos "case")
(clojure.string/upper-case "Shout, shout, let it all out")
;; => "SHOUT, SHOUT, LET IT ALL OUT"

;;; Activity 1.01: Performing Basic Operations
(println "I am not afraid of parentheses")

(* (+ 1 2 3) (- 10 3))
;; => 42

(println "Well done!")


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Evaluation of Clojure Code


'(1 2 3)
(quote (1 2 3))

;; Basic special forms
;; when do def let fn defn


;;; Exercise: 1.03:  Working with if, do and when

(if true "Yes" "No")
;; => "Yes"
(if false (+ 3 4) (rand))

(doc do)

(do
  )

(do (println "A proof that this is executed")
    (println "And this too"))

(if true (do (println "Calculating a random number...")
             (rand))
    (+ 1 2))

(when true (println "First argument")
      (println "Second argument")
      "And the last is returned")

;;; Exercise 1.04: Using def and let

(def x 10)
x
;; => 10

(def x 20)
(inc x)
;; => 21
x
;; => 20

(do (def x 42))  ; update the global symbol.
x
;; => 42

;; local scope
(let [y 3] (println y) (* 10 y))

(let [x 10 y 20]
  (str "x is " x " and y is " y))
;; => "x is 10 and y is 20"

(def message "Let's add them all!")

(let [x (* 10 3)
      y 20
      z 100]
  (println message)
  (+ x y z))
;; => 150


;;; Exercise 1.05: Creating Simple Functions with fn and defn

(fn [])
;; => #function[workshop.chap01/eval21808/fn--21809]

(fn [x] (* x x))

((fn [x] (* x x)) 2)
;; => 4

(def square (fn [x] (* x x)))

(square 2)
;; => 4

(square *1)
;; => 16

(square *1)
;; => 256

(defn square [x] (* x x))

(square 10)
;; => 100

(defn meditate [s calm]
  (println "Clojure Meditate v1.0")
  (if calm
    (clojure.string/capitalize s)
    (str (clojure.string/upper-case s) "!")))

(meditate "in calmness lies true pleasure" true)
;; => "In calmness lies true pleasure"

(meditate "in calmness lies true pleasure" false)
;; => "IN CALMNESS LIES TRUE PLEASURE!"

(defn square
  "Returns the product of the number `x` with itself"
  [x]
  (* x x))

(doc square)


;;; Activity 1.02: Predicting the Atmospheric Carbon Dioxide Level

;; Estimate = 382 + ((Year - 2006) * 2)

(def base-co2 382)
(def base-year 2006)

(defn co2-estimate
  "Returns a (conservative) year's estimate of carbon dioxide parts per million in
   the atmospehre"
  [year]
  (let [year-diff (- year base-year)]
    (+ base-co2 (* year-diff 2))))

(doc co2-estimate)

(co2-estimate 2006)
;; => 382
(co2-estimate 2050)
;; => 470


;;; Truthiness, nil, and equality
;; false and nil are the only values that are treated as falsey in Clojure

;;; Exercise 1.06: The Truth Is Simple

(if nil "Truthy" "Falsey")
;; => "Falsey"

(if false "Truthy" "Falsey")
;; => "Falsey"

(if true "Truthy" "Falsey")
;; => "Truthy"

(if 0 "Truthy" "Falsey")
;; => "Truthy"

(if -1 "Truthy" "Falsey")
;; => "Truthy"

(if '() "Truthy" "Falsey")
;; => "Truthy"

(if [] "Truthy" "Falsey")
;; => "Truthy"

(if "false" "Truthy" "Falsey")
;; => "Truthy"

(true? 1)  ; exactly true
;; => false

(if (true? 1) "Yes" "No")
;; => "No"

(true? "true")
;; => false

(true? true)
;; => true

(false? nil)
;; => false

(false? false)
;; => true

;; exactly nil
(nil? false)
;; => false

(nil? nil)
;; => true

(nil? (println "Hello"))
;; => true

;; and returns the last truthy value or false.

(and "Hello")
;; => "Hello"

(and "Hello" "Then" "Goodbey")
;; => "Goodbey"

(and false "Hello" "Goodbye")
;; => false

;; not all expressions are evaluated - special form
(and (println "Hello") (println "Goodbye"))
;; => nil

;; or

(or "Hello")
;; => "Hello"

(or "Hello" "Then" "Goodbey")
;; => "Hello"

(or false "Then" "Goodbye")
;; => "Then"

;; not all expressions are evaluated - special form
(or true (println "Hello"))
;; => true


;;; Equality and Comparisons

;;; Exercise 1.07: Comparing Values

(= 1 1)
;; => true

(= 1 2)
;; => false

(= 1 1 1)
;; => true

(= 1 1 1 -1)
;; => false

(= nil nil)
;; => true

(= false false)
;; => true

(= false nil)
;; => false

(= "hello" "hello" (clojure.string/reverse "olleh"))
;; => true

(= [1 2 3] [1 2 3])
;; => true

;; sequences of different types can be considered equal
(= '(1 2 3) [1 2 3])
;; => true

(= 1)
;; => true

(= "I will not reason and compare: my business is to create.")
;; => true

;; >, >=, <, and <=, can only be used with numbers.

(< 1 2)
;; => true
(< 1 10 10 100)
;; => false

(< 3 2 3)
;; => false

(< -1 0 1)
;; => true

(let [x 50]
  (if (or (<= 1 x 100) (= 0 (mod x 100)))
    (println "Valid")
    (println "Invalid")))
;; => nil

(let [x 50]
  (println (if (or (<= 1 x 100) (= 0 (mod x 100))) "Valid" "Invalid")))
;; => nil

;;; Activity 1.03: The meditate Function v2.0

(defn meditate
  "Return a transformed version of the string 's' based on the 'calmness-level'"
  [s calmness-level]
  (println "Clojure Meditate v2.0")
  (if (< calmness-level 4)
    (str (clojure.string/upper-case s) ", I TELL YA!")
    (if (<= 4 calmness-level 9)
      (clojure.string/capitalize s)
      (when (= calmness-level 10)
        (clojure.string/reverse s)))))

(meditate "what we do now echoes in eternity" 1)
;; => "WHAT WE DO NOW ECHOES IN ETERNITY, I TELL YA!"

(meditate "what we do now echoes in eternity" 6)
;; => "What we do now echoes in eternity"

(meditate "what we do now echoes in eternity" 10)
;; => "ytinrete ni seohce won od ew tahw"

(meditate "what we do now echoes in eternity" 50)
;; => nil

(defn meditate
  "Return a transformed version of the string 's' based on the 'calmness-level'"
  [s calmness-level]
  (println "Clojure Meditate v2.1")
  (cond
    (< calmness-level 4) (str (clojure.string/upper-case s) ", I TELL YA!")
    (<= 4 calmness-level 9) (clojure.string/capitalize s)
    (= 10 calmness-level) (clojure.string/reverse s)))







