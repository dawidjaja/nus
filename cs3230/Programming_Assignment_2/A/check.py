import sys

# read input file
input_file = open(sys.argv[1], "r")
input_boxes = [line for line in input_file]
N = int(input_boxes[0])
W = list(map(int, input_boxes[1].split()))
S = list(map(int, input_boxes[2].split()))

# read user output
user_output = []
try:
    user_output = list(map(int, raw_input().split()))
except:
    print("Invalid output format")
    sys.exit(0)
if len(user_output) != N:
    print("Wrong number of boxes")
    sys.exit(0)
if set(user_output) != set(range(1, N + 1)):
    print("Box IDs are not from 1 to N")
    sys.exit(0)

# check validity of stack
total_weight = 0
for index in user_output:
    if S[index - 1] < total_weight:
        print("Box %d can only support %d" % (index, S[index - 1]))
        print("But total weight on it is %d" % total_weight)
        sys.exit(0)
    total_weight += W[index - 1]
print("Box stack is valid")