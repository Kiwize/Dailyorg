package fr.nexa.dailyorg.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;

public class CryptographicUtilsTest {

	@Test
	public void testGenerateSHA256Hash() throws NoSuchAlgorithmException {
		String input = "Hello, World!";
		
		String precomputedHash = "dffd6021bb2bd5b0af676290809ec3a53191dd81c7f70a4b28688a362182986f";
		String actualHash = CryptographicUtils.generateSHA256Hash(input);

		assertEquals(precomputedHash, actualHash);
	}
	
	@Test
	public void testGenerateSHA256Hash_EmptyString() throws NoSuchAlgorithmException {
		String input = "";
		
		String precomputedHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
		String actualHash = CryptographicUtils.generateSHA256Hash(input);

		assertEquals(precomputedHash, actualHash);
	}
	
	@Test
	public void testGenerateSHA256Hash_SpecialCharacters() throws NoSuchAlgorithmException {
		String input = "!@#$%^&*()_+";
		
		String precomputedHash = "36d3e1bc65f8b67935ae60f542abef3e55c5bbbd547854966400cc4f022566cb";
		String actualHash = CryptographicUtils.generateSHA256Hash(input);

		assertEquals(precomputedHash, actualHash);
	}

}
