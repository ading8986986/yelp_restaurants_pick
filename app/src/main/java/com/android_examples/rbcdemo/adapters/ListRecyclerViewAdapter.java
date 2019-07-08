package com.android_examples.rbcdemo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.android_examples.rbcdemo.R;
import com.android_examples.rbcdemo.cache.ImageCacheManager;
import com.android_examples.rbcdemo.model.CategoryFeed;
import com.android_examples.rbcdemo.model.RestaurantNailFeed;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by JUNED on 6/16/2016.
 */
public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder>{
    String TAG = "RecyclerViewAdapter";
    List<CategoryFeed> feeds  = new ArrayList<CategoryFeed>();
    Context context;
    Listener listener;

    public ListRecyclerViewAdapter(Context context, Listener listener){
        this.context = context;
        this.listener = listener;
    }


    public static class ContentViewHolder extends ViewHolder{

        public TextView name;
        public TextView rate;
        public TextView review;
        public TextView category;
        public TextView addr;
        public NetworkImageView nail;
        public View view;

        public ContentViewHolder(View v){
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

    public static class CategoryViewHolder extends ViewHolder{

        public TextView categoryName, itemNum;

        public CategoryViewHolder(View v){
            super(v);
            categoryName = (TextView) v.findViewById(R.id.tv_category_name);
            itemNum = (TextView) v.findViewById(R.id.tv_category_number);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v;
        ViewHolder holder;
        if(feeds.get(viewType).type == 0){
            v = LayoutInflater.from(context).inflate(R.layout.list_recycler_view_hearder,parent,false);
            holder  = new CategoryViewHolder(v);
        } else{
            v = LayoutInflater.from(context).inflate(R.layout.list_recycler_view_items,parent,false);
            holder  = new ContentViewHolder(v);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, final int position){
        ;
        if(feeds.get(position).type == 0) {
            CategoryViewHolder holder = (CategoryViewHolder)Vholder;
            holder.categoryName.setText(feeds.get(position).name);
            holder.itemNum.setText("("+feeds.get(position).number+")");
        }
        else {
            ContentViewHolder holder = (ContentViewHolder)Vholder;
            holder.name.setText(feeds.get(position).restaurantNailFeed.name);
            holder.rate.setText(feeds.get(position).restaurantNailFeed.rate);
            holder.review.setText(feeds.get(position).restaurantNailFeed.reviewNum + " Reviews");
            holder.category.setText(feeds.get(position).restaurantNailFeed.category);
            holder.addr.setText(feeds.get(position).restaurantNailFeed.addr);
            holder.nail.setImageUrl(feeds.get(position).restaurantNailFeed.imgUrl, ImageCacheManager.getInstance(this.context).getImageLoader());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null) listener.onItemClickListener(feeds.get(position).restaurantNailFeed.id);
                }
            });
        }



    }

    @Override
    public int getItemCount(){
         return feeds.size();
    }

    public void setDataFeeds(JSONArray dataFeeds){
        this.feeds.clear();
        Map<String,List<RestaurantNailFeed>> map = new HashMap<String,List<RestaurantNailFeed>>();

        for(int i = 0; i < dataFeeds.length();i++) {
            try {
                JSONObject params = (JSONObject) dataFeeds.get(i);
                RestaurantNailFeed feed = new RestaurantNailFeed();
                feed.id = params.getString("id");
                feed.name = params.getString("name");
                feed.rate = params.getString("rating");
                feed.reviewNum = params.getInt("review_count");
                feed.category = ((JSONObject) (params.getJSONArray("categories").get(0))).getString("title");
                feed.addr = params.getJSONObject("location").getString("address1");
                feed.imgUrl = params.getString("image_url");
                if (map.get(feed.category) == null) {
                    map.put(feed.category, new ArrayList<RestaurantNailFeed>());
                }
                map.get(feed.category).add(feed);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            CategoryFeed categoryFeed = new CategoryFeed();
            categoryFeed.name = (String) pair.getKey();
            ArrayList<RestaurantNailFeed> list = (ArrayList)pair.getValue();
            categoryFeed.number = list.size();
            categoryFeed.type = 0;
            feeds.add(categoryFeed);

            for(int j = 0;j<list.size();j++){
                CategoryFeed contentFeed = new CategoryFeed();
                contentFeed.type = 1;
                contentFeed.restaurantNailFeed = list.get(j);
                feeds.add(contentFeed);
            }
        }
        Log.i(TAG,"-----"+feeds.size());

    }



    public interface Listener {
        void onItemClickListener(String id);
    }

}