(ns myapp.ch05)

;;;; Functions

;;; Anonymous function shorthand

(map #(+ (+ 2 %1) 2) [1 2 3])
;; => (5 6 7)

;;; Pre and p conditions

(defn my-sum [f g]
  {:pre  [(integer? f) (integer? g)]
   :post [(integer? %)]}
  (+ f g))

;;(my-sum "2" 2)

;; modified pre/post condition logic
(defn my-sum [f g]
  {:pre  [(integer? f) (integer? g)]
   :post [(string? %)]}
  (str "Result: " (+ f g)))

(my-sum 2 2)


;;; `clojure.core`

;; map construction

;; convert vector into a map using `apply` and `assoc`
(apply assoc {} [:foo 1 :bar 2])
;; => {:foo 1, :bar 2}

(array-map :foo 1 :bar 2)
;; => {:foo 1, :bar 2}

;; convert list/vector into a map using `apply` and `array-map`.
(apply array-map '(:foo 1 :bar 2))
;; => {:foo 1, :bar 2}
(apply array-map [:foo 1 :bar 2])
;; => {:foo 1, :bar 2}

;; Pipelining

(let [foo 1]
  (inc foo)
  (println foo))
;; => 1

;; `as->` macro to avoid immutable bindings
(as-> 1 foo
  (inc foo)
  (println foo))
;; 2

;; Using `as->` macro for granular pipeline processing
(-> [9 8 7]
    (as-> coll
        (map - coll [3 2 1])
      (apply str coll)
      (str coll " is the number of the beast!"))
    (clojure.string/upper-case))
;; => "666 IS THE NUMBER OF THE BEAST!"

;; just using the `->` macro

#_(-> [9 8 7]
      (map - [3 2 1])
      (apply str)
      (str " is the number the beast!")
      (clojure.string/upper-case))

;; thread-first and thread-last macros

#_(-> [9 8 7]
      (map - [3 2 1])
      (->> (apply str))
      (str " is the number the beast!")
      (clojure.string/upper-case))

(-> [9 8 7]
    (as-> coll
        (map - coll [3 2 1]))
    (->> (apply str))
    (str " is the number the beast!")
    (clojure.string/upper-case))

;; dropping values
(drop-last [1 2 3])
;; => (1 2)
(butlast [1 2 3])
;; => (1 2)


;;; Commenting out code.

;; Increment an atom value twice
(let [x (atom 0)]
  (swap! x inc)
  (swap! x inc))
;; => 2

(let [x (atom 0)]
  (comment (swap! x inc))
  (swap! x inc))
;; => 1

(let [x (atom 0)]
  #_(swap! x inc)
  (swap! x inc))
;; => 1

;; Endless cycle - `cycle`
(take 2 (cycle [1 2 3]))
;; => (1 2)
(take 8 (cycle [1 2 3]))
;; => (1 2 3 1 2 3 1 2)

;; Uniqueness - `distinct`
(distinct [1 2 3 1 2 3 4])
;; => (1 2 3 4)

;; Remove consecutive duplicates - `dedupe`
(dedupe [1 1 1 2 3 3 1 1 2 2 2 3 3])
;; => (1 2 3 1 2 3)

;;; Predicate consuming functions

;; `every?`
(every? even? [2 4 6 8])
;; => true
(every? even? [2 5 6 8])
;; => false

;; `every-pred`
((every-pred number? odd?) 1 2 3) ;; => false
((every-pred number? odd?) 1 3 5) ;; => true

;; `not-any?`
(not-any? odd? [2 4 6]) ;; => true
(not-any? odd? [1 3 5]) ;; => false
(not-any? odd? [2 3 6]) ;; => false

;; `some`
(some even? [1 3 5 7]) ;; => nil
(some even? [1 2 3 4]) ;; => true

(false? true) ;; => false
(false? false) ;; => true
(false? nil)  ;; => false


;;; Collection Extraction

(first [:a :b :c] );; => :a
(last [:a :b :c]) ;; => :c

;; extract the item from the first nested collection
(first [[1 2 3] :b :c]) 
;; => [1 2 3]
(ffirst [[1 2 3] :b :c])
;; => 1

(fnext [:a :b :c]) ;; => :b

