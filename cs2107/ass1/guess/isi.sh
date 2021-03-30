while true; do
  curl 'http://ctf.nus-cs2107.com:8888/submit' \
    -H 'Connection: keep-alive' \
    -H 'Accept: */*' \
    -H 'X-Requested-With: XMLHttpRequest' \
    -H 'User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36' \
    -H 'Content-Type: application/x-www-form-urlencoded; charset=UTF-8' \
    -H 'Origin: http://ctf.nus-cs2107.com:8888' \
    -H 'Referer: http://ctf.nus-cs2107.com:8888/' \
    -H 'Accept-Language: en-US,en;q=0.9,id;q=0.8,ms;q=0.7,zh-CN;q=0.6,zh;q=0.5,fr;q=0.4' \
    -H 'Cookie: connect.sid=s%3AseqnxJIJpmHWE0K9cUpAmOEqzZpdLjft.PhTa0IVeBWGHhUneZK184MrxOdjDx2Gdw%2BAIFAvCml8' \
    --data-raw 'guess=5656142275158813' \
    --compressed \
    --insecure >> "$1.txt"
  echo >> "$1.txt"
done

