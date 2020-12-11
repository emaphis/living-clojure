(ns workshop.chap02)

(use 'clojure.repl)

"I am a String"
"I am immutable"

(println "\"The measure of intelligence is the ability to change\" - Albert Einstein")

;; Strings are immutable

(def silly-string "I am immutble. I am a silly String")

(clojure.string/replace silly-string "silly" "clever")
;; => "I am immutble. I am a clever String"

silly-string
;; => "I am immutble. I am a silly String"

;; strings are collections
(first "a collection of characters")
;; => \a

(type *1)
;; => java.lang.Character

;; String functions

(str "That's the way you " "con" "ca" "te" "nate")
;; => "That's the way you concatenate"

(str *1 " - " silly-string)
;; => "That's the way you concatenate - I am immutble. I am a silly String - I am immutble. I am a silly String"

;; clojure.string namespace
(dir clojure.string)

(clojure.string/includes? "potatoes" "toes")
;; => true

;;; Numbers

(type 1)
;; => java.lang.Long

(type 1000000000000000000)
;; => java.lang.Long
(type 10000000000000000000)
;; => clojure.lang.BigInt

5/4
(/ 3 4)
;; => 3/4

(type 3/4)
;; => clojure.lang.Ratio

1.2
(/ 3 4.0)
;; => 0.75

(* 1.0 2)
;; => 2.0  -  floating point is contagious.

(type (* 1.0 2))
;; => java.lang.Double

Math/PI
;; => 3.141592653589793

(Math/random)
;; => 0.3734623618975009

(Math/sqrt 9)
;; => 3.0

(Math/round 0.7)
;; => 1


;;; Exercise 2.01: The Obfuscation Machine

(clojure.string/replace "Hello World" #"\w" "!")
;; => "!!!!! !!!!!"

(clojure.string/replace "Hell0 W0rld" #"\d" "o")
;; => "Hello World"

;; passing an anonymous function that takes the matching letter as a parameter:
(clojure.string/replace "Hello World" #"\w" (fn [letter] (do (println letter) "!")))
;; => "!!!!! !!!!!"

(int \a)
;; => 97

(char 97)
;; => \a

(first (char-array "a"))
;; => \a

(int (first (char-array "a")))
;; => 97

(Math/pow (int (first (char-array "a"))) 2)
;; => 9409.0

(defn encode-letter
  [s]
  (let [code (Math/pow (int (first (char-array s))) 2)]
    (str (int code))))

(encode-letter "a")
;; => "9409"

(defn encode
  [s]
  (clojure.string/replace s #"\w" encode-letter))

(encode "Hello World")
;; => "518410201116641166412321 756912321129961166410000"

(defn encode-letter
  [s x]
  (let [code (Math/pow (+ x (int (first (char-array s)))) 2)]
    (str "*" (int code))))

(defn encode
  [s]
  (let [number-of-words (count (clojure.string/split s #" "))]
    (clojure.string/replace s #"\w" (fn [s] (encode-letter s number-of-words)))))

(encode "Super secret message")
;; => "*7396*14400*13225*10816*13689 *13924*10816*10404*13689*10816*14161 *12544*10816*13924*13924*10000*11236*10816"


(defn decode-letter
  [x y]
  (let [number (Integer/parseInt (subs x 1))
        letter (char (- (Math/sqrt number) y))]
    (str letter)))


(defn decode [s]
  (let [number-of-words (count (clojure.string/split s #" "))]
    (clojure.string/replace s #"\*\d+" (fn [s] (decode-letter s number-of-words)))))

(decode "*7396*14400*13225*10816*13689 *13924*10816*10404*13689*10816*14161 *12544*10816*13924*13924*10000*11236*10816")
;; => "Super secret message"


;;(encode "If you want to keep a secret, you must also hide it from yourself.")
;; => "*7569*13456 *18225*15625*17161 *17689*12321*15376*16900 *16900*15625 *14641*13225*13225*15876 *12321 *16641*13225*12769*16384*13225*16900, *18225*15625*17161 *15129*17161*16641*16900 *12321*14884*16641*15625 *13924*14161*12996*13225 *14161*16900 *13456*16384*15625*15129 *18225*15625*17161*16384*16641*13225*14884*13456."


;;(decode *1)
;; => "If you want to keep a secret, you must also hide it from yourself."


;;; Booleans
;; true false


;;; Symbols - identifiers refering to something else

(def foo "bar")
foo
;; => "bar"
(defn add-2 [x] (+ x 2))
add-2
;; => #function[workshop.chap02/add-2]


;;; Keywords

:foo
;; => :foo

:another_keyword
;; => :another_keyword


;;; Collections
;;   maps, sets, vectors, lists

;;; Maps

{:artist "David Bowtie" :song "The Man Who Mapped the World" :year 1970}
;; => {:artist "David Bowtie", :song "The Man Who Mapped the World", :year 1970}

{"David Bowtie" {"The Man Who Mapped the World" {:year 1970, :duration "4:01"}
                 "Comma Oddity" {:year 1969, :duration "5:19"}}
 "Crosby Stills Hash" {"Helplessly Mapping" {:year 1969, :duration "2:38"}
                       "Almost Cut My Hair" {:year 1970, :duration "4:29", :featuring ["Neil Young", "Rich
Hickey"]}}}


;; hash-map function
(hash-map :a 1 :b 2 :c 3)
;; => {:c 3, :b 2, :a 1}

;;{:name "Lucy" :age 32 :name "Hon"}  ; no duplicate key names

{:name "Lucy" :age 32 :number-of-teeth 32}
;; => {:name "Lucy", :age 32, :number-of-teeth 32}


;;; Exercise 2.02: Using Maps

(def favorite-fruit {:name "Kiwi", :color "Green" :kcal_per_100g 61 :distinguishig_mark "Hairy"})

(get favorite-fruit :name)
;; => "Kiwi"

(get favorite-fruit :color)
;; => "Green"

;; fallback value

(get favorite-fruit :taste)
;; => nil

(get favorite-fruit :taste "Very good 8/10")
;; => "Very good 8/10"

;; maps and key-words can be used as lookup functions

(favorite-fruit :color)
;; => "Green"

(:color favorite-fruit)
;; => "Green"

;; fallback
(:shape favorite-fruit "egg-like")
;; => "egg-like"

(assoc favorite-fruit :shape "egg-like")
;; => {:name "Kiwi", :color "Green", :kcal_per_100g 61, :distinguishig_mark "Hairy", :shape "egg-like"}

(assoc favorite-fruit :color "Brown")
;; => {:name "Kiwi", :color "Brown", :kcal_per_100g 61, :distinguishig_mark "Hairy"}

;; assoc compound data
(assoc favorite-fruit :yearly_production_in_tonnes {:china 202500 :italy 541000 :new_zealand 412000 :iran 311000 :chile 225000})
;; => {:name "Kiwi", :color "Green", :kcal_per_100g 61, :distinguishig_mark "Hairy", :yearly_production_in_tonnes {:china 202500, :italy 541000, :new_zealand 412000, :iran 311000, :chile 225000}}

;; update a field
(assoc favorite-fruit :kcal_per_100g (- (:kcal_per_100g favorite-fruit) 1))
;; => {:name "Kiwi", :color "Green", :kcal_per_100g 60, :distinguishig_mark "Hairy"}

(update favorite-fruit :kcal_per_100g dec)
;; => {:name "Kiwi", :color "Green", :kcal_per_100g 60, :distinguishig_mark "Hairy"}

(update favorite-fruit :kcal_per_100g - 10)
;; => {:name "Kiwi", :color "Green", :kcal_per_100g 51, :distinguishig_mark "Hairy"}

;; remove elements
(dissoc favorite-fruit :distinguishig_mark)
;; => {:name "Kiwi", :color "Green", :kcal_per_100g 61}
(dissoc favorite-fruit :kcal_per_100g :color)
;; => {:name "Kiwi", :distinguishig_mark "Hairy"}


;;; Sets

#{1 2 3 4 5}
;; => #{1 4 3 2 5}

;; hash-set function
(hash-set :a :b :c :d)
;; => #{:c :b :d :a}

(set [:a :b :c])
;; => #{:c :b :a}

;; set doesn't error on duplicate entries
(set ["No" "Copy" "Cats" "Cats" "Please"])
;; => #{"Copy" "Please" "Cats" "No"}

;; ordered set
(sorted-set "No" "Copy" "Cats" "Cats" "Please")
;; => #{"Cats" "Copy" "No" "Please"}


;;; Exercise 2.03: Using Sets
;; supported currencies

(def supported-currencies #{"Dollar" "Japanese yen" "Euro" "Indian rupee" "British pound"})
;; => #'workshop.chap02/supported-currencies

(get supported-currencies "Dollar")
;; => "Dollar"

(get supported-currencies "Swiss franc")
;; => nil

;; just check for containment
(contains? supported-currencies "Dollar")
;; => true

(contains? supported-currencies "Swiss franc")
;; => false

;; ("Dollar" supported-currencies)  ; Strings aren't functions

;; add an element to a set - The Great and Mighty Conj
(conj supported-currencies "Monopoly Money")
;; => #{"Japanese yen" "Indian rupee" "Euro" "Dollar" "Monopoly Money" "British pound"}

(conj supported-currencies "Monopoly Money" "Gold dragon" "Gil")
;; => #{"Gold dragon" "Japanese yen" "Indian rupee" "Euro" "Dollar" "Monopoly Money" "British pound" "Gil"}

;; remove items
(disj supported-currencies "Dollar" "British pound")
;; => #{"Japanese yen" "Indian rupee" "Euro"}


;;; Vectors

[1 2 3]
;; => [1 2 3]

;; vector function
(vector 10 15 2 15 0)
;; => [10 15 2 15 0]

;; create a vector from another collection
(vec #{1 2 3})
;; => [1 3 2]

;; polymorphic
[nil :keyword "String" {:answers [:yep :nope]}]
;; => [nil :keyword "String" {:answers [:yep :nope]}]

;; Exercise 2.04: Using Vectors

;; indexed by number
(get [:a :b :c] 0)
;; => :a

(get [:a :b :c] 2)
;; => :c

(get [:a :b :c] 10)
;; => nil

(def fibonacci [0 1 1 2 3 5 8])
;; => #'workshop.chap02/fibonacci

(get fibonacci 6)
;; => 8

;; Vetors are funtions
(fibonacci 6)
;; => 8

;; adding values.
(conj fibonacci 13 21)
;; => [0 1 1 2 3 5 8 13 21]

(let [size (count fibonacci)
      last-number (last fibonacci)
      second-to-last-number (fibonacci (- size 2))]
  (conj fibonacci (+ last-number second-to-last-number)))
;; => [0 1 1 2 3 5 8 13]


;;; Lists

'(1 2 3)
;; => (1 2 3)

(+ 1 2 3)
;; => 6

'(+ 1 2 3)
;; => (+ 1 2 3)

;; list function
(list :a :b :c)
;; => (:a :b :c)

(first '(:a :b :c :d))
;; => :a

(rest '(:a :b :c :d))
;; => (:b :c :d)

(nth '(:a :b :c :d) 2)
;; => :c

;;; Exercise 2.05: Using Lists

(def my-todo (list "Feed the cat" "Clean the bathroom" "Save the world"))

(cons "Go to work" my-todo)
;; => ("Go to work" "Feed the cat" "Clean the bathroom" "Save the world")

(conj my-todo "Go to work")
;; => ("Go to work" "Feed the cat" "Clean the bathroom" "Save the world")

(conj my-todo "Go to work" "Wash my socks")
;; => ("Wash my socks" "Go to work" "Feed the cat" "Clean the bathroom" "Save the world")

(rest my-todo)
;; => ("Clean the bathroom" "Save the world")

(rest my-todo)
;; => ("Clean the bathroom" "Save the world")

(nth my-todo 2)
;; => "Save the world"


;;; Collection and Sequence Abstractions

;; Collections - maps, sets, vectors, lists
;; Sequences - vectors lists  - particular order.


(def language {:name "Clojure" 
               :creator "Rich Hickey" 
               :platforms ["Java" "JavaScript" ".NET"]})

(count language)
;; => 3

(count #{})
;; => 0

(empty? language)
;; => false

(empty? [])
;; => true

;; A map is not sequencial because it doesn't have an order
(seq language)
;; => ([:name "Clojure"] [:creator "Rich Hickey"] [:platforms ["Java" "JavaScript" ".NET"]])

(nth (seq language) 1)
;; => [:creator "Rich Hickey"]

;; collections are converted to seq by sequence functions, so you don't have to call the seq function
(first #{:a :b :c})
;; => :c

(rest #{:a :b :c})
;; => (:b :a)

(last language)
;; => [:platforms ["Java" "JavaScript" ".NET"]]

;; put one collection into another collecyion
(into [1 2 3 4] #{5 6 7 8})
;; => [1 2 3 4 7 6 5 8]

(into #{1 2 3 4} [5 6 7 8])
;; => #{7 1 4 6 3 2 5 8}

(into #{} [1 2 3 4])
;; => #{1 4 3 2}

;; pass a collection of tuples to put items into a map
(into {} [[:a 1] [:b 2] [:c 3]])
;; => {:a 1, :b 2, :c 3}

(seq {:a 1 :b 2 :c 3})
;; => ([:a 1] [:b 2] [:c 3])

;; items are added to the fron of a list
(into '() [1 2 3 4])
;; => (4 3 2 1)

(into (seq []) [1 2 3 4])
;; => (4 3 2 1)

;; concat behaves differently than into
(concat '(1 2) '(3 4))
;; => (1 2 3 4)
(into '(1 2) '(3 4))
;; => (4 3 1 2)

(concat #{1 2 3} #{1 2 3 4}) 

(concat {:a 1} ["Hello"])
;; => ([:a 1] "Hello")

;; sort
(def alphabet #{:a :b :c :d :e :f})

alphabet
;; => #{:e :c :b :d :f :a}

(sort alphabet)
;; => (:a :b :c :d :e :f)

(sort [3 7 5 1 9])
;; => (1 3 5 7 9)

;; back into a vector
(into [] (sort [3 7 5 1 9]))
;; => [1 3 5 7 9]

;; conj a tuple onto a map
(conj language [:created 2007])
;; => {:name "Clojure", :creator "Rich Hickey", :platforms ["Java" "JavaScript" ".NET"], :created 2007}

;; vector is an associative collection where the key is the index
(assoc [:a :b :c :d] 2 :z)
;; => [:a :b :z :d]  ; replaced


;;; Exercise 2.06: Working with Nested Data Structures

;; the database:
(def gemstone-db
  { :ruby {:name "Ruby"
           :stock 120
           :sales [1990 3644 6376 4918 7882 6747 7495 8573 5097 1712]
           :properties {:dispersion 0.018
                        :hardness 9.0
                        :refractive-index [1.77 1.78]
                        :color "Red" }}
   :diamond {:name "Diamond"
             :stock 10
             :sales [8295 329 5960 6118 4189 3436 9833 8870 9700 7182 7061 1579]
             :properties {:dispersion 0.044
                          :hardness 10
                          :refractive-index [2.417 2.419]
                          :color "Typically yellow, brown or gray to colorless" }}
   :moissanite {:name "Moissanite"
                :stock 45
                :sales [7761 3220]
                :properties {:dispersion 0.104
                             :hardness 9.5
                             :refractive-index [2.65 2.69]
                             :color "Colorless, green, yellow" }}
   })

;; get the harness of the Ruby
(get (get (get gemstone-db :ruby) :properties) :hardness)
;; => 9.0

;; use keywords for elegance.
(:hardness (:properties (:ruby gemstone-db)))
;; => 9.0

;; use get-in
(get-in gemstone-db [:ruby :properties :hardness])
;; => 9.0

(defn durability
  [db gemstone]
  (get-in db [gemstone :properties :hardness]))

(durability gemstone-db :ruby)
;; => 9.0
(durability gemstone-db :moissanite)
;; => 9.5

;; update db
(assoc (:ruby gemstone-db) :properties {:color "Near colorless through pink through all shades of red to a deep crimson"})
;; => {:name "Ruby", :stock 120, :sales [1990 3644 6376 4918 7882 6747 7495 8573 5097 1712], :properties {:color "Near colorless through pink through all shades of red to a deep crimson"}}

;; oops blew out other properties

(into {:a 1 :b 2} {:c 3})
;; => {:a 1, :b 2, :c 3}

;; try update with into to change :color
(update (:ruby gemstone-db) :properties into {:color  "Near colorless through pink through all shades of red to a deep crimson"})
;; => {:name "Ruby", :stock 120, :sales [1990 3644 6376 4918 7882 6747 7495 8573 5097 1712], :properties {:dispersion 0.018, :hardness 9.0, :refractive-index [1.77 1.78], :color "Near colorless through pink through all shades of red to a deep crimson"}}

;; use assoc-in
(assoc-in gemstone-db [:ruby :properties :color]
          "Near colorless through pink through all shades of red to a deep crimson")

(clojure.pprint/pprint *1)

(defn change-color
  [db gemstone new-color]
  (assoc-in db [gemstone :properties :color] new-color))

(change-color gemstone-db :ruby "Some kind of red")

;; change stock and make sale

(update-in gemstone-db [:diamond :stock] dec)

;; now change the sales vector with update-in an conj
(update-in gemstone-db [:diamond :sales] conj 999)

(defn sell
  [db gemstone client-id]
  (let [clients-updated-db (update-in db [gemstone :sales] conj client-id)]
    (update-in clients-updated-db [gemstone :stock] dec)))

(sell gemstone-db :moissanite 123)


;;; Activity 2.01: Creating a Simple In-Memory Database
;;  pg. 75

;; functions to store db in memory
(def memory-db (atom {}))
(defn read-db [] @memory-db)
(defn write-db [new-db] (reset! memory-db new-db))

;; data shape
{:table-1 {:data [] :indexes {}} :table-2 {:data [] :indexes {}}}

(def example
  {:clients {:data [{:id 1 :name "Bob" :age 30} {:id 2 :name "Alice" :age 24}]
             :indexes {:id {1 0, 2 1}}},
   :fruits {:data [{:name "Lemon" :stock 10} {:name "Coconut" :stock 3}]
            :indexes {:name {"Lemon" 0, "Coconut" 1}}},
   :purchases {:data [{:id 1 :user-id 1 :item "Coconut"} {:id 1 :user-id 2 :item "Lemon"}]
               :indexes {:id {1 0, 2 1}}}
   })


(defn refresh [] (write-db example))

(defn create-table
  [name]
  (let [db (read-db)]
    (write-db (assoc db name  {:data [] :indexes {}}))))

;; (refresh)
;; (create-table :nuts)
;; (read-db)
;; {:clients {:data [{:id 1, :name "Bob", :age 30} {:id 2, :name "Alice", :age 24}], :indexes {:id {1 0, 2 1}}},
;;  :fruits {:data [{:name "Lemon", :stock 10} {:name "Coconut", :stock 3}], :indexes {:name {"Lemon" 0, "Coconut" 1}}},
;;  :purchases {:data [{:id 1, :user-id 1, :item "Coconut"} {:id 1, :user-id 2, :item "Lemon"}], :indexes {:id {1 0, 2 1}}},
;;  :nuts {:data [], :indexes {}}}

(defn drop-table
  [name]
  (let [db (read-db)]
    (write-db (dissoc db name))))

;;(drop-table :nuts)a
;;(read-db)

(defn insert
  [table record id-key]
  (let [db (read-db)
        new-db (update-in db [table :data] conj record)
        new-idx (- (count (get-in new-db [table :data])) 1)]
    (write-db (update-in new-db [table :indexes id-key] assoc (id-key record) new-idx))))


(defn select-*
  [name]
  (get-in (read-db) [name :data]))


(defn select-*-where
  [name field field-value]
  (let [db (read-db)
        index (get-in db [name :indexes field field-value])
        data (get-in db [name :data])]
    (get data index)))


;; NOTE:  more research of update-in, get-in ...
