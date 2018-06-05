package com.android.fiby.structures;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.fiby.comparators.ComparatorSemi;

import java.util.ArrayList;
import java.util.Collections;

public class FoodItem implements Parcelable {

    private String displayName;

    private String image;

    private double price;

    private String category;

    private boolean available;

    private int id;

    private ArrayList<SemiItem> optionalAdditions;

    public FoodItem(int id, String displayName, String image, double price, String category, boolean available) {
        this.id = id;
        this.displayName = displayName;
        this.image = image;
        this.price = price;
        this.category = category;
        this.available = available;
        this.optionalAdditions = new ArrayList<>();
    }

    public void addOptionalAddition(SemiItem semiItem){
        this.optionalAdditions.add(semiItem);
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getImage() {
        return image;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return available;
    }

    public ArrayList<SemiItem> getOptionalAdditions(){
        return this.optionalAdditions;
    }

    protected FoodItem(Parcel in) {
        displayName = in.readString();
        image = in.readString();
        price = in.readDouble();
        category = in.readString();
        available = in.readByte() != 0x00;
        id = in.readInt();
        if (in.readByte() == 0x01) {
            optionalAdditions = new ArrayList<SemiItem>();
            in.readList(optionalAdditions, SemiItem.class.getClassLoader());
        } else {
            optionalAdditions = null;
        }
    }

    public void sortSemiItems(){
        Collections.sort(optionalAdditions, new ComparatorSemi());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(displayName);
        dest.writeString(image);
        dest.writeDouble(price);
        dest.writeString(category);
        dest.writeByte((byte) (available ? 0x01 : 0x00));
        dest.writeInt(id);
        if (optionalAdditions == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(optionalAdditions);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FoodItem> CREATOR = new Parcelable.Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };
}