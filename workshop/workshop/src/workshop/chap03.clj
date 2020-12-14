(ns workshop.chap03)

(use 'clojure.repl)

;;; Destructuring

(defn print-coords [coords]
  (let [lat (first coords)
        lon (last coords)]
    (println (str "Latitude: " lat " - " "Longitude: " lon))))

(print-coords [48.9615 2.4372])

;; destructuring
(defn print-coords [coords]
  (let [[lat lon] coords]
    (println (str "Latitude: " lat " - " "Longitude: " lon))))

(print-coords [48.9615 2.4372])

(let [[a b c] [1 2 3]] (println a b c))

(let [[a b c] '(1 2 3)] (println a b c))

;; Airport example
(def paris
  {:lat  48.9615, :lon 2.4372, :code 'LFPB', :name "Paris Le Bourget Airport"})

(defn print-coords [airport]
  (let [lat (:lat airport)
        lon (:lon airport)
        name (:name airport)]
    (println (str name " is located at Latitude: " lat " - " "Longitude: " lon))))

(print-coords paris)

;; associative destructuring

(defn print-coords [airport]
  (let [{lat :lat lon :lon airport-name :name} airport]
    (println (str airport-name " is located at Latitude: " lat " - " "Longitude: " lon))))

(print-coords paris)

;; when the keys and symbols have the same name - `:keys`

(defn print-coords [airport]
  (let [{:keys [lat lon name]} airport]
    (println (str name " is located at Latitude: " lat " - " "Longitude: " lon))))

(print-coords paris)


;;; Exercise 3.01: Parsing Fly Vector's Data with Sequential Destructuring
;;  airport continued

;; examples
;; a coordinate point
[48.9615, 2.4372]
;; a flight is a tuple of two coordinate points
[[48,9615, 2.4372], [37.742, -25.6978]]
;; a booking is some information followed by one or more flights
;; internal ID
;; name of the passenger.
;; some sensitive data.
[1425,
 "Bob Smith",
 "Allergic to unsalted peanuts only",
 [[48.9615, 2.4372], [37.742, -25.6976]],
 [[37.742, -25.6976], [48.9615, 2.4372]]
 ]

(def booking [1425,
              "Bob Smith",
              "Allergic to unsalted peanuts only",
              [[48.9615, 2.4372], [37.742, -25.6976]],
              [[37.742, -25.6976], [48.9615, 2.4372]] ])

;; parsing  - dont print out sensitive data
(let [[id customer-name sensitive-info flight1 flight2 flight3] booking]
  (println id customer-name flight1 flight2 flight3))

(conj booking [[37.742, -25.6976], [51.1537, 0.1821]]
      [[51.1537, 0.1821], [48.9615, 2.4372]])

;; skip last flight.
(let [big-booking (conj booking [[37.742, -25.6976], [51.1537, 0.1821]]
                        [[51.1537, 0.1821], [48.9615, 2.4372]])
      [id customer-name sensitive-info flight1 flight2 flight3] big-booking]
  (println id customer-name flight1 flight2 flight3))

;; ignore id and sensitive data - `_` symbol
(let [[_ customer-name _ flight1 flight2 flight3] booking]
  (println customer-name flight1 flight2 flight3))

;; repeating flights  - `&` symbol
(let [[_ customer-name _ & flights] booking]
  (println customer-name " booked " (count flights) " flights"))

;; function - print-flight
(defn print-flight [flight]
  (let [[[lat1 lon1] [lat2 lon2]] flight]
    (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2))))

(print-flight [[48.9615, 2.4372], [37.742 -25.6976]])

;; simplify
(defn print-flight [flight]
  (let [[departure arrival] flight
        [lat1 lon1] departure
        [lat2 lon2] arrival]
    (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2))))

(print-flight [[48.9615, 2.4372], [37.742 -25.6976]])


(defn print-booking [booking]
  (let [[_ customer-name _ & flights] booking]
    (println customer-name " booked " (count flights) " flights")
    (let [[flight1 flight2 flight3] flights]
      (when flight1 (print-flight flight1))
      (when flight2 (print-flight flight2))
      (when flight3 (print-flight flight3)))))

(print-booking booking)


;;; Exercise 3.02: Parsing MapJet Data with Associative Destructuring

(def mapjet-booking
  {:id 8773
   :customer-name "Alice Smith"
   :catering-notes "Vegetarian on Sundays"
   :flights [{:from {:lat 48.9615 :lon 2.4372 :name "Paris Le Bourget Airport"},
              :to {:lat 37.742 :lon -25.6976 :name "Ponta Delgada Airport"}},
             {:from {:lat 37.742 :lon -25.6976 :name "Ponta Delgada Airport"},
              :to {:lat 48.9615 :lon 2.4372 :name "Paris Le Bourget Airport"}}
             ]})


;; associative destructuring
(let [{:keys [customer-name flights]} mapjet-booking]
  (println (str customer-name " booked " (count flights) " flights.")))

(defn print-mapjet-flight [flight]
  (let [{:keys [from to]} flight
        {lat1 :lat lon1 :lon} from
        {lat2 :lat lon2 :lon} to]
    (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2))))

(print-mapjet-flight (first (:flights mapjet-booking)))

;; nested associative destructuring expressions

(defn print-mapjet-flight [flight]
  (let [{{lat1 :lat lon1 :lon} :from,
         {lat2 :lat lon2 :lon} :to} flight]
    (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2))))

(print-mapjet-flight (first (:flights mapjet-booking)))

