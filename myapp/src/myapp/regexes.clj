(ns myapp.regexes
  (:require [clojure.repl :as r]))

;;; Practically Clojure regex
;;; https://www.youtube.com/watch?v=iTimmZcNToY
;;; https://purelyfunctional.tv/mini-guide/regexes-in-clojure/

;;; Literal representation
#"regex"

;; Dont do this..
(let [m (re-matcher #"\w+" "the quick brown fox")]
  (loop [match (re-find m)]
    (when match
      (println match)
      (recur (re-find m)))))


(re-seq #"\w+" "the quick brown fox")

(sort (re-seq #"\w+" "the quick brown fox"))

(drop 2 (re-seq #"\w+" "the quick brown fox"))

(map clojure.string/upper-case (re-seq #"\w+" "the quick brown fox"))


(re-seq #"[A-Z]+" "bA1B3Ce ")
;; => ("A" "B" "C")


(apply str (re-seq #"[A-Z]+" "bA1B3Ce "))
;; => "ABC"

(re-seq #"[a-z]+" "bA1B3Ce ")
;; => ("b" "e")

;; Reader syntax for regex.
#"cat"

(type #"cat")
;; => java.util.regex.Pattern

(re-find #"cat"  "I like cats")
;; => "cat"

(re-seq #"cat" "ggcat8cat catcatnCATyy ")
;; => ("cat" "cat" "cat" "cat")

(re-seq #"cat" "I prefer dogs")
;; => nil


;;; What are they used for?

;; Validating information
;; - phone numbers
;; - email addresses  - emaphis85@gmai.com
;; - passwords  (size, contents) - special charaters !@%?*

;; Seaching strings
;; - words in sentances
;; - incorrect or undesirable characters
;; - extracting sections of text
;; - sustituting text
;; - reformating / cleaning text


;;; Tools
;; https://regexr.com/
;; https://regex101.com/


;;; Clojure functions

;; clojure.core/re-find
;; Returns the next regex match.
;; java.util.regex.Matcher.fine().

;; clojure.core/re-seq
;; ([re s])
;; returns a lazy sequence of succesive matches in string.
;; java.util.regex.Matcher.find()

(def phone-no-gaps "0201231234")
(def phone-one-gap "020 1231234")
(def phone-two-gaps "020 123 1234")
(def phone-two-dash "020-123-1234")

(def not-london  "0178 1231234")

;; Literal matches
(re-find #"020" phone-no-gaps) ;; => "020"
(re-find #"020" phone-one-gap) ;; => "020"
(re-find #"020" not-london) ;; => nil

(def all-london-numbers [phone-no-gaps phone-one-gap phone-two-gaps phone-two-dash])

(map #(re-find #"020" %) all-london-numbers)
;; => ("020" "020" "020" "020")

(count (map #(re-find #"020" %) all-london-numbers))
;; => 4

(def all-numbers (conj all-london-numbers not-london))

(map #(re-find #"020" %) all-numbers)
;; => ("020" "020" "020" "020" nil)

;; using string/split

(clojure.string/split "020 123 1234" #"\s+")
;; => ["020" "123" "1234"]

;; clojure.core/subs
;; [s start]
;; [s start end]

(subs "0201231234" 0 3) ;; => "020"


;;; Getting just part of a string

;; If you want an initial of the first word
(re-find #"\w" "Jenny Jetpack") ;; => "J"

;; first three letters
(re-find #"\w\w\w" "Jenny Jetpack") ;; => "Jen"
(re-find #"\w\w\w" "1 2 3 Jenny Jetpack") ;; => "Jen"

;; or digits
(re-find #"\d" "1 2 3 Jenny Jetpack") ;; => "1"
(re-find #"\d \d \d" "1 2 3 Jenny Jetpack") ;; => "1 2 3"
(re-find #"\d\d\d" "123 Jenny Jetpack") ;; => "123"


;; several patterns - using OR
(re-find #"cat|kitten" "Sometimes a cat is called a pussycat")
;; => "cat"

(re-find #"cat|kitten" "My kitten has grown into a big cat")
;; => "kitten"

(re-find #"cat|kitten" "A young cat is called a kitten")
;; => "cat"

;; re-find finds the first match.


(re-seq #"cat|kitten" "Sometimes a cat is called a pussycat")
;; => ("cat" "cat")
(re-seq #"cat|kitten" "My kitten has grown into a big cat")
;; => ("kitten" "cat")
(re-seq #"cat|kitten" "A young cat is called a kitten")
;; => ("cat" "kitten")


;;; Checking login / registration forms text

;; Multiple login names

(defn validate-user
  [username]
  (if (re-find #"jenny|sally|rachel|michelle" username)
    true
    false))

(validate-user "jenny") ;; => true
(validate-user "brian") ;; => false
 

(defn validate-similar-usernames
  [username]
  (if (re-find #"jen+" username)
    true
    false))

(validate-similar-usernames "je") ;; => false
(validate-similar-usernames "jen") ;; => true
(validate-similar-usernames "jennifer") ;; => true

;;; Matching all characters of the alphabet

#"[a-z]"

(defn match-any-word
  [word]
  (if (re-find #"[a-z]" word) ; #"[a-zA-Z]"
    true
    false))

(match-any-word "Hello regex") ;; => true
(match-any-word "1234") ;; => false
(match-any-word "HELLO") ;; => false

(defn match-word-partial-alphabet
  [word]
  (if (re-find #"[a-c]" word)
    true
    false))

(match-word-partial-alphabet "alphabet") ;; => true
(match-word-partial-alphabet "cat") ;; => true
(match-word-partial-alphabet "dog")  ;; => false


;;; Matching multiple times

(re-seq #"a+" "aardvark") ;; => ("aa" "a")
 

;; Managing case (upper and lower)
(re-seq #"a+" "Apple") ;; => nil
(re-seq #"a+"  "Aardvark") ;; => ("a" "a")

(re-seq #"[a-mA-M]" "Apple") ;; => ("A" "l" "e")
(re-seq #"[a-mA-M]" "Aarvark") ;; => ("A" "a" "a" "k")
 
;; Use match multiple times with a range
(re-seq #"[a-mA-M]+" "Aardvark") ;; => ("Aa" "d" "a" "k")


;;; Matching spaces
#"\s"

;; Match two words with a space in between
#"(\w+)\s(\w+)"

(re-matches #"(\w+)\s(\w+)" "Jenny Jetpack") ;; => ["Jenny Jetpack" "Jenny" "Jetpack"]
 

(re-seq #"(...) (...)" "foo bar")
;; => (["foo bar" "foo" "bar"])

;; Clojure's regex syntax doesn't require excaping of backslash characters
;; Java: "(\\d+)-(\\d+)"
(re-seq #"(\d+)-(\d+)" "1-3")
;; => (["1-3" "1" "3"])


;;; Destructure the results of the re-matches function
;;;  so we only use the two words

(let [[_ first-name last-name] (re-matches #"(\w+)\s(\w+)" "Jenny Jetpack")]
  (if first-name   ; then succssful match
    (str "First name: " first-name " , Last name: " last-name)
    (str "Unparsable name")))
;; => "First name: Jenny , Last name: Jetpack"

(clojure.string/split "Jenny Jetpack" #"\s")
;; => ["Jenny" "Jetpack"]

;; if it finds more than one match if returns groups in a vector
(re-matches #"abc(.*)" "abcxyz")
;; => ["abcxyz" "xyz"]


;;; Matching with groups
(re-matches #"abc" "xyz")
;; => nil


(re-matches #"abc" "abc")
;; => "abc"



;;; Matching numbers

#"[0-9]"


