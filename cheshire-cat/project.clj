(defproject cheshire-cat "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [cheshire "5.8.0"]
                 [ring/ring-json "0.5.0-beta1"]
                 [org.clojure/clojurescript "1.10.439"]
                 [cljs-http "0.1.45"]
                 [org.clojure/core.async "0.4.490"]
                 [enfocus "2.1.0"]]
  :plugins [[lein-ring "0.12.4"]
            [lein-cljsbuild "1.1.7"]]
  :ring {:handler cheshire-cat.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}}
  :cljsbuild {
              :builds [{
                        :source-paths ["src-cljs"]
                        :compiler {
                                   :output-to "resources/public/main.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]})
  
