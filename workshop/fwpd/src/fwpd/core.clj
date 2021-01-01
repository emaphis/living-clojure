(ns fwpd.core)

(def filename "suspects.csv")

(slurp filename)

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

;;; Exercises

;; 1. Turn the result of your glitter filter into a list of names.

(defn suspect-name-list
  [suspects]
  (map :name suspects))

;; 2. Write a function, append, which will append a new suspect to your list of suspects.
(defn append
  [suspect records]
  (conj suspect records))

;; 3. Write a validator function
;; validators is a map of fields to validation functions
;; record is the record that needs validation
(defn validate
  [validators record]
  )


(comment
  (= 1000 (str->int "1000"))
  (= 3 (convert :glitter-index "3"))
  (parse (slurp filename))
  (mapify [["Bella Swan" 0]])
  (first (mapify (parse (slurp filename))))
  (glitter-filter 3 (mapify (parse (slurp filename))))
  (suspect-name-list (glitter-filter 3 (mapify (parse (slurp filename)))))
  (append (suspect-name-list (glitter-filter 3 (mapify (parse (slurp filename))))) {:name "Jenny" :glitter-index 10})
  


  )
