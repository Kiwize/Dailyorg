package fr.nexa.dailyorg.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptographicUtils {

	public static String generateSHA256Hash(String input) throws NoSuchAlgorithmException {
		// Create a MessageDigest instance for SHA-256
		MessageDigest digest = MessageDigest.getInstance("SHA-256");

		// Perform the hash computation
		byte[] encodedhash = digest.digest(input.getBytes());

		// Convert byte array into a hexadecimal string
		StringBuilder hexString = new StringBuilder();
		for (byte b : encodedhash) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

}
