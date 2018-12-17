package com.secureapp.cryptoprovider;

import static com.secureapp.utils.Config.SECRET_KEY_ALGORITHM;
import static com.secureapp.utils.Config.SECRET_KEY_PROVIDER;
import static com.secureapp.utils.Config.SECRET_KEY_SIZE;
import static com.secureapp.utils.Config.SIGNATURE_ALGORITHM;

import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.secureapp.utils.SecureAppException;


public class EncryptedCryptoProvider implements CryptoProvider {
    private final PrivateKey senderPrivateKey;

    public EncryptedCryptoProvider(PrivateKey senderPrivateKey) {
        this.senderPrivateKey = senderPrivateKey;
    }

    @Override
    public Signature getSignature() throws SecureAppException {
        try {
            // create asymmetric signature using RSA key and sender private key, will be used to validate the file
            // using SHA with 256 bits and RSA, for better security
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(senderPrivateKey);
            return signature;
        } catch(Exception e) {
            throw new SecureAppException(e.getMessage());
        }
    }

    public SecretKey getSecretKey() throws SecureAppException {
        // using a symmetric algorithm to create a SecretKey that the cipher will use to encrypt the file
        // symmetric algorithm is greate for this purpose because it is fast.
        // we will need to protect the secret key when sending it to the receiver
        // we will later do that with asymmetric algorithm RSA it is slower but safer for the transit.

        // always specify the key size (and any other parameters) explicitly.
        // Do not rely on provider defaults as this will make it unclear what your application is doing,
        // and each provider may have its own defaults.
        try {
            KeyGenerator keyGenerator = (SECRET_KEY_PROVIDER == null) ? KeyGenerator.getInstance(SECRET_KEY_ALGORITHM) :
                    KeyGenerator.getInstance(SECRET_KEY_ALGORITHM, SECRET_KEY_PROVIDER);

            SecureRandom secureRandom = new SecureRandom();
            // using a 128 bit key
            // 256 bit encryption requires the JCE Unlimited Strength Jurisdiction Policy installed in the JRE
            keyGenerator.init(SECRET_KEY_SIZE, secureRandom);
            return keyGenerator.generateKey();
        } catch (Exception e) {
            throw new SecureAppException(e.getMessage());
        }
    }

}
