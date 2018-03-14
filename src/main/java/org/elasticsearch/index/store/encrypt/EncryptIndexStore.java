package org.elasticsearch.index.store.encrypt;

import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.shard.ShardPath;
import org.elasticsearch.index.store.DirectoryService;
import org.elasticsearch.index.store.IndexStore;

public class EncryptIndexStore extends IndexStore {

    public EncryptIndexStore(IndexSettings indexSettings) {
        super(indexSettings);
    }

    @Override
    public DirectoryService newDirectoryService(ShardPath path) {
        return new EncryptFsDirectoryService(indexSettings, this, path);
    }
}
