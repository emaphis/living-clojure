(ns myapp.chap18)

;;;; State

;;; It's mad of atoms

(def counter 0)
(defn greeting-message-1 [req]
  (if (zero? (mod counter 100))
    (str "Congrats! You are the " counter " visitor!")
    (str "Welcome to Blott's Books!")))

;; atoms `@` `swap!`

(def counter-2 (atom 0))
(defn greeting-message-2 [req]
  (swap! counter-2 inc)
  (if (= @counter-2 500)
    (str "Congrats! You are the " counter " visitor!")
    (str "Welcome to Blott's Books!")))

(swap! counter-2 inc)
(swap! counter-2 + 12)
(deref counter-2)


;;; swapping maps

(ns inventory)

(def by-title (atom {}))

(defn add-book [{title :title :as book}]
  (swap! by-title #(assoc % title book)))

(defn del-book [title]
  (swap! by-title #(dissoc % title)))

(defn find-book [title]
  (get @by-title title))

(find-book "Emma") ;; => nil

(add-book {:title "1984", :copies 1984})
(add-book {:title "Emma", :copies 100})

(find-book "Emma") ;; => {:title "Emma", :copies 100}
(find-book "1984") ;; => {:title "1984", :copies 1984}
(del-book "1984")
(find-book "1984") ;; => nil


;;; `Refs` `dosync` `alter` - Team-Oriented Atoms

(ns inventory)

(def by-title (ref {}))
(def total-copies (ref 0))

(defn add-book [{title :title :as book}]
  (dosync
   (alter by-title #(assoc % title book))
   (alter total-copies + (:copies book))))


;;; Agents. `send`

(def by-title (agent {}))

(defn add-book [{title :title :as book}]
  (send
   by-title
   (fn [by-title-map]
     (assoc by-title-map title book))))
