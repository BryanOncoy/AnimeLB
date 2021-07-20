package com.lb.animelb.clases;

public class AnimeInfo {
    private String title;
    private String imageUrl;
    private String animeUrl;

    public AnimeInfo(String title, String imageUrl, String animeUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.animeUrl = animeUrl;
    }

    public AnimeInfo(){

    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAnimeUrl() {
        return animeUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof AnimeInfo)) {
            return false;
        }

        AnimeInfo c = (AnimeInfo) obj;
        return this.title.equals(c.title);
    }
}
