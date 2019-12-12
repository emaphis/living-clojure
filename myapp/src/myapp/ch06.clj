(ns myapp.ch06)

;;;; Destructuring

(let [[x y] [:a :b]]
  (prn x y))

(let [{a :a b :b} {:a "A" :b "B"}]
  (prn a b))

;; `:keys`
(let [{:keys [a b]} {:a "A" :b "B"}]
  (prn a b))

;; `:keys` `:as`
(let [{:keys [a c] :as complete} {:a "A" :b "B" :c "C" :d "D"}]
  (prn a c complete))

;; `:as` feature
(def a-map {:a 1 :c 3})

(let [{:keys [a b c] :as original-data
       :or {a 11 b 22 c 33}} a-map]
  [a b c original-data])
;; => [1 22 3 {:a 1, :c 3}]

;; `&` variadic list
(let [[x y & z] [:a :b :c :d]]
  (prn x y z)
  (first z))
;; => :c


;; nested destructuring

(let [[[a b][c d]] [[:a :b][:c :d]]]
  (prn a b c d))

(let [{{:keys [foo baz]}:stuff} {:stuff {:foo "bar" :baz "qux"}}]
  (prn foo baz))

;;; Functions

(defn foo [a b & {:keys [c d]}]
  (println a b c d))

(foo "A" "B")
;; A B nil nil
(foo "A" "B" :c "C")
;; A B C nil
(foo "A" "B" :c "C" :d "D")
;; A B C D
