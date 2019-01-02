(ns wonderland.protocols
  (:import (java.io FileInputStream FileOutputStream
                    InputStream InputStreamReader OutputStreamWriter
                    OutputStream BufferedReader BufferedWriter)))


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


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Interfaces

;; (definterface IOFactory
;;   (^java.io.BufferedReader makeReader [this])
;;   (^java.io.BufferedWriter makeWriter [this]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Protocols

(defprotocol IOFactory
  "A protocol for things that can be read from and written to."
  (make-reader [this] "Creates a Buffered reader.")
  (make-writer [this] "Creates a BufferedWriter."))


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

(extend-type java.io.File
  IOFactory
  (make-reader [src]
    (make-reader (FileInputStream. src)))
  (make-writer [dst]
    (make-writer (FileOutputStream. dst))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Datatypes

;; (deftype name [& fields] & opts+specs)

(deftype CryptoVault [filename keystore password])
;; => wonderland.protocols.CryptoVault

(def vault (->CryptoVault "vault-file" "keystore" "toomanysecrets"))

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



