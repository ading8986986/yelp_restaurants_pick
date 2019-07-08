package com.android_examples.rbcdemo.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android_examples.rbcdemo.adapters.DividerGridItemDecoration;
import com.android_examples.rbcdemo.R;
import com.android_examples.rbcdemo.adapters.ListRecyclerViewAdapter;
import com.android_examples.rbcdemo.model.RestaurantNailFeed;
import com.android_examples.rbcdemo.presenter.SearchPresenter;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;



public class SearchActivity extends Activity implements SearchPresenter.MyView,  ListRecyclerViewAdapter.Listener, View.OnKeyListener, TextView.OnEditorActionListener {

    RecyclerView recyclerView;

    ListRecyclerViewAdapter recyclerViewAdapter;

    RecyclerView.LayoutManager recyclerViewLayoutManager;

    List<RestaurantNailFeed> datas = new ArrayList<RestaurantNailFeed>();
    Handler uiHandler;
    SearchPresenter searchPresenter;
    LinearLayout  mLoadingView;
    EditText etSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        recyclerView = (RecyclerView) findViewById(R.id.rc_main);
        mLoadingView = (LinearLayout) findViewById(R.id.ll_progressBar);
        etSearch = (EditText) findViewById(R.id.et_keyword);
        etSearch.setOnEditorActionListener(this);
        //Change 2 to your choice because here 2 is the number of Grid layout Columns in each row.
        recyclerViewLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewAdapter = new ListRecyclerViewAdapter(this,this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));

        searchPresenter = new SearchPresenter(this);
        searchPresenter.attachMainView(this);


        uiHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mLoadingView.setVisibility(View.GONE);
                JSONArray array = (JSONArray) msg.obj;
                if(array == null || array.length()==0)
                    Toast.makeText(SearchActivity.this, "no result", Toast.LENGTH_SHORT).show();
                else recyclerViewAdapter.setDataFeeds(array);
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

    }

    @Override
    public void onRestaurantsError() {
        Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
        Message msg = uiHandler.obtainMessage();
        msg.sendToTarget();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchPresenter.detachMainView();
    }

    @Override
    public void onItemClickListener(String id) {
        Intent intent = new Intent(this,DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            mLoadingView.setVisibility(View.VISIBLE);

            searchPresenter.getRestaurants(etSearch.getEditableText().toString());
        }
        return false;
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
