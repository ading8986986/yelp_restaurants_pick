
package com.android_examples.rbcdemo.cache;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader.ImageCache;

//import cn.winga.silkroad.util.VersionUtil;

/**
 * Basic LRU Memory cache.
 */
public class BitmapLruImageCache extends LruCache<String, Bitmap> implements ImageCache {

    private final String TAG = this.getClass().getSimpleName();

    public BitmapLruImageCache(int maxSize) {
        super(maxSize);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    protected int sizeOf(String key, Bitmap value) {
//        if (VersionUtil.hasHoneycombMR1()) {
//            return value.getByteCount();
//        }
        // Pre HC-MR1
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    public Bitmap getBitmap(String url) {
        Log.v(TAG, "Retrieved item from Mem Cache");
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        Log.v(TAG, "Added item to Mem Cache");
        put(url, bitmap);
    }
}
