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

(in-ns 'programming-clojure.ch01-welcome)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Symbol Evaluation

(defn average
  [numbers]
  (/ (apply + numbers) (count numbers)))


;; This is a textual representation of a Clojure datastructure.
;; / apply + and count evaluate to functions held in vars
;; numbers evaluates to the argument passed to the function




;;;;;;;;;;;;;;;;;;;;;;;;
;;; Special forms

;; Clojure's primitive building blocks


;;; Suppressing Evaluation: `quote`

(quote x)
;; => x

(symbol? (quote x))
;; => true

;; reader syntax:
'x
;; => x

'(+ x x)
;; => (+ x x)

(list? '(+ x x))
;; => true

;; you can have a peak at what the reader produces by quoting a form
''x
;; => (quote x)

'@x
;; => (clojure.core/deref x)

'#(+ % %) ;; => (fn* [p1__7244#] (+ p1__7244# p1__7244#))

'#(+ %1 %2) ;; => (fn* [p1__7249# p2__7250#] (+ p1__7249# p2__7250#))

'`(a b ~c)
(seq (concat (list (quote a))
             (list (quote b))
             (list c)))


;;; Code Blocks: `do`
;;  evaluates all of the expressions in order, the yields the last expression.

(do
  (println "h")
  (apply * [4 5 6]))
;; hi
;; => 120

;; other forms including `fn` `let` `loop` and `try` wrap their bodies
;; in an implicit `do`.

(let [a (inc (rand-int 6))
      b (inc (rand-int 6))]
  (println (format "You rolled a %s and a %s" a b)) ;; implicit `do`
  (+ a b))


;;; Defining Vars: `def`

(def p "foo")
;; => #'programming-clojure.ch01-welcome/p
p
;; => "foo"

;; `defn` `defn-` `defprotocol` `defonce` `defmacro` are defined in terms
;; of `def`


;;; Local Bindings: `let`
;;  let defines locals

(defn hypot
  [x y]
  (let [x2 (* x x)
        y2 (* y y)]
    (Math/sqrt (+ x2 y2))))

;; all locals are immutable.
;; * `loop` and `recur` special forms provide looping cases where values need to
;;    change on each cycle
;; * or use a reference type in local bindings


;;; Destructuring (`let` - part 2)

;; One challenge of working with abstract collection is easily accessing
;; multiple values

(def v [42 "foo" 99.2 [5 12]])

;; some approaches.
(first v)                               ; convenience values
;; => 42
(second v)
;; => "foo"
(last v)
;; => [5 12]
(nth v 2)                               ; general access function
;; => 99.2
(v 2)                                   ; index
;; => 99.2
(.get v 2)                              ; java.util.List interface.
;; => 99.2

;; but could be complex when accessing multiple values
(+ (first v) (v 2))
;; => 141.2

;; or a nested collection
(+ (first v) (first (last v)))
;; => 47

;; destructuring in a `let` provides more concise syntax
;; also usable in expressions that implicitly use`let`: `fn`, `defn` `loop`.

;; two versions, sequential, and map.

;;; Sequential destructuring
;;  * lists, vectors, seqs
;;  * java.util.List
;;  * Java arrays
;;  * Strings

(def v [42 "foo" 99.2 [5 12]])

(let [[x y z] v]     ;; (+ (first v) (v 2))
  (+ x z))
;; => 141.2

;; equivalent "normal" `let` syntax.
(let [x (nth v 0)
      y (nth v 1)
      z (nth v 2)]
  (+ x z))
;; => 141.2

;; destructuring can decompose nested structures

(let [[x _ _ [_ y]] v]   ;; (+ (first v) (first (last v)))
  (+ x y))
;; => 54


;; Extra-positional sequential values.

(let [[x & rest] v]
  rest)
;; => ("foo" 99.2 [5 12])


;; Retaining the destructured form

(let [[x _ z :as original-vector] v]
  (conj original-vector (+ x z)))
;; => [42 "foo" 99.2 [5 12] 141.2]


;;; Map destructuring

;; Works with:
;; * hash-maps, array-maps, records
;; * java.util.Map
;; * Any value that can be map-destructured using indices as keys.
;;   - Clojure vectors
;;   - Strings
;;   - Arrays


(def m {:a 5, :b 6,
        :c [7 8 9],
        :d {:e 10, :f 11}
        "foo" 88,
        42 false})

(+ (:a m) (:b m))
;; => 11

(let [{a :a  b :b} m]
  (+ a b))
;; => 11

;; other than keys work

(let [{f "foo"} m]                      ; "foo" = 88
  (+ f 12))
;; => 100

(let [{v 42} m]                         ; v = false
  (if v 1 0))
;; => 0

;; much simpler to get values from a vector
;; 3rd and 8th element

(let [{x 3 y 8} [12 0 0 -18 44 6 0 0 1]]
  (+ x y))
;; => -17

;; decomposing nested maps
;; :d -> :e = 10

(let [{d :d} m]
  d)
;; => {:e 10, :f 11}

(let [{d :d} m]
  (:e d))
;; => 10

(let [{{e :e} :d} m]
  (* 2 e))
;; => 20

;; decomposing `seq` in a `map`

(let [{c :c} m]
  c)
;; => [7 8 9]

(let [{c :c} m]
  (+ (first c) (last c)))
;; => 16

(let [{[x _ y] :c} m]
  (+ x y))
;; => 16

(def map-in-vector ["James" {:birthday (java.util.Date. 73 1 6)}])

(let [[name {bd :birthday}] map-in-vector]
  (str name " was born on " bd))
;; => "James was born on Tue Feb 06 00:00:00 EST 1973"


;; Retaining the destructured value.

(let [{r1 :x r2 :y :as randoms}
      (zipmap [:x :y :z] (repeatedly (partial rand-int 10)))]
  (assoc randoms :sum (+ r1 r2)))
;; => {:x 3, :y 5, :z 9, :sum 8}
;; => {:x 0, :y 0, :z 0, :sum 0}
;; => {:x 8, :y 2, :z 5, :sum 10}
;; => {:x 5, :y 1, :z 9, :sum 6}


;; Default values
;; use an `:or` pair to provide a default pair if a key is missing.

(let [{k :unknown x :a
       :or {k 50}} m]
  (+ k x)) ; 50 + 5
;; => 55

;; more complex alternative
(let [{k :unknown x :a} m
      k (or k 50)]
  (+ k x))
;; => 55

;; `:or` understands the difference between no value and false value.
(let [{opt1 :option} {:option false}
      opt1 (or opt1 true)
      {opt2 :option :or {opt2 true}} {:option false}]
  {:opt1 opt1 :opt2 opt2})
;; => {:opt1 true, :opt2 false}


;; Binding values to their keys' names - `:keys` `:strs` `:syms` options
;; example:
(def chas {:name "Chas" :age 31 :location "Massachusetts"})

(let [{name :name age :age location :location} chas]
  (format "%s is %s years old and lives in %s." name age location))
;; => "Chas is 31 years old and lives in Massachusetts."

(let [{:keys [name age location]} chas]
  (format "%s is %s years old and lives in %s." name age location))
;; => "Chas is 31 years old and lives in Massachusetts."

;; or use `:strs` for Strings or `:syms` for symbols

(def brian {"name" "Brian" "age" 31 "location" "British Columbia"})

(let [{:strs [name age location]} brian]
  (format "%s is %s years old and lives in %s." name age location))
;; => "Brian is 31 years old and lives in British Columbia."

(def christophe {'name "Christophe" 'age 33 'location "Rhône-Alpes"})

(let [{:syms [name age location]} christophe]
  (format "%s is %s years old and lives in %s." name age location))
;; => "Christophe is 33 years old and lives in Rhône-Alpes."


;; Destructuring rest sequences as map key/value pairs.

(def user-info ["robert8990" 2011 :name "Bob" :city "Boston"])

;; the manual approach
(let [[username account-year & extra-info] user-info
      {:keys [name city]} (apply hash-map extra-info)]
  (format "%s is in %s" name city))
;; => "Bob is in Boston"

;; map destructuring of rest seqs.
(let [[username account-year & {:keys [name city]}] user-info]
  (format "%s is in %s" name city))
;; => "Bob is in Boston"


;;; Creating Functions: `fn`

;; `fn` special form for creating functions
;; which includes features of `let` and `do`

;; a simple function
(fn [x]
  (+ 10 x))

;; The arguments to a function are matched to each name or destructuring from
;; base on their positions int the calling form.

((fn [x]
   (+ 10 x)) 8) ;; => 18

;; 8 is bound to `x` - equivalent to the `let` form:
(let [x 8]
  (+ 10 x)) ;; => 18

;; functions of multiple arguments.
((fn [x y z] (+ x y z))
 3 4 12)
;; => 19

(let [x 3
      y 4
      z 12]
  (+ x y z))
;; => 19

;; functions with multiple arities
(def strange-adder (fn adder-self-reference
                     ([x] (adder-self-reference x 1))
                     ([x y] (+ x y))))

(strange-adder 10)
;; => 11
(strange-adder 10 50)
;; => 60

;; Mutually recursive functions with `letfn`

(letfn [(odd? [n]
          (if (zero? n)
            false
            (even? (dec n))))
        (even? [n]
          (or (zero? n)
              (odd? (dec n))))]
  (odd? 11))
;; => true

(letfn [(odd? [n]
          (if (zero? n)
            false
            (even? (dec n))))
        (even? [n]
          (or (zero? n)
              (odd? (dec n))))]
  (odd? 12))
;; => false

(letfn [(odd? [n]
          (if (zero? n)
            false
            (even? (dec n))))
        (even? [n]
          (or (zero? n)
              (odd? (dec n))))]
  (even? 11))
;; => false


;;; `defn` builds on `fn`.
;;  `defn` encapsulates `def` and `fn`

;; these two definitions are equivalent.
(def strange-adder (fn strange-adder
                     ([x] (strange-adder x 1))
                     ([x y] (+ x y))))

(defn strange-adder
  ([x] (strange-adder x 1))
  ([x y] (+ x y)))


;; Single arity functions can be defined with additional parentheses eliminated

(def redundant-adder (fn redundant-adder
                       [x y z]
                       (+ x y z)))

(defn redundant-adder
  [x y z]
  (+ x y z))


;;; Destructuring function arguments

;; Variadic functions

(defn concat-rest
  [x & rest]                            ; rest is a `seq`
  (apply str (butlast rest)))

(concat-rest 0 1 2 3 4)
;; => "123"


(defn make-user
  [& [user-id]]
  {:user-id (or user-id
                (str (java.util.UUID/randomUUID)))})

(make-user)
;; => {:user-id "56186418-eb4e-4a57-bb2e-86cfa82fef6d"}

(make-user "Charley")
;; => {:user-id "Charley"}


;;; Keyword arguments
;;  using the map destructuring of rest sequences idiom.

(defn make-user
  [username & {:keys [email join-date]
               :or {join-date (java.util.Date.)}}]
  {:username username
   :join-date join-date
   :email email
   ;; 2.592e9 -> one month in ms
   :exp-date (java.util.Date. (long (+ 2.592e9 (.getTime join-date))))})

(make-user "Bobby")

;; {:username "Bobby",
;;  :join-date #inst "2019-01-17T03:45:38.695-00:00",
;;  :email nil,
;;  :exp-date #inst "2019-02-16T03:45:38.695-00:00"}

(make-user "Bobby"
           :join-date (java.util.Date. 119 0 1)
           :email "bobby@example.com")

;; {:username "Bobby",
;;  :join-date #inst "2019-01-01T05:00:00.000-00:00",
;;  :email "bobby@example.com",
;;  :exp-date #inst "2019-01-31T05:00:00.000-00:00"}


;; destructuring the rest argument map using types of key values besides keywords

(defn foo
  [& {k ["m" 9]}]
  (inc k))

(foo ["m" 9] 19)
;; => 20


;;; pre- and postconditions


;;; Function literals

;; these anonymous functions are equivalent

(fn [x y] (Math/pow x y))
;; => #function[programming-clojure.ch01-welcome/eval7173/fn--7174]

#(Math/pow %1 %2)
;; => #function[programming-clojure.ch01-welcome/eval7181/fn--7182]

(read-string "#(Math/pow %1 %2)")
;; => (fn* [p1__7187# p2__7188#] (Math/pow p1__7187# p2__7188#))

;; not implicit `do` form
(fn [x y]
  (println (str x \^ y))
  (Math/pow x y))

;; requires and explicit `do`
#(do
   (println (str %1 \^ %2))
   (Math/pow %1 %2))

;;; Arity and arguments specified using unnamed positional symbols
;; equivalent

#(Math/pow %1 %2)
#(Math/pow % %2)


;;  variadic functions

(fn [x & rest]
  (- x (apply + rest)))

#(- % (apply + %&))

;; Function literals cannot be nested

(fn [x]
  (fn [y]
    (+ x y)))

;; #( #(+ % %2))  no no


;;; Conditionals: `if`
;;  Clojure's sole primitive conditional operator

;; logical truth is anything other than `nil` or `false`.
(if "hi" \t)
;; => \t
(if 42 \t)
;; => \t
(if nil "unevaluated" \f)
;; => \f
(if false "unevaluated" \f)
;; => \f
(if (not true) \t)
;; => nil

;; other conditionals `when` `cond` `if-let` `when-let` are
;; build in terms of `if`.


;;; Looping: `loop` and `recur`
;;  `doseq` and `dotimes` imperative looping constructs are built in
;;  terms of `recur`

(loop [x 5]                             ; implicit `let`
  (if (neg? x)
    x
    (recur (dec x))))                   ; transfers control to loop head.
;; => -1

;; loop heads are also set up by functions

(defn countdown
  [x]
  (if (zero? x)
    :blastoff!
    (do (println x)
        (recur (dec x)))))

(countdown 5)
;; 5
;; 4
;; 3
;; 2
;; 1
;; => :blastoff!


;; Appropriate use of `recur`
;; usually use higher-level looping or iteration forms, `doseq` or `dotimes`
;; `map` `reduce` or `for`


;;; Referring to Vars: `var`

(def x 5)

x
;; => 5

;; refer to the var itself.
(var x)
;; => #'programming-clojure.ch01-welcome/x

;; reader syntax
#'x
;; => #'programming-clojure.ch01-welcome/x

'#'x
;; => (var x)


;;; Java Interop: `.` and `new`

;; All Java interop flow through `.` and `new`


;;; Exception Handling: `try` and `throw`
;;  Java style exception handling.

;;; Specialized Mutation: `set!`
;;  Java style setter methods.
;;  * Thread local value of vars that have non-root binding: dynamic scope.
;;  * Set the value of a Java field.
;;  * mutable fields in `deftype`.


;;; Primitive locking: `monitor-enter` and `monitor-exit`.

;;  synchronizing locking primitives, never use these,
;; use the higher level versions.


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Putting it all together.

;; given
(defn average
  [numbers]
  (/ (apply + numbers) (count numbers)))

;; * This is simply a Clojure datastructure. (Homoiconicity).
;; * Lists are evaluated as function calls.
;; * Given namespaces, symbols are then evaluated in the datastructure.

;; the `defn` is shorthand for:
(def average (fn average
               [numbers]
               (/ (apply + numbers) (count numbers))))


;;; `eval`
;; scalars `eval` to themselves
(eval :foo)
;; => :foo
(eval [1 2 3])
;; => [1 2 3]
(eval "text")
;; => "text"

;; lists `eval` to function calls
(eval '(average [60 80 100 400]))
;; => 160

;; `read` of `read-string` produce Clojure values from their
;; textual representations

(eval (read-string "(average [60 80 100 400])"))
;; => 160

;; use `recur` to loop:

;; A naive reimplementation of Clojure's REPL.

(defn embedded-repl
  "A naive Clojure REPL implementation. Enter `:quit`
  to exit."
  []
  (print (str (ns-name *ns*) ">>> "))
  (flush)
  (let [expr (read)
        value (eval expr)]
    (when (not= :quit value)
      (println value)
      (recur))))
