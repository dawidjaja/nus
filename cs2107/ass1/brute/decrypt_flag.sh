#!/bin/sh

# Generate a key
cat keys.txt | \
while read KEY; do

  AES_KEY=$KEY

  # Print the key
  echo "Decryption key: $AES_KEY"

  # Encrypt flag.txt using AES-128 in ECB mode and output ciphertext in Base64 to flag.txt.enc
  openssl enc -d -aes-128-ecb -nosalt -base64 -pass pass:$AES_KEY -in flag.txt.enc -out flag.txt -md sha256

  TMP=`cat flag.txt | head -c 5`
  FLAG="flag{"

  if [[ $TMP == $FLAG ]]; then
    echo $AES_KEY > real_key.txt
    break
  fi
done


# Remove plaintext flag
# rm flag.txt
