(ns myapp.exer01
  (:require [clojure.test :refer [deftest testing is]]))


(deftest test-1
  (testing "Example test"
    (is (= 3 3))))

;;; Ex 001 El
;; This is a clojure form. Enter a value which will make the form evaluate to true.
;; Don't over think it! If you are confused, see the getting started page.
;; Hint: true is equal to true.

(deftest test001
  (testing "Nothing but the Truth "
    (is (= true true))))


;;; Ex 002 El
;; If you are not familiar with polish notation, simple arithmetic might seem
;; confusing.
;; Note: Enter only enough to fill in the blank (in this case, a single number)
;; - do not retype the whole problem.

(deftest test002
  (testing "Simple Math"
    (is (= (- 10 (* 2 3)) 4))))


;;; Ex 003 El
;; Clojure strings are Java strings. This means that you can use any of the Java
;; string methods on Clojure strings.

(deftest test003
  (testing "Intro to Strings"
    (is (= "HELLO WORLD" (.toUpperCase "hello world")))))


;;; Ex 004 El
;; Lists can be constructed with either a function or a quoted form.

(deftest test004
  (testing "Intro to Lists"
    (is (= (list :a :b :c) '(:a :b :c)))))


;;; Ex 005 El
;;  When operating on a list, the conj function will return a new list with one
;; or more items "added" to the front.
;; Note that there are two test cases, but you are expected to supply only one
;; answer, which will cause all the tests to pass.

(def lst005 '(1 2 3 4))
(deftest test005
  (testing "Lists: conj"
    (is (= lst005 (conj '(2 3 4) 1)))
    (is (= lst005 (conj '(3 4) 2 1)))))


;;; Ex 006 El
;; Vectors can be constructed several ways. You can compare them with lists. 
;; Note: the brackets [] surrounding the blanks __ are part of the test case.

(deftest test006
  (testing "Intro to Vectors"
    (is (= [:a :b :c] (list :a :b :c) (vec '(:a :b :c)) (vector :a :b :c)))))


;; Ex 007 El.
;; When operating on a Vector, the conj function will return a new vector with
;; one or more items "added" to the end.

(def vec007 [1 2 3 4])
(deftest test007
  (testing "Vectors: conj"
    (is (= vec007 (conj [1 2 3] 4)))
    (is (= vec007 (conj [1 2] 3 4)))))


;;; Ex 008 El.
;;  Sets are collections of unique values

(def set008 #{:a :b :c :d})
(deftest test008
  (testing "Intro to Sets"
    (is (= set008 (set '(:a :a :b :c :c :c :c :d :d))))
    (is (= set008 (clojure.set/union #{:a :b :c} #{:b :c :d})))))


;;; Ex 009 El.
;;  When operating on a set, the conj function returns a new set with
;;  one or more keys "added".

(def int009 2)
(deftest test009
  (testing "Sets: conj"
    (is (= #{1 2 3 4} (conj #{1 4 3} int009)))))


;;; Ex 010 El.
;;  Maps store key-value pairs. Both maps and keywords can be used as lookup
;;  functions. Commas can be used to make maps more readable, but they are not
;;  required.

(def int010 20)
(deftest test010
  (testing "Intro to Maps"
    (is (= int010 ((hash-map :a 10, :b 20, :c 30) :b)))
    (is (= int010 (:b {:a 10, :b 20, :c 30})))))


;;; Ex 011 El.
;;  When operating on a map, the conj function returns a new map with one or
;;  more key-value pairs "added".

(def vec011 [:b 2])
(deftest test011
  (testing "Maps: conj"
    (is (= {:a 1, :b 2, :c 3} (conj {:a 1} vec011 [:c 3])))))


;;; Ex 012 El.
;;  All Clojure collections support sequencing. You can operate on sequences
;;  with functions like first, second, and last.

(def in12 3)
(deftest test012
  (testing "Intro to Sequences"
    (is (= in12 (first '(3 2 1))))
    (is (= in12 (second [2 3 4])))
    (is (= in12 (last (list 1 2 3))))))
