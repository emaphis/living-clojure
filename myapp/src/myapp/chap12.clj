(ns myapp.chap12)

;;;; Destructuring


;;; Pry open your data

(def artists [:monet :austen])

;; using code
(let [painter (first artists)
      novelist (second artists)]
  (println "The painter is:" painter
           "and the novelist is:" novelist))

;; using destructuring
(let [[painter novelist] artists]
  (println "The painter is:" painter
           "and the novelist is:" novelist))

;; more complicated
(def artists [:monet :austen :beethoven :dickinson])

(let [[painter novelist composer poet] artists]
  (println "The painter is" painter)
  (println "The novelist is" novelist)
  (println "The composer is" composer)
  (println "The poet is" poet))


;;; Getting less than everything

;; skip poet
(let [[painter novelist composer] artists]
  (println "The painter is" painter)
  (println "The novelist is" novelist)
  (println "The composer is" composer))

(let [[dummy dummy composer poet] artists]
  (println "The composer is" composer)
  (println "The poet is" poet))

(let [[_ _ composer poet] artists]
  (println "The composer is" composer)
  (println "The poet is" poet))

;; multi-level destructuring

(def pairs [[:monet :austen] [:beethoven :dickinson]])

(let [[[painter] [composer]] pairs]
  (println "The painter is" painter)
  (println "The composer is" composer))

(let [[[painter] [_ poet]] pairs]
  (println "The painter is" painter)
  (println "The poet is" poet))


;;; Destructuring in sequence

(def artist-list '(:monet :austen :beethoven :dickinson))

(let [[painter novelist composer] artist-list]
  (println "The painter is" painter)
  (println "The novelist is" novelist)
  (println "The composer is" composer))

;; Strings
(let [[c1 c2 c3 c4] "Jane"]
  (println "How do you spell Jane?")
  (println c1)
  (println c2)
  (println c3)
  (println c4))


;;; Destructuring function arguments

(defn artist-description [[novelist poet]]
  (str "The Novelist is " novelist " and the poet is " poet))

(artist-description [:austen :dickinson])
;; => "The Novelist is :austen and the poet is :dickinson"

;; mixing elements:
(defn artist-description [shout [novelist poet]]
  (let [msg (str "Novelist is " novelist
                 "and the poet is " poet)]
    (if shout (.toUpperCase msg) msg)))

(artist-description false [:austen :dickinson])
;; => "Novelist is :austenand the poet is :dickinson"
(artist-description true [:austen :dickinson])
;; => "NOVELIST IS :AUSTENAND THE POET IS :DICKINSON"


;;; Digging into maps

(def artist-map {:painter :monet :novelist :austen})

(let [{painter :painter writer :novelist} artist-map]
  (println "The painter is" painter)
  (println "The novelist is" writer))

;; order isn't importand
(let [{writer :novelist painter :painter} artist-map]
  (println "The painter is" painter)
  (println "The novelist is" writer))


;;;  Diving into nested maps

(def austen {:name "Jane Austen"
             :parents {:father "George" :mother "Cassandra"}
             :dates {:born 1775 :died 1817}})

(let [{{dad :father mom :mother} :parents}  austen]
  (println "Jane Austens's dad's name was" dad)
  (println "Jane Austen's mon's name was" mom))

(let [{name :name
       {mom :mother} :parents
       {dob :born} :dates} austen]
  (println name "was born in" dob)
  (println name "'s mother's name was" mom))


;;; The final frontier: mixing and matching

(def author {:name "Jane Austen"
             :books [{:title "Sense and Sensibility" :published 1811}
                     {:title "Emma" :published 1815}]})

;; let's get name and the second book
(let [{name :name [_ book2] :books} author]
  (println "The author is" name)
  (println "Author's second book is" book2))

;; A couple of maps inside of a vector:
(def authors [{:name "Jane Austen" :born 1775}
              {:name "Charles Dickens" :born 1882}])

;; dates of birth
(let [[{dob-1 :born} {dob-2 :born}] authors]
  (println "One author was born in" dob-1)
  (println "The other author was born in" dob-2))


;;; Going further

;; if keys are the same name of variables
{:name "Romeo" :age 16 :gender :male}

;; awkward
(defn character-desc-1 [{name :name age :age gender :gender}]
  (str "Name: " name " age: " age " gender: " gender))

;; better
(defn character-desc-2 [{:keys [name age gender]}]
  (str "Name: " name " age: " age " gender: " gender))

;; mixed
(defn character-desc-3 [{:keys [name gender] age-in-years :age}]
  (str "Name: " name " age: " age-in-years " gender: " gender))

;; saving the whole structure
(defn add-greeting-1 [character]
  (let [{:keys [name age]} character]
    (assoc character
           :greeting
           (str "Hello, my name is " name " and I am " age "."))))

(add-greeting-1 {:name "Joan London" :age 38})
;; => {:name "Joan London", :age 38,
;;     :greeting "Hello, my name is Joan London and I am 38."}

;; using `:as`
(defn add-greeting-2 [{:keys [name age] :as character}]
  (assoc character
         :greeting
         (str "Hello, my name is " name " and I am " age ".")))

(add-greeting-2 {:name "Joan London" :age 38})
;; => {:name "Joan London", :age 38,
;;v    :greeting "Hello, my name is Joan London and I am 38."}
