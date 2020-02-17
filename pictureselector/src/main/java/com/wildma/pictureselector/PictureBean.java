package com.wildma.pictureselector;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author       wildma
 * Github       https://github.com/wildma
 * Date         2020/02/16
 * Desc	        ${图片实体类}
 */
public class PictureBean implements Parcelable {

    //原图地址
    private String  path;
    //图片 Uri
    private Uri     uri;
    //是否裁剪
    private boolean isCut;

    public String getPath() {
        return path == null ? "" : path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public boolean isCut() {
        return isCut;
    }

    public void setCut(boolean cut) {
        isCut = cut;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeParcelable(this.uri, flags);
        dest.writeByte(this.isCut ? (byte) 1 : (byte) 0);
    }

    public PictureBean() {
    }

    protected PictureBean(Parcel in) {
        this.path = in.readString();
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.isCut = in.readByte() != 0;
    }

    public static final Creator<PictureBean> CREATOR = new Creator<PictureBean>() {
        @Override
        public PictureBean createFromParcel(Parcel source) {
            return new PictureBean(source);
        }

        @Override
        public PictureBean[] newArray(int size) {
            return new PictureBean[size];
        }
    };
}
