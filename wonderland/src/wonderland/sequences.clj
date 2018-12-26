(ns wonderland.sequences
  (:require [clojure.repl :as r]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Everything is a sequence

;; (first aseq)

;; (rest aseq)

;; (cons elem aseq)

;; clojure.lang.ISeq

;; (seq coll) ;; return a seq or nil if empty

;; (next aseq) => (seq (rest aseq))

;; clarify rest/next behavoir
;; (rest ())        => ()
;; (next ())        => nil
;; (seq (rest ()))  => nil


;;; Lists

(first '(1 2 3))
;; => 1

(rest '(1 2 3))
;; => (2 3)

(cons 0 '(1 2 3))
;; => (0 1 2 3)


;;; Other data structures too.

(first [1 2 3])
;; => 1

(rest [1 2 3])
;; => (2 3)

(cons 0 [1 2 3])  ;; expensive
;; => (0 1 2 3)

(class [1 2 3])
;; => clojure.lang.PersistentVector

(class (rest [1 2 3]))
;; => clojure.lang.PersistentVector$ChunkedSeq


;;; Maps and Sets

(first {:fname "Aaron" :lname "Bedra"})
;; => [:fname "Aaron"]

(rest {:fname "Aaron" :lname "Bedra"})
;; => ([:lname "Bedra"])

(cons [:mname  "James"] {:fname "Aaron" :lname "Bedra"})
;; => ([:mname "James"] [:fname "Aaron"] [:lname "Bedra"])

(first #{:the :quick :brown :fox})
;; => :fox

(rest #{:the :quick :brown :fox})
;; => (:the :quick :brown)

(cons :jumped #{:the :quick :brown :fox})
;; => (:jumped :fox :the :quick :brown)

;; elements don't come back in the order entered
#{:the :quick :brown :fox}
;; => #{:fox :the :quick :brown}

(sorted-set :the :quick :brown :fox)
;; => #{:brown :fox :quick :the}

{:a 1 :b 2 :c 3 :d 4 :e 5}
;; => {:a 1, :b 2, :c 3, :d 4, :e 5}


;; 'conj' and 'into' add elements to a list in natural order.
(conj '(1 2 3) :a)
;; => (:a 1 2 3)

(into '(1 2 3) '(:a :b :c))
;; => (:c :b :a 1 2 3) ; like a stack

(conj [1 2 3] :a)
;; => [1 2 3 :a]

(into [1 2 3] [:a :b :c])
;; => [1 2 3 :a :b :c]


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; The sequence library
