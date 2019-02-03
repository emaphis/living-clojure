(ns programming-clojure.ch04-concurrency
  (:require [clojure.repl :refer :all]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Concurrency and Parallelism - pg. 159

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Shifting Computation through time and space.
;;   delays, futures, promises.

;;; Delays.
;;  `delay` construct that suspends some body of code evaluating only on demand.

(def d (delay (println "Running...")
              :done!))

(deref d)
;; Many Clojure entities are dereferenceable,
;; including `delay` `future` `promise` /  `atom` `ref` `agent` `var`

;; a function call
(def a-fn (fn []
            (println "Running...")
            :done!))

(a-fn)
;; Running...
;; => :done!

;; delays only evaluate their bodies once.

;; delays are useful to provide expensive to produce data

;; Offering opt-in computation with delay
(defn get-document
  [id]
  ;; ... do some work to retrieve the indentified document's metadata.
  {:url "http//www.mozilla.org/en-US/about/manifesto/"
   :title "The Mozilla Manifesto"
   :mime "text/html"
   :content (delay (slurp "http://www.mozilla.org/en-US/about/manifesto/"))})

(def d2 (get-document "some-id"))

d2
;; {:url "http://www.mozilla.org/about/manifesto.en.html",
;;  :title "The Mozilla Manifesto",
;;  :mime "text/html",
;;  :content #delay[{:status :pending, :val nil} 0x15a70f55]}

;; some parts of a program may only be interested in the cheaper data.
(realized? (:content d2))
;; => false

@(:content d2)
;; => "\n  \n\n\n\n  \n\n\n\n\n<!doctype html>\n\n<html class=\"windows x86
(realized? (:content d2))
;; => true


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Futures  pg 162.
;;  runs code on a Java thread.

(def long-calculation (future (apply + (range 1e8))))
;; => #'programming-clojure.ch04-concurrency/long-calculation

@long-calculation
;; => 4999999950000000

;; dereferencing will block until code completes.

;; expression will block the REPL for five seconds before returning:
@(future (Thread/sleep 5000) :done!) ;; => :done!

;; you can provide a "timeout value" when dereferencing a future
;; not available with `@` reader syntax

(deref (future (Thread/sleep 5000) :done!)
       1000
       :impatient!)
;; => :impatient!

(defn get-document
  [id]
  ;; ... do some work to retrieve the indentified document's metadata.
  {:url "http//www.mozilla.org/en-US/about/manifesto/"
   :title "The Mozilla Manifesto"
   :mime "text/html"
   :content (future (slurp "http://www.mozilla.org/en-US/about/manifesto/"))})

(def d3 (get-document "some-id"))

;; @(:content d3)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Promises  pg 163

;; `promise` may be dereferenced with an optional timeout, dereferencing a
;; `promise` will block until it has a value to provide, and a `promise` will
;; only ever have one value.
;;  But  promises are distinct from delays and futures insofar as they are not
;; created with any code or function that will eventually define its value:

(def p (promise))

;; `promise` is an empty container.
(realized? p)
;; => false
(deliver p 42)
;; => #promise[{:status :ready, :val 42} 0x1779a9a7]
(realized? p)
;; => true
@p ;; => 42

;; a `promise` is like a one time single-value pipe, "dataflow varibles" and
;; are the basic building blocks of "declarative concurrency"

(def a (promise))
(def b (promise))
(def c (promise))

;; `c` will not be delivered until both `a` and `b` are available
(future
  (deliver c (+ @a @b))
  (println "Delivery complete!"))
;; => #future[{:status :pending, :val nil} 0x65f5c5e2]

(deliver a 15)
;; => #promise[{:status :ready, :val 15} 0x7e467578]
(deliver b 16)
;; => #promise[{:status :ready, :val 16} 0x7dc75854]
;; Delivery complete!
@c
;; => 31


;; An application of promises is in easily making callback-based APIs
;; synchronous.
;; Say you have a function that takes another function as a callback
(defn call-service
  [arg1 arg2 callback-fn]
  ;; ...perform service call, eventually invoking `callback-fn` with results.
  (future (callback-fn (+ arg1 arg2) (- arg1 arg2))))

(defn sync-fn
  [async-fn]
  (fn [& args]
    (let [result (promise)]
      (apply async-fn (conj (vec args) #(deliver result %&)))
      @result)))

((sync-fn call-service) 8 7)
;; => (15 1)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Parallelism on the cheap  pg 166


(defn phone-numbers
  [string]
  (re-seq #"(\d{3})[\.-]?(\d{3})[\.-]?(\d{4})" string))
(phone-numbers "Sunil: 617.555.2937, Betty: 508.555.2218")
;; => (["617.555.2937" "617" "555" "2937"]
;;     ["508.555.2218" "508" "555" "2218"])

(def files (repeat 100
                   (apply str        ;;   1000000
                          (concat (repeat 1000000 \space)
                                  "Sunil: 617.555.2937, Betty: 508.555.2218"))))

(time (dorun (map phone-numbers files)))
;; "Elapsed time: 2012.8953 msecs"

;; `pmap`
(time (dorun (pmap phone-numbers files)))
;; "Elapsed time: 891.6836 msecs"

;; but we must be careful in using `pmap`, the reuslting code may be slower.
(def files2 (repeat 100000
                    (apply str
                           (concat (repeat 1000 \space)
                                   "Sunil: 617.555.2937, Betty: 508.555.2218"))))

(time (dorun (map phone-numbers files2)))
;; "Elapsed time: 2185.5408 msecs"
(time (dorun (pmap phone-numbers files2)))
;; "Elapsed time: 1219.4913 msecs"

;; more work for each process through chunking
(time (->> files2
           (partition-all 250)
           (pmap (fn [chunk] (doall (map phone-numbers chunk))))
           (apply concat)
           dorun))
;; "Elapsed time: 940.3752 msecs"


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; State and Identity pg 168.
