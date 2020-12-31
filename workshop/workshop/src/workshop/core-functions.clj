(ns workshop.core-functions)

(use 'clojure.repl)

;;; Programming to Abstractions

;; seq interface - `first`, `rest` and `cons`
;; Treating Lists, Vectors, Set and Maps ans Sequences.

;; `seq` function examples

;; `map`
(map inc [1 2 3])
;; => (2 3 4)

(map str ["a" "b" "c"] ["A" "B" "C"])
;; => ("aA" "bB" "cC")

(def meat-consumption [8.1 7.3 6.6 5.0])
(def plant-consumption [0.0 0.2 0.3 1.1])
(defn unify-data
  [meat plant]
  {:meat meat
   :plant plant})

(map unify-data meat-consumption plant-consumption)
;; => ({:meat 8.1, :plant 0.0} {:meat 7.3, :plant 0.2} {:meat 6.6, :plant 0.3} {:meat 5.0, :plant 1.1})

;; pass a collection of functions
(def sum #(reduce + %))
(def avg #(/ (sum %) (count %)))
(defn stats
  [numbers]
  (map #(% numbers) [sum count avg]))

(stats [3 4 10])
;; => (17 3 17/3)

(stats [80 1 44 13 6])
;; => (144 5 144/5)

;; using keyword functions
(def identities
  [{:alias "Batman" :real "Bruce Wayne"}
   {:alias "Spider-Man" :real "Peter Parker"}
   {:alias "Santa" :real "Your mom"}
   {:alias "Easter Bunny" :real "Your dad"}])

(map :real identities)
;; => ("Bruce Wayne" "Peter Parker" "Your mom" "Your dad")


;; `reduce`

(reduce #(+ %1 %2 ) [1 2 3 4])
;; => 10
;; better
(reduce + [1 2 3 4])
;; => 10

;; transforming a map
(reduce (fn [acc [key val]]
          (assoc acc key (inc val)))
        {}  ;; acc
        {:max 30 :min 10 :diff 14})
;; => {:max 31, :min 11, :diff 15}

;; use as a filter.
(reduce (fn [acc [key val]]
          (if (> val 4)
            (assoc acc key val)
            acc))
        {}  ;; acc
        {:meat 4.1
         :plant 3.9})
;; => {:meat 4.1}


;;; `take` `drop` `take-while` `drop-while`

(take 3 [1 2 3 4 5 6 7 8 9 10])
;; => (1 2 3)

(drop 3 [1 2 3 4 5 6 7 8 9 10])
;; => (4 5 6 7 8 9 10)


;; `take-while`  `drop-while`

(def food-journal
  [{:month 1 :day 1 :meat 5.3 :plant 2.3}
   {:month 1 :day 2 :meat 5.1 :plant 2.0}
   {:month 2 :day 1 :meat 4.9 :plant 2.1}
   {:month 2 :day 2 :meat 5.0 :plant 2.5}
   {:month 3 :day 1 :meat 4.2 :plant 3.3}
   {:month 3 :day 2 :meat 4.0 :plant 3.8}
   {:month 4 :day 1 :meat 3.7 :plant 3.9}
   {:month 4 :day 2 :meat 3.7 :plant 3.6}])


;; January and February data
(take-while #(< (:month %) 3) food-journal)
;; '({:month 1, :day 1, :meat 5.3, :plant 2.3}
;;   {:month 1, :day 2, :meat 5.1, :plant 2.0}
;;   {:month 2, :day 1, :meat 4.9, :plant 2.1}
;;   {:month 2, :day 2, :meat 5.0, :plant 2.5})

;; otherwise
(drop-while #(< (:month %) 3) food-journal)
;; '({:month 3, :day 1, :meat 4.2, :plant 3.3}
;;   {:month 3, :day 2, :meat 4.0, :plant 3.8}
;;   {:month 4, :day 1, :meat 3.7, :plant 3.9}
;;   {:month 4, :day 2, :meat 3.7, "plant" 3.6})

;; February and March
(take-while #(< (:month %) 4)
            (drop-while #(< (:month %) 2) food-journal))
;; => ({:month 2, :day 1, :meat 4.9, :plant 2.1}
;;     {:month 2, :day 2, :meat 5.0, :plant 2.5}
;;     {:month 3, :day 1, :meat 4.2, :plant 3.3}
;;     {:month 3, :day 2, :meat 4.0, :plant 3.8})

;; `filter` `some`
;; meat consumption less than 5
(filter #(< (:meat %) 5) food-journal)
;; => ({:month 2, :day 1, :meat 4.9, :plant 2.1}
;;     {:month 3, :day 1, :meat 4.2, :plant 3.3}
;;     {:month 3, :day 2, :meat 4.0, :plant 3.8}
;;     {:month 4, :day 1, :meat 3.7, :plant 3.9}
;;     {:month 4, :day 2, :meat 3.7, :plant 3.6})

(filter #(< (:month %) 3) food-journal)
;; => ({:month 1, :day 1, :meat 5.3, :plant 2.3}
;;     {:month 1, :day 2, :meat 5.1, :plant 2.0}
;;     {:month 2, :day 1, :meat 4.9, :plant 2.1}
;;     {:month 2, :day 2, :meat 5.0, :plant 2.5})

;; take-while drop-while is better on sorted data

;; first logical true value
(some #(> (:plant %) 5) food-journal)
;; => nil
(some #(> (:plant %) 3) food-journal)
;; => true

(some #(and (> (:plant %) 3) %) food-journal)
;; => {:month 3, :day 1, :meat 4.2, :plant 3.3}


;; `sort`  `sort-by`
(sort [3 1 2])
;; => (1 2 3)

(sort-by count ["aaa" "c" "bb"])
;; => ("c" "bb" "aaa")

;; `concat`
(concat [ 1 2] [3 4])
;; => (1 2 3 4)

;;; Exercises.

;; map, filter in terms of reduce

(defn my-map
  [f coll]
  (reverse (reduce #(cons (f %2) %1) (seq '()) coll)))

(defn my-map
  [f coll]
  (reverse (reduce #(conj %1 (f %2)) (seq '()) coll)))

(my-map inc [1 2 3])

(defn my-filter
  [p coll]
  (reverse (reduce #(when (p %2) (cons %2 %1)) (seq '()) coll)))

(my-filter #(> % 3) [0 1 2 3 4 5 6])
;; => (4 5 6)

;;; Lazy Seqs

;; Efficiency

(def vampire-database
  {0 {:makes-blood-puns? false, :has-pulse? true  :name "McFishwich"}
   1 {:makes-blood-puns? false, :has-pulse? true  :name "McMackson"}
   2 {:makes-blood-puns? true,  :has-pulse? false :name "Damon Salvatore"}
   3 {:makes-blood-puns? true,  :has-pulse? true  :name "Mickey Mouse"}})

(defn vampire-related-details
  [social-security-number]
  (Thread/sleep 1000)
  (get vampire-database social-security-number))

(defn vampire?
  [record]
  (and (:makes-blood-puns? record)
       (not (:has-pulse? record))
       record))

(defn identify-vampire
  [social-security-numbers]
  (first (filter vampire?
                 (map vampire-related-details social-security-numbers))))

(time (vampire-related-details 0))
;; "Elapsed time: 1001.1243 msecs"
;; => {:makes-blood-puns? false, :has-pulse? true, :name "McFishwich"}

(time (def mapped-details (map vampire-related-details (range 0 1000000))))
;; "Elapsed time: 0.1214 msecs"
;; => #'workshop.core-functions/mapped-details

(time (first mapped-details))
;; "Elapsed time: 32154.1104 msecs"
;; => {:makes-blood-puns? false, :has-pulse? true, :name "McFishwich"}

(time (first mapped-details))
;; "Elapsed time: 0.1016 msecs"
;; => {:makes-blood-puns? false, :has-pulse? true, :name "McFishwich"}

(time (identify-vampire (range 0 1000000)))
;; "Elapsed time: 32154.3519 msecs"
;; => {:makes-blood-puns? true, :has-pulse? false, :name "Damon Salvatore"}

;;; Infinite Sequences
(concat (take 8 (repeat "na")) ["Batman!"])
;; => ("na" "na" "na" "na" "na" "na" "na" "na" "Batman!")

(take 3 (repeatedly (fn [] (rand-int 10))))
;; => (9 6 3)

;; construct our own
(defn even-numbers
  ([] (even-numbers 0))
  ([n] (cons n (lazy-seq (even-numbers (+ n 2))))))

(take 10 (even-numbers))
;; => (0 2 4 6 8 10 12 14 16 18)
