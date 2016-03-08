package cn.suiseiseki.www.waterdream;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

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
    public static final String TAG = "WaterDreamFragment";
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute();
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
            mgridView.setAdapter(new ArrayAdapter<GalleryItem>(getActivity(),android.R.layout.simple_gallery_item,mItems));
        }
        else
            mgridView.setAdapter(null);
    }

}
