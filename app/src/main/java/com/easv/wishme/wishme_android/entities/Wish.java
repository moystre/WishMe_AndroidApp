package com.easv.wishme.wishme_android.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Wish implements Parcelable {

    String name, price, link, description, image;
    int rating;

    public Wish() {
    }

    public Wish(String name, String price, String link, String description, String image, int rating) {
        this.name = name;
        this.price = price;
        this.link = link;
        this.description = description;
        this.image = image;
        this.rating = rating;
    }

    protected Wish(Parcel in) {
        name = in.readString();
        price = in.readString();
        link = in.readString();
        description = in.readString();
        image = in.readString();
        rating = in.readInt();
    }

    public static final Creator<Wish> CREATOR = new Creator<Wish>() {
        @Override
        public Wish createFromParcel(Parcel in) {
            return new Wish(in);
        }

        @Override
        public Wish[] newArray(int size) {
            return new Wish[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(link);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeInt(rating);
    }
}
