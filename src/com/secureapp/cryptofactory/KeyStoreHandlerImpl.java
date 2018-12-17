package com.secureapp.cryptofactory;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Enumeration;

import com.secureapp.utils.SecureAppException;


/**
 * Wrapper for the keystore, used to get the public/private key
 */
public class KeyStoreHandlerImpl implements KeyStoreHandler {
	private final String path;
	private final String storePass;
	private final String storeType;
	private KeyStore keyStore;
	
	KeyStoreHandlerImpl(String KeystorePath, String storePass, String storeType) {
		this.path = KeystorePath;
		this.storePass = storePass;
		this.storeType = storeType;
	}

	KeyStoreHandlerImpl init() throws SecureAppException {
		try {
			if(storePass == null) {
				throw new SecureAppException("keyStore password can't be null");
			}
			if(path == null) {
				throw new SecureAppException("keyStore path can't be null");
			}
			FileInputStream storeFis = new FileInputStream(path);
			keyStore = KeyStore.getInstance(storeType);
			keyStore.load(storeFis, storePass.toCharArray());
		} catch (Exception e) {
			throw new SecureAppException(e.getMessage());
		}
		return this;
	}

	@Override
	public PrivateKey getPrivateKey(String keyAliasName, String keyPass) throws SecureAppException {
		try {
			if(!aliasExist(keyAliasName)) {
				throw new SecureAppException("Couldn't fetch alias from keyStore");
			}
			if(keyPass == null) {
				throw new SecureAppException("keyPass can't be null");
			}
			return ((PrivateKeyEntry)
					keyStore.getEntry(keyAliasName, new KeyStore.PasswordProtection(keyPass.toCharArray())))
					.getPrivateKey();
		} catch (Exception e) {
			throw new SecureAppException(e.getMessage());
		}
	}

	@Override
	public PublicKey getPublicKey(String certificateAliasName) throws SecureAppException {
		return getCertificate(certificateAliasName).getPublicKey();
	}

	private Certificate getCertificate(String certificateAliasName) throws SecureAppException {
		try {
			if(!aliasExist(certificateAliasName)) {
				throw new SecureAppException("Couldn't fetch alias from keyStore");
			}
			return keyStore.getCertificate(certificateAliasName);
		} catch (Exception e) {
			throw new SecureAppException(e.getMessage());
		}
	}

	private boolean aliasExist(String alias) throws SecureAppException {
		if(alias == null || keyStore == null) {
			return false;
		}
		try {
			Enumeration<String> aliases = keyStore.aliases();
			while(aliases.hasMoreElements()) {
				String currAlias = aliases.nextElement();
				if(currAlias.equals(alias)) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			throw new SecureAppException(e.getMessage());
		}
	}
}

