(ns wonderland.sequences
  (:require [clojure.repl :as r]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Everything is a sequence

;; (first aseq)

;; (rest aseq)

;; (cons elem aseq)

;; clojure.lang.ISeq

;; (seq coll) ;; return a seq or nil if empty

;; (next aseq) => (seq (rest aseq))

;; clarify rest/next behavoir
;; (rest ())        => ()
;; (next ())        => nil
;; (seq (rest ()))  => nil


;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Lists

(first '(1 2 3))
;; => 1

(rest '(1 2 3))
;; => (2 3)

(cons 0 '(1 2 3))
;; => (0 1 2 3)


;;; Other data structures too.

(first [1 2 3])
;; => 1

(rest [1 2 3])
;; => (2 3)

(cons 0 [1 2 3])  ;; expensive
;; => (0 1 2 3)

(class [1 2 3])
;; => clojure.lang.PersistentVector

(class (rest [1 2 3]))
;; => clojure.lang.PersistentVector$ChunkedSeq


;;;;;;;;;;;;;;;;;;;;;
;;; Maps and Sets

(first {:fname "Aaron" :lname "Bedra"})
;; => [:fname "Aaron"]

(rest {:fname "Aaron" :lname "Bedra"})
;; => ([:lname "Bedra"])

(cons [:mname  "James"] {:fname "Aaron" :lname "Bedra"})
;; => ([:mname "James"] [:fname "Aaron"] [:lname "Bedra"])

(first #{:the :quick :brown :fox})
;; => :fox

(rest #{:the :quick :brown :fox})
;; => (:the :quick :brown)

(cons :jumped #{:the :quick :brown :fox})
;; => (:jumped :fox :the :quick :brown)

;; elements don't come back in the order entered
#{:the :quick :brown :fox}
;; => #{:fox :the :quick :brown}

(sorted-set :the :quick :brown :fox)
;; => #{:brown :fox :quick :the}

{:a 1 :b 2 :c 3 :d 4 :e 5}
;; => {:a 1, :b 2, :c 3, :d 4, :e 5}


;; 'conj' and 'into' add elements to a list in natural order.
(conj '(1 2 3) :a)
;; => (:a 1 2 3)

(into '(1 2 3) '(:a :b :c))
;; => (:c :b :a 1 2 3) ; like a stack

(conj [1 2 3] :a)
;; => [1 2 3 :a]

(into [1 2 3] [:a :b :c])
;; => [1 2 3 :a :b :c]


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; The sequence library

;;; Creating sequences

;; (range start? end step?)

(range 10)
;; => (0 1 2 3 4 5 6 7 8 9)

(range 10 20)
;; => (10 11 12 13 14 15 16 17 18 19)

(range 1 25 2)
;; => (1 3 5 7 9 11 13 15 17 19 21 23)


;; (repeat n x) - repeat x n times

(repeat 5 1)
;; => (1 1 1 1 1)

(repeat 10 "x")
;; => ("x" "x" "x" "x" "x" "x" "x" "x" "x" "x")

;; (iterate f x) - applies function 'f' to x then to it's output.
;; (take n sequence) - takes the first 'n' items of a given sequence

(take 10 (iterate inc 1))
;; => (1 2 3 4 5 6 7 8 9 10)

(defn whole-numbers [] (iterate inc 1))

(take 12 (whole-numbers))
;; => (1 2 3 4 5 6 7 8 9 10 11 12)

;; (repeat n) - returns a lazy infinite sequence

(take 20 (repeat 1))
;; => (1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1)

;; (cycle coll) - cycles a collection infinitely

(take 10 (cycle (range 3)))
;; => (0 1 2 0 1 2 0 1 2 0)

;; (interleave & colls) - interleave values from given collections

(interleave (whole-numbers) ["A" "B" "C" "D" "E" "F"])
;; => (1 "A" 2 "B" 3 "C" 4 "D" 5 "E" 6 "F")

;; (interpose separator coll) - returns a coll with each element segregated by a separator
(interpose "," ["apples" "bananas" "grapes"])
;; => ("apples" "," "bananas" "," "grapes")

(apply str (interpose \, ["apples" "bananas" "grapes"]))
;; => "apples,bananas,grapes"


(use '[clojure.string :only (join)])

(join \, ["apples" "bananas" "grapes"])
;; => "apples,bananas,grapes"

;; take an arbitrary number of arguments and create a collection

;; (list & elements)
;; (vector & elements)
;; (hash-set & elements)
;; (hash-map & elements)
(set [1 2 3])
;; => #{1 3 2}

(hash-set 1 2 3)
;; => #{1 3 2}

(vec (range 3))
;; => [0 1 2]

(vector 0 1 2 3)
;; => [0 1 2 3]


;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Filtering sequences

;; (filter pred coll)

(take 10 (filter even? (whole-numbers)))
;; => (2 4 6 8 10 12 14 16 18 20)


(take 10 (filter odd? (whole-numbers)))
;; => (1 3 5 7 9 11 13 15 17 19)

;; (take-while pred coll) - form a sequence while pred is true

(take-while (complement #{\a\e\i\o\u}) "the-quick-brown-fox")
;; => (\t \h)

;; (drop-while pred coll) - drops while true

(drop-while (complement #{\a\e\i\o\u}) "the-quick-brown-fox")
;; => (\e \- \q \u \i \c \k \- \b \r \o \w \n \- \f \o \x)


;; (split-at index coll)
;; (split-with pred coll)

(split-at 5 (range 10))
;; => [(0 1 2 3 4) (5 6 7 8 9)]

(split-with #(<= % 10) (range 0 20 2))
;; => [(0 2 4 6 8 10) (12 14 16 18)]


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Sequence predicates

;; (every pred coll) - asks pred of every element

(every? odd? [1 3 5])
;; => true

(every? odd? [1 3 5 8])
;; => false

;; (some pred coll) - returns first non false value for it's predicate

(some even? [1 2 3])
;; => true

(some even? [1 3 5])
;; => nil

(some identity [nil false 1 nil 2])
;; => 1

;; (not-every? pred coll)
;; (not-any? pred coll)

(not-every? even? [1 2 3 4 5])
;; => true

(not-every? even? [2 4 6 8])
;; => false

(not-every? even? [2 3 6 8])
;; => true

(not-any? even? [1 2 3 4 5 6])
;; => false


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Transforming sequences

;; (map f coll)

(map #(+ 2 %) [1 2 3 4 5])
;; => (3 4 5 6 7)

(map #(format "<%s>%s</%s>" %1 %2 %1)
     ["h1" "h2" "h3" "h1"] ["the" "quick" "brown" "fox"])
;; => ("<h1>the</h1>" "<h2>quick</h2>" "<h3>brown</h3>" "<h1>fox</h1>")


;; (reduce f coll)

(reduce + (range 1 11))
;; => 55

(reduce * (range 1 11))
;; => 3628800


;; (sort comp? coll)
;; (sort a-fn comp? coll)

(sort [42 1 7 11])
;; => (1 7 11 42)

(sort-by #(.toString %) [42 1 7 11]) ; lexical
;; => (1 11 42 7)

(sort > [42 1 7 11])
;; => (42 11 7 1)

(sort-by :grade > [{:grade 83} {:grade 90} {:grade 77}])
;; => ({:grade 90} {:grade 83} {:grade 77})


;; for comprehension

(for [word ["the" "quick" "brown" "fox"]]
  (format "<p>%s</p>" word))
;; => ("<p>the</p>" "<p>quick</p>" "<p>brown</p>" "<p>fox</p>")

(take 10 (for [n (whole-numbers) :when (even? n)] n))
;; => (2 4 6 8 10 12 14 16 18 20)

(for [n (whole-numbers) :while (even? n)] n)
;; => ()

(for [file "ABCDEFGH" rank (range 1 9)] (format "%c%d" file rank))
;; => ("A1" "A2" "A3" "A4" "A5" "A6" "A7" "A8" "B1" "B2" "B3" "B4" "B5" "B6" "B7" "B8" "C1" "C2" "C3" "C4" "C5" "C6" "C7" "C8" "D1" "D2" "D3" "D4" "D5" "D6" "D7" "D8" "E1" "E2" "E3" "E4" "E5" "E6" "E7" "E8" "F1" "F2" "F3" "F4" "F5" "F6" "F7" "F8" "G1" "G2" "G3" "G4" "G5" "G6" "G7" "G8" "H1" "H2" "H3" "H4" "H5" "H6" "H7" "H8")

(for [rank (range 1 9) file "ABCDEFGH"] (format "%c%d" file rank))
;; => ("A1" "B1" "C1" "D1" "E1" "F1" "G1" "H1" "A2" "B2" "C2" "D2" "E2" "F2" "G2" "H2" "A3" "B3" "C3" "D3" "E3" "F3" "G3" "H3" "A4" "B4" "C4" "D4" "E4" "F4" "G4" "H4" "A5" "B5" "C5" "D5" "E5" "F5" "G5" "H5" "A6" "B6" "C6" "D6" "E6" "F6" "G6" "H6" "A7" "B7" "C7" "D7" "E7" "F7" "G7" "H7" "A8" "B8" "C8" "D8" "E8" "F8" "G8" "H8")


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Lazy and Infinite Sequences

;; see primes.clj

(use 'wonderland.primes)

(def ordinals-and-primes (map vector (iterate inc 1) primes))

(take 5 (drop 1000 ordinals-and-primes))
;; => ([1001 7927] [1002 7933] [1003 7937] [1004 7949] [1005 7951])


;;; forcing sequences

(def x (for [i (range 1 3)] (do (println i) i)))
;; => #'wonderland.sequences/x

(doall x)
;; 1
;; 2
;; => (1 2)

(def x (for [i (range 1 3)] (do (println i) i)))

(dorun x)
;; 1
;; 2
;; => nil


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Clojure make Java seq-able

;; Collections
;; Regular expressions
;; File system traveral
;; XML processing
;; Relational database results


;;; Seq-ing Java collections

;; String.getBytes returns a byte array

(first (.getBytes "hello"))
;; => 104

(rest (.getBytes "hello"))
;; => (101 108 108 111)

;; Hashtables and Maps are also seq-able

(first (System/getProperties)) ; System.getProperties returns a Hashtables
;; => #object[java.util.concurrent.ConcurrentHashMap$MapEntry 0x167ebc5d "sun.desktop=windows"]

(count (System/getProperties))
;; => 57

;; Strings are seq-able
(first "Hello") ;; => \H

(rest "Hello") ;; => (\e \l \l \o)

(cons \H "ello") ;; => (\H \e \l \l \o)

(reverse "hello")
;; => (\o \l \l \e \h)

(apply str (reverse "hello"))
;; => "olleh"


;; Seq-ing regular expressions

;; don't do this!
(let [m (re-matcher #"\w+" "the quick brown fox")]
  (loop [match (re-find m)]
    (when match
      (println match)
      (recur (re-find m)))))
;; the
;; quick
;; brown
;; fox

;; use re-seq

(re-seq #"\w+" "the quick brown fox")
;; => ("the" "quick" "brown" "fox")

(sort (re-seq #"\w+" "the quick brown fox"))
;; => ("brown" "fox" "quick" "the")

(drop 2 (re-seq #"\w+" "the quick brown fox"))
;; => ("brown" "fox")

(map #(.toUpperCase %) (re-seq #"\w+" "the quick brown fox"))
;; => ("THE" "QUICK" "BROWN" "FOX")

;; Re-seqing the File System

(import '(java.io File))
;; => java.io.File

(.listFiles (File. "."))  ; .toString representation
;; => #object["[Ljava.io.File;" 0x673e5cac "[Ljava.io.File;@673e5cac"]

(seq (.listFiles (File. ".")))
;; => (#object[java.io.File 0x416ae6c6 ".\\.gitignore"] #object[java.io.File 0x73a6d5bd ...

;; using .getName - over kill
(map #(.getName %) (.listFiles (File. ".")))
;; => (".gitignore" ".lein-repl-history" ".nrepl-port" "CHANGELOG.md" "doc" "LICENSE" "project.clj" "README.md" "src" "target" "test")

;; but map already calls 'seq'
(map #(.getName %) (.listFiles (File. ".")))
;; => (".gitignore" ".lein-repl-history" ".nrepl-port" "CHANGELOG.md" "doc" "LICENSE" "project.clj" "README.md" "src" "target" "test")

(count (file-seq (File. ".")))
;; => 35

;;; check to see if any files changed recently

(defn minutes-to-millis [mins] (* mins 1000 60))

(defn recently-modified? [file]
  (> (.lastModified file)
     (- (System/currentTimeMillis) (minutes-to-millis 30))))

(filter recently-modified? (file-seq (File. ".")))
;; => (#object[java.io.File 0x650c935e ".\\src\\wonderland"] #object[java.io.File 0x5eedd1fb ".\\src\\wonderland\\sequences.clj"])


;;; Seq-ing a Stream

(use '[clojure.java.io :only (reader)])

(take 3 (line-seq (reader "src/wonderland/primes.clj")))
;; => ("(ns wonderland.primes)" "" ";; Taken from clojure.contrib.lazy-seqs")

;; using with-open

(with-open [rdr (reader "src/wonderland/primes.clj")]
  (count (line-seq rdr)))
;; => 22

;;; program
(use '[clojure.java.io :only (reader)])

(defn non-blank? [line] (if (re-find #"\S" line) true false))

(defn non-svn? [file] (not (.contains (.toString file) ".svn")))

(defn clojure-source? [file] (.endsWith (.toString file) ".clj"))

(defn clojure-loc [base-file]
  (reduce
   +
   (for [file (file-seq base-file)
         :when (and (clojure-source? file) (non-svn? file))]
     (with-open [rdr (reader file)]
       (count (filter non-blank? (line-seq rdr)))))))

(clojure-loc (java.io.File. "c:/src/Clojure"))
;; => 8638


;;; Seq-ing XML

(use '[clojure.xml :only (parse)])

(parse (java.io.File. "resource/compositions.xml"))

;; {:tag :compositions, :attrs nil, :content
;;  [{:tag :composition, :attrs {:composer "J. S. Bach"},
;;    :content [{:tag :name, :attrs nil, :content ["The Art of the Fugue"]}]}
;;   {:tag :composition, :attrs {:composer "F. Chopin"},
;;    :content [{:tag :name, :attrs nil, :content ["Fantaisie-Impromptu Op. 66"]}]}
;;   {:tag :composition, :attrs {:composer "W. A. Mozart"},
;;    :content [{:tag :name, :attrs nil, :content ["Requiem"]}]}]}

;; extracts just the composers

(for [x (xml-seq
         (parse (java.io.File. "resource/compositions.xml")))
      :when (= :composition (:tag x))]
  (:composer (:attrs x)))
;; => ("J. S. Bach" "F. Chopin" "W. A. Mozart")


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Calling structure specific functions

;;; Functions on Lists

;; stack functions
;; (peek col)
;; (pop col)

(peek '(1 2 3)) ; same a first
;; => 1

(pop '(1 2 3))  ; different then rest, throws an exception if empty 
;; => (2 3)


;;; Functions on Vectors

(peek [1 2 3])
;; => 3

(pop [1 2 3])
;; => [1 2]

;; get returns an indexed value
(get [:a :b :c] 1)
;; => :b

(get [:a :b :c] 5)
;; => nil


([:a :b :c] 1)
;; => :b

;; ([:a :b :c] 5)
;; Unhandled java.lang.IndexOutOfBoundsException

;; 'assoc' associated a new value with a particular index
(assoc [0 1 2 3 4] 2 :two)
;; => [0 1 :two 3 4]

;; (subvec avec start end?) ; returns a subvector

(subvec [1 2 3 4 5] 3)
;; => [4 5]

(subvec [1 2 3 4 5] 1 3)
;; => [2 3]

;; equivalent but more general and slower on vecs.
(take 2 (drop 1 [1 2 3 4 5]))
;; => (2 3)


;;; Functions on Maps

;; (keys map)
;; (vals map)


(keys {:sundance "spaniel", :darwin "beagle"})
;; => (:sundance :darwin)

(vals {:sundance "spaniel", :darwin "beagle"})
;; => ("spaniel" "beagle")


;; (get map key value-if-not-found?)

(get {:sundance "spaniel", :darwin "beagle"} :darwin)
;; => "beagle"

(get {:sundance "spaniel", :darwin "beagle"} :snoopy)
;; => nil

;; maps are functions on keys
({:sundance "spaniel", :darwin "beagle"} :darwin)
;; => "beagle"

({:sundance "spaniel", :darwin "beagle"} :snoopy)
;; => nil

;; keys are also functions
(:darwin {:sundance "spaniel", :darwin "beagle"})
;; => "beagle"

;; (contains? map key) ; check if key exists in map

(def score {:stu nil :joey 100})

(:stu score)
;; => nil

(contains? score :stu)
;; => true

;; or use get
(get score :stu :score-not-found)
;; => nil

(get score :aaron :score-not-found)
;; => :score-not-found

;; functions for building new maps:
;; assoc - new map with added pair
;; dissoc - new map with a pair removed
;; select-keys - new map keeping only pairs with keys
;; merge - combines maps

(def song {:name "Angus Dei"
           :artist "Krzystof Penderecki"
           :album "Polish Reqiem"
           :genre "Classical"})

(assoc song :kind "MPEG Audio File")
;; => {:name "Angus Dei", :artist "Krzystof Penderecki", :album "Polish Reqiem", :genre "Classical", :kind "MPEG Audio File"}

(dissoc song :genre)
;; => {:name "Angus Dei", :artist "Krzystof Penderecki", :album "Polish Reqiem"}

(select-keys song [:name :artist])
;; => {:name "Angus Dei", :artist "Krzystof Penderecki"}

(merge song {:size 8118166 :time 507245})
;; => {:name "Angus Dei", :artist "Krzystof Penderecki", :album "Polish Reqiem", :genre "Classical", :size 8118166, :time 507245}

;; (merge-with merge-fn & maps)

(merge-with
 concat
 {:rubble ["Barney"], :flintstone ["Fred"]}
 {:rubble ["Betty"], :flintstone ["Wilma"]}
 {:rubble ["Bam-Bam"], :flintstone ["Pebbles"]})
;; => {:rubble ("Barney" "Betty" "Bam-Bam"), :flintstone ("Fred" "Wilma" "Pebbles")}


;;; Functions on Sets

;; union - returns the set of all elements present in either input set.
;; intersection - returns the set of all elements present in both sets.
;; difference - returns the et of all elements present in the first input set minus those in the second
;; select - returns the set of all elements matching a predicate

(require '[clojure.set :as set])

(def languages #{"java" "c" "d" "clojure"})
(def beverages #{"java" "chai" "pop"})

(set/union languages beverages)
;; => #{"d" "clojure" "pop" "java" "chai" "c"}

;; languages that are not beverages
(set/difference languages beverages)
;; => #{"d" "clojure" "c"}

;; both languages and beverages
(set/intersection languages beverages)
;; => #{"java"}

(set/select #(= 1 (.length %)) languages)
;; => #{"d" "c"}

;; relation between relational algebra
;;  Algerbra   SQL     Clojure
;;  Relation   Table   set-like
;;  Tuple      Row     map-like

(def compositions
  #{{:name "The Art of the Fugue" :composer "J. S. Bach"}
    {:name "Musical Offering" :composer "J. S. Bach"}
    {:name "Requiem" :composer "Giuseppe Verdi"}
    {:name "Requiem" :composer "W. A. Mozart"}})

(def composers
  #{{:composer "J. S. Bach" :country "Germany"}
    {:composer "W. A. Mozart" :country "Austria"}
    {:composer "Giuseppe Verdi" :country "Italy"}})

(def nations
  #{{:nation "Germany" :language "German"}
    {:nation "Austria" :language "German"}
    {:nation "Italy" :language "Italian"}})


;; (rename relation rename-map)

(set/rename compositions {:name :title})
;; => #{{:composer "Giuseppe Verdi", :title "Requiem"} {:composer "W. A. Mozart", :title "Requiem"} {:composer "J. S. Bach", :title "The Art of the Fugue"} {:composer "J. S. Bach", :title "Musical Offering"}}

;; (select pred relation) - returns maps for which a predicate is true - analogous to WHERE
(set/select #(= (:name %) "Requiem") compositions)
;; => #{{:name "Requiem", :composer "Giuseppe Verdi"} {:name "Requiem", :composer "W. A. Mozart"}}

;; (project relation keys) - SELECT that specifies a subset of columns
(set/project compositions [:name])
;; => #{{:name "The Art of the Fugue"} {:name "Musical Offering"} {:name "Requiem"}}

;; cross product

(for [m compositions c composers] (concat m c))
;; (([:name "Musical Offering"] [:composer "J. S. Bach"]  [:composer "Giuseppe Verdi"] [:country "Italy"])
;;  ([:name "Musical Offering"] [:composer "J. S. Bach"] [:composer "J. S. Bach"] [:country "Germany"])
;;  ([:name "Musical Offering"] [:composer "J. S. Bach"] [:composer "W. A. Mozart"] [:country "Austria"])
;;  ([:name "The Art of the Fugue"] [:composer "J. S. Bach"] [:composer "Giuseppe Verdi"] [:country "Italy"])
;;  ([:name "The Art of the Fugue"] [:composer "J. S. Bach"] [:composer "J. S. Bach"] [:country "Germany"])
;;  ([:name "The Art of the Fugue"] [:composer "J. S. Bach"] [:composer "W. A. Mozart"] [:country "Austria"])
;;  ([:name "Requiem"] [:composer "Giuseppe Verdi"] [:composer "Giuseppe Verdi"] [:country "Italy"])
;;  ([:name "Requiem"] [:composer "Giuseppe Verdi"] [:composer "J. S. Bach"] [:country "Germany"])
;;  ([:name "Requiem"] [:composer "Giuseppe Verdi"] [:composer "W. A. Mozart"] [:country "Austria"])
;;  ([:name "Requiem"] [:composer "W. A. Mozart"] [:composer "Giuseppe Verdi"] [:country "Italy"])
;;  ([:name "Requiem"] [:composer "W. A. Mozart"] [:composer "J. S. Bach"] [:country "Germany"])
;;  ([:name "Requiem"] [:composer "W. A. Mozart"] [:composer "W. A. Mozart"] [:country "Austria"]))

(set/join compositions composers)
;; #{{:composer "W. A. Mozart", :country "Austria", :name "Requiem"}
;;   {:composer "J. S. Bach", :country "Germany", :name "Musical Offering"}
;;   {:composer "Giuseppe Verdi", :country "Italy", :name "Requiem"}
;;   {:composer "J. S. Bach", :country "Germany", :name "The Art of the Fugue"}}

(set/join composers nations {:country :nation})
;; #{{:composer "W. A. Mozart", :country "Austria", :nation "Austria", :language "German"}
;;   {:composer "J. S. Bach", :country "Germany", :nation "Germany", :language "German"}
;;   {:composer "Giuseppe Verdi", :country "Italy", :nation "Italy", :language "Italian"}}


(set/project
 (set/join
  (set/select #(= (:name %) "Requiem") compositions)
  composers)
 [:country])
;; => #{{:country "Italy"} {:country "Austria"}}
