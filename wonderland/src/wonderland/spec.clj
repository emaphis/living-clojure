(ns wonderland.spec
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]
            [clojure.repl :as r]))

;;; Specifications

(defn scale-ingredient [ingredient factor]
  (update ingredient :quantity * factor))

;; specs describing an ingredient
(s/def ::ingredient (s/keys :req [::name ::quantity ::unit]))
(s/def ::name       string?)
(s/def ::quantity   number?)
(s/def ::unit       keyword?)

;; function spec for scale-ingredient
(s/fdef scale-ingredient
  :args (s/cat :ingredient ::ingredient :factor number?)
  :ret ::ingredient)

;; tools that spec provides:
;;  data validation
;;  explanations of invalid data
;;  generation of example data
;;  automatically created generative tests for functions with a spec.


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Defining specs

;; (s/def name spec) - defines a spec.

;; At runtime, specs are read and evaluated like function definitions.
;; The global spec registry stores the spec, keyed by name

(s/def ::company-name string?)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Validating data

;;; Predicates
(s/def :wonderland.spec/company-name string?)

(s/valid? :wonderland.spec/company-name "Acme Moving")
;; => true

(s/valid? :wonderland.spec/company-name 100)
;; => false


;;; Spec for Enumerated values

(s/def :marble/color #{:red :green :blue})

(s/valid? :marble/color :red)
;; => true

(s/valid? :marble/color :pink)
;; => false

(s/def :bowling/roll #{0 1 2 3 4 5 6 7 8 9})

(s/valid? :bowling/roll 5)
;; => true

;;; Range specs

(s/def :bowling/ranged-roll (s/int-in 0 11))

(s/valid? :bowling/ranged-roll 10)
;; => true


;;; Handling 'nil

(s/def :my.app/company-name-2 (s/nilable string?))

(s/valid? :my.app/company-name-2 nil)
;; => true

;; (r/source s/nilable)

;; testing for the set #{true, false, nil}

(s/def ::nilable-boolean (s/nilable boolean?))


;;; Logical specs

(s/def ::odd-int (s/and int? odd?))
(s/valid? ::odd-int 5)
;; => true
(s/valid? ::odd-int 10)
;; => false
(s/valid? ::odd-int 5.3)
;; => false

;; multiple enumerated alternatives

(s/def ::odd-or-42 (s/or :odd ::odd-int :42 #{42}))

;; conform

(s/conform ::odd-or-42 42)
;; => [:42 42]
(s/conform ::odd-or-42 19)
;; => [:odd 19]

(s/conform ::odd-or-42 0)
;; => :clojure.spec.alpha/invalid

(s/explain ::odd-or-42 0)
;; => nil
;; 0 - failed: odd? at: [:odd] spec: :wonderland.spec/odd-int
;; 0 - failed: #{42} at: [:42] spec: :wonderland.spec/odd-or-42

(s/explain-data ::odd-or-42 0)
;; #:clojure.spec.alpha{:problems ({:path [:odd],
;;                                  :pred clojure.core/odd?,
;;                                  :val 0,
;;                                  :via [:wonderland.spec/odd-or-42
;;                                        :wonderland.spec/odd-int],
;;                                  :in []} {:path [:42],
;;                                           :pred #{42},
;;                                           :val 0,
;;                                           :via [:wonderland.spec/odd-or-42],
;;                                           :in []}), :spec
;;                      :wonderland.spec/odd-or-42,
;;                      :value 0}


;;; Collection specs
;; s/coll-of  s/map-of

(s/def ::names (s/coll-of string?))
(s/valid? ::names ["Alex" "Stu"])
;; => true
(s/valid? ::names #{"Alex" "Stu"})
;; => true
(s/valid? ::names '("Alex" "Stu"))
;; => true

(s/def ::my-set (s/coll-of int? :kind set? :min-count 2))
(s/valid? ::my-set #{10 20})
;; => true

;; map-of

(s/def ::scores (s/map-of string? int?))
(s/valid? ::scores {"Stu" 100, "Alex" 200})
;; => true
(s/valid? ::scores {100 "Stu", 200 "Alex"})
;; => false


;;; Collection sampling

;; s/every?  s/every-kv
;; s/*coll-check-limit*


;;; Tuples - vectors of a known structure

(s/def ::point (s/tuple float? float?))
(s/conform ::point [1.3 2.7])
;; => [1.3 2.7]


;;;;;;;;;;;;;;;;;;;;;;;;
;;; Information maps

(create-ns 'demo.music.release)
(alias 'music 'demo.music.release)

(s/def ::music/id uuid?)
(s/def ::music/artist string?)
(s/def ::music/title string?)
(s/def ::music/date inst?)

;; {::music/id #uuid "40e30dc1-55ac-33e1-85d3-1f1508140bfc"​
;;  ::music/artist "Rush"
;;  ::music/title "Moving Pictures"
;;  ::music/date #inst "1981-02-12"}


(s/def ::music/release
  (s/keys :req [::music/id]
          :opt [::music/artist
                ::music/title
                ::music/date]))

;; unqualified version
(s/def ::music/release-unqualified
  (s/keys :req-un [::music/id]
          :opt-un [::music/artist
                   ::music/title
                   ::music/date]))

