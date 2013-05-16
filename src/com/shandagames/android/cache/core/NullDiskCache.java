package com.shandagames.android.cache.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Jacky Lee (dreamxsky@gmail.com)
 */
public class NullDiskCache implements DiskCache {

    @Override
    public boolean exists(String key) {
        return false;
    }

    @Override
    public File getFile(String key) {
        return null;
    }

    @Override
    public InputStream getInputStream(String key) throws IOException {
        throw new FileNotFoundException();
    }

    @Override
    public void store(String key, InputStream is) {
    }

    @Override
    public void cleanup() {
    }

    @Override
    public void invalidate(String key) {
    }

    @Override
    public void clear() {
    }

}
