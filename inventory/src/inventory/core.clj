(ns inventory.core)

(defn find-by-title
  "Search for a book by title
  Where title is a string and books is a collection
  of book maps, each of which mush have a :title entry"
  [title books]
  (some #(when (= (:title %) title) %) books))


(defn number-of-copies
  "Return the number of copies in inventory of the
  given title, where title is a string and books is a collection"
  [title books]
  (:copies (find-by-title title books)))


(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
