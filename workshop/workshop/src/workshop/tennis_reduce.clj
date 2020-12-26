(ns tennis-reduce)

;;; Exercise 5.03: Winning and Losing Streaks

;; 2.
(def serena-williams-2015
  [{:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Van Uytvanck A.",
    :tournament "Australian Open",
    :location "Melbourne",
    :date "2015-01-20"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Zvonareva V.",
    :tournament "Australian Open",
    :location "Melbourne",
    :date "2015-01-22"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Svitolina E.",
    :tournament "Australian Open",
    :location "Melbourne",
    :date "2015-01-24"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Muguruza G.",
    :tournament "Australian Open",
    :location "Melbourne",
    :date "2015-01-26"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Cibulkova D.",
    :tournament "Australian Open",
    :location "Melbourne",
    :date "2015-01-28"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Keys M.",
    :tournament "Australian Open",
    :location "Melbourne",
    :date "2015-01-29"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Sharapova M.",
    :tournament "Australian Open",
    :location "Melbourne",
    :date "2015-01-31"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Niculescu M.",
    :tournament "BNP Paribas Open",
    :location "Indian Wells",
    :date "2015-03-14"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Diyas Z.",
    :tournament "BNP Paribas Open",
    :location "Indian Wells",
    :date "2015-03-15"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Stephens S.",
    :tournament "BNP Paribas Open",
    :location "Indian Wells",
    :date "2015-03-17"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Bacsinszky T.",
    :tournament "BNP Paribas Open",
    :location "Indian Wells",
    :date "2015-03-19"}
   {:loser-sets-won nil,
    :winner-sets-won nil,
    :winner-name "Halep S.",
    :loser-name "Williams S.",
    :tournament "BNP Paribas Open",
    :location "Indian Wells",
    :date "2015-03-21"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Niculescu M.",
    :tournament "Sony Ericsson Open",
    :location "Miami",
    :date "2015-03-28"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Bellis C.",
    :tournament "Sony Ericsson Open",
    :location "Miami",
    :date "2015-03-29"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Kuznetsova S.",
    :tournament "Sony Ericsson Open",
    :location "Miami",
    :date "2015-03-30"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Lisicki S.",
    :tournament "Sony Ericsson Open",
    :location "Miami",
    :date "2015-04-01"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Halep S.",
    :tournament "Sony Ericsson Open",
    :location "Miami",
    :date "2015-04-03"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Suarez Navarro C.",
    :tournament "Sony Ericsson Open",
    :location "Miami",
    :date "2015-04-04"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Brengle M.",
    :tournament "Mutua Madrid Open",
    :location "Madrid",
    :date "2015-05-03"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Stephens S.",
    :tournament "Mutua Madrid Open",
    :location "Madrid",
    :date "2015-05-04"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Azarenka V.",
    :tournament "Mutua Madrid Open",
    :location "Madrid",
    :date "2015-05-06"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Suarez Navarro C.",
    :tournament "Mutua Madrid Open",
    :location "Madrid",
    :date "2015-05-07"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Kvitova P.",
    :loser-name "Williams S.",
    :tournament "Mutua Madrid Open",
    :location "Madrid",
    :date "2015-05-08"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Pavlyuchenkova A.",
    :tournament "Internazionali BNL d'Italia",
    :location "Rome",
    :date "2015-05-12"}
   {:loser-sets-won nil,
    :winner-sets-won nil,
    :winner-name "Mchale C.",
    :loser-name "Williams S.",
    :tournament "Internazionali BNL d'Italia",
    :location "Rome",
    :date "2015-05-14"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Hlavackova A.",
    :tournament "French Open",
    :location "Paris",
    :date "2015-05-26"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Friedsam A.L.",
    :tournament "French Open",
    :location "Paris",
    :date "2015-05-28"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Azarenka V.",
    :tournament "French Open",
    :location "Paris",
    :date "2015-05-30"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Stephens S.",
    :tournament "French Open",
    :location "Paris",
    :date "2015-06-01"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Errani S.",
    :tournament "French Open",
    :location "Paris",
    :date "2015-06-03"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Bacsinszky T.",
    :tournament "French Open",
    :location "Paris",
    :date "2015-06-04"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Safarova L.",
    :tournament "French Open",
    :location "Paris",
    :date "2015-06-06"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Gasparyan M.",
    :tournament "Wimbledon",
    :location "London",
    :date "2015-06-29"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Babos T.",
    :tournament "Wimbledon",
    :location "London",
    :date "2015-07-01"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Watson H.",
    :tournament "Wimbledon",
    :location "London",
    :date "2015-07-03"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Williams V.",
    :tournament "Wimbledon",
    :location "London",
    :date "2015-07-06"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Azarenka V.",
    :tournament "Wimbledon",
    :location "London",
    :date "2015-07-07"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Sharapova M.",
    :tournament "Wimbledon",
    :location "London",
    :date "2015-07-09"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Muguruza G.",
    :tournament "Wimbledon",
    :location "London",
    :date "2015-07-11"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Bonaventure Y.",
    :tournament "Collector Swedish Open",
    :location "Bastad",
    :date "2015-07-15"}
   {:loser-sets-won nil,
    :winner-sets-won nil,
    :winner-name "Koukalova K.",
    :loser-name "Williams S.",
    :tournament "Collector Swedish Open",
    :location "Bastad",
    :date "2015-07-16"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Pennetta F.",
    :tournament "Rogers Cup",
    :location "Toronto",
    :date "2015-08-11"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Petkovic A.",
    :tournament "Rogers Cup",
    :location "Toronto",
    :date "2015-08-14"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Vinci R.",
    :tournament "Rogers Cup",
    :location "Toronto",
    :date "2015-08-15"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Bencic B.",
    :loser-name "Williams S.",
    :tournament "Rogers Cup",
    :location "Toronto",
    :date "2015-08-15"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Pironkova T.",
    :tournament "Western & Southern Financial Group Women's Open",
    :location "Cincinnati",
    :date "2015-08-19"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Knapp K.",
    :tournament "Western & Southern Financial Group Women's Open",
    :location "Cincinnati",
    :date "2015-08-20"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Ivanovic A.",
    :tournament "Western & Southern Financial Group Women's Open",
    :location "Cincinnati",
    :date "2015-08-21"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Svitolina E.",
    :tournament "Western & Southern Financial Group Women's Open",
    :location "Cincinnati",
    :date "2015-08-23"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Halep S.",
    :tournament "Western & Southern Financial Group Women's Open",
    :location "Cincinnati",
    :date "2015-08-23"}
   {:loser-sets-won 0,
    :winner-sets-won 1,
    :winner-name "Williams S.",
    :loser-name "Diatchenko V.",
    :tournament "US Open",
    :location "New York",
    :date "2015-09-01"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Bertens K.",
    :tournament "US Open",
    :location "New York",
    :date "2015-09-02"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Mattek-Sands B.",
    :tournament "US Open",
    :location "New York",
    :date "2015-09-05"}
   {:loser-sets-won 0,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Keys M.",
    :tournament "US Open",
    :location "New York",
    :date "2015-09-06"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Williams S.",
    :loser-name "Williams V.",
    :tournament "US Open",
    :location "New York",
    :date "2015-09-09"}
   {:loser-sets-won 1,
    :winner-sets-won 2,
    :winner-name "Vinci R.",
    :loser-name "Williams S.",
    :tournament "US Open",
    :location "New York",
    :date "2015-09-11"}])

