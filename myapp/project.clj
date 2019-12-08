(defproject myapp "0.1.0-SNAPSHOT"
  :description "Getting Clojure code"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :main ^:skip-aot myapp.core
  :target-path "target/%s"
  :java-source-paths ["src/java"]
  :profiles {:uberjar {:aot :all}})
