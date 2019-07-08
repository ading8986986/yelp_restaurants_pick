
package com.android_examples.rbcdemo.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android_examples.rbcdemo.request.RequestManager;


/**
 * Implementation of volley's ImageCache interface. This manager tracks the
 * application image loader and cache. Volley recommends an L1 non-blocking
 * cache which is the default MEMORY CacheType.
 * 
 * @author Trey Robinson
 */
public class ImageCacheManager {

    /**
     * Volley recommends in-memory L1 cache but both a disk and memory cache are
     * provided. Volley includes a L2 disk cache out of the box but you can
     * technically use a disk cache as an L1 cache provided you can live with
     * potential i/o blocking.
     */
    public enum CacheType {
        DISK, MEMORY
    }

    private static ImageCacheManager mInstance;
    private static int DISK_IMAGECACHE_SIZE = 1024 * 1024 * 50;
    private static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
    private static int DISK_IMAGECACHE_QUALITY = 50;

    /**
     * Volley image loader
     */
    private ImageLoader mImageLoader;

    /**
     * Image cache implementation
     */
    private ImageCache mImageCache;

    private static Context mContext;

    /**
     * @return instance of the cache manager
     */
    public static ImageCacheManager getInstance(Context ctx) {
        mContext = ctx;
        if (mInstance == null)
            mInstance = new ImageCacheManager();

        return mInstance;
    }

    /**
     * Initializer for the manager. Must be called prior to use.
     * 
     * @param context application context
     * @param uniqueName name for the cache location
     * @param cacheSize max size for the cache
     * @param compressFormat file type compression format.
     * @param quality
     */
    public void init(int cacheSize) {
        try {
            mImageCache = new BitmapLruImageCache(cacheSize);
            mImageLoader = new ImageLoader(RequestManager.getInstance(mContext)
                    .getImageRequestQueue(), mImageCache);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmap(String url) {
        try {
            return mImageCache.getBitmap(createKey(url));
        } catch (NullPointerException e) {
            throw new IllegalStateException("Disk Cache Not initialized");
        }
    }

    public void putBitmap(String url, Bitmap bitmap) {
        try {
            mImageCache.putBitmap(createKey(url), bitmap);
        } catch (NullPointerException e) {
            throw new IllegalStateException("Disk Cache Not initialized");
        }
    }

    /**
     * Executes and image load
     * 
     * @param url location of image
     * @param listener Listener for completion
     */
    public void getImage(String url, ImageListener listener) {
        try {
            getImageLoader().get(url, listener);
        } catch (Exception e) {
            return;
        }
    }

    public void getImage(final String url, final ImageView imageView,
            final int defaultImageResId, final int errorImageResId) {
        ImageListener listener = new ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageResId != 0) {
                    imageView.setImageResource(errorImageResId);
                }
            }

            @Override
            public void onResponse(ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    imageView.setImageBitmap(response.getBitmap());
                } else if (defaultImageResId != 0) {
                    imageView.setImageResource(defaultImageResId);
                }
            }
        };
        getImageLoader().get(url, listener);
    }

    /**
     * @return instance of the image loader
     */
    public ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            init(DISK_IMAGECACHE_SIZE);
        }
        return mImageLoader;
    }

    /**
     * Creates a unique cache key based on a url value
     * 
     * @param url url to be used in key creation
     * @return cache key value
     */
    private String createKey(String url) {
        return String.valueOf(url.hashCode());
    }

}
