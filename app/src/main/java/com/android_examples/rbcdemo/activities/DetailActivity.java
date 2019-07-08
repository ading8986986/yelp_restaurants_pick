package com.android_examples.rbcdemo.activities;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android_examples.rbcdemo.R;
import com.android_examples.rbcdemo.adapters.DetailViewPagerAdapter;
import com.android_examples.rbcdemo.presenter.DetailPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends Activity implements DetailPresenter.DetailView {

    DetailPresenter presenter;
    TextView name;
    TextView rate;
    TextView review;
    TextView category;
    TextView addr;
    TextView phone;
    TextView price;
    TextView picNum;
    ViewPager viewPager;
    View view;
    LinearLayout mLoadingView;
    DetailViewPagerAdapter adapter;


    Handler uiHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String id = getIntent().getBundleExtra("bundle").getString("id");

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        name = (TextView) findViewById(R.id.tv_name);
        rate = (TextView) findViewById(R.id.tv_rate);
        review = (TextView) findViewById(R.id.tv_review);
        category = (TextView) findViewById(R.id.tv_category);
        addr = (TextView) findViewById(R.id.tv_address);
        price = (TextView) findViewById(R.id.tv_price);
        phone = (TextView) findViewById(R.id.tv_phone);
        mLoadingView = (LinearLayout) findViewById(R.id.ll_progressBar);
        picNum = (TextView) findViewById(R.id.tv_imgnum);
        adapter = new DetailViewPagerAdapter(this);
        viewPager.setAdapter(adapter);


        presenter = new DetailPresenter(this);
        presenter.attachMainView(this);
        mLoadingView.setVisibility(View.VISIBLE);
        presenter.getRestaurantDetail(id);

        uiHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mLoadingView.setVisibility(View.GONE);
                setViewData((JSONObject) msg.obj);
//            mLoadingView.setVisibility(View.GONE);
//            hotelInfos=hotelPriceGrabber.getHotelInfoArrayList();
//                ArrayList<CollectionFeed> newFeeds = (ArrayList<CollectionFeed>) msg.obj;
//                collectionListAdapter.setCollectionFeeds(newFeeds);
//                collectionListAdapter.notifyDataSetInvalidated();
////            hotelListAdapter.notifyDataSetInvalidated();
            }
        };

    }

    @Override
    public void onDetailReceived(JSONObject restaurantFeed) {
        Message msg = uiHandler.obtainMessage();
        msg.obj = restaurantFeed;
        msg.sendToTarget();
    }

    @Override
    public void onDetailError(VolleyError error) {

    }

    private void setViewData( JSONObject object){
        try {
            name.setText(object.getString("name"));
            rate.setText(object.getString("rating"));
            review.setText(object.getInt("review_count") + " Reviews");
            category.setText(((JSONObject)(object.getJSONArray("categories").get(0))).getString("title"));
            addr.setText(object.getJSONObject("location").getString("address1"));
            price.setText("Price: " +object.getString("price"));
            phone.setText("Tel: "+object.getString("phone"));
            JSONArray imgs = object.getJSONArray("photos");
            picNum.setText("All " + imgs.length() + " Pictures");
            adapter.setImgUrls(imgs);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
