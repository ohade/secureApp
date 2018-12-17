package com.secureapp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

import com.secureapp.cryptoprovider.CipherProvider;
import com.secureapp.cryptoprovider.CipherProviderImpl;
import com.secureapp.cryptoprovider.DecryptedCryptoProvider;
import com.secureapp.utils.CryptoObjectHandler;
import com.secureapp.utils.EncryptionInfo;
import com.secureapp.utils.SecureAppException;


/**
 * The main class used to decrypt the file
 */
public final class FileDecryptor {
    private final PublicKey senderPublicKey;
    private final PrivateKey receiverPrivateKey;

    public FileDecryptor(PublicKey senderPublicKey, PrivateKey receiverPrivateKey) {
        this.senderPublicKey = senderPublicKey;
        this.receiverPrivateKey = receiverPrivateKey;
    }

    public boolean decrypt(String inputFilePath,
                           String outputFilePath,
                           String encryptedProcessInfoFilePath) throws SecureAppException {
        try {
            EncryptionInfo encryptionInfo = CryptoObjectHandler.loadObjFromFile(encryptedProcessInfoFilePath);
            if(encryptionInfo == null) {
                throw new SecureAppException("encryptionInfo is missing, can't decrypt the file");
            }

            CipherProvider cipherProvider = new CipherProviderImpl();
            byte[] encodedSecretKey = cipherProvider.decryptSecretKey(receiverPrivateKey, encryptionInfo.getEncryptedSecretKey());
            DecryptedCryptoProvider provider = new DecryptedCryptoProvider(senderPublicKey, encodedSecretKey);

            // create decrypt cipher for the file
            Cipher fileCipher = cipherProvider.getFileCipherForDecrypt(Cipher.DECRYPT_MODE,
                    provider.getSecretKey(),
                    encryptionInfo.getEncodedAlgorithmParameters());

            Signature signature = provider.getSignature();

            // we open the file for read twice, if the signature is not validated at the end then
            // we won't decrypt the file.
            // because decrypt the file in our file system might be risky
            boolean verificationStatus = validateFile(inputFilePath, encryptionInfo, signature, fileCipher);

            // if signature is validated then decrypt the file
            if(verificationStatus) {
                decipherEncryptedFile(inputFilePath, outputFilePath, fileCipher);
            }
            return verificationStatus;
        } catch (Exception e) {
            throw new SecureAppException(e.getMessage());
        }
    }


    private boolean validateFile(String inputFilePath, EncryptionInfo encryptionInfo, Signature signature, Cipher fileCipher) {
        try {
            FileInputStream inputStream = new FileInputStream(inputFilePath);
            CipherInputStream cipherInputStream = new CipherInputStream(inputStream, fileCipher);
            byte[] buf = new byte[1024];
            int read;
            while ((read = cipherInputStream.read(buf)) != -1) {
                signature.update(buf, 0, read);
            }
            inputStream.close();
            return signature.verify(encryptionInfo.getSignInfo());
        } catch (Exception e) {
            throw new SecureAppException(e.getMessage());
        }
    }

    private void decipherEncryptedFile(String inputFilePath, String outputFilePath, Cipher fileCipher) throws IOException {
        FileInputStream inputStream = new FileInputStream(inputFilePath);
        CipherInputStream cipherInputStream = new CipherInputStream(inputStream, fileCipher);
        FileOutputStream outStream = new FileOutputStream(outputFilePath);

        // read and decipher the file
        byte[] buf = new byte[1024];
        int read;
        while ((read = cipherInputStream.read(buf)) != -1) {
            outStream.write(buf, 0, read);
        }
        inputStream.close();
        outStream.flush();
        outStream.close();
    }
}
