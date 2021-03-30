echo "=========================================================================="
echo "=              BLOCKS: 40               THREADS PER BLOCK: 512           ="
echo "=========================================================================="
nvprof ./main 40 512 < testcases/tc$1.in
nvprof ./main 40 512 < testcases/tc$1.in
nvprof ./main 40 512 < testcases/tc$1.in
nvprof ./main 40 512 < testcases/tc$1.in
nvprof ./main 40 512 < testcases/tc$1.in
echo "=========================================================================="
echo "=              BLOCKS: 40               THREADS PER BLOCK: 256          ="
echo "=========================================================================="
nvprof ./main 40 256 < testcases/tc$1.in
nvprof ./main 40 256 < testcases/tc$1.in
nvprof ./main 40 256 < testcases/tc$1.in
nvprof ./main 40 256 < testcases/tc$1.in
nvprof ./main 40 256 < testcases/tc$1.in
echo "=========================================================================="
echo "=              BLOCKS: 80               THREADS PER BLOCK: 512           ="
echo "=========================================================================="
nvprof ./main 80 512 < testcases/tc$1.in
nvprof ./main 80 512 < testcases/tc$1.in
nvprof ./main 80 512 < testcases/tc$1.in
nvprof ./main 80 512 < testcases/tc$1.in
nvprof ./main 80 512 < testcases/tc$1.in
echo "=========================================================================="
echo "=              BLOCKS: 80               THREADS PER BLOCK: 256          ="
echo "=========================================================================="
nvprof ./main 80 256 < testcases/tc$1.in
nvprof ./main 80 256 < testcases/tc$1.in
nvprof ./main 80 256 < testcases/tc$1.in
nvprof ./main 80 256 < testcases/tc$1.in
nvprof ./main 80 256 < testcases/tc$1.in
