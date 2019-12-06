(ns myapp.pricing)

;;;; Chapter 9
;;;; Namespaces

;;; A place for your vars

(def discount-rate 0.15)

(defn discount-price [book]
  (* (- 1.0 discount-rate) (:price book)))

(println (discount-price {:title "Emma" :price 9.99}))

(ns user)

(myapp.pricing/discount-price {:title "Emma" :price 9.99}) ;; => 8.4915

*ns*  ;; => #namespace[user]

(def literature ["Emma" "Oliver Twist" "Possession"])

(def horror ["It" "Carry" "Possession"])

;; require and external namespace

(require 'clojure.data)

(clojure.data/diff literature horror)
;; => [["Emma" "Oliver Twist"] ["It" "Carry"] [nil nil "Possession"]]


