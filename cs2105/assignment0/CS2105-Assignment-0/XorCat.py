import sys

bufferarr = bytearray(1024) # allocate the buffer
filename = sys.argv[1]
mask = sys.argv[2]

with open(filename, "rb") as f:
    numBytesRead = f.readinto(bufferarr)
    while numBytesRead > 0:
        for i in range(0, numBytesRead):
            bufferarr[i] = bufferarr[i] ^ int(mask)
        sys.stdout.buffer.write(bufferarr[0:numBytesRead])
        numBytesRead = f.readinto(bufferarr)

