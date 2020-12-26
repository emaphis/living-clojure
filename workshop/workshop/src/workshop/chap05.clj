(ns workshop.chap05)

(use 'clojure.repl)

;;; Many to one - Reducing
;; `reduce` `count`

;;; The basics of `reduce`

;; sum-so-far accumulates information
(reduce (fn [sum-so-far item] (+ sum-so-far item)) [8 4000 10 300])
;; => 4318
;; or just use the operator
(reduce + [8 4000 10 300])
;; => 4318
;; or apply
(apply + [8 4000 10 300])
;; => 4318


;;; Exercise 5.01: Finding the Day with the Maximum Temperature

(def weather-days
  [{:max 31
    :min 27
    :description :sunny
    :date "2019-09-24"}
   {:max 28
    :min 25
    :description :cloudy
    :date "2019-09-25"}
   {:max 22
    :min 18
    :description :rainy
    :date "2019-09-26"}
   {:max 23
    :min 16
    :description :stormy
    :date "2019-09-27"}
   {:max 35
    :min 19
    :description :sunny
    :date "2019-09-28"}])

;; 2. Find the max of weather dats
(apply max (map :max weather-days))
;; => 35

;; 3. Find the :max temperature with `reduce`.
(reduce (fn [max-day-so-far this-day]
          (if (> (:max this-day) (:max max-day-so-far))
            this-day
            max-day-so-far))
        weather-days)
;; => {:max 35, :min 19, :description :sunny, :date "2019-09-28"}

;; 4. find the min
(reduce (fn [min-max-day-so-far this-day]
          (if (< (:max this-day) (:max min-max-day-so-far))
            this-day
            min-max-day-so-far))
        weather-days)
;; => {:max 22, :min 18, :description :rainy, :date "2019-09-26"}


;;; Initializing reduce

;; reduced value has a different type than the input
(reduce (fn [{:keys [minimum maximum]} new-number]
          {:minimum (if (and minimum (> new-number minimum))
                      minimum
                      new-number)
           :maximum (if (and maximum (< new-number maximum))
                      maximum
                      new-number)})
        {}   ; new value
        [5 23 5004 845 22])
;; => {:minimum 5, :maximum 5004}

;;; Partitioning with reduce

(partition 3 [1 2 3 4 5 6 7 8 9 10])
;; => ((1 2 3) (4 5 6) (7 8 9))

(partition-all 3 [1 2 3 4 5 6 7 8 9 10])
;; => ((1 2 3) (4 5 6) (7 8 9) (10))

(partition-by #(> % 10) [5 33 18 0 23 2 9 4 3 99])
;; => ((5) (33 18) (0) (23) (2 9 4 3) (99))

;; using `reduce`

;; partial data
{:current [5 10]
 :segments [[3 7 8]
            [17]
            [4 1 1 5 3 2]]}

(reduce (fn [{:keys [segments current] :as accum} n]
          (let [current-with-n (conj current n)
                total-with-n (apply + current-with-n)]
            (if (> total-with-n 20)  ; parameter
              (assoc accum
                     :segments (conj segments current)
                     :current [n])
              (assoc accum :current current-with-n))))
        {:segments [] :current []}  ; seed value
        [4 19 4 9 5 12 5 3 4 1 1 9 5 18])
;; => {:segments [[4] [19] [4 9 5] [12 5 3] [4 1 1 9 5]], :current [18]}

(defn segment-by-sum [limit ns]
  (let [result (reduce (fn [{:keys [segments current] :as accum} n]
                         (let [current-with-n (conj current n)
                               total-with-n (apply + current-with-n)]
                           (if (> total-with-n limit)
                             (assoc accum
                                    :segments (conj segments current)
                                    :current [n])
                             (assoc accum :current current-with-n))))
                       {:segments [] :current []}
                       ns)]
    (conj (:segments result) (:current result))))

(segment-by-sum 20 [4 19 4 9 5 12 5 3 4 1 1 9 5 18])
;; => [[4] [19] [4 9 5] [12 5 3] [4 1 1 9 5] [18]]

;;; looking back with `reduce`

(defn parity-totals [ns]
  (:ret
   (reduce (fn [{:keys [current] :as acc} n]
             (if (and (seq current)
                      (or (and (odd? (last current)) (odd? n))
                          (and (even? (last current)) (even? n))))
               (-> acc
                   (update :ret conj [n (apply + current)])
                   (update :current conj n))
               (-> acc
                   (update :ret conj [n 0])
                   (assoc :current [n]))))
           {:current [] :ret []}
           ns)))

(def numbers [4 9 2 3 7 9 5 2 6 1 4 6 2 3 3 6 1])

(parity-totals numbers)
;; => [[4 0] [9 0] [2 0] [3 0] [7 3] [9 10] [5 19] [2 0] [6 2] [1 0] [4 0] [6 4] [2 10] [3 0] [3 3] [6 0] [1 0]]


;;; Exercise 5.02: Measuring Elevation Differences on Slopes
;; see bike_race.clj

;;; Exercise 5.03: Winning and Losing Streaks
