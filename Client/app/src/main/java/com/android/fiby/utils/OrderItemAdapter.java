package com.android.fiby.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.android.fiby.R;
import com.android.fiby.structures.WrapperForHistory;
import com.android.fiby.structures.ItemToPurchase;
import com.android.fiby.structures.Order;

import java.util.ArrayList;

public class OrderItemAdapter extends ArrayAdapter<WrapperForHistory> {


    private ArrayList<Order> orders;
    private ArrayList<WrapperForHistory> items;
    public OrderItemAdapter(Context context, ArrayList<WrapperForHistory> items, ArrayList<Order> orders) {
        super(context, 0, items);
        this.orders = orders;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(getItem(position).getOrder() != null) {
            // Get the data item for this position
            final WrapperForHistory item = getItem(position);
            final OrderItemAdapter adapter = this;
            // Check if an existing view is being reused, otherwise inflate the view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_item_layout, parent, false);
            final ImageView open = (ImageView) convertView.findViewById(R.id.open);
            convertView = item.getView(convertView, open);
            open.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  item.getOrder().changeOpen();
                  items.clear();
                  for(Order i : orders){
                      items.add(new WrapperForHistory(i));
                      if(i.isOpen()){
                          for(ItemToPurchase item : i.getShopList()){
                              items.add(new WrapperForHistory(item));
                          }
                      }
                  }
                  adapter.notifyDataSetChanged();
              }});
            return convertView;
        }
        else{
            // Get the data item for this position
            final WrapperForHistory item = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_detail_item, parent, false);
            return item.getView(convertView, null);
        }


//        final LinearLayout details = (LinearLayout) convertView.findViewById(R.id.drop_menu);
//        OrderDetailAdapter adapter = new OrderDetailAdapter(convertView.getContext(), item.getShopList());
//        details.setVisibility(View.VISIBLE);
//
//        ((LinearLayout) convertView.findViewById(R.id.order_item)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(details.getVisibility()==View.GONE){
//                    details.setVisibility(View.VISIBLE);
//                    details.setAlpha(1.0f);
//                    details.getLayoutParams().height = AppBarLayout.LayoutParams.MATCH_PARENT;
//                }
//                else{
//                    details.animate()
//                            .translationY(0)
//                            .alpha(0.0f)
//                            .setListener(new AnimatorListenerAdapter() {
//                                @Override
//                                public void onAnimationEnd(Animator animation) {
//                                    super.onAnimationEnd(animation);
//                                    details.setVisibility(View.GONE);
//                                }
//                            });
//                }
//            }
//        });


        // Return the completed view to render on screen
//        return convertView;
    }
}