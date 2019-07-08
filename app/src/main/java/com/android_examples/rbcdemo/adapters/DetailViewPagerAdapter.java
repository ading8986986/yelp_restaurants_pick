package com.android_examples.rbcdemo.adapters;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import com.android_examples.rbcdemo.R;
import com.android_examples.rbcdemo.cache.ImageCacheManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DetailViewPagerAdapter extends PagerAdapter {
    private Context context;
    private List<String> imgUrls = new ArrayList<>();
    public DetailViewPagerAdapter(Context context) {
        this.context = context;
    }

    public void setImgUrls(JSONArray urls){
        imgUrls.clear();
        int length = urls.length();
        for(int i = 0;i<length;i++){
            try {
                this.imgUrls.add(urls.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        notifyDataSetChanged();
    }

    /*
    This callback is responsible for creating a page. We inflate the layout and set the drawable
    to the ImageView based on the position. In the end we add the inflated layout to the parent
    container .This method returns an object key to identify the page view, but in this example page view
    itself acts as the object key
    */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.detail_viewpager, null);
        NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.image);
        imageView.setImageUrl(this.imgUrls.get(position), ImageCacheManager.getInstance(this.context).getImageLoader());
        container.addView(view);
        return view;
    }
    /*
    This callback is responsible for destroying a page. Since we are using view only as the
    object key we just directly remove the view from parent container
    */
    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }
    /*
    Returns the count of the total pages
    */
    @Override
    public int getCount() {
        return imgUrls.size();
    }
    /*
    Used to determine whether the page view is associated with object key returned by instantiateItem.
    Since here view only is the key we return view==object
    */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

}
