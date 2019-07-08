
package com.android_examples.rbcdemo.cache;

import android.content.Context;

import com.android_examples.rbcdemo.MyApplication;


public class DiskCacheManager {
    private static DiskCacheManager mInstance;
    private ImageCache imageCache;
    private static final String IMAGE_CACHE_DIR = "images";

    public static DiskCacheManager getInstance() {
        if (mInstance == null)
            mInstance = new DiskCacheManager();

        return mInstance;
    }

    public void init(Context context) {
        if (imageCache == null) {
            ImageCache.ImageCacheParams cacheParams =
                    new ImageCache.ImageCacheParams(context, IMAGE_CACHE_DIR);
            cacheParams.memoryCacheEnabled = false;
            imageCache = new ImageCache(cacheParams);
        }
        if (imageCache.mDiskLruCache == null) {
            imageCache.initDiskCache();
        }
    }

    public ImageCache getImageCache() {
        if (imageCache == null || imageCache.mDiskLruCache == null) {
            init(MyApplication.instance);
        }
        return imageCache;
    }
}
