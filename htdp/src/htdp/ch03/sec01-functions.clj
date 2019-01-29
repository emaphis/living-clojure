(ns htdp.ch03.sec01-functions
  (:require [clojure.test :refer :all]
	    [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]
            [clojure.repl :as r]
	    [htdp.util :refer :all]
	    [clojure.string :refer :all]))


;;; Temperature program.

;; Data
(s/def ::temperature (s/and int? #(and (> % -274)(< % 500))))
;; `interpretation`: represents Celsius degrees

(s/valid? ::temperature -300)

(s/fdef convert
  :args ::temperature  :ret nat-int?)

(defn convert [temp]
  "Convert a given temperature from Celsius to Fahrenheit"
  0)


;;; Calculate the area of a square

(s/fdef area-of-square
  :args number? :ret number?)

;; computes the area of a square with side `len`
;; given: 2, expect: 4
;; given: 7, expect: 49
(defn area-of-square [len] 0)

(area-of-square 10)

(s/exercise-fn `area-of-square)
