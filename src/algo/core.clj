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
  ([xs ys] (mrg 0 [] xs ys))
  ;(mrg 0 [] coll1 coll2)
  ([cnt result xs ys]
    ;(println "mrg:: cnt: " cnt " result: " result " xs: " xs " ys: " ys)
    (let [x (first xs) y (first ys)]
    (cond
      (nil? x)
        [cnt (vec (concat result ys))]
      (nil? y) 
        [cnt (vec (concat result xs))]
      :else 
        (if (<= x y)
          ;than  ;cnt               ;acc            ;xs       ;ys
          (recur cnt                (conj result x) (rest xs) ys)
          ;else
          (recur (+ cnt (count xs)) (conj result y) xs        (rest ys)))))))
 
(defn mrg-srt
  [xs]
  ;(println (type xs))
  (let 
  [cnt-xs (count xs)]
    (cond
      (> 2 cnt-xs) [0 xs]
      :else
        (let 
          [ [left right]  (split-at (quot cnt-xs 2) xs) 
            [cntl xss]    (mrg-srt left)
            [cntr yss]    (mrg-srt right) ]
          ;merge
          (mrg (+ cntl cntr) [] xss yss)))))

;1:1 sicp lisp -> clojure
(defn cnt-cng 
  [coll]
  (let [[amount] coll]
  (defn first-denom [kinds-of-coins]
    (cond (= kinds-of-coins 1) 1
          (= kinds-of-coins 2) 5
          (= kinds-of-coins 3) 10
          (= kinds-of-coins 4) 25
          (= kinds-of-coins 5) 50))
  (defn cc 
    [amount kinds-of-coins]
    (cond (= amount 0) 1
          (or (< amount 0)
              (= kinds-of-coins 0)) 0
          :else (+ (cc amount (- kinds-of-coins 1))
                   (cc (- amount 
                          (first-denom kinds-of-coins))
                       kinds-of-coins))))
  
  (cc amount 5)))

;the number of ways giving change for x with coins '(1 5...)
;(+ 
;   the number of ways giving change for x with the rest of the coins
;   the number of ways giving change for (- x (first coins)) coins)
(defn cnt-cng-2 
  ([n] (cnt-cng-2 n (list 50 25 10 5 1)))
  ([n coll] 
  (let [ [x] n
         coins coll
         fst (first coins) 
         rst (rest coins) ]
    (cond
      (empty? coins)  0
      (< x 0)         0
      (zero? x)       1
      :else 
      (+ (cnt-cng-2 [x] rst)
         (cnt-cng-2 [(- x fst)] coins))))))

(defn dispatch-fn 
  ;This functions takes a name (fun) and returns a function with that name
  ;this handles optionsi, example:
  ;{:type typ, :input "std", :coll "[100]", 
  ; :rst "{:coins [50 25 10 5 1]}" }
  ; based on the options it calls the idempotent function with coll
  [fun] 
  (fn [options] 
    (let [ input (:input options) coll (:coll options) rst (:rst options) ]
      (cond
        (and (not (nil? coll)) (= input "std"))
          ;this is supposed to be an the rest of the options (rst)
          ;(fun [coll rst]))
          (fun (read-string coll))
        ;defaulting to reading the input file
        :else
          (let
            [data (string-to-vec (read-file input))]
            (cond
              (contains? data :ok)
                ;this is supposed to be an the rest of the options (rst)
                ;(fun [coll rst]))
                (fun (:ok data))
              :else
                ;if the file reading returns an error
                data))))))

(def brute-force-inversions 
  (dispatch-fn brt-frc-invs))
(def merge-sort 
  (dispatch-fn mrg-srt))
(def count-change-sicp
  (dispatch-fn cnt-cng))
(def count-change-clj
  (dispatch-fn cnt-cng-2))

(defn route
  [options]
  (let [fn-typ (:type options)]
  (cond
    (= fn-typ "brute-force-inversions")
      (brute-force-inversions options)
    (= fn-typ "merge-sort")
      (merge-sort options)
    (= fn-typ "count-change-clj")
      (count-change-clj options)
    (= fn-typ "count-change-sicp")
      (count-change-sicp options)
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
    ["-c" "--coll COLLECTION" "Vector or other coll type" :default "[3 2 1]"]
    ["-r" "--rest {}" "Arbitrary HashMap" :default "{}"]
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
        "  algo-1         Collection of Algo-I assignments"
        "  sicp           SICP examples"
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
        (println (route options))
      "sicp"
        (println (route options))
      ;default
        (exit 1 (usage summary)))))

;; END

