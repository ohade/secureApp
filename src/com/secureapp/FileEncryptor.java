package com.secureapp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;

import com.secureapp.cryptoprovider.CipherProvider;
import com.secureapp.cryptoprovider.CipherProviderImpl;
import com.secureapp.cryptoprovider.EncryptedCryptoProvider;
import com.secureapp.utils.CryptoObjectHandler;
import com.secureapp.utils.EncryptionInfo;
import com.secureapp.utils.SecureAppException;


/**
 * The main class used to encrypt the file
 */
public class FileEncryptor {
    private final PrivateKey senderPrivateKey;
    private final PublicKey receiverPublicKey;

    public FileEncryptor(PrivateKey senderPrivateKey, PublicKey receiverPublicKey) {
        this.senderPrivateKey = senderPrivateKey;
        this.receiverPublicKey = receiverPublicKey;
    }

    public void encrypt(String inputFilePath,
                        String outputFilePath,
                        String encryptionInfoFilePath) throws SecureAppException {
        try {
            CipherProvider cipherProvider = new CipherProviderImpl();
            EncryptedCryptoProvider provider = new EncryptedCryptoProvider(senderPrivateKey);
            SecretKey secretKey = provider.getSecretKey();
            Cipher fileCipher = cipherProvider.getFileCipherForEncrypt(Cipher.ENCRYPT_MODE, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFilePath);

            // using CipherOutputStream for encryption using the fileCipher we created using AES/CBC/PKCS7PADDING
            FileOutputStream outputStream = new FileOutputStream(outputFilePath);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, fileCipher);
            Signature signature = provider.getSignature();

            // read up to 1024 bytes at the time and write them encrypted
            byte[] buf = new byte[1024];
            int read;

            while ((read = inputStream.read(buf)) != -1) {
                // write the plain text buffer to the signature to build the signInfo at the end that we will send the
                // receiver of the file to validate the content he receives
                signature.update(buf, 0, read);
                cipherOutputStream.write(buf, 0, read);
            }
            byte[] signInfo = signature.sign();
            inputStream.close();
            outputStream.flush();
            cipherOutputStream.close();

            // we create a binary file using java file serialization
            // the file includes:
            // 1. encoded AlgorithmParameters, this object contains the IV we used and some other parameters,
            // will be used to init the cipher at the receiver end
            // 2. signInfo - the signInfo we produced from the signature
            // 3. secretKey that we will encrypt using asymmetric encryption algorithm: RSA
            storeEncryptionInfo(
                    encryptionInfoFilePath,
                    signInfo,
                    cipherProvider,
                    fileCipher.getParameters().getEncoded(),
                    secretKey);
        } catch(Exception e) {
            throw new SecureAppException(e.getMessage());
        }
    }

    private void storeEncryptionInfo(String encryptionInfoFilePath,
                                     byte[] signInfo,
                                     CipherProvider cipherProvider,
                                     byte[] encodedAlgorithmParameters,
                                     SecretKey secretKey) throws SecureAppException {
        try {
            byte[] encepySecretKey = cipherProvider.encryptSecretKey(receiverPublicKey, secretKey);
            EncryptionInfo encryptionInfo = new EncryptionInfo(signInfo, encodedAlgorithmParameters, encepySecretKey);
            CryptoObjectHandler.saveObjToFile(
                    encryptionInfo,
                    encryptionInfoFilePath);
        } catch (Exception e) {
            throw new SecureAppException(e.getMessage());
        }
    }
}
