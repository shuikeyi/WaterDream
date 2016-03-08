package cn.suiseiseki.www.waterdream;
import android.net.Uri;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/7.
 */
public class FlickrFetchr {
    public static final String TAG = "FlickrFetcher";
    private static final String ENDPOINT = "http://api.flickr.com/services/rest/",API_KEY = "4f721bgafa75bf6d2cb9af54f937bb70",METHOD_GET_RECENT = "flickr.photos.getRecent",PARAM_EXTRAS="extras",EXTRA_SMALL_URL = "url_s";
    private static final String XML_PHOTO = "photo";

    byte[] getUrlBytes(String urlSpec) throws IOException
    {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            InputStream input = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = input.read(buffer)) > 0) {
                output.write(buffer, 0, bytesRead);
            }
            output.close();
            return output.toByteArray();
        }
        finally {
            connection.disconnect();
        }
    }

    public String getUrl(String urlSpec) throws  IOException
    {
        return new String(getUrlBytes(urlSpec));
    }

    public ArrayList<GalleryItem>  fetchItems()
    {
        ArrayList<GalleryItem> items = new ArrayList<>();
        try{
            String url = Uri.parse(ENDPOINT).buildUpon().appendQueryParameter("method",METHOD_GET_RECENT).appendQueryParameter("api_key",API_KEY).appendQueryParameter(PARAM_EXTRAS,EXTRA_SMALL_URL).build().toString();
            String xmlString = getUrl(url);
            Log.i(TAG, "Received xml:"+xmlString);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));
            parseItems(items,parser);
        }
        catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        catch (XmlPullParserException xppe)
        {
            Log.e(TAG,"Failed to Parse items",xppe);
        }
        return items;
    }

    public void fetchItems(String net)
    {
        try{
            String xmlString = getUrl(net);
            Log.i(TAG, "Received xml:"+xmlString);
        }
        catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
    }

    void parseItems(ArrayList<GalleryItem> items,XmlPullParser parser) throws XmlPullParserException,IOException
    {
        int eventType = parser.next();
        while(eventType!=XmlPullParser.END_DOCUMENT)
        {
            if(eventType == XmlPullParser.START_TAG && XML_PHOTO.equals(parser.getName()))
            {
                String id = parser.getAttributeValue(null,"id");
                String Caption = parser.getAttributeValue(null,"title");
                String smallUrl = parser.getAttributeValue(null,EXTRA_SMALL_URL);
                GalleryItem item = new GalleryItem();
                item.setmId(id);item.setmCaption(Caption);item.setmUrl(smallUrl);
                items.add(item);
            }
        }
    }
}
