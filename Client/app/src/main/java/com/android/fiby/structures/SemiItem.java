package com.android.fiby.structures;

import android.os.Parcel;
import android.os.Parcelable;

public class SemiItem implements Parcelable {
    private String displayName;
    private int id;
    private boolean available;

    public SemiItem(String displayName, int id, boolean available) {
        this.displayName = displayName;
        this.id = id;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isAvailable() {
        return available;
    }

    protected SemiItem(Parcel in) {
        displayName = in.readString();
        id = in.readInt();
        available = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(displayName);
        dest.writeInt(id);
        dest.writeByte((byte) (available ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SemiItem> CREATOR = new Parcelable.Creator<SemiItem>() {
        @Override
        public SemiItem createFromParcel(Parcel in) {
            return new SemiItem(in);
        }

        @Override
        public SemiItem[] newArray(int size) {
            return new SemiItem[size];
        }
    };
}