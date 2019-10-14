import sys

# read input file
input_file = open(sys.argv[1], "r")
input_boxes = [line for line in input_file]
N, S = map(int, input_boxes[0].split())
W = list(map(int, input_boxes[1].split()))

# read user output
user_output = []
try:
    user_output = list(map(int, raw_input().split()))
except:
    print("Invalid output format")
    sys.exit(0)
for index in user_output:
    if index < 1 or index > N:
        print("Box IDs not from 1 to N")
        sys.exit(0)

# check validity of stack
total_weight = 0
for index in user_output:
    if S < total_weight:
        print("Box %d can only support %d" % (index, S))
        print("But total weight on it is %d" % total_weight)
        sys.exit(0)
    total_weight += W[index - 1]
print("Box stack is valid")
print("Total weight is %d" % total_weight)