(defn print-mapjet-booking [booking]
  (let [{:keys [customer-name flights]} booking]
    (println (str customer-name " booked " (count flights) " flights."))
    (let [[flight1 flight2 flight3] flights]
      (when flight1 (print-mapjet-flight flight1)) flights
      (when flight2 (print-mapjet-flight flight2))
      (when flight3 (print-mapjet-flight flight3)))))

(print-mapjet-booking mapjet-booking)


;;; Advanced Call Structures

;;; Destructuring Function Parameters

(defn print-flight
  [[[lat1 lon1] [lat2 lon2]]]
  (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2)))

(print-flight [[48.9615, 2.4372], [37.742 -25.6976]])

;; nested associative destructuring
(defn print-mapjet-flight
  [{{lat1 :lat lon1 :lon} :from, {lat2 :lat lon2 :lon} :to}]
  (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2)))

(print-mapjet-flight { :from {:lat 48.9615 :lon 2.4372}, :to {:lat 37.742 :lon -25.6976} })


;;; Arity Overloading

(defn no-overloading []
  (println "Same old, same old ..."))

(no-overloading)

(defn overloading
  ([] "No argument")
  ([a] (str "One argument: " a))
  ([a b] (str "Two arguments: a " a " b: " b)))

(overloading)
(overloading 1)
(overloading 1 2)
(overloading 1 nil)


;; game
(def weapon-damage {:fists 10 :staff 35 :sword 100 :cast-iron-saucepan 150})

(defn strike
  ([enemy] (strike enemy :fists))
  ([enemy weapon]
   (let [damage (weapon weapon-damage)]
     (update enemy :health - damage))))

(strike {:name "n00b-hunter" :health 100})
;; => {:name "n00b-hunter", :health 90}

(strike {:name "n00b-hunter" :health 100} :sword)
;; => {:name "n00b-hunter", :health 0}

(strike {:name "n00b-hunter" :health 100} :cast-iron-saucepan)
;; => {:name "n00b-hunter", :health -50}


;;; Variadic Functions

;; takes multiple parameters but isn't overloaded
(str "Concatenating " "is " "difficult " "to " "spell " "but " "easy " "to " "use!")
;; => "Concatenating is difficult to spell but easy to use!"


(defn welcome
  [player & friends]
  (println (str "Welcome to the Parenthmazes " player "!"))
  (when (seq friends)
    (println (str "Sending " (count friends) " friend request(s) to the following players: " (clojure.string/join ", " friends)))))

(welcome "Jon")
(welcome "Jon" "Arya" "Tyrion" "Petyr")

;; improved
(defn welcome
  ([player] (println (str "Welcome to Parenthmazes (single-player mode), " player "!")))
  ([player & friends]
   (println (str "Welcome Parenthmazes (multi-player mode), " player "!"))
   (println (str "Sending " (count friends) " friend request(s) to the following players: " (clojure.string/join ", " friends)))))

(welcome "Jon")
(welcome "Jon" "Arya" "Tyrion" "Petyr")


;;; Exercise 3.03: Multi-arity and Destructuring with Parenthmazes

(def weapon-damage {:fists 10.0 :staff 35.0 :sword 100.0 :cast-iron-saucepan 150.0})

(defn strike
  ([target weapon]
   (let [points (weapon weapon-damage)]
     (if (= :gnomes (:camp target))
       (update target :health + points)
       (update target :health - points)))))

(def enemy {:name "Zulkaz", :health 250, :camp :trolls})

(strike enemy :sword)
;; => {:name "Zulkaz", :health 150.0, :camp :trolls}

(def ally {:name "Carla", :health 80, :camp :gnomes})

(strike ally :staff)
;; => {:name "Carla", :health 45.0, :camp :gnomes}

(defn strike
  ([target weapon]
   (let [points (weapon weapon-damage)]
     (if (= :gnomes (:camp target))
       (update target :health + points)
       (let [armor (or (:armor target) 0)
             damage (* points (- 1 armor))]
         (update target :health - damage))))))

;; without armor
(strike enemy :cast-iron-saucepan)
;; => {:name "Zulkaz", :health 100.0, :camp :trolls}

;; with armor
(def enemy2 {:name "Zulkaz", :health 250, :armor 0.8, :camp :trolls})

(strike enemy2 :cast-iron-saucepan)
;; => {:name "Zulkaz", :health 220.0, :armor 0.8, :camp :trolls}

;; associative destructuring of parameters
(defn strike
  ([{:keys [camp armor] :as target} weapon]
   (let [points (weapon weapon-damage)]
     (if (= :gommes camp)
       (update target :health + points)
       (let [damage (* points (- 1 (or armor 0)))]
         (update target :health - damage))))))

(strike enemy2 :cast-iron-saucepan)
;; => {:name "Zulkaz", :health 220.0, :armor 0.8, :camp :trolls}

;; `:or` provides a default value for `:armor`
(defn strike
  "With one argument, strike a target with a default :fists `weapon`.
  With two argument, strike a target with `weapon`"
  ([target] (strike target :fists))
  ([{:keys [camp armor], :or {armor 0}, :as target} weapon]
   (let [points (weapon weapon-damage)]
     (if (= :gnomes camp)
       (update target :health + points)
       (let [damage (* points (- 1 armor))]
         (update target :health - damage))))))

(strike enemy2)
;; => {:name "Zulkaz", :health 248.0, :armor 0.8, :camp :trolls}

(strike enemy2 :cast-iron-saucepan)
;; => {:name "Zulkaz", :health 220.0, :armor 0.8, :camp :trolls}

(strike ally :staff)
;; => {:name "Carla", :health 115.0, :camp :gnomes}

(strike enemy)
;; => {:name "Zulkaz", :health 240.0, :camp :trolls}

