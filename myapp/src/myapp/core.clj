(ns myapp.core
  (:gen-class))


(defn- mult
  "Multiplies two numbers `x` `y`"
  [x y]
  (* x y))

(defn- add
  "Adds two numbers x` `t`"
  [x y]
  (+ x y))

(defn mult-3
  "Multiplies a given number by 3"
  [x]
  (mult x 3))

(mult-3 4)

(defn add-3
  "Adds 3 to a given number"
  [x]
  (add 3 x))

(add-3 4)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

;; Code evaluation
;; ctrl-alt-c enter      - Loading Current File and Dependencies
;; ctrl+alt+c e          - Evaluate Current Form Inline
;; ctrl+alt+c c          - Evaluate code and add as comment
;; ctrl+alt+c r          - Evaluate code and replace it in the editorx`
;; ctrl+alt+c ctrl+c     - Copy last evaluation results

;; REPL:
;; ctrl+alt+c ctrl+alt+n - Load current namespace in the REPL window
;; ctrl+alt+c ctrl+alt+v - Evaluate current editor form in the REPL window
;; ctrl+alt+c ctrl+space - Evaluate current editor top level form in the REPL window

;; Evaluating code in the REPL
;; alt+up/down           - REPL history.
;; alt-enter             - Submit and execute expression

;; Running tests and mark failures and errors in the Problems pane
;; ctrl+alt+c t          - Run namespace tests: 
;; ctrl+alt+c shift+t    - Run all tests
;; ctrl+alt+c ctrl+alt+t - Run current test
;; ctrl+alt+c ctrl+t     - Rerun previously failing tests

;; ctrl+alt+c s          - Select current form

;; ctrl+alt+c .          - Run custom commands, i.e. code snippets, at will


(comment
  (+ 3 4)
  )
