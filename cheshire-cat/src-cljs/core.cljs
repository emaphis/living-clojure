(ns cheshire-cat.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [clojure.browser.repl :as repl]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [enfocus.core :as ef]
            ))

(ns cheshire-cat.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [clojure.browser.repl :as repl]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [enfocus.core :as ef]))

;; TODO: enfocus compile error.

(defn ^:export init []
  (repl/connect "http://localhost:9000/repl")
  (go (let [response (<! (http/get "/cheshire-cat"))
            body (:body response)]
        (ef/at "#cat-name" (ef/content (:name body)))
        (ef/at "#status" (ef/content (:status body))))))
