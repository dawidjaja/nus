echo "testing 16 threads"
echo "1 core"
bash script/test-omp.sh 16 1
echo "2 cores"
bash script/test-omp.sh 16 2
echo "4 cores"
bash script/test-omp.sh 16 4
echo "8 cores"
bash script/test-omp.sh 16 8
echo "16 cores"
bash script/test-omp.sh 16 16
echo "32 cores"
bash script/test-omp.sh 16 32
echo "40 cores"
bash script/test-omp.sh 16 40
