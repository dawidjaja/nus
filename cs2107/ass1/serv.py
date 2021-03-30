import requests
import itertools

def req(s):
    # print(s)
    url = "http://ctf.nus-cs2107.com:2773/"
    data = {"ciphertext": s}

    return ('success' in requests.post(url, data).text)

f = '591600719a2df281c50085ad4b4fc011514761db99482720e46b4de6f8f4fda9d6051555de5b8011d67d34267947e1c61d9fa7718eac446cb23e9b6d508f94f8d6bcc6f0c3cd0f37d0a051e90d9c3f55'

arr = [0] * 16

alphahex = '0123456789abcdef'

for j in range(32* 3, len(f), 32):
    b = f[j:j + 32]
    for i in range(15, -1, -1):
        for x in range(256):
            cur = [p ^ (16 - i) for p in arr]
            cur[i] = x
            cur = [alphahex[p // 16] + alphahex[p % 16] for p in cur]

            if (req(''.join(cur) + b)):
                arr[i] = (16 - i) ^ x
                print(i, x)
    cur = [alphahex[p // 16] + alphahex[p % 16] for p in arr]
    print(''.join(cur))
