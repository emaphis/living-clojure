(ns wonderland.chat)

(defrecord Message [sender text])

(Message. "Aaron" "Hello")

(def messages (ref ()))gx

;; bad idea
(defn naive-add-message [msg]
  (dosync (ref-set messages (cons msg @messages))))


;; (alter ref update-fn & args ...) ; read and update in a single transaction

(defn add-message [msg]
  (dosync (alter messages conj msg)))

;; (cons item seq)
;; (conj seq item)

;; add messages

(add-message (Message. "user 1" "hello"))
;; => (#wonderland.chat.Message{:sender "user 1", :text "hello"})

(add-message (Message. "user 2" "howdy"))
;; => (#wonderland.chat.Message{:sender "user 2", :text "howdy"}
;;     #wonderland.chat.Message{:sender "user 1", :text "hello"})


(def validate-message-list
  (partial every? #(and (:sender %) (:text %))))


(def messages (ref () :validator validate-message-list))

;; (add-message "not a valid message")
;; 1. Unhandled java.lang.IllegalStateException
;;    Invalid reference state

(add-message (Message. "Aaron" "Real Message"))
;; => (#wonderland.chat.Message{:sender "Aaron", :text "Real Message"})
