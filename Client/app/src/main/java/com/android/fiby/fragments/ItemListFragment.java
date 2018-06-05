package com.android.fiby.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fiby.activities.MainActivity;
import com.android.fiby.comparators.ComparatorItemToPurchase;
import com.android.fiby.structures.ItemToPurchase;
import com.android.fiby.utils.ItemListAdapter;
import com.android.fiby.R;
import com.android.fiby.structures.FoodItem;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemListFragment extends ListFragment {

    private ArrayList<ItemToPurchase> array_of_items = new ArrayList<>();
    private ArrayList<FoodItem> foodList = new ArrayList<>();
    private String category;

    public ItemListFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        foodList = args.getParcelableArrayList("allTheItems");
        category = args.getString("category");

        // Create the adapter to convert the array to views
        ItemListAdapter adapter = new ItemListAdapter(getActivity().getApplicationContext(), array_of_items, true, null, null);

        for (FoodItem item: foodList) {

            if (item.getCategory().equals(category) && item.isAvailable()) {
                ItemToPurchase newItem = new ItemToPurchase(item);
                for(ItemToPurchase favItem : MainActivity.favourites){
                    if(item == favItem.getFoodItem()){
                        newItem.setFavId(favItem.getFavId());
                    }
                }
                adapter.add(newItem);
            }
        }

        Collections.sort(array_of_items, new ComparatorItemToPurchase());

        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.list_fragment_layout, container, false);

        TextView title = (TextView) v.findViewById(R.id.list_title);
        title.setText(category);

        // Remove the payment options
//        TextView buyCash = (TextView) v.findViewById(R.id.buy_now);
        LinearLayout buyCash = (LinearLayout) v.findViewById(R.id.buy_now);
        buyCash.setVisibility(View.GONE);

        return v;
    }
}
