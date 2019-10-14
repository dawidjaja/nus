import sys
import zlib

with open(sys.argv[1], "rb") as f:
    bits = f.read()
checksum = zlib.crc32(bits)

print(checksum)

