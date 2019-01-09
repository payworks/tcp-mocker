#!/usr/bin/env bash

set -x

# Decrypt signing key
openssl aes-256-cbc -K $encrypted_a3a4db86818a_key -iv $encrypted_a3a4db86818a_iv \
  -in payworks_sign_key.asc.enc -out continuous-deployment/payworks_sign_key.asc \
  -d

# Import signing key
gpg --fast-import continuous-deployment/payworks_sign_key.asc