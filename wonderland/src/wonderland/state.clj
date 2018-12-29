(ns wonderland.state)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Refs and Software Transactional Memory

;; (ref initial-state) ;; reference to mutable state

(def current-track (ref "Mars, the Bringer of War"))
;; => #'wonderland.state/current-track

(deref current-track)
;; => "Mars, the Bringer of War"

@current-track
;; => "Mars, the Bringer of War"

;; (ref-set reference new-value)  ; change a reference

;; (ref-set current-track "Venus, the Bringer of Peace")
;; Execution error (IllegalStateException) at wonderland.state/eval7288 (form-init2698847752877155351.clj:19).
;; No transaction running

;; (dosync & exprs)  ;; wrap a transaction

(dosync (ref-set current-track "Venus, the Bringer of Peace"))
;; => "Venus, the Bringer of Peace"

;; Multiple actions in a transaction are coordinated

(def current-track (ref "Venus, the Bringer of Peace"))
(def current-composer (ref "Holst"))

(dosync
 (ref-set current-track "Credo")
 (ref-set current-composer "Byrd"))
;; => "Byrd"

@current-track ;; => "Credo"
@current-composer ;; => "Byrd"


;;; alter
;; chat example - chat.clj

;; prefer alter to commute to save order of operations

(def counter (ref 0))

(defn next-counter [] (dosync (alter counter inc)))

(next-counter)
;; => 1

(next-counter)
;; => 2


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Use Atoms for Uncoordinated, Synchronous updates

;; (atom initial-state options?)
;; options include
;;  :validator validate-fn
;;  :meta metadata-map

(def current-track (atom "Venus, the Bringer of Peace"))

(deref current-track)
;; => "Venus, the Bringer of Peace"

@current-track ;; => "Venus, the Bringer of Peace"

(reset! current-track "Credo")
;; => "Credo"

(def current-track (atom {:title "Credo" :composer "Byrd"}))

(reset! current-track {:title "Spem in Alium" :composer "Tallis"})
;; => {:title "Spem in Alium", :composer "Tallis"}

;; update a structure

;; (swap! an-atom fn & args)

(swap! current-track assoc :title "Sancte Deus")
;; => {:title "Sancte Deus", :composer "Tallis"}



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Use Agents for Asynchronous Updates

;; (agent initial- state)

(def counter (agent 0))

;; (send agent update-fn & args)

(send counter inc)
;; => #agent[{:status :ready, :val 1} 0x38bc87ba]

@counter ;; => 1

;; (await & agents)
;; (await-for timeout-millis & agents)


;;; Validating Agents and Handling Errors

(def counter (agent 0 :validator number?))

(send counter (fn [_] "boo"))
;; => #agent[{:status :failed, :val 0} 0x786e8cb2]

;; but
@counter
;; => 0

(agent-errors counter)
;; => (#error {
;; :cause "Invalid reference state"
;; :via
;; [{:type java.lang.IllegalStateException
;;   :message "Invalid reference state"
;;   :at [clojure.lang.ARef validate "ARef.java" 33]}]
;; :trace
;; [[clojure.lang.ARef validate "ARef.java" 33]
;;  [clojure.lang.ARef validate "ARef.java" 46]
;;  [clojure.lang.Agent setState "Agent.java" 177]
;;  [clojure.lang.Agent$Action doRun "Agent.java" 115]
;;  [clojure.lang.Agent$Action run "Agent.java" 163]
;;  [java.util.concurrent.ThreadPoolExecutor runWorker "ThreadPoolExecutor.java" 1128]
;;  [java.util.concurrent.ThreadPoolExecutor$Worker run "ThreadPoolExecutor.java" 628]
;;  [java.lang.Thread run "Thread.java" 834]]})

(clear-agent-errors counter)

@counter
;; => 0


;;; Including Agents in Transactions
;; side effects in agents.

(def backup-agent (agent "output/messages-backup.clj"))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Managing per-thread state with vars

(def ^:dynamic foo 10)
;; => #'wonderland.state/foo

foo
;; => 10


;; reading foo from another thread

(.start (Thread. (fn [] (println foo))))
;; => nil
;; 10

;; create a thread-local binding
;; (binding [bindings] & body)

(binding [foo 42] foo)
;; => 42

(defn print-foo [] (println foo))

(let [foo "let foo"] (print-foo))d
;; => nil
;; 10

(binding [foo "bound foo"] (print-foo))
;; => nil
;; bound foo


;;; Dynamic variables - action at a distance

(defn ^:dynamic slow-double [n]
  (Thread/sleep 100)
  (* n 2))


(time 
 (loop [n 1]
   (if (>= n 2048)
     n
     (recur (slow-double n)))))
;; => 2048
;; "Elapsed time: 1143.2079 msecs"

(defn calls-slow-double []
  (map slow-double [1 2 1 2 1 2]))

(time (dorun (calls-slow-double)))
;; => nil
;; "Elapsed time: 692.7548 msecs"

(defn demo-memoize []
  (time
   (dorun
    (binding [slow-double (memoize slow-double)]
      (calls-slow-double)))))

(demo-memoize)
;; => nil
;; "Elapsed time: 202.4113 msecs"


;;; Working with Java Callback API's

;; set a thread-local dynamic binding
;; (set! var-symbol new-value)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; snake game
;;; snake.clj
