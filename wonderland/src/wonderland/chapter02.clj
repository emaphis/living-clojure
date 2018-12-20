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
;;; Destructuring  pg 73.

;; destructuring
(let [[color size] ["blue" "small"]]
  (str "The " color " door is " size))
;; => "The blue door is small"

;; alternative.
(let [x ["blue" "small"]
      color (first x)
      size (last x)]
  (str "The " color " door is " size))
;; => "The blue door is small"

;; nesting
(let [[color [size]] ["blue" ["very small"]]]
  (str "The " color " door is " size))
;; => "The blue door is very small"

;; preserving the original structure
(let [[color [size] :as original] ["blue" ["very small"]]]
  {:color color :size size :original original})
;; => {:color "blue", :size "very small", :original ["blue" ["very small"]]}

;; maps
(let [{flower1 :flower1 flower2 :flower2}
      {:flower1 "red" :flower2 "blue"}]
  (str "The flowers are " flower1 " and " flower2))
;; => "The flowers are red and blue"

;; keep the original with a map
(let [{flower1 :flower1 :as all-flowers}
      {:flower1 "red"}]
  [flower1 all-flowers])
;; => ["red" {:flower1 "red"}]

;; keep the name of the key using :keys form
(let [{:keys [flower1 flower2]}
      {:flower1 "red" :flower2 "blue"}]
  (str "The flowers are " flower1 " and " flower2))
;; => "The flowers are red and blue"


;; using destructuring in functions

;; using key functions
(defn flower-colors [colors]
  (str "The flowers are "
       (:flower1 colors)
       " and "
       (:flower2 colors)))

(flower-colors {:flower1 "red" :flower2 "blue"})
;; => "The flowers are red and blue"

;; using :keys destructuring
(defn flower-colors [{:keys [flower1 flower2]}]
  (str "The flowers are " flower1 " and " flower2))

(flower-colors {:flower1 "red" :flower2 "blue"})
;; => "The flowers are red and blue"


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; The power of laziness  pg. 76

;; infinite lists with lazy sequences
(take 5 (range))
;; => (0 1 2 3 4)

(take 10 (range))
;; => (0 1 2 3 4 5 6 7 8 9)

(range 5)
;; => (0 1 2 3 4)

(range 10)
;; => (0 1 2 3 4 5 6 7 8 9)


(count (take 1000 (range)))
;; => 1000

(count (take 100000 (range)))
;; => 100000

;; repeat lazy sequences
(repeat 3 "rabbit")
;; => ("rabbit" "rabbit" "rabbit")

(class (repeat 3 "rabbit"))
;; => clojure.lang.Repeat

;; infinite lazy sequences
(take 5 (repeat "rabbit"))
;; => ("rabbit" "rabbit" "rabbit" "rabbit" "rabbit")

(count (take 5000 (repeat "rabbit")))
;; => 5000


;; random sequence of digits:

(repeat 5 (rand-int 10))
;; => (3 3 3 3 3)  ;; opps.

;; use repeatedly which executes a function of no arguments

#(rand-int 10)
;; => #function[wonderland.chapter02/eval7543/fn--7544]

