(ns wonderland.protocols
  (:import (java.net Socket URL)
           (java.io File FileInputStream FileOutputStream
                    InputStream InputStreamReader
                    OUtputStream OutputStreamWriter
                    BufferedReader BufferedWriter)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Protocols and Datatypes

;; `proxy` and `gen-class` provided the original Java abstraction mechanisms
;; until proxies

;; Protocols provide an alternative to Java interfaces for polymorphic dispatch

;; Datatypes provide an alternative to Java classes for creating implementations
;; of abstractions defined with either protocols or interfaces.


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Programming to abstractions

;; `spit` and `slurp` are built on two abstractions reading an writing, so you can
;; use the on a variety of source and destination types, files URLs and sockets.

;; The `slurp` function takes an input source, reads the contents, and returns it]
;; as a string.

;; The `spit` function takes an output destination and a value, converts the value
;; to a string, and writes it to the output destination.

;;; see gulp.clj


;; The make-reader function makes a BufferedReader from an input source.
;; The make-writer makes a BufferedWriter from an output destination.


;; versions that only access files
(defn make-reader [src]
  (-> src
      FileInputStream.
      InputStreamReader.
      BufferedReader.))

(defn make-writer [dst]
  (-> dst
      FileOutputStream.
      OutputStreamWriter.
      BufferedWriter.))

;; add support for more types

(defn make-reader [src]
  (-> (condp = (type src)
        java.io.InputStream src
        java.lang.String (FileInputStream. src)
        java.io.File (FileInputStream. src)
        java.net.Socket (.getInputStream src)
        java.net.URL (if (= "file" (.getProtocol src))
                       (-> src .getPath FileInputStream.)
                       (.openStream src)))
      InputStreamReader.
      BufferedReader.))

(defn make-writer [dst]
  (-> (condp = (type dst)
        java.io.OutputStream dst
        java.lang.String (FileOutputStream. dst)
        java.io.File (FileOutputStream. dst)
        java.net.Socket (.getOutputStream dst)
        java.net.URL (if (= "file" (.getProtocol dst))
                       (-> dst .getPath FileOutputStream.)
                       (throw (IllegalArgumentException.
                               "Can't write to non-file URL"))))
      OutputStreamWriter.
      BufferedWriter.))


;; the definitions of`gulp` and `expectorate` that use `make-reader` and `make-writer`.

(defn gulp [src]
  (let [sb (StringBuilder.)]
    (with-open [reader (make-reader src)]
      (loop [c (.read reader)]
        (if (neg? c)
          (str sb)
          (do
            (.append sb (char c))
            (recur (.read reader))))))))

(defn expectorate [dst content]
  (with-open [writer (make-writer dst)]
    (.write writer (str content))))

;; the problem with this solution is it is inflexible, you need to update
;; make-reader and make-writer to support new types


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Interfaces

;; the mechanism in Java for supporting abstractions. Dispatches calls to an
;; abstract function, dispatched on datatype of first parameter passed.
;; Implicit in Java as the object the function is called on.

;; Following are the strengths of interfaces:
;; * Datatypes can implement multiple interfaces.
;; * Interfaces provide only specification, not implementation, which allows
;;    implementation of multiple interfaces without the problems associated with
;;    multiple class inheritance.

;; Weakness - existing datatypes cannot be extended to implement new interfaces
;;   without rewriting them.

;; (definterface name & sigs)

;;(definterface IOFactory
;; (^java.io.BufferedReader makeReader [src])
;; (^java.io.BufferedWriter makeWriter [dst]))

;; Unfortunately like the versions of make-reader and make-writer we based on
;; condp, our interface is closed to extension by parties other than the author.
;; This is part of what is called the expression problem.


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Protocols

;; Following are the strengths of protocols:
;; * Datatypes can implement multiple protocols.
;; * Protocols provide only specification, not implementation, which allows
;;   implementation of multiple interfaces without the problems associated with
;;   multiple-class inheritance.
;; * Existing datatypes can be extended to implement new interfaces with no
;;   modification to the datatypes.
;; * Protocol method names are namespaced, so there’s no risk of name collision
;;   when multiple parties choose to extend the same extant type.

;; (defprotocol name & opts+sigs)

(defprotocol IOFactory
  "A protocol for things that can be read from and written to."
  (make-reader [this] "Creates a BufferedReader.")
  (make-writer [this] "Creates a BufferedWriter."))


;; Use `extend` to associate a type to a protocol.
;; (extend type & proto+mmaps)

(extend InputStream
  IOFactory
  {:make-reader (fn [src]
                  (-> src InputStreamReader. BufferedReader.))
   :make-writer (fn [dst]
                  (throw (IllegalArgumentException.
                          "Can't open as an InputStream.")))})

(extend OutputStream
  IOFactory
  {:make-reader (fn [src]
                  (throw (IllegalArgumentException.
                          "Can't open an Outputstream.")))
   :make-writer (fn [dst]
                  (-> dst OutputStreamWriter. BufferedWriter.))})


;; (extend-type type & specs)

(extend-type java.io.File
  IOFactory
  (make-reader [src]
    (make-reader (FileInputStream. src)))
  (make-writer [dst]
    (make-writer (FileOutputStream. dst))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Datatypes

;; creating new datatype in Clojure instead of using Java class.

;; Datatypes provide the following
;; * A unique class, either named or anonymous
;; * Structure, either explicitly as fields or implicitly as a closure
;; * Fields that can have type hints and can be primitive
;; * Immutability on by default
;; * Unification with maps (via records)
;; * Optional implementations of abstract methods specified in protocols
;;   or interfaces

;; (deftype name [& fields] & opts+specs)

(deftype CryptoVault [filename keystore password])
;; => wonderland.protocols.CryptoVault

(def vault (->CryptoVault "vault-file" "keystore" "toomanysecrets"))
;; => #'wonderland.protocols/vault

(.filename vault)
;; => "vault-file"

(.keystore vault)
;; => "keystore"

(.password vault)
;; => "toomanysecrets"

(defprotocol Vault
  (init-vault [vault])
  (vault-output-stream [vault])
  (vault-input-stream [vault]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Records.

;; Record is like a `deftype` but also implements `PersistentMap` so can act
;; like a map and support type-based polymorphism
;; a map that can implement protocols.

;; (defrecord name [& fields] & opts+specs)

(defrecord Note [pitch octave duration])
;; => wonderland.protocols.Note

;; pitch will be represented by a keyword :C :C# :Db
;; octaves by whole numbers
;; durations by fractions

(->Note :D# 4 1/2)
;; => #wonderland.protocols.Note{:pitch :D#, :octave 4, :duration 1/2}

;; Access like any datatype (`deftype`)
(.pitch (->Note :D# 4 1/2))
;; => :D#

;; map-like
(map? (->Note :D# 4 1/2))
;; => true

;; so we can access with keyowrds:
(:pitch (->Note :D# 4 1/2))
;; => :D#


;; modify with `assoc` and `update-n`
(assoc (->Note :D# 4 1/2) :pitch :Db :duration 1/4)
;; => #wonderland.protocols.Note{:pitch :Db, :octave 4, :duration 1/4}

(update-in (->Note :D# 4 1/2) [:octave] inc)
;; => #wonderland.protocols.Note{:pitch :D#, :octave 5, :duration 1/2}

;; records are open like maps
(assoc (->Note :D# 4 1/2) :velocity 100)
;; => #wonderland.protocols.Note{:pitch :D#, :octave 4, :duration 1/2, :velocity 100}

;; if you `dissoc` a required record field the record is demoted to a map.

(dissoc (->Note :D# 4 1/2) :octave)
;; => {:pitch :D#, :duration 1/2}

;; records are not functions like maps
;; ((->Note :D# 4 1/2) :pitch)
;; class wonderland.protocols.Note cannot be cast to class clojure.lang.IFn

;; wire to the JDK's MIDI synthesizer.

(defprotocol MidiNote
  (to-msec [this tempo])
  (key-number [this])
  (play [this tempo midi-channel]))

;; `to-msec` returns the duration of the note in milliseconds.
;; `key-number` returns the MIDI key number corresponding to this note.
;; `play` plays this note at the given tempo on the given channel.

(import 'javax.sound.midi.MidiSystem)

(extend-type Note
  MidiNote
  (to-msec [this tempo]
    (let [duration-to-bpm {1/1 240, 1/2 120, 1/4 60, 1/8 30, 1/16 15}]
      (* 1000 (/ (duration-to-bpm (:duration this))
                 tempo))))

  (key-number [this]
    (let [scale {:C 0,  :C# 1, :Db 1,  :D 2,
                 :D# 3, :Eb 3, :E 4,   :F 5,
                 :F# 6, :Cb 6, :G 7,   :G# 8,
                 :Ab 8, :A 9,  :A# 10, :Bb 10,
                 :B 11}]
      (+ (* 12 (inc (:octave this)))
         (scale (:pitch this)))))

  (play [this tempo midi-channel]
    (let [velocity (or (:velocity this) 64)]
      (.noteOn midi-channel (key-number this) velocity)
      (Thread/sleep (to-msec this tempo)))))

(defn perform [notes & {:keys [tempo] :or {tempo 120}}]
  (with-open [synth (doto (MidiSystem/getSynthesizer) .open)]
    (let [channel (aget (.getChannels synth) 0)]
      (doseq [note notes]
        (play note tempo channel)))))


(def close-encounters [(->Note :D 3 1/2)
                       (->Note :E 3 1/2)
                       (->Note :C 3 1/2)
                       (->Note :C 2 1/2)
                       (->Note :G 2 1/1)])

(perform close-encounters)

(def jaws (for [duration [1/2 1/2 1/4 1/8 1/8 1/8 1/8]
                pitch [:E :F]]
            (Note. pitch 2 duration)))

(perform jaws)

;; raise of lower octaves
(perform (map #(update-in % [:octave] inc) close-encounters))
(perform (map #(update-in % [:octave] dec) close-encounters))

(perform (for [velocity [64 80 90 100 110 120]]
           (assoc (Note. :D 3 1/2) :velocity velocity)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; reify

;; The reify macro lets you create an anonymous instance of a datatype that
;; implements either a protocol or an interface.

;; (reify & opts+specs)

;; see generator.cl
