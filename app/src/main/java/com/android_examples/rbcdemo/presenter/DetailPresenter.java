package com.android_examples.rbcdemo.presenter;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android_examples.rbcdemo.model.RestaurantDetailFeed;
import com.android_examples.rbcdemo.model.RestaurantNailFeed;
import com.android_examples.rbcdemo.request.Constants;
import com.android_examples.rbcdemo.request.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailPresenter {
    private static Context context;
    private RestaurantDetailFeed restaurantDetailFeed;
    private DetailView detailView;
    private String TAG = "DetailPresenter";

    private static DetailPresenter mInstance;


    public DetailPresenter(Context context){
        this.context = context;
    }

    public void attachMainView(DetailView detailView){
        this.detailView = detailView;
    }

    public void detachMainView(){
        this.detailView = null;
    }

    public void getRestaurantDetail(String id){
        String url = Constants.DETAIL_URL  + id;
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                onSucceed(),
                onFail()
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + Constants.KEY);
                return params;
            }
        };

        RequestManager.getInstance(context)
                .addToRequestQueue(getRequest, TAG);

    }

    private Response.Listener<String> onSucceed() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onSucceed:" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if(object!=null){
                        if(detailView!=null) detailView.onDetailReceived(object);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(detailView!=null) detailView.onDetailError(new VolleyError());
                }
            }
        };
    }

    public Response.ErrorListener onFail() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,error.getMessage());
                if(detailView!=null) detailView.onDetailError(error);
            }
        };

    }

    public interface DetailView{
        void onDetailReceived(JSONObject restaurantFeeds);
        void onDetailError(VolleyError error);
    }

//
//
//    String TAG = 'presenter'
//    JsonObjectRequest profileRequest = new JsonObjectRequest(Request.Method.GET,
//            Constants.URL,null,
//            profileGetSucceed(), profileGetFail()) {
//    };
//
//        RequestManager.getInstance(MyApplication)
//                .addToRequestQueue(profileRequest, TAG);
}
