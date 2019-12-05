(ns myapp.chap04)

;;;; Logic

;;; If expression

(defn print-greeting [preferred-customer]
  (if preferred-customer
    (println "Welcome back to Blotts Books!")))

(defn print-greeting [preferred-customer]
  (if preferred-customer
    (println "Welcome back to Blotts Books!")
    (println "Welcome to Blotts Books!")))

(defn shipping-charge [preferred-customer order-amount]
  (if preferred-customer
    0.00
    (* order-amount 0.10)))


;;; Asking Questions

(= 1 1) ;; => true
(= 2 (+ 1 1)) ;; => true
(= "one" "one") ;; => true
(= "one" "two") ;; => false

(= (+ 2 2) 4 (/ 40 10) (* 2 2) (- 5 1)) ;; => true
(= 2 2 2 2 2 2 2 2) ;; => true

(not= "one" "one") ;; => false
(not= "one" "two")  ;; => true

(number? 1984) ;; => true
(number? "one") ;; => false
(string? "one") ;; => true
(string? 1984)  ;; => false
(map? :anna-kerena) ;; => false
(map? {:title 1984}) ;; => true
(vector? 1984)  ;; => false
(vector? [1984]) ;; => true

;;;; Only nil and false is falsy

;; execute a collection of expressions
(do
  (println "This is four expressions.")
  (println "All grouped together as one.")
  (println "That prints some stuff and then evalueates to 44")
  44)
;; => 44

(defn shipping-charge [preferred-customer order-amount]
  (if preferred-customer
    (do
      (println "Preferred customer, free shipping!")
      0.0)
    (do
      (println "Regular customer, charge them for shipping")
      (* order-amount 0.10))))

;;; cond - dealing with Multiple Conditions

(defn shipping-charge [preferred-customer order-amount]
  (cond
    preferred-customer 0.0
    (< order-amount 50.0) 5.0
    (< order-amount 100.0) 10.0
    :else (* 0.1 order-amount)))

;;; case - 

(defn customer-greeting [status]
  (case status
    :gold      "Welcome, welcome, welcome back!!!"
    :preferred "Welcome back!"
    "Welcome to Blotts Books."))


;;; Throwing and Catching.

(try
  (/ 100 0)
  (catch ArithmeticException e (println "Oh no!!")))

(defn publish-book [book]
  (when (not (:title book))
    (throw
     (ex-info "A book needs a title!" {:book book})))
  (println (:title book)))

(def book
  {
   ;; :title "Oliver Twist"
   :author "Dickens"
   :published 1838})


(try
  (publish-book book)
  (catch Exception  e (println "Error !!!")))
