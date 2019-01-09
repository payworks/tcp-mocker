#!/usr/bin/env bash

# Decrypt signing key
openssl aes-256-cbc -K $encrypted_a3a4db86818a_key -iv $encrypted_a3a4db86818a_iv
  -in payworks_sign_key.asc.enc -out ~\/temp/tcp-mocker-keys/payworks_sign_key.asc
  -d

# Import signing key
 gpg --fast-import ~\/temp/tcp-mocker-keys/payworks_sign_key.asc