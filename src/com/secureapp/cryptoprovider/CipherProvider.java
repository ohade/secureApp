package com.secureapp.cryptoprovider;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import com.secureapp.utils.SecureAppException;


/**
 * Cipher supplier and secret key encrypt/decrypt
 */
public interface CipherProvider {
    Cipher getFileCipherForEncrypt(int cipherMode, SecretKey secretKey) throws SecureAppException;

    Cipher getFileCipherForDecrypt(int cipherMode, SecretKey secretKey, byte[] encodedAlgorithmParameters) throws SecureAppException;

    byte[] encryptSecretKey(Key receiverPublicKey, SecretKey secretKey) throws SecureAppException;

    byte[] decryptSecretKey(Key receiverPrivateKey, byte[] secretKey) throws SecureAppException;
}
