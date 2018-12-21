(ns wonderland.chapter04
  (:import (java.net InetAddress)))
;;; Java Interop and Polymorphism pg. 106


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Handling Interop with Java

(class "caterpillar")
;; => java.lang.String

;; "caterpillar".toUppercase
(. "caterpillar" toUpperCase)
;; => "CATERPILLAR"

(.toUpperCase "caterpillar")
;; => "CATERPILLAR"

;; parameters after the object
(.indexOf "caterpillar" "pillar") ;; => 5

;; create new instances
(new String "Hi!!")
;; => "Hi!!"

;; short form
(String. "Hi!!")
;; => "Hi!!"

;; use imported Java classes

(InetAddress/getByName "localhost")
;; => #object[java.net.Inet4Address 0x9120773 "localhost/127.0.0.1"]

(.getHostName (InetAddress/getByName "localhost"))
;; => "localhost"

;; fully qualified
(java.net.InetAddress/getByName "localhost")
;; => #object[java.net.Inet4Address 0x43c52a1d "localhost/127.0.0.1"]

;; doto macro.
(def sb (doto (StringBuffer. "Who ")
          (.append "are ")
          (.append "you? ")))

(.toString sb)
;; => "Who are you? "


(import 'java.util.UUID)
(UUID/randomUUID)
;; => #uuid "48db6c7d-f339-4d07-9d7d-0847062bc546"


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Practical Polymorphism - pg 111

;; simple
(defn who-are-you [input]
  (cond
    (= java.lang.String (class input)) "String  Who are you?"
    (= clojure.lang.Keyword (class input)) "Keyword  Who are you?"
    (= java.lang.Long (class input)) "Number  Who are you?"))

(who-are-you :alice)
;; => "Keyword  Who are you?"

(who-are-you "alice")
;; => "String  Who are you?"

(who-are-you 123)
;; => "Number  Who are you?"

(who-are-you true)
;; => nil

;;; Multimethods

(defmulti who-are-you class)

(defmethod who-are-you java.lang.String [input]
  (str "String - who are you? " input))

(defmethod who-are-you clojure.lang.Keyword [input]
  (str "Keyword - who are you? " input))

(defmethod who-are-you java.lang.Long [input]
  (str "Long - who are you? " input))

(who-are-you :alice)
;; => "Keyword - who are you? :alice"

(who-are-you "alice")
;; => "String - who are you? alice"

(who-are-you 123)
;; => "Long - who are you? 123"

(who-are-you true)
;; java.lang.IllegalArgumentException
;; No method in multimethod 'who-are-you' for dispatch value: class
;; java.lang.Boolean

;; defualt method
(defmethod who-are-you :default [input]
  (str "I don't know - who are you? " input))

(who-are-you true)
;; => "I don't know - who are you? true"


;;; custom dispatch method

(defmulti eat-mushroom (fn [height]
                         (if (< height 3)
                           :grow
                           :shrink)))

(defmethod eat-mushroom :grow [_]
  "Eat the left side to shrink.")

(defmethod eat-mushroom :shrink [_]
  "Eat the right side to grow.")

(eat-mushroom 1)
;; => "Eat the left side to shrink."

(eat-mushroom 9)
;; => "Eat the right side to grow."


;;; protocols - pg 114.

(defprotocol BigMushroom
  (eat-mushroom [this]))

(extend-protocol BigMushroom
  java.lang.String
  (eat-mushroom [this]
    (str (.toUpperCase this) " mmm tasty!"))

  clojure.lang.Keyword
  (eat-mushroom [this]
    (case this
      :grow "Eat the right side!"
      :shrink "Eat the left side!"))

  java.lang.Long
  (eat-mushroom [this]
    (if (< this 3)
      "Eat the right side to grow"
      "Eat the left side to shrink")))

(eat-mushroom "Big Mushroom")
;; => "BIG MUSHROOM mmm tasty!"

(eat-mushroom :grow)
;; => "Eat the right side!"

(eat-mushroom 1)
;; => "Eat the right side to grow"


;;; creating our own datatypes - pg 115

(defrecord Mushroom [color height])
;; => wonderland.chapter04.Mushroom

;; create an object
(def regular-mushroom (Mushroom. "white and blue polka dots" "2 inches"))
;; => #'wonderland.chapter04/regular-mushroom

(class regular-mushroom)
;; => wonderland.chapter04.Mushroom

(.-color regular-mushroom)
;; => "white and blue polka dots"

(.-height regular-mushroom)
;; => "2 inches"


;; intefaces on records

(defprotocol Edible
  (bite-right-side [this])
  (bite-left-side [this]))


(defrecord WonderlandMushroom [color height]
  Edible
  (bite-right-side [this]
    (str "The " color " bite makes you grow bigger"))
  (bite-left-side [this]
    (str "The " color " bite makes you grow smaller")))

(defrecord RegularMushroom [color height]
  Edible
  (bite-right-side [this]
    (str "The " color " bite tastes bad"))
  (bite-left-side [this]
    (str "The " color " bite tastes bad")))

(def alice-mushroom (WonderlandMushroom. "blue dots" "3 inches"))
(def reg-mushroom (RegularMushroom. "brown" "1 inches"))


(bite-right-side alice-mushroom)
;; => "The blue dots bite makes you grow bigger"

(bite-left-side alice-mushroom)
;; => "The blue dots bite makes you grow smaller"

(bite-right-side reg-mushroom)
;; => "The brown bite tastes bad"

(bite-left-side reg-mushroom)
;; => "The brown bite tastes bad"


;;; using deftype - pg 116

(defprotocol Edible
  (bite-right-side [this])
  (bite-left-side [this]))


(deftype WonderlandMushroom []
  Edible
  (bite-right-side [this]
    (str "The bite makes you grow bigger"))
  (bite-left-side [this]
    (str "The bite makes you grow smaller")))

(defrecord RegularMushroom []
  Edible
  (bite-right-side [this]
    (str "The bite tastes bad"))
  (bite-left-side [this]
    (str "The bite tastes bad too")))


(def alice-mushroom (WonderlandMushroom.))
(def reg-mushroom (RegularMushroom.))

(bite-right-side alice-mushroom)
;; => "The bite makes you grow bigger"
(bite-left-side alice-mushroom)
;; => "The bite makes you grow smaller"

(bite-right-side reg-mushroom)
;; => "The bite tastes bad"
(bite-left-side reg-mushroom)
;; => "The bite tastes bad too"

;;; using regular maps

(defn bite-right-side [mushroom]
  (if (= (:type mushroom) "wonderland")
    "The bite makes you grow bigger"
    "The bite tastes bad"))

(defn bite-left-side [mushroom]
  (if (= (:type mushroom) "wonderland")
    "The bite makes you grow smaller"
    "The bite tastes bad too"))

(bite-right-side {:type "wonderland"})
;; => "The bite makes you grow bigger"

(bite-left-side {:type "wonderland"})
;; => "The bite makes you grow smaller"

(bite-right-side {:type "regular"})
;; => "The bite tastes bad"

(bite-left-side {:type "regular"})
;; => "The bite tastes bad too"
