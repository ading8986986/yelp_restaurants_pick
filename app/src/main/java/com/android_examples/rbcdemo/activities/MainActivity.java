package com.android_examples.rbcdemo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android_examples.rbcdemo.adapters.DividerGridItemDecoration;
import com.android_examples.rbcdemo.R;
import com.android_examples.rbcdemo.adapters.RecyclerViewAdapter;
import com.android_examples.rbcdemo.model.RestaurantNailFeed;
import com.android_examples.rbcdemo.presenter.MainPresenter;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends Activity implements MainPresenter.MainView, View.OnClickListener, RecyclerViewAdapter.Listener {

    RecyclerView recyclerView;

    RecyclerViewAdapter recyclerViewAdapter;

    RecyclerView.LayoutManager recyclerViewLayoutManager;

    List<RestaurantNailFeed> datas = new ArrayList<RestaurantNailFeed>();
    Handler uiHandler;
    MainPresenter mainPresenter;
    LinearLayout  mLoadingView;
    TextView sortByRecommended, sortByRating;
    EditText etSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rc_main);
        mLoadingView = (LinearLayout) findViewById(R.id.ll_progressBar);
        sortByRecommended = (TextView) findViewById(R.id.tv_recommend);
        sortByRating = (TextView) findViewById(R.id.tv_rate);
        sortByRecommended.setSelected(true);
        sortByRecommended.setOnClickListener(this);
        sortByRating.setOnClickListener(this);
        etSearch = (EditText) findViewById(R.id.et_keyword);
        etSearch.setOnClickListener(this);
        //Change 2 to your choice because here 2 is the number of Grid layout Columns in each row.
        recyclerViewLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(this,this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));

        mainPresenter = new MainPresenter(this);
        mainPresenter.attachMainView(this);
        mLoadingView.setVisibility(View.VISIBLE);


        mainPresenter.getRestaurants();



        uiHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.obj!=null) recyclerViewAdapter.setDataFeeds((JSONArray) msg.obj);
                uiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingView.setVisibility(View.GONE);
                    }
                }, 2000);
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
    public void onRestaurantsReceived( JSONArray restaurantFeeds) {
        Message msg = uiHandler.obtainMessage();
        msg.obj = restaurantFeeds;
        msg.sendToTarget();
//        List<CollectionFeed> collectionFeeds = new ArrayList<CollectionFeed>();
//        for(int i = 0; i < list.length();i++){
//            try {
//                JSONObject params = (JSONObject)list.get(i);
//                CollectionFeed feed = new CollectionFeed();
//                feed.name  = params.getString("title");
//                feed.updateDate = params.getString("updated_at");
//                JSONObject image = params.getJSONObject("image");
//                feed.imageUrl = image.getString("src");
//                feed.bodyHtml = params.getString("body_html");
//                feed.id = params.getString("id");
//                collectionFeeds.add(feed);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        msg.obj = collectionFeeds;
//        msg.sendToTarget();

    }

    @Override
    public void onRestaurantsError() {
        Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
        Message msg = uiHandler.obtainMessage();
        msg.sendToTarget();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_recommend:
                recyclerViewAdapter.setSortType(0);
                sortByRecommended.setSelected(true);
                sortByRating.setSelected(false);
                break;
            case R.id.tv_rate:
                recyclerViewAdapter.setSortType(1);
                sortByRecommended.setSelected(false);
                sortByRating.setSelected(true);
                break;
            case R.id.et_keyword:
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.detachMainView();
    }

    @Override
    public void onItemClickListener(String id) {
        Intent intent = new Intent(this,DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }

    //
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent=new Intent(this,CollectionDetailActivity.class);
//        CollectionFeed collectionFeed = collectionListAdapter.getItem(position);
//        Bundle bundle = new Bundle();
//        bundle.putString("name", collectionFeed.name);
//        bundle.putString("src", collectionFeed.imageUrl);
//        bundle.putString("bodyHtml", collectionFeed.bodyHtml);
//        bundle.putString("id", collectionFeed.id);
//        intent.putExtra("info", bundle);
//        startActivity(intent);
//    }
}
