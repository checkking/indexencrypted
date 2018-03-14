package org.elasticsearch.index.store.encrypt;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockFactory;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.shard.ShardPath;
import org.elasticsearch.index.store.FsDirectoryService;
import org.elasticsearch.index.store.IndexStore;

import java.io.IOException;
import java.nio.file.Path;

class EncryptFsDirectoryService  extends FsDirectoryService {
    public EncryptFsDirectoryService(IndexSettings indexSettings, IndexStore indexStore, ShardPath path) {
        super(indexSettings, indexStore, path);
    }

    @Override
    protected Directory newFSDirectory(Path location, LockFactory lockFactory) throws IOException {
        logger.debug("wrapping EncryptFsDirectoryService for index-encrypt");
        return null;
    }
}