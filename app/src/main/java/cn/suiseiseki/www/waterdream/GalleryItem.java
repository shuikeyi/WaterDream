package cn.suiseiseki.www.waterdream;

/**
 * Created by Administrator on 2016/3/8.
 */
public class GalleryItem {
    private String mCaption;

    public String getmCaption() {
        return mCaption;
    }

    public void setmCaption(String mCaption) {
        this.mCaption = mCaption;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    private String mId;
    private String mUrl;
    public String toString()
    {
        return mCaption;
    }
}