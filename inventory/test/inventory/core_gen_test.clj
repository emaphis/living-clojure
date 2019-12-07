(ns inventory.core-gen-test
  (:require [inventory.core :as i])
  (:require [clojure.test.check.clojure-test :as ctest])
  (:require [clojure.test.check :as tc])
  (:require [clojure.test.check.generators :as gen])
  (:require [clojure.test.check.properties :as prop]))

(def title-gen (gen/such-that not-empty gen/string-alphanumeric))

(def author-gen (gen/such-that not-empty gen/string-alphanumeric))

(def copies-gen (gen/such-that (complement zero?) gen/pos-int))

(def book-gen
  (gen/hash-map :title title-gen :author author-gen :copies copies-gen))

(def inventory-gen (gen/not-empty (gen/vector book-gen)))

(def inventory-and-book-gen
  (gen/let [inventory inventory-gen
            book (gen/elements inventory)]
    {:inventory inventory :book book}))


(tc/quick-check 50
                (prop/for-all [i-and-b inventory-and-book-gen]
                              (= (i/find-by-title (-> i-and-b :book :title)
                                                  (:inventory i-and-b))
                                 (:book i-and-b))))
;; => {:result true, :pass? true, :num-tests 50, :time-elapsed-ms 216, :seed 1575746436951}
;; => {:result true, :pass? true, :num-tests 50, :time-elapsed-ms 147, :seed 1575746434277}
;; => {:result true, :pass? true, :num-tests 50, :time-elapsed-ms 100, :seed 1575746429932}

(ctest/defspec find-by-title-finds-books 50
  (prop/for-all [i-and-b inventory-and-book-gen]
                (= (i/find-by-title (-> i-and-b :book :title)
                                    (:inventory i-and-b))
                   (:book i-and-b))))

