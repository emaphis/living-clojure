(ns bike-race)

;; 2. bike race data
;; [distance, elevation]

(def distance-elevation
  [[0 400]
   [12.5 457]
   [19 622]
   [21.5 592]
   [29 615]
   [35.5 892]
   [39 1083]
   [43 1477]
   [48.5 1151]
   [52.5 999]
   [57.5 800]
   [62.5 730]
   [65 1045]
   [68.5 1390]
   [70.5 1433]
   [75 1211]
   [78.5 917]
   [82.5 744]
   [84 667]
   [88.5 860]
   [96 671]
   [99 584]
   [108 402]
   [115.5 473]])

;; 3. skeleton

(defn distances-elevation-to-next-peack-or-valley-template
  [data]
  (->
   (reduce
    (fn [{:keys [current] :as acc} [distance elevation :as this-position]]
      ,,,)  ; TODO:
    {:current []
     :calculated []}  ; seed data
    (reverse data))  ; end reduce
   :calculated
   reverse))

;; 4. calculate whether the new position is on the same slop as current
(defn same-slope-as-current? [current elevation]
  (or (= 1 (count current))
      (let [[[_ next-to-last] [_ the-last]] (take-last 2 current)]
        (or (>= next-to-last the-last elevation)
            (<= next-to-last the-last elevation)))))

;; 6. test `same-slope-as current?`
(same-slope-as-current? [[1 5] [2 10]] 15)
;; => true
(same-slope-as-current? [[1 5] [2 10]] 5)
;; => false
(same-slope-as-current? [[1 5] [2 10]] 10)
;; => true
(same-slope-as-current? [[1 5]] 10)
;; => true
(same-slope-as-current? [[1 5] [2 10] [3 15]] 20)
;; => true

;; 7.
(defn distances-elevation-to-next-peak-or-valley
  [data]
  (->
   (reduce
    (fn [{:keys [current] :as acc} [distance elevation :as this-position]]
      (cond (empty? current)
            {:current [this-position]
             :calculated [{:race-position distance
                           :elevation elevation
                           :distance-to-next 0
                           :elevation-to-next 0}]}
            (same-slope-as-current? current elevation)
            (-> acc
                (update :current conj this-position)
                (update :calculated
                        conj
                        {:race-position distance
                         :elevation elevation
                         :distance-to-next (- (first (first current)) distance)
                         :elevation-to-next (- (second (first current)) elevation)}))
            :otherwise-slope-change
            (let [[prev-distance prev-elevation :as peak-or-valley] (last current)]
              (-> acc
                  (assoc :current [peak-or-valley this-position])
                  (update :calculated
                          conj
                          {:race-position distance
                           :elevation elevation
                           :distance-to-next (- prev-distance distance)
                           :elevation-to-next (- prev-elevation elevation)})))))
    {:current []
     :calculated []}
    (reverse data))
   :calculated
   reverse))

#_(distances-elevation-to-next-peak-or-valley distance-elevation)
