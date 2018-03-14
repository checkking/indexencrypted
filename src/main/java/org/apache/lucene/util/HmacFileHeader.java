package org.apache.lucene.util;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.apache.logging.log4j.Logger;

public class HmacFileHeader extends FileHeader {

    private byte[] hmacBytes;
    private byte[] plainTextBytes;
    private KeyProvider keyProvider;
    private String indexName;

    public HmacFileHeader(RandomAccessFile raf, KeyProvider keyProvider, String indexName) {
        super(raf);
        this.keyProvider = keyProvider;
        this.indexName = indexName;
    }

    private void writeByteArray(byte[] byteArray) throws IOException {
        this.raf.writeInt(byteArray.length);
        this.raf.write(byteArray);
    }

    private byte[] readBytesFromCurrentFilePointer() throws IOException {
        int numBytes = raf.readInt();
        byte[] byteArray = new byte[numBytes];
        raf.readFully(byteArray);
        return byteArray;
    }

    @Override
    long writeHeader() {
        try {
            // write index name
            byte[] indexNameBytes = this.indexName.getBytes();
            writeByteArray(indexNameBytes);

            // write plaintext bytes
            int numBytes = 8;
            byte[] plainTextBytes = HmacUtil.generateRandomBytes(numBytes);
            writeByteArray(plainTextBytes);

            // write HMAC bytes
            hmacBytes = HmacUtil.hmacValue(plainTextBytes, keyProvider.getKey(indexName));
            writeByteArray(hmacBytes);
            return raf.getFilePointer();
        } catch (IOException ioe) {
            return 0L;
        } catch (NoSuchAlgorithmException nsae) {
            return 0L;
        } catch (InvalidKeyException ike) {
            return 0L;
        }
    }

    @Override
    byte[][] readHeader() {
        try {
            raf.seek(0);
            byte[] indexNameBytes = readBytesFromCurrentFilePointer();
            byte[] plainTextBytes = readBytesFromCurrentFilePointer();
            byte[] hmacBytes = readBytesFromCurrentFilePointer();
            byte[][] ret = new byte[][]{indexNameBytes, plainTextBytes, hmacBytes};
            return ret;
        } catch (IOException ioe) {
            return null;
        }
    }

}
