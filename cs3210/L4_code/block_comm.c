/**
 * CS3210 - Blocking communication in MPI.
 */

#include <mpi.h>
#include <stdio.h>

int main(int argc,char *argv[])
{
	int numtasks, rank, dest, source, rc, count, tag=1;  
	char inmsg, outmsg='x';
	MPI_Status Stat;

	MPI_Init(&argc,&argv);
	MPI_Comm_size(MPI_COMM_WORLD, &numtasks);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);

	if (rank == 0)	{
		dest = 1;
		source = 1;
        char mod[10] = "abcdefghi";
		rc = MPI_Send(&mod, 10, MPI_CHAR, dest, tag, MPI_COMM_WORLD);
		rc = MPI_Recv(&inmsg, 1, MPI_CHAR, source, tag, MPI_COMM_WORLD, &Stat);
		
	} 	else if (rank == 1)	{
		dest = 0;
		source = 0;
        char mod[10];
		rc = MPI_Send(&outmsg, 1, MPI_CHAR, dest, tag, MPI_COMM_WORLD);
		rc = MPI_Recv(&mod, 10, MPI_CHAR, source, tag, MPI_COMM_WORLD, &Stat);
        for (int i = 9; i >= 0; i--) {
            printf("%c\n", mod[i]);
        }
	}

	rc = MPI_Get_count(&Stat, MPI_CHAR, &count);
	printf("Task %d: Received %d char(s) from task %d with tag %d \n",
       		rank, count, Stat.MPI_SOURCE, Stat.MPI_TAG);

	MPI_Finalize();
	
	return 0;
}
