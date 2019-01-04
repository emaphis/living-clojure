(ns wonderland.files
  (:require [clojure.java.io :as io]))

;;; read a file into a string

(def long-string (slurp "project.clj"))

;; long-string

(slurp "src/wonderland/files.clj")

;;; Read a file one line at time

(with-open [rdr (io/reader "project.clj")]
  (doall (map #(println %) (line-seq rdr))))
;; => (nil nil nil nil nil nil)


;;; Write a long string out to a new file

;; overwrites file
(spit "foo.txt"
      "A long
multi-line string.
Bye.")

;; appends file.
(spit "foo.txt" "\nmore file content" :append true)


;;; Write a file one line at a a time

(def my-vec [0 1 2 3 4 5 "end."])

(with-open [wrtr (io/writer "foo.txt")]
  (doseq [i my-vec]
    (.write wrtr (str i "\n"))))


;;; Check if a file exists

(.exists (io/file "foo.txt"))

;; => true
(.exists (io/file "some.file"))
;; => false

;; is it a directory
(.isDirectory (io/file "src/wonderland"))
;; => true

;;; more fucntions
;; exists        Does the file exist?
;; isDirectory   Is the File object a directory?
;; getName       The basename of the file.
;; getParent     The dirname of the file.
;; getPath       Filename with directory.
;; mkdir         Create this directory on disk.


;;; Get a list of the files and dirs in a directory

(map #(.getName %) (.listFiles (io/file "src/wonderland")))

(apply str (.list (io/file "src/wonderland")) )
