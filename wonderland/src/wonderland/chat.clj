(ns wonderland.chat
  (:require [clojure.repl :as r]))

;;; example of `alter`

(defrecord Message [sender text])

;; create a message
(->Message "Aaron" "Hello")
;; => #wonderland.chat.Message{:sender "Aaron", :text "Hello"}

;; create a message ref to store a list of messages.
(def messages (ref ()))

;; bad idea - can be simpler.
(defn naive-add-message [msg]
  (dosync (ref-set messages (cons msg @messages))))


;; (alter ref update-fn & args ...) ; read and update in a single transaction

(defn add-message [msg]
  (dosync (alter messages conj msg)))

;; (cons item seq)
;; (conj seq item) ; takes items in a proper order for `alter` 

;; add messages

(add-message (->Message "user 1" "hello"))
;; => (#wonderland.chat.Message{:sender "user 1", :text "hello"})

(add-message (->Message "user 2" "howdy"))
;; => (#wonderland.chat.Message{:sender "user 2", :text "howdy"}
;;     #wonderland.chat.Message{:sender "user 1", :text "hello"})


;;; (commute ref update-fn & args ...) - update-fn must be commutative.
;;   - updates can occur in any order.

(defn add-message-commute [msg]
  (dosync (commute messages conj msg)))


;;; Adding validation to refs
;;  You can create validation functions to validate transactions

;; (ref initial-state options*)
;; options include:
;;  :validator validate-fn
;;  :meta metadata-map

(defrecord Message [sender text])

(defn valid-message? [msg]
  (and (:sender msg) (:text msg)))

(def validate-message-list #(every? valid-message? %))

(def messages (ref () :validator validate-message-list))

(defn add-message [msg]
  (dosync (alter messages conj msg)))


;; (add-message "not a valid message")

;; 1. Unhandled java.lang.IllegalStateException
;;    Invalid reference state

@messages

(add-message (->Message "Aaron" "Real Message"))
;; => (#wonderland.chat.Message{:sender "Aaron", :text "Real Message"})
