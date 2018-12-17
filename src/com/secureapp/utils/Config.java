package com.secureapp.utils;


/**
 * Configuration file for the algorithms to use
 */
public class Config
{
	// CIPHER
	// support a way to change algorithm provider easily
	public static final String FILE_CIPHER_PROVIDER = null;

	public static final String FILE_CIPER_ALGO = "AES";

	public static final String FILE_CIPER_ALGO_EXTENDED = "AES/CBC/PKCS5PADDING";

	// SECRET KEY
	public static final String SECRET_KEY_PROVIDER = null;

	public static final String SECRET_KEY_CIPHER_PROVIDER = null;

	public static final String SECRET_KEY_ALGORITHM = "AES";

	public static final String SECRET_KEY_ENCRYPTION_ALGORITHM = "RSA";

	public static final int SECRET_KEY_SIZE = 128;

	// SIGNATURE
	public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

	// KEY-STORE INFO
	public static final String KEY_STORE_TYPE = "JKS";

	public static final String KEY_STORE_ENCRYPTION_PATH = "./akeystore.jks";

	public static final String KEY_STORE_DECRYPTION_PATH = "./bkeystore.jks";

	// PATH
	public static final String INPUT_FILE_PATH_ENCRYPTION = "./plaintext.txt";

	public static final String OUTPUT_FILE_PATH_ENCRYPTION = "./encrypted.txt";

	public static final String INPUT_FILE_PATH_DECRYPTION = "./encrypted.txt";

	public static final String OUTPUT_FILE_PATH_DECRYPTION = "./decrypted.txt";

	public static final String INCRYPTION_INFO_FILE_PATH = "./info.bin";

	private Config() {}
}
