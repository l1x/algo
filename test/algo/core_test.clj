(ns algo.core-test
  (:require [clojure.test :refer :all]
            [algo.core    :refer :all]))
; simple input output pairs
;
(defn sort-pairs [typ] [ 
  [ {:type typ, :input "data/q1/test0.txt"}       [3   [1 2 3 4 5 6]]  ]
  [ {:type typ, :input "data/q1/test1.txt"}       [4   [1 2 3 4 5  ]]  ]
  [ {:type typ, :input "data/q1/test2.txt"}       [10  [1 2 3 4 5  ]]  ]
  [ {:type typ, :input "data/q1/test3.txt"}       [5   [1 2 3 4 5 6]]  ]
  [ {:type typ, :input "data/q1/test4.txt"}       [0   [0 0        ]]  ]
  [ {:type typ, :input "data/q1/test5.txt"}       [0   [0 1        ]]  ]
  [ {:type typ, :input "data/q1/test6.txt"}       [2   [0 1 1      ]]  ]
  [ {:type typ, :input "data/q1/test7.txt"}       [1   [0 1 1 1    ]]  ]
  [ {:type typ, :input "data/q1/test8.txt"}       [1   [0 0 0 1    ]]  ]
  ; std takes input from -c coll
  [ {:type typ, :input "std", :coll "[0 0 0 1]"}  [0   [0 0 0 1    ]]]
  [ {:type typ, :input "std", :coll "[0 0 1 0]"}  [1   [0 0 0 1    ]]]
  [ {:type typ, :input "std", :coll "[0 1 1 0]"}  [2   [0 0 1 1    ]]]
  ; errors - all of the functions catch all exceptions locally
  ; functions may return errors from underlying layer but not exceptions
  ; all the functions returning {:error...} on error
  [ {:type typ, :input "/dev/null"}
    {:error "Exception" :fn "read-file" :exception "Input is not a file"} ]
  ])


(defn count-change-pairs [typ] [
  [ {:type typ, :input "std", :coll "[100]"} 292 ]
  [ {:type typ, :input "std", :coll "[200]"} 2435 ]
  ;end
  ])

(deftest count-change-test
  (testing "count-change"
    (doseq
      [pair (count-change-pairs "count-change-clj")]
      (is
        (=
          (count-change-clj (nth pair 0)) (nth pair 1))))))

(deftest count-change-test
  (testing "count-change"
    (doseq
      [pair (count-change-pairs "count-change-sicp")]
      (is
        (=
          (count-change-clj (nth pair 0)) (nth pair 1))))))

(deftest merge-sort-test
  (testing "merge-sort"
    (doseq 
      [pair (sort-pairs "merge-sort")] 
      (is 
        (= 
          (merge-sort (nth pair 0)) (nth pair 1))))))