(#(rand-int 10)) ;; => 8


(repeatedly 5 #(rand-int 10))
;; => (8 9 1 9 7)

;; lazy infinite sequence

(take 10 (repeatedly #(rand-int 10)))
;; => (4 6 0 3 2 6 3 2 6 7)


;; return a lazy sequence of a collection repeated

(take 3 (cycle ["big" "small"]))
;; => ("big" "small" "big")

(take 6 (cycle ["big" "small"]))
;; => ("big" "small" "big" "small" "big" "small")

;; end of infinity??
(take 3 (rest (cycle ["big" "small"])))
;; => ("small" "big" "small")



;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Recursion  pg.79

(def adjs ["normal"
           "too small"
           "too big"
           "is swimming"])

(defn alice-is [in out]
  (if (empty? in)
    out
    (alice-is (rest in)
              (conj out
                    (str "Alice is " (first in))))))

(alice-is adjs [])
;; => ["Alice is normal" "Alice is too small"
;;     "Alice is too big" "Alice is is swimming"]


;; using loop recure
(defn alice-is [input]
  (loop [in input
         out []]
    (if (empty? in)
      out
      (recur (rest in)
             (conj out
                   (str "Alice is " (first in)))))))

(alice-is adjs)
;; => ["Alice is normal" "Alice is too small"
;;     "Alice is too big" "Alice is is swimming"]

;; loop-recur doesn't use stack space

(defn countdown [n]
  (if (= n 0)
    n
    (countdown (- n 1))))  ;; uses stack space

(countdown 3) ;; => 0

;; (countdown 100000)  ; stack overflow

(defn countdown [n]
  (if (= n 0)
    n
    (recur (- n 1))))

(countdown 100000) ;; => 0
;; no stack overflow.


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Functional Shape of Data Transformations  pg. 82
;;; map and reduce.

;;; Map the Ultimate

(def animals [:mouse :duck :dodo :lory :eaglet])

;; transform a keyword into a string

(#(str %) :mouse) ;; => ":mouse"

(map #(str %) animals)
;; => (":mouse" ":duck" ":dodo" ":lory" ":eaglet")

;; map returns a lazy sequence
(class (map #(str %) animals)) ;; => clojure.lang.LazySeq

(take 3 (map #(str %) (range))) ;; => ("0" "1" "2")

(take 10 (map #(str %) (range)))
;; => ("0" "1" "2" "3" "4" "5" "6" "7" "8" "9")

;; side effects on lazy sequences

(def animal-print (map #(println %) animals)) 
;; => #'wonderland.chapter02/animal-print

animal-print
;; => (nil nil nil nil nil)

;; force evaluation of side effects.
(def animal-print (doall (map #(println %) animals)))
;; => #'wonderland.chapter02/animal-print


;; take more than one collection to map against

(def animals
  ["mouse" "duck" "dodo" "lory" "eaglet"])

(def colors
  ["brown" "black" "blue" "pink" "gold"])

(defn gen-animal-string [animal color]
  (str color "-" animal))

(map gen-animal-string animals colors)
;; => ("brown-mouse" "black-duck"
;;     "blue-dodo" "pink-lory" "gold-eaglet")

(map gen-animal-string animals (cycle ["brown" "black"]))
;; => ("brown-mouse" "black-duck" "brown-dodo"
;;      "black-lory" "brown-eaglet")


;;; Reduce the Ultimate

(reduce + [1 2 3 4 5]) ;; => 15

(reduce (fn [r x] (+ r (* x x))) [1 2 3])
;; => 14

;; changing the shape

(reduce (fn [ r x] (if (nil? x) r (conj r x)))
        []
        [:mouse nil :duck nil nil :lory])
;; => [:mouse :duck :lory]


;;; Other useful data shaping expressions
;;; filter


;; filter for

((complement nil?) nil) ;; => false

((complement nil?) 1) ;; => true

(filter (complement nil?) [:mouse nil :duck nil])
;; => (:mouse :duck)

;; or keyword
(filter keyword? [:mouse nil :duck nil])
;; => (:mouse :duck)

;; for

(for [animal [:mouse :duck :lory]]
  (str (name animal)))
;; => ("mouse" "duck" "lory")

;; two collections

(for [animal [:mouse :duck :lory]
      color [:red :blue]]
  (str (name color) (name animal)))
;; => ("redmouse" "bluemouse"
;;     "redduck" "blueduck"
;;     "redlory" "bluelory")

(name :dog);; => "dog"


;; suport modifiers - let
(for [animal [:mouse :duck :lory]
      color [:red :blue]
      :let [animal-str (str "animal-" (name animal))
            color-str (str "color-" (name color))
            display-str (str animal-str "-" color-str)]]
  display-str)
;; => ("animal-mouse-color-red" "animal-mouse-color-blue"
;;     "animal-duck-color-red" "animal-duck-color-blue"
;;     "animal-lory-color-red" "animal-lory-color-blue")

;; when
(for [animal [:mouse :duck :lory]
      color [:red :blue]
      :let [animal-str (str "animal-"(name animal))
            color-str (str "color-"(name color))
            display-str (str animal-str "-" color-str)]
      :when (= color :blue)]
  display-str)
;; => ("animal-mouse-color-blue"
;;     "animal-duck-color-blue"
;;     "animal-lory-color-blue")

;; flatten
(flatten [ [:duck [:mouse] [[:lory]]]])
;; => (:duck :mouse :lory)

;; changing type

(vec '(1 2 3))
;; -> [1 2 3]

(into [] '(1 2 3))
;; -> [1 2 3]

(sorted-map :b 2 :a 1 :z 3)
;; -> {:a 1, :b 2, :z 3}

(into (sorted-map) {:b 2 :c 3 :a 1})
;; -> {:a 1, :b 2, :c 3}

(into {} [[:a 1] [:b 2] [:c 3]])
;; -> {:a 1, :b 2, :c 3}

(into [] {:a 1, :b 2, :c 3})
;; -> [[:c 3] [:b 2] [:a 1]]


(partition 3 [1 2 3 4 5 6 7 8 9])
;; -> ((1 2 3) (4 5 6) (7 8 9))

(partition 3 [1 2 3 4 5 6 7 8 9 10])
;; -> ((1 2 3) (4 5 6) (7 8 9))

(partition-all 3 [1 2 3 4 5 6 7 8 9 10])
;; -> ((1 2 3) (4 5 6) (7 8 9) (10))

(partition-by #(= 6 %) [1 2 3 4 5 6 7 8 9 10])
;; -> ((1 2 3 4 5) (6) (7 8 9 10))
