(ns myapp.ch07)

;;;; Concurency

;; Retriable, Coordinated, Asynchronous, Thread safe.

;;; `delay` - asynchronous

(def later (delay (prn "hello")))

@later
;; or
(deref later)
@later

;; actually calculate something
(def calcd (delay (do (prn "calc delay") (Math/sin 90))))

@calcd ;; => 0.8939966636005579
@calcd ;; => 0.8939966636005579

;; `force` instead of deref

(def d (delay (prn "hello")))
(force d)

;;; `promise` - asynchronous

(def foo (promise))

(future
  (prn "child thread doing suff...")
  (Thread/sleep 10000) ; wait 10 seconds
  (prn "... ok.")
  (deliver foo :bar))

@foo
;; => :bar

;;; `future` - asynchronous

(def fut (future
           (Thread/sleep 10000)
           (println "done...")
           100))

;; specify a time-out
(deref fut 500 "fail..")


;;; `atom` - thread safe and retriable

;; using an `atom`
(def counter (atom 0))
(swap! counter inc) ;; => 1

;; Anonymous functions
(deref counter) ;; => 1
(swap! counter #(+ 2 %)) ;; => 3
(swap! counter #(+ 2 %1 %2) 3) ;; => 8

;; `set-validator!` - validating an `atom`
(def ev-counter (atom 0))
(set-validator! ev-counter #(even? %))

;; (swap! ev-counter inc)
;; java.lang.IllegalStateException - Invalid reference state

(swap! ev-counter #(+ 2 %)) ;; => 2
(swap! ev-counter #(+ 2 %));; => 4

(set-validator! ev-counter nil)
(swap! ev-counter inc);; => 5

;; inline validator
(def ev-counter-2 (atom 0 :validator #(even? %)))

(reset! ev-counter-2 0)

;; validating specific conditions
(def counter (atom 0))
(swap! counter #(+ 4 %)) ;; => 4

@counter 
(compare-and-set! counter 4 0);; => true

@counter
(compare-and-set! counter 4 0) ;; => false

@counter ;; => 0

;; watching an `atom`
(def state (atom {}))

(defn state-change [key atom old new]
  (prn (format "Key: %s, atom: %s, old val: %s, new val %s"
               key atom old new)))

(add-watch state :foo state-change)
(swap! state assoc :bar "baz")
;; => {:bar "baz"}
;; "Key: :foo, atom: clojure.lang.Atom@22f61ae0, old val: {}, new val {:bar \"baz\"}"

;; (revove-watch atom key)


;;; `locking`  -  Thread safe

(def foo (atom []))

(future
  (locking foo
    (Thread/sleep 1000)
    (swap! foo #(conj % 1))))
;; => #future[{:status :pending, :val nil} 0x5e729568]

(locking foo
  (swap! foo #(conj % 2)))
;; => [1 2]

(deref foo);; => [1 2]


;;; `agent` - asynchronous - thread safe

;; exmple of sending a value to an agent
(def ag (agent 0))
(send ag inc)

(deref ag);; => 1

;; dereferencing an agent is non-blocking
(def ag2 (agent 0))

(future
  (prn "start ...")
  (Thread/sleep 8000)
  (prn "... increment the value")
  (send ag2 inc))

(deref ag2)
;; => 1

;;; `wait/wait-for`

;; standard non-blocking dereference

(def ag3 (agent 0))

(send ag3 #(do (Thread/sleep 10000) (prn "added 5" (+ % 5))))
(send ag3 #(do (Thread/sleep 10000) (prn "added 4" (+ % 4))))

(await ag3)
ag3
