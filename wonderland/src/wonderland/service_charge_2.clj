(ns wonderland.service-charge-2
  (:require [wonderland.account :refer :all]))

(in-ns 'wonderland.account)
(clojure.core/use 'clojure.core)
;; implementing `service-charge` using `account-level` and `:tag`
;; as a dispatch function

(defmulti service-charge (fn [acct] [(account-level acct) (:tag acct)]))
(defmethod service-charge [::acc/basic ::acc/checking]   [_] 25)
(defmethod service-charge [::acc/basic ::acc/savings]    [_] 10)
(defmethod service-charge [::acc/premium ::acc/checking] [_] 0)
(defmethod service-charge [::acc/premium ::acc/savings]  [_] 0)

;; testing
(service-charge {:tag ::acc/checking :balance 1000})
;; => 25

(service-charge {:tag ::acc/savings :balance 1000})
;; => 0
