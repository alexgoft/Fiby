package com.android.fiby.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.fiby.R;
import com.android.fiby.structures.WrapperForHistory;
import com.android.fiby.structures.ItemToPurchase;
import com.android.fiby.structures.Order;
import com.android.fiby.utils.OrderItemAdapter;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends ListFragment {

    private ArrayList<Order> orders;
    private ArrayList<WrapperForHistory> a;
    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.list_fragment_layout, container, false);

        Bundle args = getArguments();
        orders = args.getParcelableArrayList("items");

        // Remove the payment options
        LinearLayout buyCash = (LinearLayout) v.findViewById(R.id.buy_now);
        buyCash.setVisibility(View.GONE);
        
        TextView title = (TextView) v.findViewById(R.id.list_title);
        title.setText("היסטורית הזמנות");

        a = new ArrayList<>();
        for(Order i : orders){
            a.add(new WrapperForHistory(i));
            if(i.isOpen()){
                for(ItemToPurchase item : i.getShopList()){
                    a.add(new WrapperForHistory(item));
                }
            }
        }

        // Create the adapter to convert the array to views
        final OrderItemAdapter adapter = new OrderItemAdapter(getActivity().getApplicationContext(), a, orders);
        setListAdapter(adapter);
        return v;
    }
}
