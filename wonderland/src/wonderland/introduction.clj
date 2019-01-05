(ns wonderland.introduction
  (:require [clojure.string :as str]
            [clojure.repl :as r]))


;;; stuff

(println "hello world")

(defn hello
  "Writes hello message to *out*. Calls you by username"
  [name]
  (str "Hello, " name))

(hello "Edward")
;; => "Hello, Edward"


;;; Shared state.

(def visitors (atom #{}))


(defn hello
  "Writes hello message to *out*. Calls you by username.
  Knows if you have been here before."
  [username]
  (swap! visitors conj username)
  (str "Hello, " username))


(comment   ; testing code
  #{}
  (conj #{} "Stu")
  (def visitors (atom #{}))
  (swap! visitors conj "Stu")
  (deref visitors)
  @visitors
  (hello "Rich")
  (hello "Aaron")
  @visitors

  )


;;; Libraries

(require 'clojure.java.io)

;; (r/doc require)
;; (require 'wonderland.introduction)

(System/getProperty "user.dir")
;; => "c:\\src\\Clojure\\Living-Clojure\\wonderland"
(-> (java.io.File. ".") .getAbsolutePath)
;; => "c:\\src\\Clojure\\Living-Clojure\\wonderland\\."

(comment ; example code

  (r/doc str)
  (r/doc hello)

  (r/find-doc "reduce")

  (r/source identity)

  (instance? java.util.Collection [1 2 3])

  (r/source instance?)
  )


;; removing defs
(def thing 1) ; value of thing is now 1
                                        ; do some stuff with thing
(alter-var-root #'thing (constantly nil)) ; value of thing is now nil

(ns-unmap 'user 'foo)
