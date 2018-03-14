package org.apache.lucene.util;

import javax.crypto.spec.SecretKeySpec;

public interface KeyProvider {
    public SecretKeySpec getKey(String indexName);
}
