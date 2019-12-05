(ns myapp.spec
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;; ingredient example

;; Specs describing an ingredient
(s/def ::ingredient (s/keys :req [::name ::quantity ::unit]))
(s/def ::name  string?)
(s/def ::quantity number?)
(s/def ::unit keyword?)

(defn scale-ingredient [ingredient factor]
  (update ingredient :quantity * factor))

(s/fdef scale-ingredient
  :args (s/cat :ingredient ::ingredient :factor number?)
  :ret ::ingredient)

(def ing1 {:name "vanilla" :quantity 10 :unit :oz})

(scale-ingredient ing1 6)
;; => {:name "vanilla", :quantity 60, :unit :oz}

;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;; Defining specs
;; - (s/def name spec)

(s/def :myapp/company-name string?)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;; Validating data  - s/valid?

;;; Predicates
;; boolean? string? keyword? 

(s/def :myapp.spec/company-name string?)

(s/valid? :myapp.spec/company-name "Acme Movig")
;; => true

(s/valid? :myapp.spec/company-name 100)
;; => false


;;; Enumverated values - sets

(s/def :marble/color #{:red :green :blue})

(s/valid? :marble/color :red)
;; => true

(s/valid? :marble/color :pink)
;; => false

(s/def :bowling/roll #{0 1 2 3 4 5 6 7 8 9 10})

(s/valid? :bowling/roll 5)
;; => true

;;; Ranged specs
;; s/int-in s/double-in s/inst-in

(s/def :bowling/ranged-roll (s/int-in 0 11))

(s/valid? :bowling/ranged-roll 10)
;; => true

;;; Handling nil
;; s/nilable

(s/def :myapp.spec/company-name-2 (s/nilable string?))

(s/valid? :myapp.spec/company-name-2 "Microsoft")
;; => true
(s/valid? :myapp.spec/company-name-2 nil)
;; => true

;;; Logical specs
;; s/and s/or

(s/def ::odd-int? (s/and int? odd?))
(s/valid? ::odd-int? 5)
;; => true
(s/valid? ::odd-int? 10)
;; => false
(s/valid? ::odd-int? 5.2)
;; => false

(s/def ::odd-or-42 (s/or :odd ::odd-int? :42 #{42}))

;; - s/conform  s/explain

(s/conform ::odd-or-42 42)
;; => [:42 42]
(s/conform ::odd-or-42 19)
;; => [:odd 19]

(s/explain ::odd-or-42 0)
;; => 0 - failed: odd? at: [:odd] spec: :myapp.spec/odd-int?
;; => 0 - failed: #{42} at: [:42] spec: :myapp.spec/odd-or-42

(s/explain-str ::odd-or-42 0)
;; => "0 - failed: odd? at: [:odd] spec: :myapp.spec/odd-int?\r\n
;; => 0 - failed: #{42} at: [:42] spec: :myapp.spec/odd-or-42\r\n"

(s/explain-data ::odd-or-42 0)
;; => #:clojure.spec.alpha{:problems
;;                         ({:path [:odd],
;;                           :pred clojure.core/odd?,
;;                           :val 0,
;;                           :via [:myapp.spec/odd-or-42 :myapp.spec/odd-int?],
;;                           :in []}
;;                          {:path [:42], :pred #{42}, :val 0, :via [:myapp.spec/odd-or-42], :in []}),
;;                         :spec :myapp.spec/odd-or-42,
;;                         :value 0}


;;;  Collection specs
;; s/coll-of  - seqs - lists, vectors, sets
;; s/map-of 

(s/def ::names (s/coll-of string?))
(s/valid? ::names ["Alex" "Stu"])
;; => true
(s/valid? ::names #{"Alex" "Stur"})
;; => true
(s/valid? ::names '("Alex" "Stu"))
;; => true

;; s/coll-of options  :kind :into :count :min-count :max-count :distint

(s/def ::my-set (s/coll-of int? :kind set? :min-count 2))

(s/valid? ::my-set #{ 10 20})
;; => true

(s/def ::scores (s/map-of string? int?))
(s/valid? ::scores {"Stu" 100, "Alex" 200})
;; => true

;;; Collection sampling
;; s/every s/every-kv
;; like s/map-of or s/coll-of but for long collections

;;; Tuples

(s/def ::point (s/tuple float? float?))
(s/conform ::point [1.3 2.7])
;; => [1.3 2.7]


;;; Information maps

(s/def :music/id uuid?)
(s/def :music/artist string?)
(s/def :music/title string?)
(s/def :music/date inst?)


#_{:music/id #uuid "40e30dc1-55ac-33e1-85d3-1f1508140bfc" ​​
 :music/artist  "Rush" ​
 :music/title  "Moving Pictures" 
 :music/date  ​​ #inst "1981-02-12"}

(s/def :music/release-unqualified
  (s/keys :req-un [:music/id]
          :opt-un [:music/artist]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;; Validating functions



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;; Generative function testing



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;; Wrapping up.