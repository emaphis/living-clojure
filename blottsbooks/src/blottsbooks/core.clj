(ns blottsbooks.core
  (:require [blottsbooks.pricing :as pricing])
  (:gen-class))


(defn -main
  "Demonstrate pricing function"
  []
  (println
   (pricing/discount-price
    {:title "Emma" :price 9.99})))
