; Licensed under the Apache License, Version 2.0 (the "License"); 
; you may not use this file except in compliance with the License. 
; You may obtain a copy of the License at
;
; http://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software 
; distributed under the License is distributed on 
; an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
; either express or implied. See the License for the specific 
; language governing permissions and limitations under the License.
(ns algo.core
  (:require 
    [clojure.tools.cli  :refer [parse-opts]       ]
    [clojure.string     :as     string            ]
    [clojure.edn        :as     edn               ]
    [clojure.walk       :refer [keywordize-keys]  ]
    [clj-http.client    :as     http-client       ]
    [clojure.data.json  :as     json              ]
    [clojure.string     :refer [split]            ]
    [clojure.java.io    :as     io                ])
   (:import
    [com.google.common.hash     Hashing HashFunction HashCode ]
    [java.nio.charset           Charset                       ]
    [java.io                    File                          ])
  (:gen-class))

;; PARAMETERS

;; Helpers

;; Algo

;algo-1

(defn read-file
  "Returns {:ok string } or {:error...}"
  [^String file]
  (try
    (cond 
      (.isFile (File. file))
        {:ok (slurp file) }                         ; everything is ok return {:ok string}
      :else
        (throw (Exception. "Input is not a file"))) ;the input is not a file, throw exception
  (catch Exception e
    {:error "Exception" :fn "read-file" :exception (.getMessage e) }))) ; catch all exceptions
                                                                        ; return {:error... } instead
(defn string-to-vec
  [file-string]
  (cond 
    (contains? file-string :ok)
      {:ok (vec (map read-string (split (:ok file-string) #"\n"))) }
    (contains? file-string :error)
      ;let the :error go back to the caller
      file-string))

(defn inversions 
  [^Integer num ^Integer position vect] 
  (count 
    (filter #(< % num) (vec (nth (split-at position vect) 1)))))

(defn brt-frc-invs
  "Input:[]
   Output:{:ok ...}"
  [vect]
  {:ok {:inversions 
    (reduce + (for [x (range (count vect))] (inversions (nth vect x) x vect)))}})

; based on http://blog2samus.tumblr.com/post/20763915302/playing-with-merge-sort-in-clojure
; this is the tail recursive version
(defn mrg
  ;(mrg coll1 coll2) -> (mrg [] coll1 coll2)
  ([xs ys] (mrg [] xs ys))
  ;(mrg [] coll1 coll2)
  ([result xs ys]
    (let [x (first xs) y (first ys)]
    (cond
      (nil? x)
        (vec (concat result ys))
      (nil? y) 
        (vec (concat result xs))
      :else 
        (if (< x y)
          ;than
          (recur (conj result x) (rest xs) ys)
          ;else
          (recur (conj result y) xs (rest ys)))))))
 
(defn mrg-srt
  [xs]
  ;(println (type xs))
  (let 
  [cnt-xs (count xs)]
    (cond
      (> 2 cnt-xs) xs
      (= 2 cnt-xs) (sort xs)
      :else
        (let 
          [ [left right] (split-at (quot cnt-xs 2) xs) ]
          (mrg (mrg-srt left) (mrg-srt right))))))

(defn dispatch-algo [fun] 
  (fn [options] 
    (let [ input (:input options) coll (:coll options) ]
      (cond
        (and (not (nil? coll)) (= input "std"))
          (fun (read-string coll))
        ;defaulting to reading the input file
        :else
          (let
            [data (string-to-vec (read-file input))]
            (cond
              (contains? data :ok)
                (fun (:ok data))
              :else
                ;if the file reading returns an error
                data))))))

(def brute-force-inversions 
  (dispatch-algo brt-frc-invs))
(def merge-sort 
  (dispatch-algo mrg-srt))

(defn algo-1 
  [options]
  (let [fn-typ (:type options)]
  (cond
    (= fn-typ "brute-force-inversions")
      (brute-force-inversions options)
    (= fn-typ "merge-sort")
      (merge-sort options)
    :else
      "This never happens!")))


;; CLI

(defn print-config
  [m]
  (println m))


(def cli-options
  [
    ["-i" "--input FILE" "Input file for processing" :default "/dev/null"]
    ["-t" "--type TYPE" "Algo type" :default "algo-1-brute-force"]
    ["-c" "--coll COLLECTION" "Algo type" :default "[3 2 1]"]
    ["-h" "--help" "Print the help"]
  ])

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn usage [options-summary]
  ;http://clojuredocs.org/clojure_core/clojure.core/-%3E%3E
  (->> [
        "Usage: program-name [options] action"
        ""
        "Options:"
        options-summary
        ""
        "Actions:"
        "  algo-1         Executes the first assignment"
        ""
        "Please refer to the readme for more information."]
       (string/join \newline)))

(defn -main [& args]
  (let [ {:keys [options arguments errors summary]} (parse-opts args cli-options) ]
    (println options)
    ; Handle help and error conditions
    (cond
      (:help options)
        (exit 0 (usage summary))
      errors
        (exit 1 (println errors)))

    ; Execute program with options
    (case (first arguments)
      "algo-1"
        (println (algo-1 options))
      ;default
        (exit 1 (usage summary)))))

;; END

