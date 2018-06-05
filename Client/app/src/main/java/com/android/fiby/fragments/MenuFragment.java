package com.android.fiby.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.fiby.R;
import com.android.fiby.activities.MainActivity;
import com.android.fiby.structures.FoodItem;
import com.android.fiby.structures.SemiItem;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    public MenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        ImageView softDrinks = (ImageView) v.findViewById(R.id.bt1);
        ImageView hotDrinks = (ImageView) v.findViewById(R.id.bt2);
        ImageView pastry = (ImageView) v.findViewById(R.id.bt3);
        ImageView toasts = (ImageView) v.findViewById(R.id.bt4);
        ImageView salads = (ImageView) v.findViewById(R.id.bt5);
        ImageView sandwiches = (ImageView) v.findViewById(R.id.bt6);

        softDrinks.setOnClickListener(getOCLConst("משקאות קלים"));
        hotDrinks.setOnClickListener(getOCLConst("משקאות חמים"));
        pastry.setOnClickListener(getOCLConst("מאפים"));

        toasts.setOnClickListener(getOCLWithBread("טוסט"));
        salads.setOnClickListener(getOCLWithBread("סלט"));
        sandwiches.setOnClickListener(getOCLWithBread("כריך"));

        return v;
    }

    public View.OnClickListener getOCLConst (final String category){
        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                ItemListFragment itemsList = new ItemListFragment();

                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("allTheItems", MainActivity.foodList);
                arguments.putString("category", category);
                itemsList.setArguments(arguments);

                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_container, itemsList);
                transaction.addToBackStack("list");
                transaction.commit();
            }
        };
    }

    public View.OnClickListener getOCLWithBread(final String itemName){
        return new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                ComplexFoodFragment fragment = new ComplexFoodFragment();

                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("allTheItems", MainActivity.foodList);
                arguments.putString("title", itemName);
                fragment.setArguments(arguments);

                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack("building");
                transaction.commit();
            }
        };
    }

}
