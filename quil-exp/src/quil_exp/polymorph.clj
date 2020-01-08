(ns quil-exp.polymorph)

;; Polymorphism in Clojure
;; https://clojure.org/reference/protocols

;;; Parametric polymorphism


;;; Ad hoc polymorphism
;; Polymorphism of functions

;; Closed dispatch.
(defn ad-hoc-type-namer [thing]
  (condp = (type thing)
    java.lang.String              "string"
    clojure.lang.PersistentVector "vector"))

(ad-hoc-type-namer "I'm a string") ;; => "string"

(ad-hoc-type-namer []) ;; => "vector"

;;(ad-hoc-type-namer {})


;; open dispatch
(def type-namer-implementations
  {java.lang.String               (fn [thing] "string")
   clojure.lang.PersistentVector  (fn [thing] "vector")})

(defn open-ad-hoc-type-namer [thing]
  (let [dispatch-value (type thing)]
    (if-let [implemetation
             (get type-namer-implementations dispatch-value)]
      (implemetation thing)
      (throw (IllegalArgumentException.
              (str "No implementation found for " dispatch-value))))))


(open-ad-hoc-type-namer "Im a string") ;; => "string"

(open-ad-hoc-type-namer []) ;; => "vector"

;;(open-ad-hoc-type-namer {})

;; extend implementations
(def type-namer-implementations
  (assoc type-namer-implementations
         clojure.lang.PersistentArrayMap (fn [thing] "map")))

(open-ad-hoc-type-namer {}) ;; => "map"


;;; Subtype polymorphism

(defn map-type-namer [thing]
  (condp = (type thing)
    clojure.lang.PersistentArrayMap "map"
    clojure.lang.PersistentHashMap  "map"))

(map-type-namer (hash-map)) ;; => "map"
(map-type-namer (array-map)) ;; => "map"
;;(map-type-namer (sorted-map))

(defn subtyping-map-type-namer [thing]
  (cond
    (instance? clojure.lang.APersistentMap thing)  "map"
    :else (throw (IllegalArgumentException.
                  (str "No implementation found fo " (type thing))))))

(subtyping-map-type-namer (hash-map)) ;; => "map"
(subtyping-map-type-namer (array-map)) ;; => "map"
(subtyping-map-type-namer (sorted-map)) ;; => "map"



;;; Multimethods.

(def example-user {:login "rob" :referer "mint.com" :salary 100000})

;;; dispatch without multimethods

(defn fee-amount [percentage user]
  (with-precision 16 :rounding HALF_EVEN
    (* 0.01M percentage (:salary user))))

;; closed dispatch, ad hoc polymorphism
(defn affiliate-fee [user]
  (case (:referer user)
    "google.com" (fee-amount 0.01M user)
    "mint.com"   (fee-amount 0.03M user)
    :else (fee-amount 0.02M user)))

(affiliate-fee example-user) ;; => 30.0000M

;;; Ad hoc polymorphism using multimethods

(defmulti affiliate-fee (fn [user] (:referer user)))

(defmethod affiliate-fee "mint.com" [user]
  (fee-amount 0.03M user))

(defmethod affiliate-fee "google.com" [user]
  (fee-amount 0.01M user))

(defmethod affiliate-fee :default [user]
  (fee-amount 0.02M user))

(affiliate-fee example-user) ;; => 30.0000M

