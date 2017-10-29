(ns wonderland.chapter01)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Simple expressions - pg 20

(+ 1 1) ; simple expression
;; 2

12.43 ; decimal
;;12.43

1/3 ; ration
;;1/3

4/2  ; ratios reduce
;;2

;4.0/2 ; invalid - not a ratio
;;NumberFormatException Invalid number: 4.0/2  clojure.lang.LispReader.readNumber (LispReader.java:330)

(/ 1 3)
;;1/3

(/ 1 3.0)  ; simple expression - operator goes first.
;;0.3333333333333333

"jam"  ; string type
;;"jam"

:jam   ; keyword - symbolic identifier.
;;:jam

\j     ; char type
;;\j

(class \j)
;;java.lang.Character

true  ; boolean type
;;true

false
;;false

nil  ; nil type
;;nil

(class nil) ; absence of value
;;nil

;;(+ 1 nil)
;;NullPointerException   clojure.lang.Numbers.ops (Numbers.java:1013)

(+ 1 (+ 8 3)) ; nested expression
;;12


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Collections  - pg 33

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Lists

;;; list manipulation -- first rest cons conj

(first '(:rabbit :pocket-watch :marmalade :door))
;;:rabbit

(rest '(:rabbit :pocket-watch :marmalade :door))
;;(:pocket-watch :marmalade :door)

(first (rest '(:rabbit :pocket-watch :marmalade :door)))
;;:pocket-watch

(rest (rest '(:rabbit :pocket-watch :marmalade :door)))
;;(:marmalade :door)

(first (rest (rest '(:rabbit :pocket-watch :marmalade :door))))
;;:marmalade

(first (rest (rest (rest '(:rabbit :pocket-watch :marmalade :door)))))
;;:door

(first (rest (rest (rest (rest '(:rabbit :pocket-watch :marmalade :door)))))) ; now 'nil
;;nil

(cons 5 '())
;;(5)

(cons 5 nil)  ; nil is empty list
;;(5)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; building lists
(cons 4 (cons 5 nil))
;;(4 5)

(cons 3 (cons 4 (cons 5 nil)))
;;(3 4 5)

'(1 2 3 4 5)  ; simpler way - reader macro
;;(1 2 3 4 5)

(list 1 2 3 4 5)  ; function call
;;(1 2 3 4 5)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; vectors -- collecting data by index
;; first rest last nth conj

[:jar1 1 2 3 :jar2]
;;[:jar1 1 2 3 :jar2]

(first [:jar1 1 2 3 :jar2])
;;:jar1

(rest [:jar1 1 2 3 :jar2])
;;(1 2 3 :jar2)

(nth [:jar1 1 2 3 :jar2] 0)  ; zeroth index
;;:jar1

(nth [:jar1 1 2 3 :jar2] 2)
;;2

(last [:jar1 1 2 3 :jar2])
;;:jar2

(last '(:jar1 1 2 3 :jar2))
;;:jar2

;;; nth and last are more efficient for vectors than lists

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; conj adds items efficiently

(conj [:tost :butter] :jam)  ; add to end of vector
;;[:tost :butter :jam]

(conj [:tost :butter] :jam :honey)  ; multiple items
;;[:tost :butter :jam :honey]

(conj '(:toast :butter) :jam)  ; add to front list
;;(:jam :toast :butter)

(conj '(:toast :butter) :jam :honey)
;;(:honey :jam :toast :butter)


;;; Maps for storing key-value pairs+ 3 4
;; get :keys keys values assoc dissoc merge functions+ 3 4

{:jam1 "strawberry" :jam2 "blackberry"}
;; => {:jam1 "strawberry", :jam2 "blackberry"}

;; explicit get
(get {:jam1 "strawberry" :jam2 "blackberry"} :jam2)
;; => "blackberry"

;; defualt keys
(get {:jam1 "strawberry" :jam2 "blackberry"} :jam3 "not found")
;; => "not found"

;; using keys as functions for look up.
(:jam2 {:jam1 "strawberry" :jam2 "blackberry"})
;; => "blackberry"


;; keys and values
(keys {:jam1 "strawberry" :jam2 "blackberry" :jam3 "marmalade"})
;; => (:jam1 :jam2 :jam3)

(vals {:jam1 "strawberry" :jam2 "blackberry" :jam3 "marmalade"})
;; => ("strawberry" "blackberry" "marmalade")

;; "updating" a map
(assoc {:jam1 "red" :jam2 "black"} :jam1 "orange")
;; => {:jam1 "orange", :jam2 "black"}

(dissoc {:jam1 "strawberry" :jam2 "blackberry"} :jam1)
;; => {:jam2 "blackberry"}

;; combining maps
(merge {:jam1 "red" :jam2 "black"}
       {:jam1 "orange" :jam3 "red"}
       {:jam4 "blue"})
;; => {:jam1 "orange", :jam2 "black", :jam3 "red", :jam4 "blue"}


;;; sets - pg 42
;; union difference intersection set get contains?

#{:red :blue :white :pink}
;; => #{:white :red :blue :pink}

;; no duplicates
;;#{:red :blue :white :pink :pink}
;; IllegalArgumentException Duplicate key: :pink
;; clojure.lang.PersistentHashSet.createWithCheck (PersistentHashSet.java:68)

;; some set functions
(clojure.set/union #{:r :b :w} #{:w :p :y})
;; => #{:y :r :w :b :p}

(clojure.set/difference #{:r :b :w} #{:w :p :y})
;; => #{:r :b}

(clojure.set/intersection #{:r :b :w} #{:w :p :y})
;; => #{:w}

;; converting to a set
(set [:rabbit :rabbit :watch :door])
;; => #{:door :watch :rabbit}

(set {:a 1 :b 2 :c 3})
;; => #{[:c 3] [:b 2] [:a 1]}

;; using get
(get #{:rabbit :door :watch} :rabbit)
;; => :rabbit

(get #{:rabbit :door :watch} :jar)
;; => nil

;; a set is a function
(#{:rabbit :door :watch} :watch)
;; => :watch

(contains? #{:rabbit :door :watch} :rabbit)
;; => true
(contains? #{:rabbit :door :watch} :jam)
;; => false

;; adding elements
(conj #{:rabbit :door} :jam)
;; => #{:door :rabbit :jam}

;; removing elements
(disj #{:rabbit :door} :door)
;; => #{:rabbit}


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Lists are the hear of Clojure  pg 47

'("marmalade-jar" "empty-jar" "pickle-jam-jar")

'(+ 1 1)
;; => (+ 1 1)

(first '(+ 1 1))  ; first of list
;; => +

;; All code in Clojure is made of lists -- Code is data.


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Sumbols and the Art of Binding

;; define symbols with 'def'

(def developer "Allice")
;; => #'wonderland.chapter01/developer

developer
;; => "Allice"

;; local bindings with let
;; the bindings are defined in a vector

(let [developer "Alice in Wonderland"]
  developer)
;; => "Alice in Wonderland"

developer
;; => "Allice"

(let [developer "Alice in Wonderland"
      rabbit "White Rabbit"]
  [developer rabbit])
;; => ["Alice in Wonderland" "White Rabbit"]

;;rabbit
;;Unable to resolve symbol: rabbit in this context

