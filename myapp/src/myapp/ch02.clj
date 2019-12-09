(ns myapp.ch02)

;;;; Data structures

;;; List

(cons 4 '(1 2 3))
;; => (4 1 2 3)

(conj '(1 2 3) 4)
;; => (4 1 2 3)

(conj [1 2 3] 4)
;; => [1 2 3 4]


;;; Vector

;; Index access
(get [1 2 3 4 5] 3)
;; => 4

(assoc [1 2 3 4 5] 5 6)
;; => [1 2 3 4 5 6]

;; last item removed
(pop [1 2 3 4 5])
;; => [1 2 3 4]

;; removing elements from the middle of a vector
(subvec [1 2 3 4 5] 1 3) ; [2,3)
;; => [2 3]

;; retrieve a slice of data...
(vec
 (concat
  (subvec [1 2 3 4 5] 0 2)
  (subvec [1 2 3 4 5] 3)))
;; => [1 2 4 5]

;; remove element from the middle of a Vector
(defn drop-nth [n coll]
  (->> coll
       (map vector (iterate inc 1))
       (remove #(zero? (mod (first %) n)))
       (map second)))

(drop-nth 3 [:a :b :c :d])
;; => (:a :b :d)

(map vector (iterate inc 1) [:a :b :c]) ;; => ([1 :a] [2 :b] [3 :c])
(remove #(zero? (mod (first %) 3)) '([1 :a] [2 :b] [3 :c]))
;; => ([1 :a] [2 :b])
(map second '([1 :a] [2 :b])) ;; => (:a :b)


;;; Map

{:map-key "this is my value"}

(get {:map-key "this is my value"} :map-key)
;; => "this is my value"

(find {:a 1 :b 2} :a) ;; => [:a 1]

(assoc {:foo "bar"} :baz "qux") ;; => {:foo "bar", :baz "qux"}

(dissoc {:foo "bar" :baz "qux"} :baz) ;; => {:foo "bar"}

;; return a new map that consists of a specific key.
(select-keys {:name "Mark" :age 33 :location "London"} [:name :location])
;; => {:name "Mark", :location "London"}


;;; Keywords
["hi" "hi" "hi"] ; three times in memory.
[:hi :hi :hi] ; once in memory

;; converting
(keyword "my-string");; => :my-string
(name :my-string) ;; => "my-string"

;; keyword as a function
(get {:foo "bar" :baz "qux"} :baz)  ;; => "qux"
(:baz {:foo "bar" :baz "qux"}) ;; => "qux"

(contains? {:foo "bar" :baz "qux"} :foo) ;; => true


;;; Keys, Values and Replacement
(keys {:foo "bar" :baz "qux"}) ;; => (:foo :baz)

(vals {:foo "bar" :baz "qux"}) ;; => ("bar" "qux")

;; extract values based on keys
(replace {:a 1 :b 2 :c 3} [:c :b :a]) ;; => [3 2 1]

(replace [:a :b :c] [2 1 0]) ;; => [:c :b :a]

;; structs

(def mark {:name "Mark" :age 35})
(def rich {:name "Richard" :age 40})
(def cath {:name "Catherine" :age 30})

(def person (create-struct :name :age :sex))

(struct person "Mark" 35 "Male")
;; => {:name "Mark", :age 35, :sex "Male"}
(struct person "Richard" 40 "Make")
;; => {:name "Richard", :age 40, :sex "Make"}
(struct person "Catherine" 30 "Female")
;; => {:name "Catherine", :age 30, :sex "Female"}


;;; Set

#{1 2 3 :a :b :c}

(sorted-set 3 1 2)
;; => #{1 2 3}

;;(sorted-set "a" 1 2 3 :a :b :c)

;; converting to sorted-set
(apply sorted-set #{3 1 2 5})
;; => #{1 2 3 5}

;; filter out duplicates
(set [1 1 2 2 3 3 4 5 6 6])
;; => #{1 4 6 3 2 5}

(apply sorted-set [1 2 2 1 3 3 4 5 6 6])
;; => #{1 2 3 4 5 6}

;; adding values with `conj`
(conj #{1 2 3} 4)
;; => #{1 4 3 2}

(conj #{1 2 3} 3)
;; => #{1 3 2}

;; removing items from a `Set` with `disj`
(disj #{1 2 3 4} 3)
;; => #{1 4 2}


;;; Vars and Symbols

(def foo "hello")

;; Assigning Functions

(def foo
  (fn [p]
    (prn
     (str "Hello " p "!"))))

(foo "you")
;; => nil

(defn foo [p]
  (prn
   (str "Hello " p "!")))


;;; Temp Variables
;; - `let`

(let [xyz "Hi!"]
  (prn xyz))


;;; Dynamic Variables

(def ^:dynamic my-name "Mark")

(prn my-name)

(binding [my-name "Bob"]
  (prn my-name))

(prn my-name)
