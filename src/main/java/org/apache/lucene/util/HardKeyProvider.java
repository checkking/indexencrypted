package org.apache.lucene.util;

import javax.crypto.spec.SecretKeySpec;

public class HardKeyProvider implements KeyProvider {

    private final static String ALGORITHM_AES = "AES";

    private byte[] keyValue;

    public HardKeyProvider(byte[] keyValue) {
        this.keyValue = keyValue;
    }

    @Override
    public SecretKeySpec getKey(String indexName) {
        return new SecretKeySpec(keyValue, HardKeyProvider.ALGORITHM_AES);
    }
}
