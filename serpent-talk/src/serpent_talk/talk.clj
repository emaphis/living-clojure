(ns serpent-talk.talk
  (:require [camel-snake-kebab.core :as csk]))

(csk/->snake_case "hello pigeon")
;; => "hello_pigeon"

(defn serpent-talk [input]
  (str "Serpent!  You said: "
       (csk/->snake_case input)))

(defn -main [& args]
  (println (serpent-talk (first args))))
