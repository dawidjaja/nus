#!/usr/bin/env sh

g++ -O3 -o lcs-seq LCS-seq.cpp
perf stat -e duration_time -- ./lcs-seq testcases/DNA-10-1.in testcases/DNA-10-2.in
perf stat -e duration_time -- ./lcs-seq testcases/DNA-10-1.in testcases/DNA-10-2.in
perf stat -e duration_time -- ./lcs-seq testcases/DNA-10-1.in testcases/DNA-10-2.in
perf stat -e duration_time -- ./lcs-seq testcases/DNA-1000-1.in testcases/DNA-1000-2.in
perf stat -e duration_time -- ./lcs-seq testcases/DNA-1000-1.in testcases/DNA-1000-2.in
perf stat -e duration_time -- ./lcs-seq testcases/DNA-1000-1.in testcases/DNA-1000-2.in
perf stat -e duration_time -- ./lcs-seq testcases/DNA-100000-1.in testcases/DNA-100000-2.in
perf stat -e duration_time -- ./lcs-seq testcases/DNA-100000-1.in testcases/DNA-100000-2.in
perf stat -e duration_time -- ./lcs-seq testcases/DNA-100000-1.in testcases/DNA-100000-2.in
