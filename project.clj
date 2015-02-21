(defproject reagent-react-router "0.1.0-0"
  :description "Reagent wrapper for the react-router "
  :url "https://github.com/ghedamat/reagent-react-router"
  :clojurescript? true
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies  [[org.clojure/clojure "1.6.0"]
                  [cljsjs/react-router "0.12.0-0"]
                  [reagent "0.5.0-alpha3"]]
  :plugins  [[codox "0.6.4"]]
  :profiles  {:dev
              {:dependencies  [[org.clojure/clojurescript "0.0-2322"]]
               :plugins  [[lein-cljsbuild "1.0.3"]]}})
