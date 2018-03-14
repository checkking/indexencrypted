package org.apache.lucene.util;

import java.io.RandomAccessFile;

abstract public class FileHeader {

    protected byte[] indexNameBytes;
    protected RandomAccessFile raf;

    public FileHeader(RandomAccessFile raf) {
        this.raf = raf;
    }

    abstract long writeHeader();
    abstract byte[][] readHeader();
}