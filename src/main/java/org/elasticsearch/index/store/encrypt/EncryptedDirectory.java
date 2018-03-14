package org.elasticsearch.index.store.encrypt;

import org.apache.lucene.codecs.lucene62.Lucene62SegmentInfoFormat;
import org.apache.lucene.index.IndexFileNames;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.*;
import org.elasticsearch.client.Client;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.index.shard.ShardId;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

public class EncryptedDirectory extends NIOFSDirectory {

    private Logger logger;
    private int pageSize;
    private String indexName;
    private final static String keyStr = "eoFqT0WnkhTQi4OQrwoXxkQzDEnEsChY";

    public EncryptedDirectory(Path path, LockFactory lockFactory, ShardId shardId, Client client) throws IOException {
        super(path, lockFactory);
        this.logger = ESLoggerFactory.getRootLogger();
        this.pageSize = 64;
        this.indexName = shardId.getIndexName();
    }

    protected FileHeader buildFileHeader(RandomAccessFile raf) {
        return new HmacFileHeader(raf, new HardKeyProvider(keyStr.getBytes()), this.indexName);
    }

    @Override
    public IndexOutput createOutput(String name, IOContext context) throws IOException {
        if (isSegmentMetadataFile(name)) {
            return super.createOutput(name, context);
        } else {
            ensureOpen();
            ensureCanRead(name);
            File path = new File(getDirectory().toFile(), name);
            RandomAccessFile readerRaf = new RandomAccessFile(path, "r");
            FileHeader readerFileHeader = buildFileHeader(readerRaf);

        }
    }

    private boolean isSegmentMetadataFile(String fileName) {
        return (fileName == IndexFileNames.OLD_SEGMENTS_GEN
                || fileName.startsWith(IndexFileNames.SEGMENTS + "_")
                || fileName.contains("." + IndexFileNames.SEGMENTS + "_")
                || fileName.endsWith("." + Lucene62SegmentInfoFormat.SI_EXTENSION));
    }

    protected AESReader createAESReader(File path, RandomAccessFile raf, int pageSize, KeyProvider keyProvider, FileHeader fileHeader) {
        try {
            return new AESReader(path.getName(), raf, pageSize, keyProvider, indexName, fileHeader);
        } catch (Exception e) {
            return null;
        }
    }

    protected AESWriter createAESWriter(File path, RandomAccessFile raf, int pageSize, KeyProvider keyProvider, FileHeader fileHeader) {
        try {
            return new AESWriter(path.getName(), raf, pageSize, keyProvider, indexName, fileHeader);
        } catch (Exception e) {
            return null;
        }
    }

}
