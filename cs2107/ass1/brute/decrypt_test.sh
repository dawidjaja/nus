#!/bin/sh

# Generate a key

AES_KEY="drvra"

# Print the key
echo "Decryption key: $AES_KEY"

# Encrypt flag.txt using AES-128 in ECB mode and output ciphertext in Base64 to flag.txt.enc
openssl enc -d -aes-128-ecb -nosalt -base64 -pass pass:$AES_KEY -in flagtest.txt.enc -out testis.txt -md sha256

echo "Otto" $KEY
TMP=`cat flag.txt | head -c 5`
FLAG="flag{"

echo "-Tmp-" $TMP
echo "Flag" $FLAG
if [[ $TMP == $FLAG ]]; then
  echo $AES_KEY > real_key.txt
  break
fi
echo "done checking"

# Remove plaintext flag
# rm flag.txt
