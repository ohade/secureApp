package com.secureapp.cryptofactory;

import static com.secureapp.utils.Config.KEY_STORE_TYPE;

import java.security.PrivateKey;
import java.security.PublicKey;

import com.secureapp.FileDecryptor;
import com.secureapp.FileEncryptor;
import com.secureapp.utils.SecureAppException;


/**
 * factory to create the mail two classes in the program. FileEncryptor for encrypting and FileDecryptor for decrypting
 */
public class CryptoFactoryImpl implements CryptoFactory {
    @Override
    public FileEncryptor getFileEncryptor(
            String keystorePath,
            String storePass,
            String privateKeyAliasName,
            String privateKeyPass,
            String publicKeyAliasName) throws SecureAppException {
        KeyStoreHandler keyStoreHandler = new KeyStoreHandlerImpl(keystorePath, storePass, KEY_STORE_TYPE).init();
        PrivateKey senderPrivateKey = keyStoreHandler.getPrivateKey(privateKeyAliasName, privateKeyPass);
        PublicKey receiverPublicKey = keyStoreHandler.getPublicKey(publicKeyAliasName);
        if(senderPrivateKey == null || receiverPublicKey == null) {
            throw new SecureAppException("One of the requested keys is null, please check and retry");
        }
        return new FileEncryptor(senderPrivateKey, receiverPublicKey);
    }

    @Override
    public FileDecryptor getFileDecryptor(
            String keystorePath,
            String storePass,
            String privateKeyAliasName,
            String privateKeyPass,
            String certificateAliasName) throws SecureAppException {
        KeyStoreHandler keyStoreHandler = new KeyStoreHandlerImpl(keystorePath, storePass, KEY_STORE_TYPE).init();
        PublicKey senderPublicKey = keyStoreHandler.getPublicKey(certificateAliasName);
        PrivateKey receiverPrivateKey = keyStoreHandler.getPrivateKey(privateKeyAliasName, privateKeyPass);
        if(senderPublicKey == null || receiverPrivateKey == null) {
            throw new SecureAppException("One of the requested keys is null, please check and retry");
        }
        return new FileDecryptor(senderPublicKey, receiverPrivateKey);
    }
}
