(ns quil-exp.getting-started.atom-exp)

;;; Atom examples.

(def total-count (atom 0))

(deref total-count) ;; => 0
;; or
@total-count ;; => 0

;;; Mutation.

;; `reset!`

(reset! total-count 4) ;; => 4

;; `swap!`  -  (swap! the-atom the-function & more-args)

(swap! total-count + 100) ;; => 104

@total-count;; => 104

(+ 100 @total-count) ;; => 204

(swap! total-count #(* % 2)) ;; => 208
