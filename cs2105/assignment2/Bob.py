from socket import *
import sys
import zlib

def num2bytes(x):
    return x.to_bytes(4, 'big')

def bytes2num(x):
    return int.from_bytes(x, 'big')

def modify(seq):
    cs = num2bytes(zlib.crc32(seq.encode()))
    st = cs + seq.encode()
    return st

def parse(data):
    try:
        mes = data[8:]
        cs = data[4:8]
        seq = bytes2num(data[:4])
        new_cs = num2bytes(zlib.crc32(mes))
        mes = mes.decode()
        if (new_cs == cs):
            return (mes, seq, True)
        return (mes, seq, False)
    except:
        return ('', 0, False)

def main():
    UDP_IP = "localhost"
    UDP_PORT = int(sys.argv[1])

    sock = socket(AF_INET, SOCK_DGRAM) # UDP
    sock.bind((UDP_IP, UDP_PORT))

    exp_seq = 0

    while True:
        data, addr = sock.recvfrom(1024) # buffer size is 1024 bytes
        (mes, seq, check) = parse(data)
        if (seq != exp_seq):
            sock.sendto(modify(str(exp_seq)), addr)
            continue
        # print("received message:", mes, check)
        if (check == True):
            print(mes, end='')
            sys.stdout.flush()
            exp_seq = exp_seq + len(mes)
            # print("check = true", exp_seq)
        sock.sendto(modify(str(exp_seq)), addr)


if __name__ == '__main__':
    main()
