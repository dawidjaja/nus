import sys
import os
from socket import *

key = {}

class HttpRequest:
    def __init__(self, sentence):
        (self.header, self.body) = sentence

    def httpResponse(self, status, cLen=-1, content=b''):
        resp = ""
        resp += str(status) + ' '
        if (status == 200):
            resp += "OK"
        elif (status == 404):
            resp += "NotFound"
        elif (status == 403):
            resp += "Forbidden"
        if (cLen == -1):
            resp += "  "
            # # print("httpResponse -> " + resp)
            return (resp, content)
        resp += ' content-length ' + str(cLen) + '  '
        # # print("httpResponse -> " + resp)
        return (resp, content)

    def parseRequest(self):
        # # print("ini request list '" + self.request + "'")
        requestList = self.header.split(' ')
        method = requestList[0].lower()
        path = requestList[1]
        requestList = self.header.lower().split(' ')
        # # print(requestList)
        try:
            clPos = requestList.index('content-length') 
        except ValueError:
            clPos = -1
            
        contentLength = int(requestList[clPos + 1]) if clPos != -1 else -1
        contentBody = self.body
        # contentBody = self.request.split(' ')[-1] if contentLength != -1 else ""
        if (path[:4] == '/key'):
            if (method == 'post'):
                key[path[5:]] = (contentLength, contentBody)
                # # print("pas post ada '" + str(contentLength) + "' cBody '" + contentBody + "'")
                return self.httpResponse(200)
            elif (method == 'get'):
                if (path[5:] in key):
                    (cLen, cBody) = key[path[5:]]
                    # # print("get clen '", cLen, "' cbody'", cBody, "'")
                    return self.httpResponse(200, cLen, cBody)
                else:
                    return self.httpResponse(404)
            elif (method == 'delete'):
                if (path[5:] in key):
                    (cLen, cBody) = key[path[5:]]
                    key.pop(path[5:])
                    return self.httpResponse(200, cLen, cBody)
                else:
                    return self.httpResponse(404)
        else:
            path = '.' + path[5:]
            try:
                content = ""
                with open(path, "rb") as f:
                    content = f.read()
                    f.close()
                    cLen = len(content)
                    # print("cLen ", cLen)
                    # print(content)
                    return self.httpResponse(200, cLen, content)
            except FileNotFoundError:
                return self.httpResponse(404)
            except PermissionError:
                return self.httpResponse(403)

class Reciever:
    def __init__(self):
        self.msg = bytearray()
    
    def getContentLength(self, header):
        pos = header.lower().index('content-length')
        return int(header[pos:].split(' ')[1])

    def hasRequest(self, msg):
        if (msg == None):
            return False
        # # print('ini nanya request ada ato kaga "', msg, '"')
        # print("msg type = ", type(msg))
        # # print("'" + msg + "'")
        if ('  '.encode() not in msg):
            return False
        header = msg[:msg.index('  '.encode())].decode()
        if ('content-length' in header.lower()):
            # # print('ini harusnya ada content length "', header,'"')
            cLen = self.getContentLength(header)
            if (len(msg[:msg.index('  '.encode()) + 2]) < cLen):
                return False
        return True

    def getRequest(self, msg):
        header = msg[:msg.index('  '.encode())].decode()
        cLen = 0
        if ('content-length' in header.lower()):
            cLen = self.getContentLength(header)
        tmp = msg.index('  '.encode()) + len('  '.encode())
        # print("header", header)
        # print("getRequest tmp '", tmp, "'")
        return (header + '  ', msg[tmp : tmp + cLen])

    def recv(self, connectionSocket):
        # print('self.msg = ', self.msg)
        while (not self.hasRequest(self.msg)):
            tmp = connectionSocket.recv(1024)
            # # print('tmp ada "' + tmp + '"')
            if (len(tmp) == 0):
                return None
            self.msg += tmp
            # print('self.msg = ', self.msg)
        request = self.getRequest(self.msg)
        # print('request = ', request)
        (a, b) = request
        self.msg = self.msg[len(a) + len(b):]
        return request

class WebServer:
    def __init__(self):
        serverSocket = 0

    def encodeHeader(self, msg):
        # # print('mau diencode "' + msg + '"')
        (a, b) = msg
        ret = (a).encode()
        # # print(type(ret), type(b))
        ret += b
        return ret

    def handleClientSocket(self):
        connectionSocket, addr = self.serverSocket.accept()
        rcv = Reciever()
        r = rcv.recv(connectionSocket)
        while (r != None):
            httpQuery = HttpRequest(r)
            tmp = httpQuery.parseRequest()
            # # print("ini response blom di encode '" + tmp + "'")  
            connectionSocket.send(self.encodeHeader(tmp))
            # # print('output = "' + tmp + '"')
            r = rcv.recv(connectionSocket)
        connectionSocket.close()

    def start(self, serverPort):
        self.serverSocket = socket(AF_INET, SOCK_STREAM)
        self.serverSocket.bind(('', serverPort))
        self.serverSocket.listen(1)
        print("Server establish!")
        while True:
            self.handleClientSocket()

def main():
    server = WebServer()
    server.start(int(sys.argv[1]))

if __name__ == "__main__":
    main()
