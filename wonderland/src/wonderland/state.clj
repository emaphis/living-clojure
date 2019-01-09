(ns wonderland.state
  (:require [clojure.repl :as r]
            [clojure.string :as string]))

;;; Clojure's state model separated identity from values

;; identities are defined as four reference types

;; * Refs manage coordinated, synchronous changes to shared state.
;; * Atoms manage uncoordinated, synchronous changes to shared state.
;; * Agents manage asynchronous changes to shared state.
;; * Vars manage thread-local state.

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Refs and Software Transactional Memory

;; (ref initial-state) ;; reference to mutable state

(def current-track (ref "Mars, the Bringer of War"))
;; => #'wonderland.state/current-track

;; The ref wraps and protects access to its internal state.
;; To read the contents of the reference, you can call `deref`:

(deref current-track)
;; => "Mars, the Bringer of War"

;; reader macro
@current-track
;; => "Mars, the Bringer of War"

;; (ref-set reference new-value)  ; change a reference

;;(ref-set current-track "Venus, the Bringer of Peace")
;; Execution error (IllegalStateException) at wonderland.state/eval7288 (form-init2698847752877155351.clj:19).
;; No transaction running

;; You must rap access to state change updates in a transaction
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


;;;  prefer alter to commute to save order of operations

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

;; atoms don't participate in transactions so don't require `dosync`

;; (reset! an-atom newval)

(reset! current-track "Credo")
;; => "Credo"

;; like the `ref` example store title and composer in an atom
(def current-track (atom {:title "Credo" :composer "Byrd"}))

(reset! current-track {:title "Spem in Alium" :composer "Tallis"})
;; => {:title "Spem in Alium", :composer "Tallis"}

;; update a structure in an atom
;; (swap! an-atom fn & args)

(swap! current-track assoc :title "Sancte Deus")
;; => {:title "Sancte Deus", :composer "Tallis"}

;; calls to `swap!` may be retried if other threads are accessing the atom.


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Use Agents for Asynchronous Updates

;; (agent initial- state)

(def counter (agent 0))
;; => #'wonderland.state/counter

@counter ;; => 0

;; send the `agent` a function to update.
;; (send agent update-fn & args)

(send counter inc)
;; => #agent[{:status :ready, :val 1} 0x38bc87ba]


@counter ;; => 1

;; wait for agent to update - synchronous
;; (await & agents)

;; wait until timeout
;; (await-for timeout-millis & agents)

(await counter)


;;; Validating Agents and Handling Errors

;; (agent initial-state options*)
;;  options include:
;;    :validator validate-fn
;;    :meta metadata-map
;;    :error-handler handler-fn
;;    :error-mode mode-keyword (:continue or :fail)

(def counter (agent 0 :validator number?))
;; => #'wonderland.state/counter

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

(restart-agent counter 0)

@counter
;; => 0

;;; error handler

(defn handler [agent err]
  (println "ERR!" (.getMessage err)))

(def counter2 (agent 0 :validator number? :error-handler handler))

(send counter2 (fn [_] "boo"))
;; ERR! Invalid reference state
;; => #agent[{:status :ready, :val 0} 0x328ebe94]

(send counter2 inc)
;; => #agent[{:status :ready, :val 1} 0x328ebe94]

@counter2
;; => 1


;;; Including Agents in Transactions
;;   side effects in agents.

(def backup-agent (agent "resource/messages-backup.clj"))
;; => #'wonderland.state/backup-agent

;; create a version of add-message

;; Grab the return value of `commute`, which is the current database of messages,
;; in a let binding.

;; While still inside a transaction, `send` an action to the backup agent that
;; writes the message database to `filename`.

(defrecord Message [sender text])

(def messages (ref ()))

(defn add-message-with-backup [msg]
  (dosync
   (let [snapshot (commute messages conj msg)]
     (send-off backup-agent (fn [filename]
                              (spit filename snapshot)
                              filename))
     snapshot)))

(add-message-with-backup (->Message "John" "Message One"))
;; => (#wonderland.state.Message{:sender "John", :text "Message One"})

(add-message-with-backup (->Message "Jane" "Message Two"))
;; => (#wonderland.state.Message{:sender "Jane", :text "Message Two"}
;;     #wonderland.state.Message{:sender "John", :text "Message One"})


;;; Unified update model



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

;; create a thread-local binding with dynamic scope
;; (binding [bindings] & body)

(binding [foo 42] foo)
;; => 42

(defn print-foo [] (println foo))

(let [foo "let foo"] (print-foo))
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

;;; memoize
;; (memoize function)

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


;;; Different models

;; Refs and STM  Coordinated, synchronous updates     Pure Functions
;; Atoms         Uncoordinated, synchronous updates   Pure Functions
;; Agents        Uncoordinated, asynchronous updates  Any
;; Vars          Thread-local dynamic scopes          Any
;; Java locks    Coordinated, synchronous updates     Any



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; snake game
;;; snake.clj
