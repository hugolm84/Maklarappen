/**
 * Hugo Lindström (C) 2013
 * huli1000
 */
package com.itm.projekt.Network.Cache;

import android.util.Log;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;


public class NetworkResponseCache extends ResponseCache {

    private static final String TAG = NetworkResponseCache.class.getName();
    private String cacheDir = null;

    public NetworkResponseCache(String cacheDir)  {
        this.cacheDir = cacheDir;
    }

    @Override
    public CacheResponse get(URI uri, String requestMethod, Map<String, List<String>> requestHeaders) {
        Log.d(TAG, "Trying to get cached response for uri " + uri.toString());
        final File file = new File(cacheDir, hash(uri.toString()));
        if (file.exists()) {
            Log.d(TAG, "Found cached body for uri " + uri.toString() + " file: " + file.getAbsoluteFile());
            FileCache c = new FileCache(file);
            String jString = null;

            try {
                FileChannel fc = ((FileInputStream)c.getBody()).getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                /* Instead of using default, pass in a decoder. */
                jString = Charset.defaultCharset().decode(bb).toString();
                Log.d(TAG, jString);

            } catch(Exception e) {
                e.printStackTrace();
                Log.d(TAG, jString);
            } finally {
                try {
                    c.getBody().close();
                } catch(Exception e) { e.printStackTrace(); }
            }

            return new FileCache(file);
        } else {
            Log.d(TAG, "Did not find cached response for uri " + uri.toString());
            return null;
        }
    }

    @Override
    public CacheRequest put(final URI reqUri, URLConnection conn) {
        try {

            /**
             * BUG: Uri in CacheReq is always empty?
             * Workaround: get it from URLConn
             */
            final URI uri = conn.getURL().toURI();
            final File file = new File(cacheDir, hash(uri.toString()));

            Log.d(TAG, "Trying to put response in cache for uri " + uri.toString());

            return new CacheRequest() {
                @Override
                public OutputStream getBody() throws IOException {
                    Log.d(TAG, "getBody for uri " + uri.toString() + " at location " + file.getAbsoluteFile());
                    return new FileOutputStream(file);
                }

                @Override
                public void abort() {
                    Log.d(TAG, "Aborting put for uri " + uri.toString() + " and file " + file.getAbsoluteFile());
                    file.delete();
                }
            };
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private synchronized String hash(String url) {
        String created_hash = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(url.getBytes());
            BigInteger hash = new BigInteger(1, md5.digest());
            created_hash = hash.toString(16);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception : " + e.getMessage());
        }
        return created_hash;
    }
}

