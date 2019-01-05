(ns wonderland.spec
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]
            [clojure.repl :as r]
            [clojure.string :as str]))

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


(s/def :company-name string?)

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


;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Validating functions

;;; sequences with structure

(s/def ::cat-example (s/cat :s string? :i int?))

(s/valid? ::cat-example ["abc" 100])
;; => true

(s/conform ::cat-example ["abc" 100])
;; => {:s "abc", :i 100}

(s/def ::alt-example (s/alt :i int? :k keyword?))

(s/valid? ::alt-example [100])
;; => true

(s/valid? ::alt-example [:foo])
;; => true

(s/conform ::alt-example [:foo])
;; => [:k :foo]


;;; Repetition operators
;; s/? 0 or 1
;; s/* 0 or more
;; s/+ 1 or more


;; one or more odd numbers and an optional trailing even number
(s/def ::oe (s/cat :odds (s/+ odd?) :even (s/? even?)))

(s/conform ::oe [1 3 5 100])
;; => {:odds [1 3 5], :even 100}

;; factored into smaller pieces

(s/def ::odds (s/+ odd?))
(s/def ::optional-even (s/? even?))
(s/def ::oe2 (s/cat :odds ::odds :even ::optional-even))

(s/conform ::oe2 [1 3 5 100])
;; => {:odds [1 3 5], :even 100}


;;; variable argument lists

(s/def ::println-args (s/* any?))

;; one initial set, followed by any number of sets
;; (r/doc clojure.set/intersection)

(clojure.set/intersection #{1 2} #{2 3} #{2 5})
;; => #{2}

(s/def ::intersection-args
  (s/cat :s1 set?
         :sets (s/* set?)))

(s/conform ::intersection-args '[#{1 2} #{2 3} #{2 5}])
;; => {:s1 #{1 2}, :sets [#{3 2} #{2 5}]}

(s/def ::intersection-args-2 (s/+ set?))
(s/conform ::intersection-args-2 '[#{1 2} #{2 3} #{2 5}])
;; => [#{1 2} #{3 2} #{2 5}]


;; (atom x & options)  :meta :validator

(s/def ::meta map?)

(s/def ::validator ifn?)

(s/def ::atom-args
  (s/cat :x any? :options (s/keys* :opt-un [::meta ::validator])))

(s/conform ::atom-args [100 :meta {:foo 1} :validator int?])
;; {:x 100,
;;  :options {:meta {:foo 1},
;;            :validator #function[clojure.core/int?]}}


;;; Multi-arity argument list

;; repeat function ([x] [n x])
(s/def ::repeat-args
  (s/cat :n (s/? int?) :x any?))

(s/conform ::repeat-args [100 "foo"])
;; => {:n 100, :x "foo"}

(s/conform ::repeat-args ["foo"])
;; => {:x "foo"}


;;; Specifying functions

;; rand - ([] [n])

(s/def ::rand-args (s/cat :n (s/? number?)))

(s/def ::rand-ret double?)

(s/def ::rand-fn
  (fn [{:keys [args ret]}]
    (let [n (or (:n args) 1)]
      (cond (zero? n) (zero? ret)
            (pos? n) (and (>= ret 0) (< ret n))
            (neg? n) (and (<= ret 0) (> ret n))))))

(s/fdef clojure.core/rand
  :args ::rand-args
  :ret  ::rand-ret
  :fn   ::rand-fn)


;;; Anonymous functions

(defn opposite
  "Take a predicate and creates the opposite predicate"
  [pred]
  (comp not pred))

(s/def ::pred
  (s/fspec :args (s/cat :x any?)
           :ret boolean?))

(s/fdef opposite
  :args (s/cat :pred ::pred)
  :ret ::pred)


;;; instrumenting functions

(stest/instrument 'clojure.core/rand)
;; => [clojure.core/rand]

;; (rand :boom)
;; Problems: 
;;   val: :boom
;;   in: [0]
;;   failed: number?
;;   spec: :wonderland.spec/rand-args
;;   at: [:n]


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Generative function testing

;; specing symbol
;; (r/doc symbol)
;; clojure.core/symbol
;; ([name] [ns name])
;; Returns a Symbol with the given namespace and name. Arity-1 works
;; on strings, keywords, and vars.

(s/fdef clojure.core/symbol
  :args (s/cat :ns (s/? string?) :name string?)
  :ret symbol?
  :fn (fn [{:keys [args ret]}]
        (and (= (name ret) (:name args))
             (= (namespace ret) (:ns args)))))


(stest/check 'clojure.core/symbol)
;; ({:spec #object[clojure.spec.alpha$fspec_impl$reify__2524 0x66462fd5
;;                 "clojure.spec.alpha$fspec_impl$reify__2524@66462fd5"],
;;   :clojure.spec.test.check/ret {:result true,
;;                                 :num-tests 1000,
;;                                 :seed 1546702334559},
;;   :sym clojure.core/symbol})


;;; Generating examples

;; check for `symbol`
(s/exercise (s/cat :ns (s/? string?) :name string?))
;; ([("" "") {:ns "", :name ""}]
;;  [("") {:name ""}]
;;  [("Y") {:name "Y"}]
;;  [("FpE") {:name "FpE"}]
;;  [("i" "DOy") {:ns "i", :name "DOy"}]
;;  [("" "") {:ns "", :name ""}]
;;  [("6AWf5") {:name "6AWf5"}]
;;  [("z22" "33") {:ns "z22", :name "33"}]
;;  [("3" "Ds45b") {:ns "3", :name "Ds45b"}]
;;  [("j4") {:name "j4"}])


;; Combining generators with s/and

;; spec for odd numbers greater than 100

(defn big? [x] (> x 100))

(s/def ::big-odd (s/and odd? big?))

;; (s/exercise ::big-odd)
;; fails with exception

(s/def ::big-odd-int (s/and int? odd? big?))

(s/exercise ::big-odd-int)
;;([664403 664403]
;; [351 351]
;; [2619 2619]
;; [267 267]
;; [8545 8545]
;; [63895 63895]
;; [227 227]
;; [377 377]
;; [17807 17807]
;; [101445 101445])


;;; Creating custom generators
;;  marble

(s/def :marble/color-red
  (s/with-gen :marble/color #(s/gen #{:red})))

(s/exercise :marble/color-red)
;; => ([:red :red] [:red :red] [:red :red] ...)


;; a constrained string

(s/def ::sku
  (s/with-gen (s/and string? #(str/starts-with? % "SKU-"))
    (fn [] (gen/fmap #(str "SKU-" %) (s/gen string?)))))

(s/exercise ::sku)
;;(["SKU-" "SKU-"]
;; ["SKU-0" "SKU-0"]
;; ["SKU-" "SKU-"]
;; ["SKU-zmv" "SKU-zmv"]
;; ["SKU-" "SKU-"]
;; ["SKU-8c8mz" "SKU-8c8mz"]
;; ["SKU-" "SKU-"]
;; ["SKU-CdB" "SKU-CdB"]
;; ["SKU-Xm4hX4" "SKU-Xm4hX4"]
;; ["SKU-5510Ehryc" "SKU-5510Ehryc"])

