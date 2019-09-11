package com.hbhgdating.databaseLocal;

/**
 * Created by Developer on 5/23/17.
 */

public class ImageData {


    private int id;
    private String url;
    private String thumb_image;

    public ImageData() {
        super();
    }


    public ImageData(int id, String url, String thumb_image) {
        this.id = id;
        this.url = url;
        this.thumb_image = thumb_image;
    }


    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }
}
