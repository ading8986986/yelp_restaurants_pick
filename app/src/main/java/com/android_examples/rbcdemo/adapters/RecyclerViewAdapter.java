package com.android_examples.rbcdemo.adapters;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android_examples.rbcdemo.R;
import com.android_examples.rbcdemo.activities.DetailActivity;
import com.android_examples.rbcdemo.cache.ImageCacheManager;
import com.android_examples.rbcdemo.model.RestaurantNailFeed;
import com.android.volley.toolbox.NetworkImageView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JUNED on 6/16/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    String TAG = "RecyclerViewAdapter";
    List<RestaurantNailFeed> dataFeedsByRecommendation  = new ArrayList<RestaurantNailFeed>();
    List<RestaurantNailFeed> dataFeedsByRating=new ArrayList<RestaurantNailFeed>();
    List<RestaurantNailFeed> curList;
    int sortType = 0; //0 recommend, 1 rating
    Context context;
    Listener listener;

    public RecyclerViewAdapter(Context context, Listener listener){
        this.context = context;
        this.listener = listener;
        this.curList = dataFeedsByRecommendation;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public TextView rate;
        public TextView review;
        public TextView category;
        public TextView addr;
        public NetworkImageView nail;
        public View view;

        public ViewHolder(View v){
            super(v);
            view = v;
            name = (TextView) v.findViewById(R.id.tv_name);
            rate = (TextView) v.findViewById(R.id.tv_rate);
            review = (TextView) v.findViewById(R.id.tv_review);
            category = (TextView) v.findViewById(R.id.tv_category);
            addr = (TextView) v.findViewById(R.id.tv_address);
            nail = (NetworkImageView) v.findViewById(R.id.image_nail);
        }
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view1 = LayoutInflater.from(context).inflate(R.layout.recycler_view_items,parent,false);

        ViewHolder viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, final int position){
        Vholder.name.setText(curList.get(position).name);
        Vholder.rate.setText(curList.get(position).rate);
        Vholder.review.setText(curList.get(position).reviewNum + " Reviews");
        Vholder.category.setText(curList.get(position).category);
        Vholder.addr.setText(curList.get(position).addr);
        Vholder.nail.setImageUrl(curList.get(position).imgUrl, ImageCacheManager.getInstance(this.context).getImageLoader());

        Vholder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null) listener.onItemClickListener(curList.get(position).id);
            }
        });

    }

    @Override
    public int getItemCount(){
         return curList.size();
    }

    public void setDataFeeds(JSONArray dataFeeds){
        this.dataFeedsByRating.clear();
        this.dataFeedsByRecommendation.clear();
        this.curList = this.dataFeedsByRecommendation;
        for(int i = 0; i < dataFeeds.length();i++){
            try {
                JSONObject params = (JSONObject)dataFeeds.get(i);
                RestaurantNailFeed feed = new RestaurantNailFeed();
                feed.id = params.getString("id");
                feed.name  = params.getString("name");
                feed.rate = params.getString("rating");
                feed.reviewNum = params.getInt("review_count");
                feed.category = ((JSONObject)(params.getJSONArray("categories").get(0))).getString("title");
                feed.addr = params.getJSONObject("location").getString("address1");
                feed.imgUrl = params.getString("image_url");
                this.dataFeedsByRecommendation.add(feed);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.sortFeedsByRating();
    }


    public void setSortType(int type){ //1 recommend, 2 rating
        this.sortType = type;
//        if(this.sortType ==1 && this.dataFeedsByRating.size() == 0) this.sortFeedsByRating();
        this.curList =  this.sortType == 0? this.dataFeedsByRecommendation:this.dataFeedsByRating;
        this.notifyDataSetChanged();
    }

    private void sortFeedsByRating(){
        final int  size = this.dataFeedsByRecommendation.size();
        dataFeedsByRating.add(this.dataFeedsByRecommendation.get(0));
        for (int i = 1; i<size;i++){
            double newRating = Double.parseDouble(this.dataFeedsByRecommendation.get(i).rate);
            boolean flag = false;
            for(int j = dataFeedsByRating.size() -1;j>=0;j--){
                double rating = Double.parseDouble(this.dataFeedsByRating.get(j).rate);
                if(newRating>=rating){  continue;
                } else{
                    dataFeedsByRating.add(j+1,this.dataFeedsByRecommendation.get(i));
                    flag = true;
                    break;
                }
            }
            if(!flag) dataFeedsByRating.add(0,this.dataFeedsByRecommendation.get(i));
        }
        Log.d(TAG, "size"+dataFeedsByRating.size());
    }

    public interface Listener {
        void onItemClickListener(String id);
    }

}