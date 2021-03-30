echo "=========================================================================="
echo "=              BLOCKS: 80               THREADS PER BLOCK: 512           ="
echo "=========================================================================="
nvprof ./main 80 512 < testcases/tc1.in > testcases/out1.txt
nvprof ./main 80 512 < testcases/tc2.in > testcases/out2.txt
nvprof ./main 80 512 < testcases/tc3.in > testcases/out3.txt
nvprof ./main 80 512 < testcases/tc4.in > testcases/out4.txt
nvprof ./main 80 512 < testcases/tc5.in > testcases/out5.txt
