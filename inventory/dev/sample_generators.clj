
(ns sample-generators
  (:require [clojure.test.check.clojure-test :as ctest])
  (:require [clojure.test.check :as tc])
  (:require [clojure.test.check.generators :as gen])
  (:require [clojure.test.check.properties :as prop])
  (:require [inventory.core-gen-test :as it]))


(gen/sample gen/string-alphanumeric)
;; => ("" "" "" "" "83c5" "" "Uq" "u09" "" "7pb8T9v1h")
;; => ("" "K" "o" "I" "" "3b4e9" "" "" "9" "Lo")
;; => ("" "" "8" "" "" "5Qf3" "" "" "bfKdz8" "18Lgx")

(gen/sample it/inventory-and-book-gen)
;; => ({:inventory
;;      [{:title "w", :author "1", :copies 1}
;;       {:title "1v", :author "z", :copies 2}],
;;      :book {:title "w", :author "1", :copies 1}}
;;     {:inventory [{:title "8", :author "y", :copies 1}],
;;      :book {:title "8", :author "y", :copies 1}}
;;     {:inventory [{:title "l5o", :author "t4", :copies 1}],
;;      :book {:title "l5o", :author "t4", :copies 1}}
;;     {:inventory [{:title "8V1", :author "N4", :copies 2}],
;;      :book {:title "8V1", :author "N4", :copies 2}}
;;     {:inventory
;;      [{:title "9n", :author "tU", :copies 3}
;;       {:title "h", :author "W", :copies 2}
;;       {:title "874", :author "W", :copies 1}
;;       {:title "FZ", :author "4", :copies 1}],
;;      :book {:title "h", :author "W", :copies 2}}
;;     {:inventory
;;      [{:title "AfT9k", :author "6p24", :copies 2}
;;       {:title "4mq", :author "7v", :copies 2}
;;       {:title "aLA", :author "ylFQ", :copies 3}
;;       {:title "bh0aC", :author "1", :copies 3}
;;       {:title "B6S", :author "m", :copies 4}],
;;      :book {:title "B6S", :author "m", :copies 4}}
;;     {:inventory
;;      [{:title "nX0", :author "56", :copies 7}
;;       {:title "iPtXa", :author "41Y6nAqO", :copies 2}
;;       {:title "cWhu5", :author "0", :copies 4}
;;       {:title "h9KsQ", :author "8BOsl", :copies 1}
;;       {:title "C0582", :author "cMagn0", :copies 7}
;;       {:title "4", :author "y41UKN", :copies 7}
;;       {:title "AHsom", :author "j", :copies 7}],
;;      :book {:title "AHsom", :author "j", :copies 7}}
;;     {:inventory
;;      [{:title "Fwn6C", :author "M7xl", :copies 1}
;;       {:title "E94E9", :author "o", :copies 5}],
;;      :book {:title "E94E9", :author "o", :copies 5}}
;;     {:inventory
;;      [{:title "t20F6596", :author "RG8Y1Hj", :copies 6}
;;       {:title "qt", :author "l9aLg2wg", :copies 4}
;;       {:title "O76WI", :author "OnP865Ct", :copies 9}
;;       {:title "PkX6", :author "nxIT", :copies 6}],
;;      :book {:title "qt", :author "l9aLg2wg", :copies 4}}
;;     {:inventory
;;      [{:title "g0QB2p", :author "G4fnCs3C", :copies 3}
;;       {:title "DEv2", :author "wzg", :copies 2}
;;       {:titble "rrL4299", :author "XHvtR92B", :copies 1}
;;       {:title "T0", :author "6lP1", :copies 5}
;;       {:title "5P", :author "55J7j9", :copies 5}
;;       {:title "z", :author "9l1A", :copies 6}],
;;      :book {:title "5P", :author "55J7j9", :copies 5}})

;;; Checking properties

;; i should be less than inc i
(prop/for-all [i gen/pos-int]
              (< i (int i)))

(tc/quick-check 50
                (prop/for-all [i gen/pos-int]
                              (< i (inc i))))
;; => {:result true, :pass? true, :num-tests 50, :time-elapsed-ms 0, :seed 1575746169388}





