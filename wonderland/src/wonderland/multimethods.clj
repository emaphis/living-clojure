(ns wonderland.multimethods
  (:require [clojure.string :as str]
            [clojure.repl :as r]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Multimethods


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Living without multimethods

(defn my-print [ob]
  (.write *out* ob))

;; add a line feed
(defn my-println [ob]
  (my-print ob)
  (.write *out* "\n"))

(my-println "hello")
;; hello
;; => nil

;; (my-println nil)
;; 1. Unhandled java.lang.NullPointerException

;; so add a special case.

(defn my-print [ob]
  (cond
    (nil? ob) (.write *out* "nil")
    (string? ob) (.write *out* ob)))

(my-println nil)
;; nil

;; ... but
(my-println [1 2 3])
;; nil

;; .. so

(defn my-print-vector [ob]
  (.write *out* "[")
  (.write *out* (str/join " " ob))
  (.write *out* "]"))

(defn my-print [ob]
  (cond
    (vector? ob) (my-print-vector ob)
    (nil? ob) (.write *out* "nil")
    (string? ob) (.write *out* ob)))

(my-println [1 2 3])
;; [1 2 3]

;; works but conflates the decision process with specific implementation detials
;; this can be solved by protocols and multimethods


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; defining multimethods

;; (defmulti name dispatch-fn)

(defmulti my-print class)

;; (my-print "foo")
;; 1. Unhandled java.lang.IllegalArgumentException
;; No method in multimethod 'my-print' for dispatch value: class

;; (defmethod name dispatch-val & fn-tail)

(defmethod my-print String [s]
  (.write *out* s))

(my-println "Stu")
;; Stu

(defmethod my-print nil [s]
  (.write *out* "nil"))

(my-println nil)
;; nil


;;; dispatch is inheritance-aware
(defmethod my-print Number [n]
  (.write *out* (.toString n)))

(my-println 42)
;; 42

;; dispatch used `isa?` function
;; (isa? child parent)

(isa? Long Number)
;; => true


;;; multimethod defaults

(defmethod my-print :default [s]
  (.write *out* "#<")
  (.write *out* (.toString s))
  (.write *out* ">"))

(my-println (java.sql.Date. 0))
;; #<1969-12-31>

(my-println (java.util.Random.))
;; #<java.util.Random@29fbb00c>

;; create a multimethod using an alternate signature
;; (defmulti name dispatch-fn :default default-value)

(defmulti my-print class :default :everything-else)
(defmethod my-print String [s]
  (.write *out* s))
(defmethod my-print :everything-else [_]
  (.write *out* "Not implemented yet ..."))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; moving beyond simple dispatch

(defmethod my-print java.util.Collection [c]
  (.write *out* "(")
  (.write *out* (str/join " " c))
  (.write *out* ")"))

(my-println (take 6 (cycle [1 2 3])))
;; (1 2 3 1 2 3)

(my-println [1 2 3])
;; ( 1 2 3)

;; specialized method for vectors
(defmethod my-print clojure.lang.IPersistentVector [c]
  (.write *out* "[")
  (.write *out* (str/join " " c))
  (.write *out* "]"))

;; ... but
;; (my-println [1 2 3])

;; Unhandled java.lang.IllegalArgumentException
;; Multiple methods in multimethod 'my-print' match dispatch value:
;; class clojure.lang.PersistentVector -> interface
;; clojure.lang.IPersistentVector and interface java.util.Collection,
;; and neither is preferred

;; resolve conflicts
;; (prefer-method multi-name loved-dispatch dissed-dispatch)

(prefer-method
 my-print clojure.lang.IPersistentVector java.util.Collection)
;; => #multifn[my-print 0xdb395b0]

(my-println (take 6 (cycle [1 2 3])))
;; (1 2 3 1 2 3)
(my-println [1 2 3])
;; [1 2 3]


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Creating ad-hoc taxonomies

;; See:
;; account.clj
;; service_charge_1.clj
;; service_charge_2.clj
;; service_charge_3.clj

