(ns myapp.chap16)

;;;; Java interop.

(def authors (java.io.File. "authors.txt"))

;; test for file
(if (.exists authors)
  (println "Our authors file is there")
  (println "Our authors file is missing"))

(if (.canRead authors)
  (println "We can read it!"))

(.setReadable authors true)
;;
;; selecting object fields
(def rect (java.awt.Rectangle. 0 0 10 20))

(println "Width:" (.-width rect))
(println "Height:" (.-height rect))


;;; Packages
(ns read-authors
  (:import (java.io File InputStream)))

(def authors (File. "authors.txt"))

;; Create a temporary file

;;(def temp-authors-file (File/createTempFile "authors_list" ".txt"))
