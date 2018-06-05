package com.android.fiby.structures;


import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.fiby.R;

import java.util.ArrayList;

public class ItemToPurchase implements Parcelable {
    private int favId;
    private int orderId;


    private FoodItem foodItem;
    private ArrayList<SemiItem> semiItems;

    public ItemToPurchase(FoodItem foodItem, int favId) {
        this.foodItem = foodItem;
        this.favId = favId;
        this.semiItems = new ArrayList<>();
    }

    public ItemToPurchase(FoodItem foodItem) {
        this.foodItem = foodItem;
        this.semiItems = new ArrayList<>();
    }

    public ItemToPurchase(){
        this.semiItems = new ArrayList<>();
    }

    protected ItemToPurchase(Parcel in) {
        favId = in.readInt();
        foodItem = in.readParcelable(FoodItem.class.getClassLoader());
        semiItems = in.createTypedArrayList(SemiItem.CREATOR);
    }


    public void addSemiItem (SemiItem semiItem){
        if(this.semiItems == null){
            this.semiItems = new ArrayList<>();
        }
        this.semiItems.add(semiItem);
    }

    public void removeSemiItem (SemiItem semiItem){
        if(this.semiItems == null){
            this.semiItems = new ArrayList<>();
        }
        this.semiItems.remove(semiItem);
    }

    public int getFavId() {
        return favId;
    }

    public void setFavId(int favId) {
        this.favId = favId;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public ArrayList<SemiItem> getSemiItems() {
        if(this.semiItems == null){
            semiItems = new ArrayList<>();
        }
        return semiItems;
    }

    public void setFoodItem(FoodItem foodItem){
        this.foodItem = foodItem;
    }


    public boolean equals(Object other){
        if(((ItemToPurchase)(other)).getFoodItem().getId()== this.foodItem.getId()){
            if(((ItemToPurchase)(other)).getSemiItems().size() == this.semiItems.size()){
                for (SemiItem semiItem : this.semiItems){
                    if(!((ItemToPurchase)(other)).getSemiItems().contains(semiItem)){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean isAvailable(){
        if(this.foodItem.isAvailable()){
            for(SemiItem semiItem : this.semiItems){
                if(!semiItem.isAvailable()){
                    return false;
                }
            }
            return true;
        }
        else{
            return false;
        }
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(favId);
        dest.writeParcelable(foodItem, flags);
        dest.writeTypedList(semiItems);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemToPurchase> CREATOR = new Creator<ItemToPurchase>() {
        @Override
        public ItemToPurchase createFromParcel(Parcel in) {
            return new ItemToPurchase(in);
        }

        @Override
        public ItemToPurchase[] newArray(int size) {
            return new ItemToPurchase[size];
        }
    };


    public View getView(View convertView) {
// Populate the data into the template view using the data object

        ((TextView) convertView.findViewById(R.id.history_item)).setText(getFoodItem().getDisplayName());
        ((TextView) convertView.findViewById(R.id.item_price)).setText(Double.toString(getFoodItem().getPrice()));

        TextView item_details =  ((TextView) convertView.findViewById(R.id.details));
        if(getSemiItems().size() > 0) {
            String text = "";
            for (SemiItem semiItem : getSemiItems()){
                text += semiItem.getDisplayName() + ", ";
            }
            text = text.substring(0, text.length()-2);
            item_details.setText(text);
        }
        else{
            item_details.setVisibility(View.GONE);
        }
       return convertView;
    }
}
