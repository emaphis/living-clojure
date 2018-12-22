(ns async-tea-party.core
  (:gen-class)
  (:require [clojure.core.async :as async]))


;;; tea-service pg 143.

(def google-tea-service-chan (async/chan 10))
(def yahoo-tea-service-chan (async/chan 10))
(def result-chan (async/chan 10))

(defn random-add []
  (reduce + (conj [] (repeat 1 (rand-int 100000)))))

(defn request-google-tea-service []
  (async/go
    (random-add)
    (async/>! google-tea-service-chan
              "tea compliments of google")))

(defn request-yahoo-tea-service []
  (async/go
    (random-add)
    (async/>! yahoo-tea-service-chan
              "tea compliments of yahoo")))


;; request the defined tea services

(defn request-tea []
  (request-google-tea-service)
  (request-yahoo-tea-service)
  (async/go (let [[v] (async/alts!
                       [google-tea-service-chan
                        yahoo-tea-service-chan])]
              (async/>! result-chan v))))


;; pg 147
(defn -main [& args]
  (println "Requesting tea!")
  (request-tea)
  (println (async/<!! result-chan)))
