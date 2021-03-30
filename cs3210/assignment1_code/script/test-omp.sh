#!/usr/bin/env sh

g++ -fopenmp -O3 -o lcs-omp LCS-omp.cpp
export OMP_NUM_THREADS=$1
perf stat -e duration_time -- ./lcs-omp testcases/DNA-10-1.in testcases/DNA-10-2.in
perf stat -e duration_time -- ./lcs-omp testcases/DNA-10-1.in testcases/DNA-10-2.in
perf stat -e duration_time -- ./lcs-omp testcases/DNA-10-1.in testcases/DNA-10-2.in
perf stat -e duration_time -- ./lcs-omp testcases/DNA-1000-1.in testcases/DNA-1000-2.in
perf stat -e duration_time -- ./lcs-omp testcases/DNA-1000-1.in testcases/DNA-1000-2.in
perf stat -e duration_time -- ./lcs-omp testcases/DNA-1000-1.in testcases/DNA-1000-2.in
perf stat -e duration_time -- ./lcs-omp testcases/DNA-100000-1.in testcases/DNA-100000-2.in
perf stat -e duration_time -- ./lcs-omp testcases/DNA-100000-1.in testcases/DNA-100000-2.in
perf stat -e duration_time -- ./lcs-omp testcases/DNA-100000-1.in testcases/DNA-100000-2.in
