#include "tasks.h"
#include <stdbool.h>

// Struct used to implement a queue of task nodes
typedef struct task_node {
    task_t task;
    struct task_node *next;
} task_node_t;

int main(int argc, char *argv[]) {
    /*
     * =======================================================================
     * | [START]             DO NOT MODFIY THIS PORTION              [START] |
     * =======================================================================
     */
    int rank, num_procs;
    params_t params;

    // Initialise MPI processes and their ranks
    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &num_procs);

    /*
     * Create MPI datatypes from general datatypes, displacement and block sizes
     * Constant arrays initialised in tasks.h - beware of C structure packing!
     */
    MPI_Datatype MPI_PARAMS_T, MPI_METRIC_T;

    MPI_Type_create_struct(2, params_t_lengths, params_t_displs,
            params_t_types, &MPI_PARAMS_T);
    MPI_Type_create_struct(2, metric_t_lengths, metric_t_displs,
            metric_t_types, &MPI_METRIC_T);

    MPI_Type_commit(&MPI_PARAMS_T);
    MPI_Type_commit(&MPI_METRIC_T);

    /*
     * By default, mpirun directs stdin to the rank 0 process and directs
     * standard input to /dev/null on all other processes
     */
    if (rank == 0) {
        if (argc != 5) {
            printf("Syntax: ./distr-sched <H> <Nmin> <Nmax> <P>\n");
            printf("ERROR: incorrect number of command line arguments\n");
            MPI_Abort(MPI_COMM_WORLD, EXIT_FAILURE);
        }

        // Parse task generation parameters from command-line arguments
        params = parse_params(argv);
    }

    /*
     * Broadcast task generation parameters then set them as globals locally
     * All other processes block here until rank 0 process executes broadcast
     */
    MPI_Bcast(&params, 1, MPI_PARAMS_T, 0, MPI_COMM_WORLD);
    set_generation_params(&params);

    // Start tracking execution metrics for each MPI process
    timespec_t start;
    clock_gettime(CLOCK_MONOTONIC, &start);
    metric_t stats = { rank, { 0, 0, 0, 0, 0 }, 1, 0 };

    /*
     * =======================================================================
     * | [END]              OK TO MODIFY CODE AFTER HERE               [END] |
     * =======================================================================
     */

    const int nitems=8;
    int          blocklengths[8] = {1, 1, 1, 1, 1, 1, 4, 4};
    MPI_Datatype mpi_task_t;

    MPI_Aint     offsets[8];
    offsets[0] = offsetof(task_t, id);
    offsets[1] = offsetof(task_t, gen);
    offsets[2] = offsetof(task_t, type);
    offsets[3] = offsetof(task_t, arg_seed);
    offsets[4] = offsetof(task_t, output);
    offsets[5] = offsetof(task_t, num_dependencies);
    offsets[6] = offsetof(task_t, dependencies);
    offsets[7] = offsetof(task_t, masks);

    MPI_Datatype types[8] = {
        MPI_UNSIGNED,
        MPI_INT,
        MPI_INT,
        MPI_UNSIGNED,
        MPI_UNSIGNED,
        MPI_INT,
        MPI_UNSIGNED,
        MPI_UNSIGNED,
    };

    MPI_Type_create_struct(nitems, blocklengths, offsets, types, &mpi_task_t);
    MPI_Type_commit(&mpi_task_t);
    int default_tag = 1;
    int poison_tag = 2;

    if (rank == 0) {
        // Head and tail pointers of the task queue
        task_node_t *head, *tail;
        head = tail = NULL;

        // Read initial tasks
        int count;
        scanf("%d", &count);

        for (int i = 0; i < count; i++) {
            task_node_t *node = (task_node_t *)calloc(1, sizeof(task_node_t));
            if (node == NULL) {
                fprintf(stderr, "Failed to calloc task queue node\n");
                MPI_Abort(MPI_COMM_WORLD, EXIT_FAILURE);
            }

            scanf("%d %u", &node->task.type, &node->task.arg_seed);
            node->task.id = node->task.arg_seed;
            node->task.gen = 0;
            node->next = NULL;

            if (i == 0) {
                // First take is both head and tail of queue
                head = node;
                tail = node;
            } else {
                // Second task onwards added at tail
                tail->next = node;
                tail = node;
            }
        }

        // Declare array to store generated descendant tasks
        int num_new_tasks = 0;
        task_t *task_buffer = (task_t *)malloc(Nmax * sizeof(task_t));
        int running_workers = 0;
        bool available[num_procs];
        for (int i = 1; i < num_procs; i++) {
            available[i] = true;
        }

        // Clear tasks in queue one by one
        while (true) {
            // Terminate when done
            if (head == NULL && running_workers == 0) {
                for (int i = 1; i < num_procs; i++) {
                    MPI_Send(NULL, 0, mpi_task_t, i, poison_tag,
                             MPI_COMM_WORLD);
                }
                break;
            }

            // Assign tasks
            for (int i = 1; i < num_procs; i++) {
                task_node_t *curr = head;

                if (head == NULL) {
                    break;
                }

                if (available[i]) {
                    MPI_Send(&curr->task, 1, mpi_task_t, i, default_tag,
                             MPI_COMM_WORLD);
                    available[i] = false;
                    running_workers++;

                    // Destroy node for completed task
                    head = head->next;
                    free(curr);
                }
            }

            // Receive new tasks
            MPI_Status status;
            MPI_Recv(task_buffer, Nmax, mpi_task_t, MPI_ANY_SOURCE, MPI_ANY_TAG,
                     MPI_COMM_WORLD, &status);
            MPI_Get_count(&status, mpi_task_t, &num_new_tasks);
            int worker = status.MPI_SOURCE;
            available[worker] = true;
            running_workers--;

            for (int i = 0; i < num_new_tasks; i++) {
                // Construct new node for descendant task
                task_node_t *node = calloc(1, sizeof(task_node_t));
                node->task = task_buffer[i];
                node->next = head;
                head = node;
            }
        }

        free(task_buffer);
    } else {
        int num_new_tasks = 0;
        task_t *task_buffer = (task_t *)malloc(Nmax * sizeof(task_t));

        while (true) {
            task_t task;
            MPI_Status status;
            MPI_Recv(&task, 1, mpi_task_t, 0, MPI_ANY_TAG, MPI_COMM_WORLD,
                     &status);

            if (status.MPI_TAG == poison_tag) {
                break;
            }

            execute_task(&stats, &task, &num_new_tasks, task_buffer);
            MPI_Send(task_buffer, num_new_tasks, mpi_task_t, 0, default_tag,
                     MPI_COMM_WORLD);
        }

        free(task_buffer);
    }

    /*
     * =======================================================================
     * | [START]             DO NOT MODFIY THIS PORTION              [START] |
     * =======================================================================
     */
    MPI_Barrier(MPI_COMM_WORLD);

    // Complete tracking execution metrics for each process
    timespec_t end;
    clock_gettime(CLOCK_MONOTONIC, &end);
    stats.total_time += compute_interval(&start, &end);

    if (DEBUG == 0) {
        if (rank == 0) {
            metric_t *metrics = (metric_t*)
                malloc(num_procs * sizeof(metric_t));
            if (metrics == NULL) {
                fprintf(stderr,
                        "Failed to malloc buffer for execution metrics\n");
                MPI_Abort(MPI_COMM_WORLD, EXIT_FAILURE);
            }

            MPI_Gather(&stats, 1, MPI_METRIC_T, metrics, 1, MPI_METRIC_T,
                    0, MPI_COMM_WORLD);

            // Print execution metrics by rank order
            for (int i = 0; i < num_procs; i++) {
                print_metrics(&metrics[i]);
            }

            free(metrics);
        } else {
            MPI_Gather(&stats, 1, MPI_METRIC_T, NULL, 0, MPI_DATATYPE_NULL,
                    0, MPI_COMM_WORLD);
        }
    }

    MPI_Barrier(MPI_COMM_WORLD);

    // Terminate MPI execution environment
    MPI_Finalize();

    return 0;
    /*
     * =======================================================================
     * | [END]                  END OF SKELETON CODE                   [END] |
     * =======================================================================
     */
}
