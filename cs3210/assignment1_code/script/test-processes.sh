#!/usr/bin/env sh

g++ -pthread -O3 -o lcs-processes LCS-processes.cpp
perf stat -e duration_time -- ./lcs-processes testcases/DNA-10-1.in testcases/DNA-10-2.in
perf stat -e duration_time -- ./lcs-processes testcases/DNA-10-1.in testcases/DNA-10-2.in
perf stat -e duration_time -- ./lcs-processes testcases/DNA-10-1.in testcases/DNA-10-2.in
perf stat -e duration_time -- ./lcs-processes testcases/DNA-1000-1.in testcases/DNA-1000-2.in
perf stat -e duration_time -- ./lcs-processes testcases/DNA-1000-1.in testcases/DNA-1000-2.in
perf stat -e duration_time -- ./lcs-processes testcases/DNA-1000-1.in testcases/DNA-1000-2.in
perf stat -e duration_time -- ./lcs-processes testcases/DNA-100000-1.in testcases/DNA-100000-2.in
perf stat -e duration_time -- ./lcs-processes testcases/DNA-100000-1.in testcases/DNA-100000-2.in
perf stat -e duration_time -- ./lcs-processes testcases/DNA-100000-1.in testcases/DNA-100000-2.in
