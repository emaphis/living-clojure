(ns wonderland.account
  (:require [clojure.string :as str]
            [clojure.repl :as r]))

;;; Multimethods - Ad-hoc taxonomies

;; keyword resolution
:checking
;; => :checking
::checking
;; => :wonderland.account/checking

;; alias for tedious keyword resolution
;; (alias short-name namespace-symbol)

(alias 'acc 'wonderland.account)
;; => nil

(def test-savings {:id 1, :tag ::acc/savings, ::balance 100M})

(def test-checking {:id 2, :tag ::acc/checking, ::balance 250M})

;; methods

(defmulti interest-rate :tag)
(defmethod interest-rate ::acc/checking [_] 0M)
(defmethod interest-rate ::acc/savings [_] 0.05M)

(comment ;; tests
  (interest-rate test-savings)
  ;; => 0.05M
  (interest-rate test-checking)
  ;; => 0M
  )

;; Accounts have an annual service charge, with rules as follows:
;; *Normal checking accounts incur a $25 fee.
;; *Normal savings accounts incur a $10 fee.Premium accounts have no fee.
;; *Checking accounts with a balance of $5,000 or more are premium.
;; *Savings accounts with a balance of $1,000 or more are premium

(defmulti account-level :tag)
(defmethod account-level ::acc/checking [acct]
  (if (>= (:balance acct) 5000) ::acc/premium ::acc/basic))
(defmethod account-level ::acc/savings [acct]
  (if (>= (:balance acct) 1000) ::acc/premium ::acc/basic))

(account-level {:id 1, :tag ::acc/savings, :balance 2000M})
;; => :wonderland.account/premium

(account-level {:id 1, :tag ::acc/checking :balance 2000M})
;; => :wonderland.account/basic
