(ns myapp.my-space
  (:require [clojure.spec.alpha :as s]
            [clojure.repl :as r]))

;; Predicates

(s/conform even? 1000)
;; => 1000
(s/conform even? 1001)
;; => :clojure.spec.alpha/invalid

(s/valid? even? 10)
;; => true
(s/valid? even? 11)
;; => false

(s/valid? nil? nil)
;; => true
(s/valid? string? "abc")
;; => true

;;; user defined spec

(s/valid? #(> % 5) 10)
;; => true
(s/valid? #(> % 5) 3)
;; => false

(import java.util.Date)
(s/valid? inst? (Date.))
;; => true

;; using sets as predicates
(s/valid? #{:club :diamond :heart :spade} :diamond)
;; => true
(s/valid? #{:club :diamond :heart :spade} 42)
;; => false
(s/valid? #{42} 42)
;; => true

;;; Registry
(s/def ::date inst?)
(s/def ::suit  #{:club :diamond :heart :spade})

(s/valid? ::date (Date.))
;; => true
(s/valid? ::suit :club)
;; => true

;;; Composing predicates
;; `s/and` `s/or/`

(s/def ::big-even (s/and int? even? #(> % 1000)))

(s/valid? ::big-even :foo)
;; => false
(s/valid? ::big-even 10)
;; => false
(s/valid? ::big-even 10000)
;; => true

(s/def ::name-or-id (s/or :name string? 
                          :id int?))

(s/valid? ::name-or-id "abc")
;; => true
(s/valid? ::name-or-id 10)
;; => truex`
(s/valid? ::name-or-id :foo)
;; => false

;;; Conform

(s/conform ::name-or-id "abc")
;; => [:name "abc"]
(s/conform ::name-or-id 10)
;; => [:id 10]
(s/conform ::name-or-id :foo)
;; => :clojure.spec.alpha/invalid


;;; nilable for data that can be nil

(s/valid? string? nil)
;; => false
(s/valid? (s/nilable string?) nil)
;; => true


;;; Explain

(s/explain ::suit 42)
;;=> 42 - failed: #{:spade :heart :diamond :club} spec: :myapp.my-space/suit
(s/explain ::big-even 5)
;;=> 5 - failed: even? spec: :myapp.my-space/big-even
(s/explain ::name-or-id :foo)
;; => nil
;;=> :foo - failed: string? at: [:name] spec: :myapp.my-space/name-or-id
;;=> :foo - failed: int? at: [:id] spec: :myapp.my-space/name-or-id

(s/explain-data ::name-or-id :foo)
;; => {:clojure.spec.alpha/problems ({:in [],
;;                                    :path [:name],
;;                                    :pred clojure.core/string?,
;;                                    :val :foo,
;;                                    :via [:myapp.my-space/name-or-id]}
;;                                   {:in [],
;;                                    :path [:id],
;;                                    :pred clojure.core/int?,
;;                                    :val :foo,
;;                                    :via [:myapp.my-space/name-or-id]}),
;;     :clojure.spec.alpha/spec :myapp.my-space/name-or-id,
;;     :clojure.spec.alpha/value :foo}


;;; Entity maps

(def email-regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")

(s/def ::email-type (s/and string? #(re-matches email-regex %)))

(s/def ::acctid int?)
(s/def ::first-name string?)
(s/def ::last-name string?)

(s/def ::person (s/keys :req [::first-name ::last-name ::email]
                        :opt [::phone]))


(s/valid? ::person
          {::first-name "Bugs"
           ::last-name "Bugs"
           ::email "bugs@example.com"})
;; => true

;; fails
(s/explain ::person
           {::first-name "Bugs"})
;; => nil
;;=> #:myapp.my-space{:first-name "Bugs"} - failed: (contains? % :myapp.my-space/last-name) spec: :myapp.my-space/person
;;=> #:myapp.my-space{:first-name "Bugs"} - failed: (contains? % :myapp.my-space/email) spec: :myapp.my-space/person

(s/explain ::person
           {::first-name "Bugs"
            ::last-name "Bunny"
            ::email "n/s"})
;;=> "n/s" - failed: (re-matches email-regex %) in: [:myapp.my-space/email] at: [:myapp.my-space/email] spec: :myapp.my-space/email-type

;; Unqualified keys
(s/def :myapp.my-space/person
  (s/keys :req-un [::first-name ::last-name ::email]
          :opt-un [::phone]))

(s/conform :myapp.my-space/person
           {:first-name "Bugs"
            :last-name "Bunny"
            :email "bugs@example.com"})
;; => {:email "bugs@example.com", :first-name "Bugs", :last-name "Bunny"}

(s/explain :myapp.my-space/person
           {:first-name "Bugs"
            :last-name "Bunny"
            :email "n/a"})
;;=> "n/a" - failed: (re-matches email-regex %) in: [:email] at: [:email] spec: :myapp.my-space/email-type

(s/explain :myapp.my-space/person
           {:first-name "Bugs"})
;;=> {:first-name "Bugs"} - failed: (contains? % :last-name) spec: :myapp.my-space/person
;;=> {:first-name "Bugs"} - failed: (contains? % :email) spec: :myapp.my-space/person

;;; using unqualified keys can validate record attributes

(defrecord Person [first-name last-name email phone])

(s/explain :myapp.my-space/person
           (->Person "Bugs" nil nil nil))
;;=> nil - failed: string? in: [:last-name] 
;;=>                       at: [:last-name] spec: :myapp.my-space/last-name

(s/conform :myapp.my-space/person
           (->Person "Bugs" "Bunny" "bugs@example.com" nil))
;;=>  {:first-name "Bugs", :last-name "Bunny", :email "bugs@example.com", :phone nil}


(s/def ::port number?)
(s/def ::host string?)
(s/def ::id keyword?)
(s/def ::server (s/keys* :req [::id ::host] :opt [::port]))

(s/conform ::server [::id :s1 ::host "example.com" ::port 5555])
;; => #:myapp.my-space{:id :s1, :host "example.com", :port 5555}

(s/def :animal/kind string?)
(s/def :animal/says string?)
(s/def :animal/common (s/keys :req [:animal/kind :animal/says]))

(s/def :dog/tail? boolean)
(s/def :dog/breed string?)
(s/def :animal/dog (s/merge :animal/common
                            (s/keys :req [:dog/tail? :dog/breed])))

(s/valid? :animal/dog
          {:animal/kind "dog"
           :animal/says "woof"
           :dog/tail? true
           :dog/breed "retriver"})
;; => true


;;; multi-spec

;; Event attibutes
(s/def :event/type keyword?)
(s/def :event/timestamp int?)
(s/def :searc/url string?)
(s/def :error/messsage string?)
(s/def :error/code int?)

;; multimethod that ddfine a dispatch function of choosing a selector
(defmulti event-type :event/type)
(defmethod event-type :even/search [_]
  (s/keys :req [:event/type :event/timestamp :searc/url]))
(defmethod event-type :eval-error [_]
  (s/keys :req [:event/type :event/timestamp :error/messsage :error/code]))

(s/def :event/event (s/multi-spec event-type :event/timestamp))

(s/valid? :event/event
          {:event/type :even/search
           :event/timestamp 146397012300
           :searc/url "https://coljure.org"})
;; => true

(s/valid? :event/event
          {:event/type :event/error
           :event/timestamp 146397012300
           :error/messsage "Invalid host"
           :error/code 500})

(s/explain :event/event
           {:event/type :event/error
            :event/timestamp 146397012300
            :error/messsage "Invalid host"
            :error/code 500})
;; => nil


;;; Collections - coll-of, tuple, map-of

(s/conform (s/coll-of keyword?) [:a :b :c])
;; => [:a :b :c]
(s/conform (s/coll-of number?) #{5 10 2})
;; => #{2 5 10}

;; coll-of  :kind :count :min-count :max-count :distinct :into
(s/def ::vnum3 (s/coll-of number? :kind vector? :count 3 :distinct true :into #{}))

(s/conform ::vnum3 [1 2 3])
;; => #{1 3 2}
(s/explain ::vnum3 #{1 2 3})
;; => #{1 3 2} - failed: vector? spec: :myapp.my-space/vnum3
(s/explain ::vnum3 [ 1 1 1])
;; => [1 1 1] - failed: distinct? spec: :myapp.my-space/vnum3
(s/explain ::vnum3 [1 2 :a])
;; => :a - failed: number? in: [2] spec: :myapp.my-space/vnum3

;; Tuples
(s/def ::point (s/tuple double? double? double?))
(s/conform ::point [1.5 2.5 -0.5])
;; => [1.5 2.5 -0.5]

;; regulat expression
(s/cat :x double? :y double? :z double?)
;; collection
(s/coll-of double?)
;; tuple
(s/tuple double? double? double?)

;; maps
(s/def ::scores (s/map-of string? int?))
(s/conform ::scores {"Sally" 1000, "Joe" 500})
;; => {"Sally" 1000, "Joe" 500}


;;; Sequences - cat alt * + ?













