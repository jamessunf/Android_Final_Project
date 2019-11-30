package com.example.android_lab1.news;

/**
 * It's the data model for the list view adapter
 */

public class NewsSearchResultModel {
    private String title;
    private String url;
    private String description;

    public NewsSearchResultModel() {
        this(null, null,null);
    }

    public NewsSearchResultModel(String title, String url, String description) {
        setTitle(title);
        setUrl(url);
        setDescription(description);
    }

    public void setTitle(String title) {

        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return this.url;
    }

   public void setDescription(String description) {
      this.description = description;
    }
    public String getDescription() {
        return this.description;
   }


}
