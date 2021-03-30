/*
 * Hello World in CUDA
 *
 * CS3210
 *
 * This program start from "hello world" string and should print "HELLO WORLD"
 *
 */

#include <stdio.h>

#define N       32 

// #define      DISCRETE

__global__ void hello(char *a, int len)
{
        int tid = blockIdx.z * 32 + blockIdx.y * 16 + blockIdx.x * 8 + threadIdx.x * 4 + threadIdx.y;
        if (tid >= len)
                return;
        a[tid] += 'A' - 'a';
}

int main()
{
        // original string
        char a[N] = "hello@world";
        // length
        int len = strlen(a);
        // pointer to the string on device
        char* ad;
        // pointer to the final string on host
        char* ah;
        // CUDA returned error code
        cudaError_t rc;


        //allocate space for the string on device (GPU) memory
        cudaMalloc((void**)&ad, N);
        cudaMemcpy(ad, a, N, cudaMemcpyHostToDevice);

        dim3 gridDimensions (2, 2, 2);
        dim3 blockDimensions (2, 4);

        // launch the kernel
        hello<<<gridDimensions, blockDimensions>>>(ad, len);
        cudaDeviceSynchronize();

	// for discrete GPUs, get the data from device memory to host memory
        cudaMemcpy(a, ad, N, cudaMemcpyDeviceToHost);
        ah = a;

        // was there any error?
        rc = cudaGetLastError();
        if (rc != cudaSuccess)
                printf("Last CUDA error %s\n", cudaGetErrorString(rc));

        // print final string
        printf("%s!\n", ah);

        // free memory
        cudaFree(ad);

        return 0;
}

