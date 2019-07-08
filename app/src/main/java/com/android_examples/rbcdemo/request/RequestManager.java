
package com.android_examples.rbcdemo.request;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

public class RequestManager {
    public final String TAG = "RequestManager";

    private static RequestQueue mRequestQueue;

    private static RequestQueue mImageRequestQueue;

    private static RequestManager mInstance;
    private static Context mContext = null;

    public static RequestManager getInstance(Context ctx) {
        mContext = ctx;
        if (mInstance == null) {
            synchronized (RequestManager.class) {
                if (mInstance == null) {
                    mInstance = new RequestManager();
                }
            }
        }
        return mInstance;
    }

    private RequestManager() {

    }

    public void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mImageRequestQueue = Volley.newRequestQueue(context);
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    public RequestQueue getImageRequestQueue() {
        if (mImageRequestQueue == null) {
            mImageRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mImageRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {

        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());
        req.setShouldCache(false);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {

        addToRequestQueue(req, TAG);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
