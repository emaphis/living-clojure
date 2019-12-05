(ns myapp.chap08)

;;;; Def Symbols and Vars


;;; A global, stable place for your stuff

(def title "Emma")

;; constants

(def PI 3.14)

(def COMPANY-NAME "Blotts Books")

(def ISBN-LENGTH 13)

(def OLD-ISBN-LENGHT 10)

;; use bindings in other defs.

(def isbn-lenghts [OLD-ISBN-LENGHT ISBN-LENGTH])


;; `defn` - a mashup of `def` and `fn`

(defn book-description [book]
  (str (:title book)
       " Written by "
       (:author book)))

;; defs can be used in function definitions

(defn valid-isbn [isbn]
  (or (= (count isbn) OLD-ISBN-LENGHT)
      (= (count isbn) ISBN-LENGTH)))


;;; Symbols are things

;; `def` binds a symbol to a value.

(def author "Austen")

;; Similar to keywords but keywords alway evaluate to themselves but
;; symbols evaluate to an other value

author ;; => "Austen"

;; prevent symbols from being evaluated
'author ;; => author
'title  ;; => title

;; symbols are just another type of value
(str 'author) ;; => "author"

(= 'author 'some-other-symbol) ;; => false
(= 'author 'author) ;; => true

;;; Bindings are things too - `var`.
;; `var` - a binding between a symbol and a value.

#'author ;; => #'myapp.chap08/author

(def the-var #'author) ;; grab the var

;; get the value and the symbol
(.get the-var) ;; => "Austen"
(.-sym the-var) ;; => author

;; value - var - symbol


;;; Varying your vars.

;; debugging attempt

(def ^:dynamic *debug-enabled* false)

(defn debug [msg]
  (if *debug-enabled*
    (println msg)))

(defn some-troublesome-function-that-needs-logging []
  (println "Some troublesome function"))

(binding [*debug-enabled* true]
  (debug "Calling that darned function")
  (some-troublesome-function-that-needs-logging)
  (debug "Back from that darned function"))
