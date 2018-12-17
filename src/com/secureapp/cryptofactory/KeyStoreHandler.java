package com.secureapp.cryptofactory;

import java.security.PrivateKey;
import java.security.PublicKey;

import com.secureapp.utils.SecureAppException;


/***
 * Wrapper class to keyStore to extract public and private keys
 */
public interface KeyStoreHandler {
    PrivateKey getPrivateKey(String keyAliasName, String keyPass) throws SecureAppException;

    PublicKey getPublicKey(String certificateAliasName) throws SecureAppException;
}
