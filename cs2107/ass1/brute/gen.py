import string
alphabet = string.ascii_lowercase[::-1]

def rec(s):
    if (len(s) == 5):
        print(s)
    else:
        for i in alphabet:
            rec(s + i)

rec("")
