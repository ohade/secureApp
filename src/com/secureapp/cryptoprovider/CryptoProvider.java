package com.secureapp.cryptoprovider;

import java.security.Signature;

import javax.crypto.SecretKey;

import com.secureapp.utils.SecureAppException;


/**
 * supplier for IV and signature
 */
public interface CryptoProvider {
    Signature getSignature() throws SecureAppException;

    default SecretKey getSecretKey() throws SecureAppException { return null;}
}
