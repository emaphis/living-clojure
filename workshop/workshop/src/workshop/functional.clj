(ns workshop.functional)

(use 'clojure.repl)

;; Clojure has no assignment.
;; local variables - a new scope.

(def great-name "Tommy")
great-name
;; => "Tommy"

(let [great-name "Billy"]
  great-name)
;; => "Billy"

great-name
;; => "Tommy"

;; recursion works around lack of assignment

(defn sum
  ([vals] (sum vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (sum (rest vals) (+ (first vals) accumulating-total)))))

(sum [39 5 1])
;; => 45
(sum [39 5 1] 0)
;; => 45
(sum [39 5] 1)
;; => 45
(sum [1] 44)
;; => 45
(sum [] 45)
;; => 45

(defn sum'
  ([vals] (sum vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (recur (rest vals) (+ (first vals) accumulating-total)))))


;;; Functional Composition Instead of Attribute Mutation

(require '[clojure.string :as s])
(defn clean
  [text]
  (s/replace (s/trim text) #"lol" "LOL"))

(clean "My boa constructor is so sassy lol!")
;; => "My boa constructor is so sassy LOL!"


;;; Pure functions

;; `comp`

((comp inc *) 2 3)
;; => 7

;; retrieving elements from a game data structure.

(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strenght 4
                :dextarety 5}})

(def c-int (comp :intelligence :attributes))
(def c-str (comp :strenght :attributes))
(def c-dex (comp :dextarety :attributes))

(c-int character)
;; => 10
(c-str character)
;; => 4
(c-dex character)
;; => 5

;; instead of `comp`
(fn [c] (:strenght (:attributes c)))

(defn spell-slots
  [char]
  (int (inc (/ (c-int char) 2))))

(spell-slots character)
;; => 6

;; if a function takes more than one parameter
(def spell-slots-comp (comp int inc #(/ % 2) c-int))

(spell-slots-comp character)
;; => 6

;; compose two functions
(defn two-comp
  [f g]
  (fn [& args]
    (f (apply g args))))

((two-comp - +) 1 2 3)
;; => -6

;; had to look up %&
(defn my-comp
  [& funs]
  (reduce (fn [f g]
            #(f (apply g %&))) funs))

((my-comp inc inc inc) 1)
;; => 4

;;; memoize

(defn sleepy-identity
  "Returns the given value after 1 second"
  [x]
  (Thread/sleep 1000)
  x)
(sleepy-identity "Mr. Fantasico")

;; memoized version
(def memo-sleepy-identity (memoize sleepy-identity))
(memo-sleepy-identity "Mr. Fantasico")





