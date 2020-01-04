(ns quil-exp.polymorph)

;; Polymorphism in Clojure
;; https://clojure.org/reference/protocols

(defprotocol AProtocol
  "A doc string for AProtocol abstraction"
  (bar [a b] "bar docs")
  (baz [a] [a b] [a b c] "baz docs"))


(defprotocol P
  (foo [x])
  (bar-me [x] [x y]))


(deftype Foo [a b c]
  P
  (foo [x] a)
  (bar-me [x] b)
  (bar-me [x y] (+ c y)))

(bar-me (Foo. 1 2 3) )
