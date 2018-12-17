package com.secureapp.cryptoprovider;

import static com.secureapp.utils.Config.FILE_CIPER_ALGO;
import static com.secureapp.utils.Config.FILE_CIPER_ALGO_EXTENDED;
import static com.secureapp.utils.Config.FILE_CIPHER_PROVIDER;
import static com.secureapp.utils.Config.SECRET_KEY_CIPHER_PROVIDER;
import static com.secureapp.utils.Config.SECRET_KEY_ENCRYPTION_ALGORITHM;

import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.secureapp.utils.SecureAppException;


public class CipherProviderImpl implements CipherProvider {
    @Override
    // generate cipher key to encrypt the file, using:
    // 1. AES/CBC/PKCS7PADDING as requested. AES with CBC is a not the fastest to encrypt but will be fast to decrypt.
    //    but it is surely better then asymmetric algorithm.
    // 2. secretKey that was init with AES algo
    // 3. random IV
    public Cipher getFileCipherForEncrypt(int cipherMode, SecretKey secretKey) throws SecureAppException {
        try {
            Cipher cipher = (FILE_CIPHER_PROVIDER == null)? Cipher.getInstance(FILE_CIPER_ALGO_EXTENDED) :
                    Cipher.getInstance(FILE_CIPER_ALGO_EXTENDED, FILE_CIPHER_PROVIDER);
            cipher.init(cipherMode, secretKey, getIVParameterSpec(cipher));
            return cipher;
        } catch(Exception e) {
            throw new SecureAppException(e.getMessage());
        }
    }

    @Override
    // create the cipher key for decryption, using the encoded AlgorithmParameters that was sent from
    // the sender, the encoded iv was sent as is because any change to it will interfere with the decryption and
    // prevent the validation
    public Cipher getFileCipherForDecrypt(int cipherMode, SecretKey secretKey, byte[] encodedAlgorithmParameters) throws SecureAppException {
        try {
            Cipher cipher = (FILE_CIPHER_PROVIDER == null)? Cipher.getInstance(FILE_CIPER_ALGO_EXTENDED) :
                    Cipher.getInstance(FILE_CIPER_ALGO_EXTENDED, FILE_CIPHER_PROVIDER);
            AlgorithmParameters algParams = AlgorithmParameters.getInstance(FILE_CIPER_ALGO);
            algParams.init(encodedAlgorithmParameters);
            cipher.init(cipherMode, secretKey, algParams);
            return cipher;
        } catch(Exception e) {
            throw new SecureAppException(e.getMessage());
        }
    }

    @Override
    // encrypt the secret key with asymmetric algorithm RSA
    public byte[] encryptSecretKey(Key receiverPublicKey, SecretKey secretKey) throws SecureAppException {
        try {
            Cipher cipher = getSecretKeyCipher(receiverPublicKey, Cipher.ENCRYPT_MODE);
            return cipher.doFinal(secretKey.getEncoded());
        } catch (Exception e) {
            throw new SecureAppException(e.getMessage());
        }
    }

    @Override
    // decrypt the secret key with asymmetric algorithm RSA
    public byte[] decryptSecretKey(Key receiverPrivateKey, byte[] secretKey) throws SecureAppException {
        try {
            Cipher cipher = getSecretKeyCipher(receiverPrivateKey, Cipher.DECRYPT_MODE);
            return cipher.doFinal(secretKey);
        } catch (Exception e) {
            throw new SecureAppException(e.getMessage());
        }
    }

    // create a IV using SecureRandom and the recommended size of the buffer as taken from the cipher
    private IvParameterSpec getIVParameterSpec(Cipher cipher) {
        SecureRandom randomSecureRandom = new SecureRandom();
        byte[] iv = new byte[cipher.getBlockSize()];
        randomSecureRandom.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private Cipher getSecretKeyCipher(Key key, int cipherMode) throws SecureAppException {
        try {
            Cipher cipher = (SECRET_KEY_CIPHER_PROVIDER == null)? Cipher.getInstance(SECRET_KEY_ENCRYPTION_ALGORITHM) :
                    Cipher.getInstance(SECRET_KEY_ENCRYPTION_ALGORITHM, FILE_CIPHER_PROVIDER);
            cipher.init(cipherMode, key);
            return cipher;
        } catch (Exception e) {
            throw new SecureAppException(e.getMessage());
        }
    }
}
