package cn.suiseiseki.www.waterdream;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogRecord;

/**
 * Created by Administrator on 2016/3/8.
 */
public class ThumbnailDownloader<Token> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;
    Handler mHandler;
    Handler mResponseHandler;
    Listener<Token> mListener;
    Map<Token,String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());

    public interface Listener<Token>
    {
        void onThumbnailDownloaded(Token token,Bitmap thumbnail);
    }

    public void setListener(Listener<Token> listener)
    {
        mListener =listener;
    }

    public ThumbnailDownloader()
    {
        super(TAG);
    }
    public ThumbnailDownloader(Handler responseHandler)
    {
        super(TAG);
        mResponseHandler = responseHandler;
    }
    public void queueThumbnail(Token token,String url)
    {
        requestMap.put(token,url);
        Log.i(TAG, "Got an URL:" + url);
    }

    @Override
    @SuppressLint("HandlerLeak")
    protected void onLooperPrepared()
    {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message message)
            {
                if(message.what == MESSAGE_DOWNLOAD){
                    @SuppressWarnings("unchecked")
                    Token token = (Token) message.obj;
                    Log.i(TAG,"Got a request for url:"+requestMap.get(token));
                    handleRequest(token);}
            }
        };
    }
    private void handleRequest(final Token token)
    {
        try{
            final String url = requestMap.get(token);
            if(url == null)
                return;
            byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes,0,bitmapBytes.length);
            Log.i(TAG,"Bitmap Created");
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(requestMap.get(token).equals(url))
                        return;
                    requestMap.remove(token);
                    mListener.onThumbnailDownloaded(token,bitmap);
                }
            });
        }
        catch (IOException ioe)
        {
            Log.e(TAG,"Error downloading image",ioe);
        }
    }
        public void clearQueue()
        {
             mHandler.removeMessages(MESSAGE_DOWNLOAD);
            requestMap.clear();
        }


}
