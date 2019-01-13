(ns wonderland.clojure-interop
  (:require [clojure.string :as str]
            [clojure.repl :as r]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Java Interop


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Creating Java Objects in Clojure

;; Direct use of Java types and interfaces
;; * Anonymous interface implementation with `reify`
;; * and class extension with `proxy`

;;; Direct use of Java types

;; Clojure directly uses Java types.
;; Clojure implements many key Java interfaces.
;; Functions implement `Callable` and `Runnable`

;; A `Thread` is started with `Runnable`so any function can be used.

(defn say-hi []
  (println "Hello from thread" (.getName (Thread/currentThread))))

(dotimes [_ 3]
  (.start (Thread. say-hi)))
;; Hello from threadHello from thread Hello from thread Thread-58
;; Thread-57
;; Thread-59

(.start (Thread. (:tag {:tag "message" :hello "world"})))

;; Clojure datastructures implement key interfaces `Collection`, `List`, `Map`,
;; `Sets`

;; Vectors implement `Collections` and `RandomAccess`
;; so ...
(java.util.Collections/binarySearch [1 13 42 1000] 42)
;; => 2


;;; Implementing Java interfaces

;; If you just need to generate an Interface to make a call use `reify`

;; Implement a function that takes a directory and a suffix and returns
;; a sequence of all files with that sequence in the directory.

(import [java.io File FilenameFilter])

(defn suffix-filter [suffix]
  (reify FilenameFilter      ; anonymous instance for one shot use
    (accept [this dir name]  ; implement `accept`
      (.endsWith name suffix))))

(defn list-files [dir suffix]
  (seq (.list (File. dir) (suffix-filter suffix))))

(list-files "." ".clj")
;; => ("project.clj")
(list-files "./src/wonderland" ".clj")
;; => ("account.clj" "chapter01.clj" "chapter02.clj" "chapter03.clj" ...


;; In other cases you want to create an object with data fields and
;; a type that implements a Java interface.  `defrecord` `deftype`

;; create a `Counter` that counts up to `n`

(defrecord Conter [n]
  Runnable
  (run [this] (println (range n))))

(def c (->Conter 5))

(.start (Thread. c))
;; (0 1 2 3 4)

;; `c` is a concrete type

(:n c)
;; => 5

(def c2 (assoc c :n 8))

(.start (Thread. c2))
;; (0 1 2 3 4 5 6 7)


;; to create stateful objects, use fields that hold reference types like atoms.


;;; Extending classes with Proxies.

;; Parsing XML

(import '[org.xml.sax InputSource]
        '[org.xml.sax.helpers DefaultHandler]
        '[java.io StringReader]
        '[javax.xml.parsers SAXParserFactory])

;; To use a SAX parser we need to implement a callback mechanism.
;; So extend a `Defaulthandler` class.

;; (proxy class-and-interfaces super-cons-args & fns)
;;(r/doc proxy)

(def print-element-handler
  (proxy [DefaultHandler] []
    (startElement [uri local qname atts]
      (println (format "Saw element: %s" qname)))))

;; create a function that parses a string:
(defn demo-sax-parse [source handler]
  (.. SAXParserFactory newInstance newSAXParser
      (parse (InputSource. (StringReader. source)) handler)))

(demo-sax-parse
 "<foo>
<bar>Body of bar</bar>
</foo>"
 print-element-handler)
;; Saw element: foo
;; Saw element: bar


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Calling Clojure from Java

;; reading, function lookup, and invocation,

;; clojure.java.api.Clojure  class
;; clojure.lang.IFn  interface

;; IFn plus = Clojure.var("clojure.core", "+");
;; System.out.println(plus.invoke(1, 2, 3));

;; a `read` method that can be used to read literal data into Clojure
;; data structures:
;; Object vector = Clojure.read("[1 2 3]");

;; requiring a namespace.
;; IFn require = Clojure.var("clojure.core", "require");
;; require.invoke(Clojure.read("clojure.set"));


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Exception handling

;;; Keeping exception handling simple
;; Clojure doesn't expect you to deal with checked exceptions.

;;; Rethrowing with ex-info


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Optimizing for performance

;;; Adding type hints

;; Ask Clojure how much type information it can infer.
(set! *warn-on-reflection* true)
;; => true

(defn describe-class [c]
  {:name (.getName c)
   :final (java.lang.reflect.Modifier/isFinal (.getModifiers c))})

;; Reflection warning,168:10
;; - reference to field getName can't be resolved.
;; Reflection warning,:169:47
;; - reference to field getModifiers can't be resolved.


(defn describe-class [^Class c]
  {:name (.getName c)
   :final (java.lang.reflect.Modifier/isFinal (.getModifiers c))})
;; now the compiled code will be the same as Java

(describe-class StringBuffer)
;; => {:name "java.lang.StringBuffer", :final true}

;; (describe-class "foo")
;; 1. Unhandled java.lang.IllegalArgumentException
;; No matching field found: getName for class java.lang.String

;; type-hints only effect code that call Java methods.
(defn wants-a-string [^String s] (println s))

;; but this works
(wants-a-string "foo")
;; foo
(wants-a-string 0)
;; 0

