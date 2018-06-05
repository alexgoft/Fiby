package com.android.fiby.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fiby.Colors;
import com.android.fiby.R;
import com.android.fiby.activities.MainActivity;
import com.android.fiby.comparators.ComparatorSemi;
import com.android.fiby.structures.FoodItem;
import com.android.fiby.structures.ItemToPurchase;
import com.android.fiby.structures.SemiItem;
import com.android.fiby.utils.SemiItemAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ComplexFoodFragment extends Fragment {

    public ComplexFoodFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.complex_food_fragment, container, false);

        // Basic initiation
        final Button addToChart = (Button)v.findViewById(R.id.create_order);
        final ItemToPurchase itemToPurchase = new ItemToPurchase();
        Bundle args = getArguments();


        ArrayList<FoodItem> foodList = args.getParcelableArrayList("allTheItems");
        String title = args.getString("title");
        ((TextView)(v.findViewById(R.id.complex_title))).setText("הרכבת " + title);
        ArrayList<SemiItem> semiItems = new ArrayList<>();

        if(title.equals("סלט")){
            (v.findViewById(R.id.choose_bread)).setVisibility(View.GONE);
            (v.findViewById(R.id.types_of_bread)).setVisibility(View.GONE);
        }

        for(FoodItem foodItem : foodList) {
            if (foodItem.getDisplayName().equals(title)) {
                semiItems = foodItem.getOptionalAdditions();
                itemToPurchase.setFoodItem(foodItem);
                break;
            }
        }

        addToChart.setText("  הוספה לסל (" + Double.toString(itemToPurchase.getFoodItem().getPrice())+"₪)  ");

        HashMap<SemiItem, ImageButton> semiItemsWithIcons = new HashMap<>();
        final ArrayList<SemiItem> semiItemsWithoutIcons = new ArrayList<>();
        final ArrayList<SemiItem> typesOfBread = new ArrayList<>();

        for (SemiItem semiItem : semiItems){
            switch (semiItem.getDisplayName()){
                case "חציל":
                    semiItemsWithIcons.put(semiItem, (ImageButton) v.findViewById(R.id.aubergine));
                    break;
                case "אבוקדו":
                    semiItemsWithIcons.put(semiItem, (ImageButton) v.findViewById(R.id.avocado));
                    break;
                case "ברוקולי":
                    semiItemsWithIcons.put(semiItem, (ImageButton) v.findViewById(R.id.broccoli));
                    break;
                case "חסה":
                    semiItemsWithIcons.put(semiItem, (ImageButton) v.findViewById(R.id.cabbag));
                    break;
                case "גזר":
                    semiItemsWithIcons.put(semiItem, (ImageButton) v.findViewById(R.id.carrot));
                    break;
                case "גבינה צהובה":
                    semiItemsWithIcons.put(semiItem, (ImageButton) v.findViewById(R.id.cheese));
                    break;
                case "צ'ילי":
                    semiItemsWithIcons.put(semiItem, (ImageButton) v.findViewById(R.id.chili));
                    break;
                case "תירס":
                    semiItemsWithIcons.put(semiItem, (ImageButton) v.findViewById(R.id.corn));
                    break;
                case "ביצה קשה":
                    semiItemsWithIcons.put(semiItem,  (ImageButton) v.findViewById(R.id.eggs));
                    break;
                case "מלפפון":
                    semiItemsWithIcons.put(semiItem, (ImageButton) v.findViewById(R.id.cucumber));
                    break;
                case "טונה":
                    semiItemsWithIcons.put(semiItem, (ImageButton) v.findViewById(R.id.fish));
                    break;
                case "פטריות":
                    semiItemsWithIcons.put(semiItem, (ImageButton) v.findViewById(R.id.mushrooms));
                    break;
                case "בצל":
                    semiItemsWithIcons.put(semiItem, (ImageButton) v.findViewById(R.id.onion));
                    break;
                case "אפונה":
                    semiItemsWithIcons.put(semiItem, (ImageButton) v.findViewById(R.id.peas));
                    break;
                case "פלפל":
                    semiItemsWithIcons.put(semiItem, (ImageButton) v.findViewById(R.id.pepper));
                    break;
                case "עגבניה":
                    semiItemsWithIcons.put(semiItem, (ImageButton) v.findViewById(R.id.tomato));
                    break;
                case "לחם לבן":
                    typesOfBread.add(semiItem);
                    break;
                case "לחם מלא":
                    typesOfBread.add(semiItem);
                    break;
                case "לחם דגנים":
                    typesOfBread.add(semiItem);
                    break;
                default:
                    semiItemsWithoutIcons.add(semiItem);
                   break;
            }
            
        }

        // Add to the view the semi items without the icons
        Collections.sort(semiItemsWithoutIcons, new ComparatorSemi());
        final SemiItemAdapter adapterA = new SemiItemAdapter(this.getActivity(), semiItemsWithoutIcons);
        ((GridView)(v.findViewById(R.id.double_list))).setAdapter(adapterA);

        // Add to the view the types of bread
        final SemiItemAdapter adapterB = new SemiItemAdapter(this.getActivity(), typesOfBread);
        ((GridView)(v.findViewById(R.id.types_of_bread))).setAdapter(adapterB);



        // Set listeners
        ((GridView)(v.findViewById(R.id.double_list))).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!itemToPurchase.getSemiItems().contains(semiItemsWithoutIcons.get(i))) {
                    itemToPurchase.addSemiItem(semiItemsWithoutIcons.get(i));
                    ((TextView)adapterView.findViewWithTag(semiItemsWithoutIcons.get(i))).setTextColor(Color.parseColor("#97db82"));
                }
                else{
                    itemToPurchase.removeSemiItem(semiItemsWithoutIcons.get(i));
                    ((TextView)adapterView.findViewWithTag(semiItemsWithoutIcons.get(i))).setTextColor(Color.parseColor("#777777"));
                }
                checkAddToChart(itemToPurchase, addToChart, typesOfBread);
            }
        });


        ((GridView)(v.findViewById(R.id.types_of_bread))).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!itemToPurchase.getSemiItems().contains(typesOfBread.get(i))) {
                    for(int k = 0; k <3; k++){
                        if(i != k){
                            itemToPurchase.removeSemiItem(typesOfBread.get(k));
                            ((TextView)adapterView.findViewWithTag(typesOfBread.get(k))).setTextColor(Color.parseColor("#777777"));
                        }
                        else {
                            itemToPurchase.addSemiItem(typesOfBread.get(k));
                            ((TextView)adapterView.findViewWithTag(typesOfBread.get(i))).setTextColor(Color.parseColor("#97db82"));
                        }
                    }
                }
                checkAddToChart(itemToPurchase, addToChart, typesOfBread);
            }
        });


        for(final Map.Entry<SemiItem, ImageButton> entry : semiItemsWithIcons.entrySet()){
            // Mark unavailable semiItems
            entry.getValue().setAlpha(entry.getKey().isAvailable() ? 255 : 32);
            entry.getValue().setEnabled(entry.getKey().isAvailable());

            entry.getValue().setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    Drawable background = entry.getValue().getBackground();
                    if(!itemToPurchase.getSemiItems().contains(entry.getKey())) {
                        background.setColorFilter(Color.parseColor("#b0e89e"), PorterDuff.Mode.SRC);
                        itemToPurchase.addSemiItem(entry.getKey());
                    }
                    else{
                        background.setColorFilter(Color.parseColor("#d6d7d7"), PorterDuff.Mode.SRC);
                        itemToPurchase.removeSemiItem(entry.getKey());
                    }
                    entry.getValue().setBackground(background);
                    checkAddToChart(itemToPurchase, addToChart, typesOfBread);
                }
            });

            entry.getValue().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(view.getContext(), entry.getKey().getDisplayName(), Toast.LENGTH_LONG).show();
                    return false;
                }
            });

        }

        checkAddToChart(itemToPurchase, addToChart, typesOfBread);

        return v;
    }

    private void checkAddToChart(final ItemToPurchase itemToPurchase, Button addToChart,
                                 ArrayList<SemiItem> typesOfBread){
        if (itemToPurchase.getSemiItems().size() < 3) {
            disableToAddToChart(addToChart, itemToPurchase);
            return;
        }
        if (!itemToPurchase.getFoodItem().getDisplayName().equals("סלט")) {
            boolean bread = false;
            for(int k = 0; k <3; k++){
                if(itemToPurchase.getSemiItems().contains(typesOfBread.get(k))){
                    bread = true;
                }
            }
            if(!bread){
                disableToAddToChart(addToChart, itemToPurchase);
                return;
            }
        }

        // Active the button
        addToChart.setTextColor(Colors.activeTextBtn);
        addToChart.setBackgroundColor(Colors.activeBackgroundBtn);
        addToChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.shoppingChart.add(itemToPurchase);
                Snackbar.make(view, "ה" + itemToPurchase.getFoodItem().getDisplayName() + " נוסף לסל הקניות", Snackbar.LENGTH_LONG)
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

                FragmentManager manager = getFragmentManager();
                if (manager.getBackStackEntryCount() > 0) {
                    FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
                    manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        });



    }

    private void disableToAddToChart(Button addToChart, final ItemToPurchase itemToPurchase){
        addToChart.setTextColor(Colors.disactiveTextBtn);
        addToChart.setBackgroundColor(Colors.disactiveBackgroundBtn);
        addToChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemToPurchase.getSemiItems().size() < 3) {
                    Toast.makeText(getContext(), "יש לבחור 3 מרכיבים לפחות", Toast.LENGTH_LONG).show();
                }
                else {
                    if (!itemToPurchase.getFoodItem().getDisplayName().equals("סלט")) {
                        Toast.makeText(getContext(), "לא נבחר לחם", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
