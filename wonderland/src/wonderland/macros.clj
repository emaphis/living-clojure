(ns wonderland.macros
  (:require [clojure.string :as str]
            [clojure.repl :as r]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Macros

;; Rules of Macros
;; * Don't write macros.
;; * But write a macro to encapsulate a pattern.
;; - Exception, write a macro when it makes it easier on a caller.

;; `unless` perform a test then execute the body if test fails

;; This is doomed to fail...
(defn unless [expr form]
  (if expr nil form))

(unless false (prn "This should print"))
;; "This should print"

(unless true (println "this should not print"))
;; this should not print

;; unless is eager.

(defn unless [expr form]
  (println "About to test")
  (if expr nil form))

(unless false (println "this should print"))
;; this should print
;; About to test

(unless true (println "this should not print"))
;; this should not print
;; About to test

;; should do:
;; (unless expr form) -> (if expr nil form)

;; (defmacro name doc-string? attr-map [params*] body)

(defmacro unless [expr form]
  (list 'if expr nil form))

(unless false (println "this should print"))
;; => nil
;; this should print

(unless true (println "this should not print"))
;; => nil


;;; Expanding macros

;; (macroexpand-1 form)

(macroexpand-1 '(unless false (println "this should print")))
;; => (if false nil (println "this should print"))

;; bad macro for testing
(defmacro bad-unless [expr form]
  (list 'if 'expr nil form))

I(macroexpand-1 '(bad-unless false (println "this should print")))
;; => (if expr nil (println "this should print"))

;; (bad-unless false (println "this should print"))
;; Unable to resolve symbol: expr in this context

(macroexpand-1 '(.. arm getHand geFinger))
;; => (.. (. arm getHand) geFinger)

;; (macroexpand form)  - expand all the way

(macroexpand '(.. arm getHand geFinger))
;; => (. (. arm getHand) geFinger)

(macroexpand '(and 1 2 3))
;; (let* [and__5499__auto__ 1]
;;   (if and__5499__auto__ (clojure.core/and 2 3)
;;       and__5499__auto__))


;;; `when` and `when-not`

;; (unless false (println "this") (println "and also this"))
;; Wrong number of args (3) passed to: wonderland.macros/unless

;; better unless
(when-not false (println "this") (println "and also this"))
;; this
;; and also this

;; source of `when-not`
;; (r/source when-not)
;; (defmacro when-not
;;   "Evaluates test. If logical false, evaluates body in an implicit do."
;;   {:added "1.0"}
;;   [test & body]
;;   (list 'if test nil (cons 'do body)))

(macroexpand-1 '(when-not false (println "1") (println "2")))
;; => (if false nil (do (println "1") (println "2")))


;;; Making macros simpler

;; foo#
;;;   Auto-gensym: Inside a syntax-quoted section, create a unique name
;;    prefixed with foo.
;; (gensym prefix?)
;;    Create a unique name, with optional prefix.
;; (macroexpand form)
;;    Expand form with macroexpand-1 repeatedly until the returned form is
;;    no longer a macro.
;; (macroexpand-1 form)
;;    Show how Clojure will expand form.
;; (list-frag? ~@form list-frag?)
;;    Splicing unquote: Use inside a syntax quote to splice an unquoted list
;;    into a template.
;; ‘form
;;    Syntax quote: Quote form, but allow internal unquoting so that form acts
;;    as a template. Symbols inside form are resolved to help prevent
;;    inadvertent symbol capture.
;; ~form
;;    Unquote: Use inside a syntax quote to substitute an unquoted value.

;; redo Clojure's `..` macro

;; (chain arm getHand)  -> (. arm getHand)
;; (chain arm getHand getFinger) -> (. (. arm getHand) getFinger)

;;`chain` reimplements Clojure's `..` macro
(defmacro chain[x form]
  (list '. x form))

(defmacro chain
  ([x form] (list '. x form))
  ([x form & more] (concat (list 'chain (list '. x form)) more)))

(macroexpand '(chain arm getHand))
;; => (. arm getHand)

(macroexpand '(chain arm getHand getFinger))
;; => (. (. arm getHand) getFinger)

;; simplifying

;; hypothetical templating language.
(defmacro chain
  ([x form] (. ${x} ${form}))
  ([x form & more] (chain (. ${x} ${from}) ${more})))

;; ${} lets you substitute arguments into the macro expansion


;;; Syntax Quote, Unquote, and Splicing Unquote

(defmacro chain [x form]
  `(. ~x ~form))

(macroexpand '(chain arm getHand))
;; => (. arm getHand)

;; This version doesn't quite work
(defmacro chain
  ([x form] `(. ~x ~form))
  ([x form & more] `(chain (. ~x ~form) ~more)))

(macroexpand '(chain arm getHand getFinget))
;; => (. (. arm getHand) (getFinget))

;; rewrite using splicing ~@
(defmacro chain
  ([x form] `(. ~x ~form))
  ([x form & more] `(chain (. ~x ~form) ~@more)))

(macroexpand '(chain arm getHand getFinget))
;; => (. (. arm getHand) getFinget)


;; many macros follow this recipe.
;; * Begin the macro body with a syntax quote (‘) to treat the entire thing
;; as a template.
;; * Insert individual arguments with an unquote (~).
;; * Splice in more arguments with splicing unquote (~@). 


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Creating names in a macro

;; the `time` macro.

(time (str "a" "b"))
;; => "ab"
;; "Elapsed time: 0.1433 msecs"

;; let's call our version `bench`

;; (bench (str "a" "b"))
;; ...should expand to...
(let [start (System/nanoTime)
      result (str "a" "b")]
  {:result result :elapsed (- (System/nanoTime) start)})
;; => {:result "ab", :elapsed 80300}


;; but this wont work.
(defmacro bench [expr]
  `(let [start (System/nanoTime)
         result ~expr]
     {:result result :elapsed (- (System/nanoTime) start)}))

;; (macroexpand (bench (str "a" "b")))
;; Call to clojure.core/let did not conform to spec.
;; (macroexpand-1 (bench (str "a" "b")))

;; Clojure is fully qualifying symbols like `let` which protects us from
;; `symbol capture`.
;; * What if symbols like `start` and `result` already exist in the namespace?

;; so use `#` to create local names

(defmacro bench [expr]
  `(let [start# (System/nanoTime)
         result# ~expr]
     {:result result# :elapsed (- (System/nanoTime) start#)}))

(macroexpand '(bench (str "a" "b")))
;; (let* [start__7273__auto__ (java.lang.System/nanoTime)
;;        result__7274__auto__ (str "a" "b")]
;;   {:result result__7274__auto__,
;;    :elapsed (clojure.core/- (java.lang.System/nanoTime) start__7273__auto__)})

(bench (str "a" "b"))
;; => {:result "ab", :elapsed 30100}


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Taxonomy of macros.

;; * Don't write macros. Macros are complex. Use functions if you can,
;; * Write a macro if it's the only way to Encapsulate a Pattern.
;;   (Special Form Rule)

;; Special forms:
;; * Basic flow control structures. All flow control must eventually call
;;   a special form.
;; * Provide direct access to Java from Clojure. You go through at least one
;;   special form. `.` of `new`.
;; * Names are created and bound through special forms `def`, `let` or
;;;  `binding`.

;; * are not functions so aren't first class. They cant use `apply` can't be
;;   stored in a `var` or use in a filter.

;; But all language features are first class at macro expansion time.

;; Exception:
;; * Because macros don’t evaluate their arguments, callers can pass raw code
;;   to a macro, instead of wrapping the code in an anonymous function. Or,
;;   callers can pass unescaped names, instead of quoted symbols or strings.

;; Justification      Category                Examples
;; Special form       Conditional evaluation  when, when-not, and, or, comment
;; Special form       Defining vars           defn, defmacro, defmulti,
;;                                            defstruct, declare
;; Special form       Java interop            .., doto, import-static 
;; Caller convenience Postponing evaluation   lazy-cat, lazy-seq, delay
;; Caller convenience Wrapping evaluation     with-open, dosync, with-out-str,
;;                                            time, assert
;; Caller convenience Avoiding a lambda       (Same as for “Wrapping evaluation”)


;;; Conditional evaluation

;; * Evaluate some argument (the condition)
;; * Based on that evaluation pick which other arguments to evaluate

;; `unless`
(defmacro unless [expr form]
  (list 'if expr nil form))

;; `and`
;;(r/source and)
;; (defmacro and
;;   ([] true)
;;   ([x] x)
;;   ([x & next]
;;    `(let [and# ~x]
;;       (if and# (and ~@next) and#))))

;; `or`
;; (r/source or)
;; (defmacro or
;;   ([] nil)
;;   ([x] x)
;;   ([x & next]
;;    `(let [or# ~x]
;;       (if or# or# (or ~@next)))))

;; `and` stops on the first logical `false`,
;; `or` stops on the first logical `true`

(and 1 0 nil false)
;; => nil
(or 1 0 nil false)
;; => 1

;; `comment` never evaluates on of it's arguments
;; (r/source comment)
;; (defmacro comment
;;   [& body])


;;; creating vars

;; eventually created by `def` special form.
;; `defn`, `defmacro`, `defmulti` are all macros that call `def`.

;; (defstruct & key-symbols)

(def person (create-struct :first-name :last-name))
;; => #'wonderland.macros/person

;; (r/source create-struct)
;; (defn create-struct
;;   "Returns a structure basis object."
;;   [& keys]
;;   (. clojure.lang.PersistentStructMap (createSlotMap keys)))

;; (defstruct name &key-symbols)
;; (r/source defstruct)
;; (defmacro defstruct
;;   "Same as (def name (create-struct keys...))"
;;   [name & keys]
;;   `(def ~name (create-struct ~@keys)))

;; (declare & names)
;; (r/source declare)
;; (defmacro declare
;;   [& names]
;;   `(do ~@(map #(list 'def (vary-meta % assoc :declared true)) names)))

;; from book
;; (defmacro declare
;;   [& names] `(do ~@(map #(list 'def %) names)))

(macroexpand-1 `(declare a b c d))
;; (do (def wonderland.macros/a)
;;     (def wonderland.macros/b)
;;     (def wonderland.macros/c)
;;     (def wonderland.macros/d))


;; Java interop

Math/PI
;; => 3.141592653589793

(Math/pow 10 3)
;; => 1000.0

(def PI Math/PI)
(defn pow [b e] (Math/pow b e))


;;; Postponing evaluation
;;  lazy sequences.

;; (delay & exprs)
;; (force x)

(def slow-calc (delay (Thread/sleep 5000) "done!"))

(force slow-calc)
;; => "done!"

(force slow-calc) ;; returns instantly.
;; => "done!"

;; (r/source delay)
;; (defmacro delay
;;   [& body]
;;   (list 'new 'clojure.lang.Delay (list* `^{:once true} fn* [] body)))


;;; Wrapping evaluation.

;; time, let, binding, with-open, dosync

;; (with-out-str & exprs)

(with-out-str (print "hello, ") (print "world"))
;; => "hello, world"

(macroexpand '(with-out-str (print "hello, ") (print "world")))
;; (let* [s__6336__auto__ (new java.io.StringWriter)]
;;   (clojure.core/binding [clojure.core/*out* s__6336__auto__]
;;     (print "hello, ")
;;     (print "world")
;;     (clojure.core/str s__6336__auto__)))

;; (r/source with-out-str)
;; (defmacro with-out-str
;;   [& body]
;;   `(let [s# (new java.io.StringWriter)]
;;      (binding [*out* s#]
;;        ~@body
;;        (str s#))))

;; Wrapper macros usually take a variable number of arguments (line 2), which
;; are the forms to be evaluated.
;; They then proceed in three steps:

;; Setup: Create some special context for evaluation, introducing bindings with
;; let (line 3) and bindings (line 4) as necessary.

;; Evaluation: Evaluate the forms (line 5). Since there is typically a variable
;; number of forms, insert them via a splicing unquote: ~@.

;; Teardown: Reset the execution context to normal and return a value as
;; appropriate (line 6).


;;; Special forms - built into Clojure

;; (def symbol doc-string? init?)

;; (if test then else?)

;; (do expr*)

;; (let [binding*] expr*)

;; (quote form)

;; (var symbol)

;; (fn name? [params*] expr*)
;; (fn name? ([params*] expr*)+)

;; (fn name? [param* ] condition-map? expr*)
;; (fn name? ([param* ] condition-map? expr*)+)

;; (loop [binding* ] expr*)

;; (recur expr*)

;; (throw expr)

;; (try expr* catch-clause* finally-clause?)

;; (monitor-enter expr)
;; (monitor-exit expr)

