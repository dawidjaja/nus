#include <cinttypes>
#include <chrono>
#include <iostream>
#include <limits>
#include <sstream>
#include "hash.h"

#define FREEZE_TIMESTAMP 0

// Big endian. Please reverse the bit position labels in the pdf next time.
union Input {
    struct {
        uint32_t dummy; // for alignment
        uint8_t prev_digest[32];
        uint32_t timestamp;
        uint64_t txn_id;
        uint64_t nonce;
    } as_parts;
    struct {
        uint32_t dummy; // for alignment
        uint8_t bytes[52];
    } as_bytes;
};

__managed__ Input input;
__managed__ uint8_t digest[32];
__managed__ bool is_done;
__managed__ uint64_t n;

uint64_t ascii_to_u64(const std::string str) {
    uint64_t result;
    std::istringstream iss(str);

    for (size_t i = 0; i < sizeof(uint64_t); i++) {
        uint8_t byte;
        iss >> byte;
        result = result << 8 | byte;
    }

    return result;
}

void check_cuda_errors() {
    cudaError_t rc;
    rc = cudaGetLastError();
    if (rc != cudaSuccess)
    {
        std::cout << "Last CUDA error: " << cudaGetErrorString(rc) << std::endl;
    }
}

__global__ void work() {
    Input localInput = input;
    uint8_t res[32];

    uint64_t start;
    size_t threadId = blockIdx.x * blockDim.x + threadIdx.x;
    size_t numThreads = gridDim.x * blockDim.x;
    start = (threadId * (ULLONG_MAX / numThreads));

    for (uint64_t nonce = start;; nonce++) {
        if (is_done) {
            break;
        }

        localInput.as_parts.nonce = nonce;
        sha256(res, localInput.as_bytes.bytes, 52);

        uint64_t prefix = 0;
        for (size_t i = 0; i < 8; i++) {
            prefix = (prefix << 8) | res[i];
        }
        if (prefix < n) {
            atomicExch((unsigned long long *)&input.as_parts.nonce, (unsigned long long)nonce);
            is_done = 1;
        }
        if (nonce == ULLONG_MAX) {
            break;
        }
    }
}

__global__ void get_final_digest() {
    sha256(digest, input.as_bytes.bytes, 52);
}

int main(int argc, char **argv) {
    srand(0);

    std::string prev_digest;
    std::cin >> prev_digest;
    const char *prev_digest_chars = prev_digest.c_str();
    for (size_t i = 0; i < 32 * sizeof(uint8_t); i++) {
        sscanf(prev_digest_chars + 2 * i, "%02hhx", input.as_parts.prev_digest + i);
    }

    // TODO: Generate this
#if FREEZE_TIMESTAMP
    uint32_t timestamp = 1601555562;
#else
    uint32_t timestamp = std::chrono::duration_cast<std::chrono::seconds>(
        std::chrono::system_clock::now().time_since_epoch()
    ).count();
#endif
    std::string raw_tid;
    std::cin >> raw_tid;
    uint64_t tid = ascii_to_u64(raw_tid);

    // uint64_t n;
    std::cin >> n;
    // TODO: Remove this
    // n = 1;
    // n <<= 60;

    input.as_parts.timestamp = __builtin_bswap32(timestamp);
    input.as_parts.txn_id = __builtin_bswap64(tid);

    int BLOCKS, THREADS;

    sscanf(argv[1], "%d", &BLOCKS);
    sscanf(argv[2], "%d", &THREADS);

    work<<<BLOCKS, THREADS>>>();
    cudaDeviceSynchronize();
    check_cuda_errors();

    get_final_digest<<<1, 1>>>();
    cudaDeviceSynchronize();
    check_cuda_errors();

    std::cout << __builtin_bswap32(input.as_parts.timestamp) << std::endl;
    std::cout << __builtin_bswap64(input.as_parts.nonce) << std::endl;
    for (size_t i = 0; i < 32; i++) {
        printf("%02x", digest[i]);
    }
    printf("\n");
}
