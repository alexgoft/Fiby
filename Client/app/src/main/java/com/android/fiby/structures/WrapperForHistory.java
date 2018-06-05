package com.android.fiby.structures;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;

public class WrapperForHistory implements Parcelable {
    private Order order;
    private ItemToPurchase item;

    public WrapperForHistory(Order order) {
        this.order = order;
    }

    public WrapperForHistory(ItemToPurchase item) {
        this.item = item;
    }

    public Order getOrder() {
        return order;
    }

    public ItemToPurchase getItem() {
        return item;
    }

    public View getView(View convertView, final ImageView open) {
        if(order != null){
            return order.getView(convertView, open);
        }
        else{
            return item.getView(convertView);
        }
    }

    protected WrapperForHistory(Parcel in) {
        order = in.readParcelable(Order.class.getClassLoader());
        item = in.readParcelable(ItemToPurchase.class.getClassLoader());
    }

    public static final Creator<WrapperForHistory> CREATOR = new Creator<WrapperForHistory>() {
        @Override
        public WrapperForHistory createFromParcel(Parcel in) {
            return new WrapperForHistory(in);
        }

        @Override
        public WrapperForHistory[] newArray(int size) {
            return new WrapperForHistory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(order, i);
        parcel.writeParcelable(item, i);
    }
}
