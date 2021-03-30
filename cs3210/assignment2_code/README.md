# Bitcompute

To compile for the T4:

``` sh
cmake -DCMAKE_CUDA_FLAGS="-arch=compute_75" .
make
```

For the V100:

``` sh
nvcc -arch=compute_70 -dc hash.cu -o hash.o
nvcc -arch=compute_70 -std=c++11 -dc main.cu -o main.o
nvcc -arch=compute_70 main.o hash.o -o main
```

To run the test cases (e.g. tc1 on T4):

``` sh
./bench_t4.sh 1
```
