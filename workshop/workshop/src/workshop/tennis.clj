(ns workshop.tennis
  (:require
   [clojure.data.csv :as csv]
   [clojure.java.io :as io]
   [semantic-csv.core :as sc]))


(defn first-match [csv]
  (with-open [r (io/reader csv)]
    (->> (csv/read-csv r)
         sc/mappify
         first)))

#_(first-match "c:/test/tennis-data/match_scores_1991-2016_unindexed_csv.csv")

(defn five-matches [csv]
  (with-open [r (io/reader csv)]
    (->> (csv/read-csv r)
         sc/mappify
         (map #(select-keys % [:tourney_year_id
                               :winner_name
                               :loser_name
                               :winner_sets_won
                               :loser_sets_won]))
         (take 5)
         doall)))

#_(five-matches "c:/test/tennis-data/match_scores_1991-2016_unindexed_csv.csv")

(defn five-matches-int-sets [csv]
  (with-open [r (io/reader csv)]
    (->> (csv/read-csv r)
         sc/mappify
         (map #(select-keys % [:tourney_year_id
                               :winner_name
                               :loser_name
                               :winner_sets_won
                               :loser_sets_won]))
         (sc/cast-with {:winner_sets_won sc/->int
                        :loser_sets_won sc/->int})
         (take 5)
         doall)))


;;; Exercise 4.13: Querying the Data with filter

;; 3. Federer predicate
#(= "Roger Federer" (:winner_name %))

(defn federer-wins [csv]
  (with-open [r (io/reader csv)]
    (->> (csv/read-csv r)
         sc/mappify
         (filter #(= "Roger Federer" (:winner_name %)))
         (map #(select-keys % [:winner_name
                               :loser_name
                               :winner_sets_won
                               :loser_sets_won
                               :winner_games_won
                               :loser_games_won
                               :tourney_year_id
                               :tourney_slug]))
         doall)))

#_(federer-wins "c:/test/tennis-data/match_scores_1991-2016_unindexed_csv.csv")


;;; Exercise 4.14: A Dedicated Query Function

;; 2.
(defn match-query [csv pred]
  (with-open [r (io/reader csv)]
    (->> (csv/read-csv r)
         sc/mappify
         (filter pred)
         (map #(select-keys % [:winner_name
                               :loser_name
                               :winner_sets_won
                               :loser_sets_won
                               :winner_games_won
                               :loser_games_won
                               :tourney_year_id
                               :tourney_slug]))
         doall)))

;; 3. specialized predicate - Federer's matches, wins, and losses.
#(or (= "Roger Federer" (:winner_name %))
     (= "Roger Federer" (:loser_name %)))

#((hash-set (:winner_name %) (:loser_name %)) "Roger Federer")



(count (match-query "c:/test/tennis-data/match_scores_1991-2016_unindexed_csv.csv"
                    #((hash-set (:winner_name %) (:loser_name %)) "Roger Federer")))
;; => 1290

(count (match-query "c:/test/tennis-data/match_scores_1991-2016_unindexed_csv.csv"
                    #(= (:winner_name %) "Roger Federer")))
;; => 1050


;;; Exercise 4.15: Using filter to Find a Tennis Rivalry

;; 2.
#(and
  (or (= (:winner_name %) "Roger Federer")
      (= (:winner_name %) "Rafael Nadal"))
  (or (= (:loser_name %) "Roger Federer")
      (= (:loser_name %) "Rafael Nadal")))

#(= (hash-set (:winner_name %) (:loser_name %))
    #{"Roger Federer" "Rafael Nadal"})

(take 3 (match-query "c:/test/tennis-data/match_scores_1991-2016_unindexed_csv.csv"
                     #(= (hash-set (:winner_name %) (:loser_name %))
                         #{"Roger Federer" "Rafael Nadal"})))

;; 3. Arithmetic on fields
(defn match-query [csv pred]
  (with-open [r (io/reader csv)]
    (->> (csv/read-csv r)
         sc/mappify
         (sc/cast-with {:winner_sets_won sc/->int
                        :loser_sets_won sc/->int
                        :winner_games_won sc/->int
                        :loser_games_won sc/->int})
         (filter pred)
         (map #(select-keys % [:winner_name
                               :loser_name
                               :winner_sets_won
                               :loser_sets_won
                               :winner_games_won
                               :loser_games_won
                               :tourney_year_id
                               :tourney_slug]))
         doall)))

;; predicate for close matches - a test for the differences in sets won
#(and (= (hash-set (:winner_name %) (:loser_name %))
         #{"Roger Federer" "Rafael Nadal"})
      (= 1 (- (:winner_sets_won %) (:loser_sets_won %))))

(take 3 (match-query "c:/test/tennis-data/match_scores_1991-2016_unindexed_csv.csv"
                     #(and (= (hash-set (:winner_name %) (:loser_name %))
                              #{"Roger Federer" "Rafael Nadal"})
                           (= 1 (- (:winner_sets_won %) (:loser_sets_won %))))))


;;; Activity 4.02: Arbitrary Tennis Rivalries

(defn rivalry-data [csv player-1 player-2]
  (with-open [r (io/reader csv)]
    (let [rivalry-seq (->> (csv/read-csv r)
                           sc/mappify
                           (sc/cast-with {:winner_sets_won sc/->int
                                          :loser_sets_won sc/->int
                                          :winner_games_won sc/->int
                                          :loser_games_won sc/->int})
                           (filter #(= (hash-set (:winner_name %) (:loser_name %))
                                       #{player-1 player-2}))

                           (map #(select-keys % [:winner_name
                                                 :loser_name
                                                 :winner_sets_won
                                                 :loser_sets_won
                                                 :winner_games_won
                                                 :loser_games_won
                                                 :tourney_year_id
                                                 :tourney_slug])))
          player-1-victories (filter #(= (:winner_name %) player-1) rivalry-seq)
          player-2-victories (filter #(= (:winner_name %) player-2) rivalry-seq) ]
      {:first-victory-player-1 (first player-1-victories)
       :first-victory-player-2 (first player-2-victories)
       :total-matches (count rivalry-seq)
       :total-victories-player-1 (count player-1-victories)
       :total-victories-player-2 (count player-2-victories)
       :most-competitive-matches (->> rivalry-seq
                                      (filter #(= 1 (- (:winner_sets_won %) (:loser_sets_won %)))))})))



(rivalry-data "c:/test/tennis-data/match_scores_1968-1990_unindexed_csv.csv"
              "Boris Becker" "Jimmy Connors")
