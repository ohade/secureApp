import static com.secureapp.utils.Config.INCRYPTION_INFO_FILE_PATH;
import static com.secureapp.utils.Config.INPUT_FILE_PATH_DECRYPTION;
import static com.secureapp.utils.Config.INPUT_FILE_PATH_ENCRYPTION;
import static com.secureapp.utils.Config.KEY_STORE_DECRYPTION_PATH;
import static com.secureapp.utils.Config.KEY_STORE_ENCRYPTION_PATH;
import static com.secureapp.utils.Config.OUTPUT_FILE_PATH_DECRYPTION;
import static com.secureapp.utils.Config.OUTPUT_FILE_PATH_ENCRYPTION;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.secureapp.FileDecryptor;
import com.secureapp.FileEncryptor;
import com.secureapp.cryptofactory.CryptoFactory;
import com.secureapp.cryptofactory.CryptoFactoryImpl;
import com.secureapp.utils.SecureAppException;

public class Main {
    private static final String CLEAR_SCREEN = "\033[H\033[2J";
    private static final String GREEN_BOLD = "\033[1;32m";
    private static final String MAGENTA_UNDERLINED = "\033[4;35m";
    private static final String RED_BOLD = "\033[0;31m";
    private static final String RESET = "\033[0m";

    public static void main(String[] args) {
        System.out.println(CLEAR_SCREEN);
        if (args.length <= 1 || args.length > 5)
        {
            System.out.println("Usage: \n" +
                    "   1. encrypt <keystore password> <private key alias name> <private key password> <public key alias name>\n" +
                    "   2. decrypt <keystore password> <private key alias name> <private key password> <certificate alias name>");
            System.exit(0);
        }

        try {
            if ("encrypt".equals(args[0])) {
                encrypt(args);
            } else if ("decrypt".equals(args[0])) {
                decrypt(args);
            }
        } catch (SecureAppException e) {
            System.out.println(RED_BOLD + e.getMessage() + RESET + "\n");
        }
    }

    private static void encrypt(String[] args) throws SecureAppException {
        if (args.length != 5)
        {
            System.out.println("Usage: encrypt <keystore password> <private key alias name> <private key password> <public key alias name>");
            System.exit(0);
        }

        System.out.println(MAGENTA_UNDERLINED + "SecureApp Encryption" + RESET);

        String storePass = args[1];
        String privateKeyAliasName = args[2];
        String privateKeyPass = args[3];
        String publicKeyAliasName = args[4];

        System.out.println("Going to encrypt file: " + INPUT_FILE_PATH_ENCRYPTION);
        checkAllFileExistEncrypt();

        CryptoFactory factory = new CryptoFactoryImpl();
        FileEncryptor fileEncryptor = factory.getFileEncryptor(
                KEY_STORE_ENCRYPTION_PATH,
                storePass,
                privateKeyAliasName,
                privateKeyPass,
                publicKeyAliasName);
        fileEncryptor.encrypt(INPUT_FILE_PATH_ENCRYPTION, OUTPUT_FILE_PATH_ENCRYPTION, INCRYPTION_INFO_FILE_PATH);


        System.out.println("File encryption finished " + GREEN_BOLD + "successfully\n" + RESET +
                "Output file located in: "+
                GREEN_BOLD + OUTPUT_FILE_PATH_ENCRYPTION + RESET +
                ",\nEncryption info file located in: " +
                INCRYPTION_INFO_FILE_PATH + "\n");

    }

    private static void decrypt(String[] args) throws SecureAppException {
        if (args.length != 5)
        {
            System.out.println("Usage: decrypt <keystore password> <private key alias name> <private key password> <certificate alias name>");
            System.exit(0);
        }

        System.out.println(MAGENTA_UNDERLINED + "SecureApp Decryption" + RESET);

        String storePass = args[1];
        String privateKeyAliasName = args[2];
        String privateKeyPass = args[3];
        String certificateAliasName = args[4];

        System.out.println("Going to decrypt file: " + INPUT_FILE_PATH_DECRYPTION);
        checkAllFileExistDecrypt();

        CryptoFactory factory = new CryptoFactoryImpl();
        FileDecryptor fileDecryptor = factory.getFileDecryptor(
                KEY_STORE_DECRYPTION_PATH,
                storePass,
                privateKeyAliasName,
                privateKeyPass,
                certificateAliasName);

        boolean signatureResult = fileDecryptor.decrypt(
                INPUT_FILE_PATH_DECRYPTION,
                OUTPUT_FILE_PATH_DECRYPTION,
                INCRYPTION_INFO_FILE_PATH
        );

        System.out.println("File decryption finished " + GREEN_BOLD + "successfully\n" + RESET +
                "output file located in: "+
                GREEN_BOLD + OUTPUT_FILE_PATH_DECRYPTION + RESET +
                ", signature is: " + (signatureResult? GREEN_BOLD + "verified" + RESET: RED_BOLD + "unverified" + RESET) + "\n");
    }

    private static void checkAllFileExistEncrypt() {
        List<String> files = Arrays.asList(KEY_STORE_ENCRYPTION_PATH,
                INPUT_FILE_PATH_ENCRYPTION);
        fileChecker(files);
    }

    private static void checkAllFileExistDecrypt() {
        List<String> files = Arrays.asList(KEY_STORE_DECRYPTION_PATH,
                INPUT_FILE_PATH_DECRYPTION,
                INCRYPTION_INFO_FILE_PATH);
        fileChecker(files);
    }

    private static void fileChecker(List<String> files) {
        files.forEach(filePath -> {
            File file = new File(filePath);
            if (!file.exists() || file.isDirectory()) {
                throw new SecureAppException("Missing file: " + filePath + "\n");
            }
        });
    }
}
