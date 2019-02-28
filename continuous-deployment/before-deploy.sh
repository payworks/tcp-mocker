#!/usr/bin/env bash

# Decrypt signing key
openssl aes-256-cbc -K $encrypted_a3a4db86818a_key -iv $encrypted_a3a4db86818a_iv \
  -in continuous-deployment/payworks_sign_key.asc.enc -d | gpg --fast-import
