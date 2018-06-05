package com.android.fiby.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.fiby.R;
import com.android.fiby.activities.MainActivity;
import com.android.fiby.fragments.FavFragment;
import com.android.fiby.net.UpdateVolley;
import com.android.fiby.structures.ItemToPurchase;
import com.android.fiby.structures.SemiItem;
import com.plattysoft.leonids.ParticleSystem;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ItemListAdapter extends ArrayAdapter<ItemToPurchase> {

    private boolean isPlus;
    private LinearLayout buyCash;
    final private FavFragment fav;

    public ItemListAdapter(Context context, ArrayList<ItemToPurchase> items, boolean isPlus, LinearLayout buyCash, FavFragment fav) {
        super(context, 0, items);
        this.isPlus = isPlus;
        this.buyCash = buyCash;
        this.fav = fav;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        // Get the data item for this position
        final ItemToPurchase item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout, parent, false);
        }

        // Lookup view for data population
        TextView item_name = (TextView) convertView.findViewById(R.id.item_name);
        TextView item_price = (TextView) convertView.findViewById(R.id.item_price);
        TextView item_details = (TextView) convertView.findViewById(R.id.details);

        // Like
        final ImageView like = (ImageView)  convertView.findViewById(R.id.like);

        if(MainActivity.favourites.contains(item)){
            like.setImageResource(R.drawable.like);
        }
        else {
            like.setImageResource(R.drawable.like_not_pushed);
        }

        final ItemListAdapter adapter = this;

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.favourites.contains(item)){
                    like.setImageResource(R.drawable.like_not_pushed);
                    MainActivity.favourites.remove(item);
                    UpdateVolley.markAsUnliked(item, getContext());
                }
                else {
                    // Fireworks
                    ParticleSystem ps = new ParticleSystem(MainActivity.mainActivity, 50, R.drawable.star_pink, 800);
                    ps.setScaleRange(0.1f, 0.24f);
                    ps.setSpeedRange(0.1f, 0.13f);
                    ps.setRotationSpeedRange(90, 180);
                    ps.setFadeOut(200, new AccelerateInterpolator());
                    ps.oneShot(view, 70);

                    like.setImageResource(R.drawable.like);
                    MainActivity.favourites.add(item);
                    UpdateVolley.markAsLiked(item, getContext());
                }
                adapter.notifyDataSetChanged();
            }
        });


        // Add/remove to/from chart
        ImageView plus = (ImageView) convertView.findViewById(R.id.plus);
        if(isPlus) {
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.shoppingChart.add(item);
                    Snackbar.make(view, item.getFoodItem().getDisplayName() + " נוסף לסל הקניות", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    MainActivity.fab.setVisibility(View.VISIBLE);
                    MainActivity.fab.animate()
                            .alpha(1f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    MainActivity.fab.setImageAlpha(255);
                                }
                            });
                }
            });
            if (!item.isAvailable()) {
                plus.setImageAlpha(32);
                plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(view.getContext(), "מצטערים, פריט זה אינו זמין כרגע", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                plus.setImageAlpha(255);
            }
        }
        else{
            final ItemListAdapter currentItems = this;
            plus.setImageResource(R.drawable.minus);
            final View v = convertView;
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    v.animate()
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    currentItems.notifyDataSetChanged();
                                    v.setAlpha(1);
                                }
                            });
                    MainActivity.shoppingChart.remove(item);
                    fav.updateTotalPrice();
                    Snackbar.make(view, item.getFoodItem().getDisplayName() + " הוסר מסל הקניות", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    if(MainActivity.shoppingChart.size()==0){
                        buyCash.setVisibility(View.GONE);
                        MainActivity.fab.animate()
                                .alpha(0.0f)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        MainActivity.fab.setImageAlpha(255);
                                        MainActivity.fab.setVisibility(View.GONE);
                                    }
                                });

                    }
                }
            });
        }


        // Populate the data into the template view using the data object
        item_name.setText(item.getFoodItem().getDisplayName());
        item_price.setText(Double.toString(item.getFoodItem().getPrice()));
        if(item.getSemiItems().size() > 0) {
            String text = "";
            for (SemiItem semiItem : item.getSemiItems()){
                text += semiItem.getDisplayName() + ", ";
            }
            text = text.substring(0, text.length()-2);
            item_details.setText(text);
        }
        else{
            item_details.setVisibility(View.GONE);
        }



        // Return the completed view to render on screen
        return convertView;
    }
}