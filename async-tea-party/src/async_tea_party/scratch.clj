(ns async-tea-party.scratch
  (:require [clojure.core.async :as async]))

;;; practice code for core.async

(def tea-channel (async/chan))
;; => #'async-tea-party.scratch/tea-channel

;; buffered channel
(def tea-channel (async/chan 10))
;; => #'async-tea-party.scratch/tea-channel

;; synchronous put -- !! blocking call
(async/>!! tea-channel :cup-of-tea)
;; => true

;; blocking take
(async/<!! tea-channel)
;; => :cup-of-tea

(async/>!! tea-channel :cup-of-tea-2)
;; => true
(async/>!! tea-channel :cup-of-tea-3)
;; => true
(async/>!! tea-channel :cup-of-tea-4)
;; => true

(async/close! tea-channel)
;; => nil

(async/>!! tea-channel :cup-of-tea-5)
;; => false

(async/<!! tea-channel)
;; => :cup-of-tea-2

(async/<!! tea-channel)
;; => :cup-of-tea-3

(async/<!! tea-channel)
;; => :cup-of-tea-4


;;; asynchronously

(let [tea-channel (async/chan)]
  (async/go (async/>! tea-channel :cup-of-tea1))
  (async/go (println "Thanks for the " (async/<! tea-channel))))
;; => #object[clojure.core.async.impl.channels.ManyToManyChannel 0x2cf9a699 "clojure.core.async.impl.channels.ManyToManyChannel@2cf9a699"]
;; => Thanks for the  :cup-of-tea1

;;; continuously execute

(def tea-channel (async/chan 10))

(async/go-loop []
  (println "Thanks for the " (async/<! tea-channel))
  (recur))

(async/>!! tea-channel :hot-cup-of-tea)
;; => true
;; Thanks for the  :hot-cup-of-tea

(async/>!! tea-channel :tea-with-sugar)
;; => true

(async/>!! tea-channel :tea-with-milk)
;; => true


;;; multiple channels - alt! - pg 143

(def tea-channel (async/chan 10))
(def milk-channel (async/chan 10))
(def sugar-channel (async/chan 10))

(async/go-loop []
  (let [[v ch] (async/alts![tea-channel
                            milk-channel
                            sugar-channel])]
    (println "Got " v " from " ch)
    (recur)))
;; => #object[clojure.core.async.impl.channels.ManyToManyChannel 0x3f8ffe85 "clojure.core.async.impl.channels.ManyToManyChannel@3f8ffe85"]

(async/>!! sugar-channel :sugar)
;; Got  :sugar  from  #object[clojure.core.async.impl.channels.ManyToManyChannel 0x4ab63024 clojure.core.async.impl.channels.ManyToManyChannel@4ab63024]

(async/>!! milk-channel :milk)
;; => true

(async/>!! tea-channel :tea)
;; => true
