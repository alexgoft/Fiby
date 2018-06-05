package com.android.fiby.comparators;

import com.android.fiby.structures.SemiItem;
import java.util.Comparator;

public class ComparatorSemi implements Comparator<SemiItem> {
    @Override
    public int compare(SemiItem semiItem, SemiItem t1) {
        return semiItem.getDisplayName().compareTo(t1.getDisplayName());
    }
}
