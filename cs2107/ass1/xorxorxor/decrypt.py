from binascii import hexlify
from itertools import cycle

def decrypt(flag):
    # f = open('flag', 'r')
    # flag = f.readline()

    # No one can guess my 512 bit key muahaha
    assert len(flag) < 64
    assert flag[:5] == 'flag{'
    assert flag[-1] == '}'

    key = list(map(ord, flag))

    f = open('transmission.txt', 'r')
    txt = bytearray.fromhex(f.read()[:-1])

    out = []

    key_pos = 0
    ctr = 0;
    for char in txt[:len(flag) * 30]:

        # Oh shit, key is too short, re-use the key
        if (key_pos >= len(key)): key_pos = 0

        out.append(chr(key[key_pos] ^ char))
        key_pos += 1

    print(''.join(out))



## TOP SECRET! FOLLOW PROTOCOL WHEN DECRYPTING MESSAGE! ##
if __name__ == '__main__':
    # 35 + 1 + 8 = 44
    
    print("=" * 44)
    decrypt("flag{XORd1naRy_c1pH3rs_3xtrXORdin4Ry_sP3ecH}")
