(ns wonderland.chapter03)
;;; State and Concurrency


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Using atoms for independent items.

(def who-atom (atom :caterpillar))
;; => #'wonderland.chapter03/who-atom

who-atom
;; => #atom[:caterpillar 0x791772ef]

;; dereferencing
@who-atom
;; => :caterpillar

;; synchronous update - reset!
(reset! who-atom :chrysalis)
;; => :chrysalis

@who-atom
;; => :chrysalis

;; synchronous update with passed function - swap!

(def who-atom (atom :caterpillar))
@who-atom
;; => :caterpillar

;; change function
(defn change [state]
  (case state
    :caterpillar :chrysalis
    :chrysalis :butterfly
    :butterfly))

(swap! who-atom change)
;; => :chrysalis

(swap! who-atom change)
;; => :butterfly

(swap! who-atom change)
;; => :butterfly

@who-atom
;; => :butterfly


;; no side effects in a change function

(def counter (atom 0))

@counter
;; => 0

(dotimes [_ 5] (swap! counter inc))

@counter
;; => 5

;; multi-threads

(def counter (atom 0))

@counter ;; => 0

(let [n 5]
  (future (dotimes [_ n] (swap! counter inc)))
  (future (dotimes [_ n] (swap! counter inc)))
  (future (dotimes [_ n] (swap! counter inc))))

@counter
;; => 15


;; now with a side effect:

(def counter (atom 0))

(defn inc-print [val]
  (println val)
  (inc val))

(swap! counter inc-print)
;; => 1


(def counter (atom 0))

(let [n 2]
  (future (dotimes [_ n] (swap! counter inc-print)))
  (future (dotimes [_ n] (swap! counter inc-print)))
  (future (dotimes [_ n] (swap! counter inc-print))))

@counter
;; => 6


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Using refs for coordinated changes

(def alice-height (ref 3))
(def right-hand-bites (ref 10))

@alice-height ;; => 3
@right-hand-bites ;; => 10

;; Alice height grows by 24 inches for every rh bite she consumes
(defn eat-from-right-hand []
  (when (pos? @righ-hand-bites)
    (alter right-hand-bites dec)
    (alter alice-height #(+ % 24))))

;; error - no transaction running
;;(eat-from-right-hand)

;; run transaction
(dosync (eat-from-right-hand))
;; => 27

@alice-height ;; => 27
@right-hand-bites ;; => 9


;; move dosync into function

(def alice-height (ref 3))
(def right-hand-bites (ref 10))

(defn eat-from-right-hand []
  (dosync (when (pos? @righ-hand-bites)
            (alter right-hand-bites dec)
            (alter alice-height #(+ % 24)))))

(let [n 2]
  (future (dotimes [_ n] (eat-from-right-hand)))
  (future (dotimes [_ n] (eat-from-right-hand)))
  (future (dotimes [_ n] (eat-from-right-hand))))

@alice-height ;; => 147
@right-hand-bites ;; => 4


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Using commute for commutative functions + *

(def alice-height (ref 3))
(def right-hand-bites (ref 10))


(defn eat-from-right-hand []
  (dosync (when (pos? @righ-hand-bites)
            (commute right-hand-bites dec)
            (commute alice-height #(+ % 24)))))

(let [n 2]
  (future (dotimes [_ n] (eat-from-right-hand)))
  (future (dotimes [_ n] (eat-from-right-hand)))
  (future (dotimes [_ n] (eat-from-right-hand))))

@alice-height ;; => 147
@right-hand-bites ;; => 4


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Use ref-set when one value is related to another value

(def x (ref 1))
(def y (ref 1))

(defn new-values []
  (dosync
   (alter x inc)
   (ref-set y (+ 2 @x))))

(let [n 2]
  (future (dotimes [_ n] (new-values)))
  (future (dotimes [_ n] (new-values))))

@x ;; => 5
@y ;; => 7



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Using agents to manage changes on their own.

(def who-agent (agent :caterpillar))

@who-agent;; => :caterpillar

(defn change [state]
  (case state
    :caterpillar :chrysalis
    :chrysalis :butterfly
    :butterfly))

(send who-agent change)  ; send return immediatly
;; => #agent[{:status :ready, :val :chrysalis} 0x555ca5f6]

@who-agent
;; => :chrysalis


;;; send-off for potentially blocking operations
(send-off who-agent change)
;; => #agent[{:status :ready, :val :butterfly} 0x555ca5f6]

@who-agent ;; => :butterfly


;;; agent with an error.

(def who-agent (agent :caterpillar))

(defn change [state]
  (case state
    :caterpillar :chrysalis
    :chrysalis :butterfly
    :butterfly))

(defn change-error [state]
  (throw (Exception. "Boom!")))

(send who-agent change-error)
;; => #agent[{:status :failed, :val :caterpillar} 0x74449e48]

@who-agent
;; => :caterpillar

;; now error will be reported
(send-off who-agent change)
;; java.lang.Exception Boom!

;;; restart agent in error state
(restart-agent who-agent :caterpillar)

(send who-agent change)
;; => #agent[{:status :ready, :val :chrysalis} 0x74449e48]

@who-agent
;; => :chrysalis


;;; progamatic error handlers

(def who-agent (agent :caterpillar))

(set-error-mode! who-agent :continue)

(defn err-handler-fn [a ex]
  (println "error " ex " value is " @a))

(set-error-handler! who-agent err-handler-fn)

(send who-agent change-error)
;; => #agent[{:status :ready, :val :caterpillar} 0x6007c0f7]
;;  Execution error at wonderland.chapter03/change-error (form-init2799456892983138243.clj:224).
;;  Boom!

@who-agent
;; => :caterpillar
