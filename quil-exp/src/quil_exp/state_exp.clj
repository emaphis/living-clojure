(ns quil-exp.state-exp
  (:require [quil.core :as q]))

;; Managing global state.  A built-in Atom?
;; `set-state!`  `state` `state-atom`
;; http://quil.info/api/state
;; See fun-mode-sketch for another method of handling state.

(defn setup []
  ;;  (q/set-state! :text-2 "More state")   ;; can only set state once.
  (q/set-state! :text "I'm state!" :year (q/year) :foo 0))


(defn update-state []
  (swap! (q/state-atom) update-in [:foo] inc))

(defn draw []
  (q/background 255)
  (q/fill 0)
  (q/text (q/state :text) 10 20)
  (q/text (str "counter: " ( q/state :foo)) 10 40)
  (q/text (str "Full state: " (q/state)) 10 60))

(q/defsketch state-exp
  :title "State Experiments"
  :setup setup
  :update update-state
  :draw draw)
