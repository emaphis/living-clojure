(ns blottsbooks.pricing)

(def literature ["Emma" "Oliver Twist" "Possession"])

(def horror ["It" "Carry" "Possession"])

(def discount-rate 0.15)

(defn discount-price [book]
  (- (:price book)
     (* discount-rate (:price book))))

(discount-price {:title "Emma" :price 9.99})

