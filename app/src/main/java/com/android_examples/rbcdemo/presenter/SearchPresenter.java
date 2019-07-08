package com.android_examples.rbcdemo.presenter;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android_examples.rbcdemo.model.RestaurantNailFeed;
import com.android_examples.rbcdemo.request.Constants;
import com.android_examples.rbcdemo.request.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchPresenter {
    private static Context context;
    private List<RestaurantNailFeed> restaurantNailFeeds;
    private MyView myView;
    private String TAG = "Presenter";

    private static SearchPresenter mInstance;


    public SearchPresenter(Context context){
        this.context = context;
    }

    public void attachMainView(MyView mainView){
        this.myView = mainView;
    }

    public void detachMainView(){
        this.myView = null;
    }

    public void getRestaurants(String keyword){
        String url = Constants.URL +"location="+keyword;
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
                        JSONArray data = object.optJSONArray("businesses");
                        if(myView!=null) myView.onRestaurantsReceived(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if(myView!=null) myView.onRestaurantsError();
                }
            }
        };
    }

    public Response.ErrorListener onFail() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(myView!=null) myView.onRestaurantsError();
            }
        };

    }

    public interface MyView{
        void onRestaurantsReceived(JSONArray restaurantFeeds);
        void onRestaurantsError();
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
