(defproject algo "0.0.1"
  :description "Algorithms: Design and Analysis, Part 1"
  :url "https://class.coursera.org/algo-005/"
  :license {:name " Apache License Version 2.0"
            :url "https://www.apache.org/licenses/LICENSE-2.0.txt"}
  :dependencies [
    [org.clojure/clojure    "1.6.0"]
    [org.clojure/tools.cli  "0.3.1"]
    [com.google.guava/guava "16.0" ]
    [clj-http               "0.9.1"]
    [org.clojure/data.json  "0.2.4"]
  ]
  ;:profiles {
  ;  :uberjar {
  ;    ;:aot :all
  ;  }
  ;}
  :jvm-opts [
    "-Xms256m" "-Xmx512m" "-server" "-XX:MaxPermSize=128m"
    "-XX:NewRatio=2" "-XX:+UseConcMarkSweepGC"
    "-XX:+TieredCompilation" "-XX:+AggressiveOpts"
    "-Dcom.sun.management.jmxremote"
    "-Dcom.sun.management.jmxremote.local.only=false"
    "-Dcom.sun.management.jmxremote.authenticate=false"
    "-Dcom.sun.management.jmxremote.ssl=false"
    ;"-Xprof" "-Xrunhprof"
  ]
  :main algo.core)
