(ns cheshire-cat.scratch
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [cheshire.core :as json]
            [ring.middleware.json :as ring-json]
            [ring.util.response :as rr]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Just testing some example code

;;;;;;;;;;;;;;;;;;;;;
;;; cheshire.core

;; take a Clojure data cstructure and turn it into json
(json/generate-string {:name "Cheshire Cat" :state :grinning})
;; => "{\"name\":\"Cheshire Cat\",\"state\":\"grinning\"}"

;; take json and convert it into clojure
(json/parse-string
 "{\"name\":\"Cheshire Cat\",\"state\":\"grinning\"}")
;; => {"name" "Cheshire Cat", "state" "grinning"}

;; true changes keys into keywords
(json/parse-string
 "{\"name\":\"Cheshire Cat\",\"state\":\"grinning\"}" true)
;; => {:name "Cheshire Cat", :state "grinning"}


;; ring.util.response
(rr/response {:name "Cheshire Cat" :status :grinning})
;; => {:status 200,
;;     :headers {},
;;     :body {:name "Cheshire Cat", :status :grinning}}
