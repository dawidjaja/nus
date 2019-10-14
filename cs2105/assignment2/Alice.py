from socket import *
import signal
import math
import re
import sys
import zlib

def num2bytes(x):
    return x.to_bytes(4, 'big')

def bytes2num(x):
    return int.from_bytes(x, 'big')

def modify(st, seq):
    tmp = num2bytes(seq)
    cs = num2bytes(zlib.crc32(st.encode()))
    # print(tmp, cs, st.encode())
    st = tmp + cs + st.encode()
    # print(st)
    return st

def parse(data):
    try:
        seq = data[4:]
        cs = data[:4]
        if (cs == num2bytes(zlib.crc32(seq))):
            return (int(seq), True)
        return (int(seq), False)
    except:
        return (0, False)

def main():
    UDP_IP = "localhost"
    UDP_PORT = int(sys.argv[1])

    '''
    print("UDP target IP:", UDP_IP)
    print("UDP target port:", UDP_PORT)
    '''

    sock = socket(AF_INET, SOCK_DGRAM) # UDP
    sock.settimeout(0.05)
    seq = 0
    '''
    input_message = ''
    while True:
        try:
            input_message += input() + '\n'
        except EOFError:
            break

    input_message = input_message[:-1]
    '''
    input_message = sys.stdin.read()
    in_m = []
    for i in range(0, math.ceil(len(input_message) / 20)):
        in_m.append(input_message[20 * i:20 * (i + 1)])

    '''
    for i in in_m:
        print('-' *56)
        print(i)
    '''

    for MESSAGE in in_m:
        is_done = False
        while (not is_done):
            try:
                # print("seq, message", seq, MESSAGE)
                mes = modify(MESSAGE, seq)
                sock.sendto(mes, (UDP_IP, UDP_PORT))
                ack, addr = sock.recvfrom(1024)
                (ack, check) = parse(ack)
                # print("aa", ack, check)
                if (check and ack == seq + len(MESSAGE)):
                    # print('True')
                    is_done = True
                # print('ack = ', ack)
            except timeout: 
                pass
                # print('Timeout')
        seq += len(MESSAGE)

if __name__ == '__main__':
    main()
