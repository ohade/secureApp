package com.secureapp.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Save/Load EncryptionInfo object to file, using java serialization
 */
public class CryptoObjectHandler {
    public static void saveObjToFile(EncryptionInfo EncryptedProcessInfo, String outputPath) throws SecureAppException {
        try {
            FileOutputStream fileOut;
            fileOut = new FileOutputStream(outputPath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(EncryptedProcessInfo);
            out.close();
            fileOut.close();
        } catch (Exception e) {
            throw new SecureAppException(e.getMessage());
        }
    }

    public static EncryptionInfo loadObjFromFile(String inputPath) throws SecureAppException {
        try {
            FileInputStream fileIn = new FileInputStream(inputPath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            EncryptionInfo encryptedProcessInfo = (EncryptionInfo) in.readObject();
            in.close();
            fileIn.close();
            return encryptedProcessInfo;
        } catch (Exception e) {
            throw new SecureAppException(e.getMessage());
        }
    }

}
