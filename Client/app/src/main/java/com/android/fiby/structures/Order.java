package com.android.fiby.structures;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fiby.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Order implements Parcelable{
    private ArrayList<ItemToPurchase> shopList;

    private int id;

    private String date;

    private boolean isOpen;

    public Order(int id, ArrayList<ItemToPurchase> shopList, String date) {
        this.id = id;
        this.shopList = shopList;
        this.date = date;
        if(this.date.length() != 8){
            this.date = "01012017";
        }
        isOpen = false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void changeOpen() {
        isOpen = !isOpen;
    }

    protected Order(Parcel in) {
        shopList = in.createTypedArrayList(ItemToPurchase.CREATOR);
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(shopList);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public ArrayList<ItemToPurchase> getShopList() {
        return shopList;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public double getTotalPrice(){
        double sum = 0;
        for(ItemToPurchase item : shopList){
            sum += item.getFoodItem().getPrice();
        }
        return sum;
    }


    public View getView(View convertView, final ImageView open) {
        // Lookup view for data population
        TextView orderDate = (TextView) convertView.findViewById(R.id.order_date);
        TextView orderPrice = (TextView) convertView.findViewById(R.id.total_price);

        // Populate the data into the template view using the data object
        orderDate.setText(getDate().substring(0,2)+"-"+getDate().substring(2,4)+"-"+getDate().substring(4,8));
        orderPrice.setText(Double.toString(getTotalPrice()));
        if(this.isOpen){
            open.setRotation(270);
        }
        return convertView;
    }
}
