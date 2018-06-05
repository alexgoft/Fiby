package com.android.fiby.comparators;

import com.android.fiby.structures.ItemToPurchase;
import java.util.Comparator;

public class ComparatorItemToPurchase implements Comparator<ItemToPurchase>{
    @Override
    public int compare(ItemToPurchase itemToPurchase, ItemToPurchase t1) {
        return itemToPurchase.getFoodItem().getDisplayName().compareTo(t1.getFoodItem().getDisplayName());
    }
}
