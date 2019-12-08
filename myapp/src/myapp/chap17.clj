(ns myapp.chap17)

;;;; Threads, Promises, and Futures.

;;;;  Great Power ...

(ns blottsbooks.threads)

(defn -main []
  (println "Coming to you live from the main threads!"))

;; Make a threads
(defn do-something-in-a-thread []
  (println "Hello from the thread.")
  (println "Good bye from the thread."))

(def the-thread (Thread. do-something-in-a-thread))

;; And run it.
(.start the-thread)

;; Message with a three second pause.
(defn do-something-else []
  (println "Hello from the thread.")
  (Thread/sleep 6000)
  (println "Good bye from the tread."))

(.start (Thread. do-something-else))

;;; ... And Great Responsibility.

(def fav-book "Jaws")

(defn make-emma-favorite [] (def fav-book "Emma"))

(defn make-2001-favorite [] (def fav-book "2001"))

(make-emma-favorite)
(make-2001-favorite)

(defn foo []
  (.start (Thread. make-emma-favorite))
  (.start (Thread. make-2001-favorite)))


;;; Good Fences make Happy Threads


(def inventory [{:title "Emma" :sold 51 :revenue 255}
                {:title "2001" :sold 17 :revenue 170}
                ;; Lots and lots of books...
                ])

(defn sum-copies-sold [inv]
  (apply + (map :sold inv)))

(defn sum-revenue [inv]
  (apply + (map :revenue inv)))

(.start (Thread. (sum-copies-sold inventory)))
(.start (Thread. (sum-revenue inventory)))

;; Could interfere with each other

;; Don't worry about dynamic vars.
;; Thread local storage.

(def ^:dynamic *favorite-book* "Oliver Twist")

(def thread-1
  (Thread.
   #(binding [*favorite-book* "2001"]
      (println "My favorite book is" *favorite-book*))))

(def thread-2
  (Thread.
   #(binding [*favorite-book* "Emma"]
      (println "My favorite book is" *favorite-book*))))

(defn bar []
  (.start thread-1)
  (.start thread-2))

;;(bar)
;;*favorite-book*


;;; Promise Me a Result
;; thread - join

(def the-result (promise))

(deliver the-result "Emma")  ;; snaps shut

(println "The value of my promise is" (deref the-result))
;; The value of my promise is Emma

;; @promise - deref the promise - wait for the result
(println "The value of my promise is" @the-result)


;; back to inventory example

(let [copies-promise (promise)
      revenue-promise (promise)]
  (.start (Thread. #(deliver copies-promise (sum-copies-sold inventory))))
  (.start (Thread. #(deliver revenue-promise (sum-revenue inventory))))
  ;; other stuff ...
  (println "The total number of books sold is" @copies-promise)
  (println "The total revenue is " @revenue-promise))


;;; A Value with a Future.
;; future - thread : promise combo.

(def revenue-future
  (future (apply + (map :revenue inventory)))) ; fire off thread.

(println "The total revenue is " @revenue-future) ; block until calculated
