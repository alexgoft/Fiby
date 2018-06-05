package com.android.fiby.comparators;

import com.android.fiby.structures.FoodItem;

import java.util.Comparator;

public class ComparatorFoodItem implements Comparator<FoodItem> {
    @Override
    public int compare(FoodItem foodItem, FoodItem t1) {
        return foodItem.getDisplayName().compareTo(t1.getDisplayName());
    }
}