;; 3. skeleton
(defn serena-williams-win-los-streaks-template [matches]
  (reduce (fn [acc match]
            ;; TODO:
            
            )
          {:matches []   ; default value
           :current-wins 0
           :curren-losses 0}
          matches))

;; 4. Extract the matches from the call to `reduce`
(defn serena-williams-win-los-streaks-template [matches]
  (:matches
   (reduce (fn [acc match]
             ;; TODO:
             
             )
           {:matches []   ; default value
            :current-wins 0
            :curren-losses 0}
           matches)))

;; 5. A helper function to format a string for presenting the current streaks
(defn streak-string [current-wins current-losses]
  (cond (pos? current-wins) (str "Won " current-wins)
        (pos? current-losses) (str "Lost " current-losses)
        :else "First match of the year"))

;; 6. reducing function
(fn [{:keys [current-wins current-losses] :as acc}
     {:keys [winner-name] :as match}]
  ;; TODO:
  )

;; 7. introduce a `let` binding for the current match.
(fn [{:keys [current-wins current-losses] :as acc}
     {:keys [winner-name] :as match}]
  (let [this-match (assoc match :current-streak (streak-string current-wins current-losses))]
    (update acc :matches #(conj % this-match))))

;; 8. produce contextual information
(fn [{:keys [current-wins current-losses] :as acc} match]
  (let [this-match (assoc match :current-streak (streak-string current-wins current-losses))
        serena-victory? (= (:winner-name match) "Williams S.")]
    (-> acc
        (update acc :matches #(conj % this-match))
        (assoc :current-wins (if serena-victory?
                               (inc current-wins)
                               0))
        (assoc :curren-losses (if serena-victory?
                                0
                                (inc current-losses))))))


(defn serena-williams-win-los-streaks [matches]
  (:matches
   (reduce (fn [{:keys [current-wins current-losses] :as acc} match]
             (let [this-match (assoc match :current-streak (streak-string current-wins current-losses))
                   serena-victory? (= (:winner-name match) "Williams S.")]
               (-> acc
                   (update acc :matches #(conj % this-match))
                   (assoc :current-wins (if serena-victory?
                                          (inc current-wins)
                                          0))
                   (assoc :curren-losses (if serena-victory?
                                           0
                                           (inc current-losses))))))

           {:matches []   ; default value
            :current-wins 0
            :curren-losses 0}
           matches)))


#_(serena-williams-win-los-streaks serena-williams-2015)
