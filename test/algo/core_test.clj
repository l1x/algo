(ns algo.core-test
  (:require [clojure.test :refer :all]
            [algo.core :refer :all]))

(deftest algo-1-test-0
  (testing "Testcase: 0 {1,3,5,2,4,6}"
    (is (= (get-in (algo-1-brute-force-inversions {:input "data/q1/test0.txt" }) [:ok :inversions]) 3))))

(deftest algo-1-test-1
  (testing "Testcase: 1 {1,5,3,2,4}"
    (is (= (get-in (algo-1-brute-force-inversions {:input "data/q1/test1.txt" }) [:ok :inversions]) 4))))

(deftest algo-1-test-2
  (testing "Testcase: 2 {5,4,3,2,1}"
    (is (= (get-in (algo-1-brute-force-inversions {:input "data/q1/test2.txt" }) [:ok :inversions]) 10))))

(deftest algo-1-test-3
  (testing "Testcase: 3 {1,6,3,2,4,5}"
    (is (= (get-in (algo-1-brute-force-inversions {:input "data/q1/test3.txt" }) [:ok :inversions]) 5))))

(deftest algo-1-test-4
  (testing "Testcase: 4 {0,0}"
    (is (= (get-in (algo-1-brute-force-inversions {:input "data/q1/test4.txt" }) [:ok :inversions]) 0))))

(deftest algo-1-test-5
  (testing "Testcase: 5 {0,1}"
    (is (= (get-in (algo-1-brute-force-inversions {:input "data/q1/test5.txt" }) [:ok :inversions]) 0))))

(deftest algo-1-test-6
  (testing "Testcase: 6 {1, 1, 0}"
    (is (= (get-in (algo-1-brute-force-inversions {:input "data/q1/test6.txt" }) [:ok :inversions]) 2))))

(deftest algo-1-test-7
  (testing "Testcase: 7 {1, 0, 1, 1}"
    (is (= (get-in (algo-1-brute-force-inversions {:input "data/q1/test7.txt" }) [:ok :inversions]) 1))))

(deftest algo-1-test-8
  (testing "Testcase: 7 {0, 0, 1, 0}"
    (is (= (get-in (algo-1-brute-force-inversions {:input "data/q1/test8.txt" }) [:ok :inversions]) 1))))

(deftest algo-1-test-9
  (testing "Testcase: 9 {...}"
    (is (= (get-in (algo-1-brute-force-inversions {:input "data/q1/hw1_test" }) [:ok :inversions]) 242698))))

(deftest algo-1-test-10
  (testing "Testcase: 10 MergeSort (time (mrg-srt (shuffle (range 1000000))))"
    (is (= (range 1000000) (mrg-srt (shuffle (range 1000000)))))))

(deftest algo-1-test11 
  (testing "Testcase: 11 BruteForce (time (algo-1-brt-frc-invs (shuffle (range 100))))"
    (algo-1-brt-frc-invs (into [] (shuffle (range 100))))))

(deftest algo-1-test-not-a-file
  (testing "Testcase: not-a-file /dev/null"
    (is 
      (= 
        (algo-1-brute-force-inversions {:input "/dev/null"}) 
        {:error "Exception" :fn "read-file" :exception "Input is not a file"}))))
