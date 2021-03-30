/*******************************************************************
* prod-con-threads.c
* Producer Consumer With C
* Compile: gcc -pthread -o prodcont prod-con-threads.c
* Run: ./prodcont
*******************************************************************/
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#define PRODUCERS 2
#define CONSUMERS 1
#define MAX_N 100
#define MAX_SIZE 10

int producer_buffer[MAX_SIZE];
int cur_size = 0;
int consumer_sum = 0;
int producer_ptr = 0;
int consumer_ptr = 0;
int prod_cons_counter = 0;
int limit = 0;
pthread_mutex_t producer_lock = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t consumer_lock = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t size_lock = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t thread_count_lock = PTHREAD_MUTEX_INITIALIZER;

pthread_cond_t count_cur_size_consumer;
pthread_cond_t count_cur_size_producer;
pthread_cond_t final_sum_cond;

void* producer(void* threadid)
{
    while(1) {
        pthread_mutex_lock(&size_lock);
        while (cur_size == MAX_SIZE) {
            pthread_cond_wait(&count_cur_size_producer, &size_lock);
        }
        cur_size++;

        if (cur_size == 1) {
            pthread_cond_signal(&count_cur_size_consumer);
        }

        pthread_mutex_lock(&producer_lock);
        producer_buffer[producer_ptr] = (rand() % 10) + 1;

        producer_ptr++;
        producer_ptr %= MAX_SIZE;
        pthread_mutex_unlock(&producer_lock);

        pthread_mutex_lock(&thread_count_lock);
        limit++;
        if (limit >= MAX_N) {
            pthread_cond_signal(&final_sum_cond);
            pthread_mutex_unlock(&thread_count_lock);
            pthread_mutex_unlock(&size_lock);
            break;
        }
        pthread_mutex_unlock(&thread_count_lock);
        pthread_mutex_unlock(&size_lock);
    }
}

void* consumer(void* threadid)
{
    while(1) {
        pthread_mutex_lock(&size_lock);
        while (cur_size == 0) {
            pthread_cond_wait(&count_cur_size_consumer, &size_lock);
        }

        cur_size--;

        if (cur_size == MAX_SIZE - 1) {
            pthread_cond_signal(&count_cur_size_producer);
        }

        pthread_mutex_lock(&consumer_lock);
        consumer_sum += producer_buffer[consumer_ptr];
        consumer_ptr++;
        consumer_ptr %= MAX_SIZE;
        printf("Current sum = %d\n", consumer_sum);
        pthread_mutex_unlock(&consumer_lock);

        pthread_mutex_lock(&thread_count_lock);
        if (limit >= MAX_N) {
            pthread_mutex_unlock(&thread_count_lock);
            pthread_mutex_unlock(&size_lock);
            break;
        }
        pthread_mutex_unlock(&thread_count_lock);
        pthread_mutex_unlock(&size_lock);
    }
}

int main(int argc, char* argv[])
{
    pthread_t producer_threads[PRODUCERS];
    pthread_t consumer_threads[CONSUMERS];
    long producer_threadid[PRODUCERS];
    long consumer_threadid[CONSUMERS];

    pthread_cond_init (&count_cur_size_producer, NULL);
    pthread_cond_init (&count_cur_size_consumer, NULL);

    int rc;
    long t1, t2;
    for (t1 = 0; t1 < PRODUCERS; t1++) {
        int tid = t1;
        producer_threadid[tid] = tid;
        printf("creating producer %d\n", tid);
        rc = pthread_create(&producer_threads[tid], NULL, producer,
            (void*)producer_threadid[tid]);
        if (rc) {
            printf("Error: Return code from pthread_create() is %d\n", rc);
            exit(-1);
        }
    }

    for (t2 = 0; t2 < CONSUMERS; t2++) {
        int tid = t2;
        consumer_threadid[tid] = tid;
        printf("creating consumer %d\n", tid);
        rc = pthread_create(&consumer_threads[tid], NULL, consumer,
            (void*)consumer_threadid[tid]);
        if (rc) {
            printf("Error: Return code from pthread_create() is %d\n", rc);
            exit(-1);
        }
    }

    pthread_mutex_lock(&thread_count_lock);
    while (limit < MAX_N) {
        pthread_cond_wait(&final_sum_cond, &thread_count_lock);
        printf("### consumer_sum final value = %d ###\n",
            consumer_sum);
    }
    pthread_mutex_unlock(&thread_count_lock);
    pthread_exit(NULL);
}
