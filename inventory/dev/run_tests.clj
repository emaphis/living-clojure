(ns inventory.core-test)

(require '[inventory.core-test :as ct])
(require '[clojure.test :as test])


;; Three ways to run the tests in a namespace
(test/run-tests)
(test/run-tests *ns*)
(test/run-tests 'inventory.core-test)
(test/run-all-tests)
