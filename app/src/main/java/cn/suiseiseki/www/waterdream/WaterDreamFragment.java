package cn.suiseiseki.www.waterdream;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/7.
 */
public class WaterDreamFragment extends Fragment {
    // define inner class FetchItemsTask
    private class FetchItemsTask extends AsyncTask<Void,Void,ArrayList<GalleryItem>>
    {
        @Override
        protected ArrayList<GalleryItem> doInBackground(Void... params)
        {
            return new FlickrFetchr().fetchItems();
        }
        @Override
        protected void onPostExecute(ArrayList<GalleryItem> items)
        {
            mItems = items;
            setupAdapter();
        }
    }
    GridView mgridView;
    ArrayList<GalleryItem> mItems;
    ThumbnailDownloader<ImageView> mThumbnailThread;
    public static final String TAG = "WaterDreamFragment";
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute();
        mThumbnailThread = new ThumbnailDownloader<ImageView>(new Handler());
        mThumbnailThread.setListener(new ThumbnailDownloader.Listener<ImageView>()
        {
            public void onThumbnailDownloaded(ImageView imageView,Bitmap thumbnail)
            {
                if(isVisible())
                    imageView.setImageBitmap(thumbnail);
            }
        });
        mThumbnailThread.start();
        mThumbnailThread.getLooper();
        Log.i(TAG,"Background thread started");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState)
    {
        super.onCreateView(inflater, parent, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_water_dream,parent,false);
        mgridView = (GridView)v.findViewById(R.id.gridView);
        setupAdapter();
        return v;
    }

    void setupAdapter()
    {
        if(getActivity() == null || mgridView == null) return;
        if(mItems != null)
        {
            mgridView.setAdapter(new GalleyItemAdapter(mItems));
        }
        else
            mgridView.setAdapter(null);
    }

    private class GalleyItemAdapter extends ArrayAdapter<GalleryItem>{
        public GalleyItemAdapter(ArrayList<GalleryItem> items)
        {
            super(getActivity(),0,items);
        }
        @Override
        public View getView(int position,View convertView,ViewGroup parent)
        {
            if(convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.gallery_item,parent,false);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.gallery_item_imageView);
            imageView.setImageResource(R.drawable.brian_up_close);
            GalleryItem item = getItem(position);
            mThumbnailThread.queueThumbnail(imageView,item.getmUrl());
            return convertView;
        }
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mThumbnailThread.quit();
        Log.i(TAG,"Background thread destroyed");
    }
    @Override
    public void onDestroyView()
    {
        mThumbnailThread.clearQueue();
        super.onDestroyView();
    }

}
