import sys

ip = sys.argv[1]
num = '.'.join([str(int(ip[i:i+8], 2)) for i in range(0, len(ip), 8)])

print(num)

