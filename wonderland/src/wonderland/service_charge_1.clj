(ns wonderland.service-charge-1
  (:require [wonderland.account :refer :all]))

;; implementing `service-charge` using `account-level` as a dispatch function

;; bad approach

(defmulti service-charge account-level)
(defmethod service-charge ::basic [acct]
  (if (- (:tag acct) ::checking) 25 10))
(defmethod service-charge ::premium [_] 0)
