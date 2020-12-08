(ns workshop.chap02)

(use 'clojure.repl)

"I am a String"
"I am immutable"

(println "\"The measure of intelligence is the ability to change\" - Albert Einstein")

;; Strings are immutable

(def silly-string "I am immutble. I am a silly String")

(clojure.string/replace silly-string "silly" "clever")
;; => "I am immutble. I am a clever String"

silly-string
;; => "I am immutble. I am a silly String"

;; strings are collections
(first "a collection of characters")
;; => \a

(type *1)
;; => java.lang.Character

;; String functions

(str "That's the way you " "con" "te" "nate")
;; => "That's the way you contenate"

(str *1 " - " silly-string)
;; => "That's the way you contenate - I am immutble. I am a silly String"

;; clojure.string namespace
(dir clojure.string)

(clojure.string/includes? "potatoes" "toes")

;;; Numbers

(type 1)
;; => java.lang.Long

(type 1000000000000000000)
;; => java.lang.Long
(type 10000000000000000000)
;; => clojure.lang.BigInt

5/4
(/ 3 4)

(type 3/4)
;; => clojure.lang.Ratio

1.2
(/ 3 4.0)
;; => 0.75

(* 1.0 2)
;; => 2.0  -  floating point is contageous.

(type (* 1.0 2))
;; => java.lang.Double

Math/PI
;; => 3.141592653589793

(Math/random)
;; => 0.3734623618975009

(Math/sqrt 9)
;; => 3.0

(Math/round 0.7)
;; => 1


;;; Exercise 2.01: The Obfuscation Machine

(clojure.string/replace "Hello World" #"\w" "!")
;; => "!!!!! !!!!!"

(clojure.string/replace "Hell0 World" #"\d" "o")
;; => "Hello World"

;; passing an anonymous function that takes the matching letter as a parameter:
(clojure.string/replace "Hello World" #"\w" (fn [letter] (do (println letter) "!")))
;; => "!!!!! !!!!!"

(int \a)
;; => 97

(char 97)
;; => \a

(first (char-array "a"))
;; => \a

(int (first (char-array "a")))
;; => 97

(Math/pow (int (first (char-array "a"))) 2)
;; => 9409.0

(defn encode-letter
  [s]
  (let [code (Math/pow (int (first (char-array s))) 2)]
    (str (int code))))

(encode-letter "a")
;; => "9409"

(defn encode
  [s]
  (clojure.string/replace s #"\w" encode-letter))

(encode "Hello World")
;; => "518410201116641166412321 756912321129961166410000"

(defn encode-letter
  [s x]
  (let [code (Math/pow (+ x (int (first (char-array s)))) 2)]
    (str "#" (int code))))

(defn encode
  [s]
  (let [number-of-words (count (clojure.string/split s #" "))]
   (clojure.string/replace s #"\w" (fn [s] (encode-letter s number-of-words)))))

(encode "Super secret message")
;; => "#7396#14400#13225#10816#13689 #13924#10816#10404#13689#10816#14161 #12544#10816#13924#13924#10000#11236#10816"

(defn decode-letter
  [x y]
  (let [number (Integer/parseInt (subs x 1))
        letter (char (- (Math/sqrt number) y))]
    (str letter)))


(defn decode [s]
  (let [number-of-words (count (clojure.string/split s #" "))]
    (clojure.string/replace s #"\#\d+" (fn [s] (decode-letter s number-of-words)))))

(decode "#7396#14400#13225#10816#13689 #13924#10816#10404#13689#10816#14161 #12544#10816#13924#13924#10000#11236#10816")
;; => "Super secret message"

(encode "If you want to keep a secret, you must also hide it from yourself.")
;; => "#7569#13456 #18225#15625#17161 #17689#12321#15376#16900 #16900#15625 #14641#13225#13225#15876 #12321 #16641#13225#12769#16384#13225#16900, #18225#15625#17161 #15129#17161#16641#16900 #12321#14884#16641#15625 #13924#14161#12996#13225 #14161#16900 #13456#16384#15625#15129 #18225#15625#17161#16384#16641#13225#14884#13456."

(decode *1)
;; => "If you want to keep a secret, you must also hide it from yourself."







