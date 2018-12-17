package com.secureapp.cryptoprovider;

import static com.secureapp.utils.Config.SECRET_KEY_ALGORITHM;
import static com.secureapp.utils.Config.SIGNATURE_ALGORITHM;

import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.secureapp.utils.SecureAppException;


public class DecryptedCryptoProvider implements CryptoProvider {
    private final PublicKey senderPublicKey;
    private final byte[] secretKeyBuff;

    public DecryptedCryptoProvider(PublicKey senderPublicKey,
                                   byte[] secretKeyBuff) {
        this.senderPublicKey = senderPublicKey;
        this.secretKeyBuff = secretKeyBuff;
    }

    @Override
    public Signature getSignature() throws SecureAppException {
        try {
            // create the matching signature for validating the file,
            // the algorithm to create the file is asymmetric algorithm RSA, so we need the publicKey of the sender
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(senderPublicKey);
            return signature;
        } catch(Exception e) {
            throw new SecureAppException(e.getMessage());
        }
    }

    @Override
    // rebuild the SecretKey
    public SecretKey getSecretKey() {
        return new SecretKeySpec(secretKeyBuff, SECRET_KEY_ALGORITHM);
    }
}