(nnext [1 2 3 4 5 6]) ;; => (3 4 5 6)
(nfirst [[1 2 3] 4 5 6]) ;; => (2 3)


;; String formatting
;; example `format` function
(format "Hello %s, I hear you're %d years old",
        "Mark", 35)
;; => "Hello Mark, I hear you're 35 years old"

;; Frequency
(frequencies [:a :a :b :c :c :d :e :c])
;; => {:a 2, :b 1, :c 3, :d 1, :e 1}

;; Zipping values

(interleave [:a :b :c] [:x :y :z])
;; => (:a :x :b :y :c :z)

(interleave [:a :x] [:b :y] [:c :z] [1 :d] [:e 2] [3 :f] [:g 4] [:h :i])
;; => (:a :b :c 1 :e 3 :g :h :x :y :z :d 2 :f 4 :i)

;; odd number of items.
(interleave [:a :b :c] [:x :y])
;; => (:a :x :b :y)

(interleave [:a :b] [:x :y :z])
;; => (:a :x :b :y)

(zipmap [:a :b :c :d :e] [1 2 3 4 5])
;; => {:a 1, :b 2, :c 3, :d 4, :e 5}

;; Interposing values

(interpose " - " [:a :b :c])
;; => (:a " - " :b " - " :c)

(clojure.string/join ", " [:a :b :c])
;; => ":a, :b, :c"

;; Partitioning data

(partition 4 (range 20))
;; => ((0 1 2 3) (4 5 6 7) (8 9 10 11) (12 13 14 15) (16 17 18 19))

(partition 3 (range 20))
;; => ((0 1 2) (3 4 5) (6 7 8) (9 10 11) (12 13 14) (15 16 17))

(partition 3 1 [:a] (range 2))
;; => ((0 1 :a))

(partition-by odd? [1 1 1 2 2 3 3])
;; => ((1 1 1) (2 2) (3 3))

(partition-by even? [1 1 1 2 2 3 3])
;; => ((1 1 1) (2 2) (3 3))

;; Simple parallelization
(defn slow [n]
  (Thread/sleep 1000)
  (println "finished sleeping")
  (inc n))

(map slow [1 2 3 4 5])
;; => (2 3 4 5 6)

(pmap slow [1 2 3 4 5])

;; example of the `pvalues` function

(defn slow [n]
  (Thread/sleep (* n 1000))
  (println "finished sleeping for " n " seconds.")
  (inc n))

(pvalues (slow 5) (slow 5) (slow 4) (slow 4))

;; Repeating yourself
(take 5 (repeatedly #(rand-int 10)))
;; => (2 6 7 3 3)


;; Basic I/O

(spit "foo.txt" "abc")
(slurp "foo.txt") ;; => "abc"

(spit "foo.txt" "xyz" :append true)
(slurp "foo.txt") ;; => "abcxyz"

(spit "foo.txt" 123)
(slurp "foo.txt") ;; => "123"

(with-open [rdr (java.io.BufferedReader. (java.io.FileReader. "foo.txt"))]
  (let [lines (line-seq rdr)]
    (print lines)))


;;; `clojure.string`

;; Checking for whitespace
(clojure.string/blank? "") ;; => true
(clojure.string/blank? "  ") ;; => true
(clojure.string/blank? "  a") ;; => false
(clojure.string/blank? "123") ;; => false

;; Beginnings and endings
(clojure.string/starts-with? "I am a string" "g") ;; => false
(clojure.string/starts-with? "I am a string" "i") ;; => false
(clojure.string/starts-with? "I am a string" "I") ;; => true
(clojure.string/starts-with? "I am a string" "I am") ;; => true

(clojure.string/ends-with? "I am a string" "!") ;; => false
(clojure.string/ends-with? "I am a string" "G") ;; => false
(clojure.string/ends-with? "I am a string" "g") ;; => true
(clojure.string/ends-with? "I am a string" " string") ;; => true

(re-find #"(?i)^i" "I am a string") ;; => "I"

;; Trimming whitespace

(clojure.string/triml "  some space at the start")
;; => "some space at the start"
(clojure.string/trimr "some space at the end  ")
;; => "some space at the end"

(clojure.string/trim-newline "a newline\n\r")  ;; => "a newline"

(clojure.string/trim-newline "trim newline\nr last\n\r")
;; => "trim newline\nr last"
