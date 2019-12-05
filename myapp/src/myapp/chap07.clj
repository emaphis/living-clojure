(ns myapp.chap07)

;;;; `let`

;;; A Local, Temporary Place for Your Stuff

;; discount policy

(defn compute-discount-amount-1 [amount discount-percentage min-charge]
  (if (> (* amount (- 1.0 discount-percentage)) min-charge)
    (* amount (- 1.0 discount-percentage))
    min-charge))

(compute-discount-amount-1 20.0 0.05 10.0) ;; => 19.0

;; use `let` to create binding

(defn compute-discount-amount-2 [amount discount-percent min-charge]
  (let [discounted-amount (* amount (- 1.0 discount-percent))]
    (if (> discounted-amount min-charge)
      discounted-amount
      min-charge)))

(compute-discount-amount-2 20.0 0.05 10.0) ;; => 19.0


;; break up `discounted-amount` calculation.

(defn compute-discount-amount-3 [amount discount-percent min-charge]
  (let [discount (* amount discount-percent)
        discounted-amount (- amount discount)]
    (if (> discounted-amount min-charge)
      discounted-amount
      min-charge)))

(compute-discount-amount-3 20.0 0.05 10.0) ;; => 19.0


;;; Let over fn
;;  compute it in a let, use it in an fn

(def user-discounts
  {"Nicolas" 0.10 "Jonathan" 0.07 "Felicia" 0.05})

(defn compute-discount-amount-4 [amount user-name user-discounts min-charge]
  (let [discount-percent (user-discounts user-name)
        discount (* amount discount-percent)
        discounted-amount (- amount discount)]
    (if (> discounted-amount min-charge)
      discounted-amount
      min-charge)))

(compute-discount-amount-4 20.0 "Felicia" user-discounts 10.0) ;; => 19.0

;; create custom functions

(defn mk-discount-price-f [user-name user-discounts min-charge]
  (let [discount-percent (user-discounts user-name)]
    (fn [amount]
      (let [discount (* amount discount-percent)
            discount-amount (- amount discount)]
        (if (> discount-amount min-charge)
          discount-amount
          min-charge)))))

;; Price function for Felicia

(def compute-felicia-price (mk-discount-price-f "Felicia" user-discounts 10.0))

(compute-felicia-price 20.0) ;; => 19.0


;;; Variations on the Theme

;; `if-let`

(def anonymous-book
  {:title "Sir Gawian and the Green Knight"})

(def with-author
  {:title "Once and Future King" :author "White"})

(defn uppercase-author-1 [book]
  (let [author (:author book)]
    (if author
      (.toUpperCase author))))

(uppercase-author-1 anonymous-book) ;; => nil
(uppercase-author-1 with-author) ;; => "WHITE"

;; use `if-let`
(defn uppercase-author-2 [book]
  (if-let [author (:author book)]
    (.toUpperCase author)))

(uppercase-author-2 anonymous-book) ;; => nil
(uppercase-author-2 with-author) ;; => "WHITE"

(defn uppercase-author-3 [book]
  (if-let [author (:author book)]
    (.toUpperCase author)
    "ANONYMOUS"))

(uppercase-author-3 anonymous-book) ;; => "ANONYMOUS"
(uppercase-author-3 with-author) ;; => "WHITE"

;; `when-let`

(defn uppercase-author-4 [book]
  (when-let [author (:author book)]
    (.toUpperCase author)))

(uppercase-author-4 anonymous-book) ;; => nil
(uppercase-author-4 with-author) ;; => "WHITE"
