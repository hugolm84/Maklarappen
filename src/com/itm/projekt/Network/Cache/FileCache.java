/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Network.Cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CacheResponse;
import java.util.List;
import java.util.Map;


public class FileCache extends CacheResponse {

    private File file;

    FileCache(File file) {
        this.file = file;
    }
    @Override
    public Map<String, List<String>> getHeaders() throws IOException {
        return null;
    }

    @Override
    public InputStream getBody() throws IOException {
        return new FileInputStream(file);
    }

}