#!/usr/bin/env sh

g++ -pthread -O3 -o lcs-threads LCS-threads.cpp
perf stat -e duration_time -- ./lcs-threads testcases/DNA-10-1.in testcases/-10-2.in
perf stat -e duration_time -- ./lcs-threads testcases/DNA-10-1.in testcases/-10-2.in
perf stat -e duration_time -- ./lcs-threads testcases/DNA-10-1.in testcases/-10-2.in
perf stat -e duration_time -- ./lcs-threads testcases/DNA-1000-1.in testcases/-1000-2.in
perf stat -e duration_time -- ./lcs-threads testcases/DNA-1000-1.in testcases/-1000-2.in
perf stat -e duration_time -- ./lcs-threads testcases/DNA-1000-1.in testcases/-1000-2.in
perf stat -e duration_time -- ./lcs-threads testcases/DNA-100000-1.in testcases/-100000-2.in
perf stat -e duration_time -- ./lcs-threads testcases/DNA-100000-1.in testcases/-100000-2.in
perf stat -e duration_time -- ./lcs-threads testcases/DNA-100000-1.in testcases/-100000-2.in
