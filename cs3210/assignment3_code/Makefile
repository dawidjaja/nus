all: compile compile-skeleton

compile:
	mpicc tasks.h tasks.c main.c -lm -Wpedantic -Wall -o distr-sched

config-chains:
	mpirun -np 4 ./distr-sched 8 1 1 0.00 < chains.in > chains.out

config-heaps:
	mpirun -np 4 ./distr-sched 5 2 2 0.00 < heaps.in > heaps.out

config-sparse:
	mpirun -np 4 ./distr-sched 12 0 10 0.16 < sparse.in > sparse.out

config-dense:
	mpirun -np 4 ./distr-sched 5 3 5 0.50 < dense.in > dense.out

config-shallow-4-0:
	mpirun -np 4 --hostfile hostfile-4-0 ./distr-sched 0 0 0 0.00 < shallow.in > shallow-4-0.out

config-shallow-2-2:
	mpirun -np 4 --hostfile hostfile-2-2 ./distr-sched 0 0 0 0.00 < shallow.in > shallow-2-2.out

config-shallow-0-4:
	mpirun -np 4 --hostfile hostfile-0-4 ./distr-sched 0 0 0 0.00 < shallow.in > shallow-0-4.out

compile-skeleton:
	mpicc tasks.h tasks.c skeleton.c -lm -Wpedantic -Wall -o skeleton

config-chains-skeleton:
	mpirun -np 4 ./skeleton 8 1 1 0.00 < chains.in > chains-skeleton.out

config-heaps-skeleton:
	mpirun -np 4 ./skeleton 5 2 2 0.00 < heaps.in > heaps-skeleton.out

config-sparse-skeleton:
	mpirun -np 4 ./skeleton 12 0 10 0.16 < sparse.in > sparse-skeleton.out

config-dense-skeleton:
	mpirun -np 4 ./skeleton 5 3 5 0.50 < dense.in > dense-skeleton.out

config-shallow-skeleton:
	mpirun -np 4 ./skeleton 0 0 0 0.00 < shallow.in > shallow-skeleton.out
