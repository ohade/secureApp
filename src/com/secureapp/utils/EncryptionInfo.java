package com.secureapp.utils;

import java.io.Serializable;


/**
 * Used as an information file passed between the sender and the receiver on how to decrypt the file
 */
public class EncryptionInfo implements Serializable {
    private final byte[] signInfo;
    private final byte[] encodedAlgorithmParameters;
    private final byte[] encryptedSecretKey;

    public EncryptionInfo(byte[] signInfo, byte[] iv, byte[] encryptedSecretKey) {
        this.signInfo = signInfo;
        this.encodedAlgorithmParameters = iv;
        this.encryptedSecretKey = encryptedSecretKey;
    }

    public byte[] getSignInfo() {
        return signInfo;
    }

    public byte[] getEncodedAlgorithmParameters() {
        return encodedAlgorithmParameters;
    }

    public byte[] getEncryptedSecretKey() {
        return encryptedSecretKey;
    }
}
