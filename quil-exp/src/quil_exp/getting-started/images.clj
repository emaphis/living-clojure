(ns quil-exp.getting-started.images
  (:require [quil.core :as q]))

(defn setup []
  (let [gr-1  (q/create-graphics 70 70)
        im-2  (q/create-image 100 100 :rgb)
        url-3 "https://dummyimage.com/100x100/2c3e50/ffffff.png"
        url-4 "resources//lunar.jpg"]
    (q/with-graphics gr-1
      (q/ellipse 35 35  70 70))
    (dotimes [x 100]
      (dotimes [y 100]
        (q/set-pixel im-2 x y (q/color (* 2 x) (* 2 y) (+ x y)))))
    (q/set-state! :image-1 gr-1  :image-2 im-2
                  :image-3 (q/load-image url-3)
                  :image-4 (q/load-image url-4))))

(defn draw []
  (q/background 170 200 230)
  (let [im-1 (q/state :image-1)
        im-2 (q/state :image-2)
        im-3 (q/state :image-3)
        im-4 (q/state :image-4)]
    (q/image im-1 10 10)
    (q/image im-1 450 100)
    (q/image im-2 100 50)
    (q/image im-2 170 10)
    (when (q/loaded? im-3) (q/image im-3 300 50))
    (when (q/loaded? im-4) (q/image im-4 40 210))
    (q/fill 0)
    (q/text "Hello World" 300 480)))

(q/defsketch scratch
  :title "Scratch Pad"
  :size [580 500]
  :setup setup
  :draw draw)
