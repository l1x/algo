(ns algo.core-test
  (:require [clojure.test :refer :all]
            [algo.core    :refer :all]))

(def test-pairs [
      [ {:type "merge-sort", :input "data/q1/test0.txt"} [1 2 3 4 5 6]  ]
      [ {:type "merge-sort", :input "data/q1/test1.txt"} [1 2 3 4 5]    ]
      [ {:type "merge-sort", :input "data/q1/test2.txt"} [1 2 3 4 5]    ]
      [ {:type "merge-sort", :input "data/q1/test3.txt"} [1 2 3 4 5 6]  ]
      [ {:type "merge-sort", :input "data/q1/test4.txt"} [0 0]          ]
      [ {:type "merge-sort", :input "data/q1/test5.txt"} [0 1]          ]
      [ {:type "merge-sort", :input "data/q1/test6.txt"} [0 1 1]        ]
      [ {:type "merge-sort", :input "data/q1/test7.txt"} [0 1 1 1]      ]
      [ {:type "merge-sort", :input "data/q1/test8.txt"} [0 0 0 1]      ]
      ; errors
      [ {:type "merge-sort", :input "/dev/null/"}
        {:error "Exception" :fn "read-file" :exception "Input is not a file"} ]
  ])

(deftest merge-sort-test
  (testing "merge-sort"
    (doseq 
      [pair test-pairs] 
      (is 
        (= 
          (merge-sort (nth pair 0)) (nth pair 1))))))
