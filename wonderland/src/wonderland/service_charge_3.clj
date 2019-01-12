(ns wonderland.service-charge-2
  (:require [wonderland.account :refer :all]))

(in-ns 'wonderland.account)
(clojure.core/use 'clojure.core)

(derive ::acc/savings ::acc/account)
(derive ::acc/checking ::acc/account)

(isa? ::acc/savings ::acc/account)
;; => true

(defmulti service-charge (fn [acct] [(account-level acct) (:tag acct)]))
(defmethod service-charge [::acc/basic ::acc/checking]   [_] 25)
(defmethod service-charge [::acc/basic ::acc/savings]    [_] 10)
(defmethod service-charge [::acc/premium ::acc/account]  [_] 0)
