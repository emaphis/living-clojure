(ns myapp.chap13)

;;;; Records and Protocols

;;; The trouble with maps

(defn get-watson-1 [] (,,,))
(defn get-watson-2 [] (,,,))

(let [watson-1 (get-watson-1)
      watson-2 (get-watson-2)]
  (str watson-1 ,,, watson-2 ,,,))

;; A fictional character.

(defn get-watson-1 []
  {:name "John Watson"
   :appears-in "Sign of the Four"
   :author "Doyl"})

(defn get-watson-2 []
  {:cpu "Power 7"
   :no-cpus 2880
   :storage-gb 4000})


;;; Striking a more specific bargain with records

;; define `defrecord`

(defrecord FictionalCharacter [name appears-in author])

(def watson
  (->FictionalCharacter "John Watson" "Sign of the Four" "Doyl"))

(def elizabeth (map->FictionalCharacter
                {:name "Elizabeth Bennet"
                 :appears-in "Pride and Prejudice"
                 :author "Austen"}))


;;; Records are maps

(:name elizabeth) ;; => "Elizabeth Bennet"
(:appears-in watson) ;; => "Sign of the Four"

(count elizabeth) ;; => 3
(keys watson) ;; => (:name :appears-in :author)

;; `assoc` update data in records
(def specific-watson (assoc watson :appears-in "Hound of the Baskerville"))

;; add data to records
(def more-about-watson (assoc watson :address "2218 Baker Street"))


;;; Record advantages

(def irene {:name "Itene Adler"
            :appears-in "A Scandal in Bohemia"
            :author "Doyl"})

(:name watson) ;; => "John Watson"
(:name irene) ;; => "Itene Adler"

;; Clearer code

;; define the record type
(defrecord FictionalCharacter [name appears-in author])
(defrecord SuperComputer [cpu no-cpus storage-gb])

(def watson-1 (->FictionalCharacter
               "John Watson"
               "Sign of the Four"
               "Doys"))

(def watson-2 (->SuperComputer "Power7" 2880 4000))

(class watson-1) ;; => myapp.chap13.FictionalCharacter
(class watson-2) ;; => myapp.chap13.SuperComputer

(instance? FictionalCharacter watson-1) ;; => true
(instance? SuperComputer watson-1) ;; => false

;; Don't do this!
(defn process-fictional-charater [char]
  (:name char))
(defn process-computer [com]
  (:cpu com))

(defn process-thing [x]
  (if (= (instance? FictionalCharacter x))
    (process-fictional-charater x)
    (process-computer x)))

(process-thing watson-1) ;; => "John Watson"
(process-thing watson-2) ;; => nil


;;; Protocols

(defrecord Employee [first-name last-name department])

(def alice (->Employee "Alice" "Smith" "Engineering"))

;; Let's define commonality between Employee and Fictionalcharacter

(defprotocol Person
  (full-name [this])
  (greeting [this msg])
  (description [this]))

(defrecord FictionalCharacter [name appears-in author]
  Person
  (full-name [this] (:name this))
  (greeting [this msg] (str msg " " (:name this)))
  (description [this]
    (str (:name this) " is a character in " (:appears-in this))))

(defrecord Employee [first-name last-name department]
  Person
  (full-name [this] (str first-name " " last-name))
  (greeting [this msg] (str msg  " " (:first-name this)))
  (description [this]
    (str (:first-name this) " works in " (:department this))))

(def sofia (->Employee "Sofia" "Diego" "Finance"))

(def sam (->FictionalCharacter "Sam Weller" "The Pickwick Papers" "Dickens"))

(full-name sofia) ;; => "Sofia Diego"
(full-name sam) ;; => "Sam Weller"

(description sofia) ;; => "Sofia works in Finance"
(description sam) ;; => "Sam Weller is a character in The Pickwick Papers"

(greeting sofia "Hello!") ;; => "Hello! Sofia"
(greeting sam "Greetings");; => "Greetings Sam Weller"

(:author sam) ;; => "Dickens"

                                        ;
;;; Decentralized polymorphism

;; Add polymorphism after the fact.

(defprotocol Marketable
  (make-slogan [this]))

(extend-protocol Marketable
  Employee
  (make-slogan [e] (str (:first-name e) " is the BEST employee!"))
  FictionalCharacter
  (make-slogan [fc] (str (:name fc) " is the GREATEST character!"))
  SuperComputer
  (make-slogan [sc] (str "This computer has " (:no-cpus sc) " CPUs!")))

;; Extend any type

(extend-protocol Marketable
  String
  (make-slogan [s] (str \" s \" " is a string! WOW!"))
  Boolean
  (make-slogan [b] (str b " is one of the two surviving Booleans!")))

(make-slogan sam) ;; => "Sam Weller is the GREATEST character!"
(make-slogan sofia) ;; => "Sofia is the BEST employee!"
(make-slogan watson-2) ;; => "This computer has 2880 CPUs!"
(make-slogan "Cherie") ;; => "\"Cherie\" is a string! WOW!"
(make-slogan false)  ;; => "false is one of the two surviving Booleans!"
