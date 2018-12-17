package com.secureapp.cryptofactory;

import com.secureapp.FileDecryptor;
import com.secureapp.FileEncryptor;
import com.secureapp.utils.SecureAppException;


/**
 * Factory to supply the FileEncryptor and FileDecryptor
 */
public interface CryptoFactory {
    FileEncryptor getFileEncryptor(
            String keystorePath,
            String storePass,
            String privateKeyAliasName,
            String privateKeyPass,
            String publicKeyAliasName) throws SecureAppException;

    FileDecryptor getFileDecryptor(
            String keystorePath,
            String storePass,
            String privateKeyAliasName,
            String privateKeyPass,
            String certificateAliasName) throws SecureAppException;
}
