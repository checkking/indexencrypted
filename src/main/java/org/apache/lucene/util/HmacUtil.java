package org.apache.lucene.util;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ThreadLocalRandom;

public class HmacUtil {
    private final static String HMAC_ERROR_PREFIX = "HMAC Error - ";
    private final static String DATA_CIPHER_ALGORITHM = "AES";
    private final static int DATA_KEY_SIZE = 256;
    private final static String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    public static byte[] hmacValue(byte[] plainText, SecretKey key) throws RuntimeException, NoSuchAlgorithmException, InvalidKeyException {
        if (plainText.length == 0 || key == null) {
            return null;
        }
        if (!key.getAlgorithm().equalsIgnoreCase(DATA_CIPHER_ALGORITHM)) {
            throw new RuntimeException("Expected key of type " + DATA_CIPHER_ALGORITHM + ", but found: " + key.getAlgorithm());
        }

        int keySize = key.getEncoded().length * 8;
        if (keySize < DATA_KEY_SIZE) {
            throw new RuntimeException("Key is too small. Expected at least " + DATA_KEY_SIZE + ", but found: " + keySize);
        }
        Mac hmac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        hmac.init(key);
        return hmac.doFinal();
    }

    public static byte[] generateRandomBytes(int numBytes) {
        byte[] randomBytes = new byte[numBytes];
        ThreadLocalRandom.current().nextBytes(randomBytes);
        return randomBytes;
    }
}
