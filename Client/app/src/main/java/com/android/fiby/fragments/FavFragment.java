package com.android.fiby.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.fiby.R;
import com.android.fiby.activities.MainActivity;
import com.android.fiby.comparators.ComparatorItemToPurchase;
import com.android.fiby.net.UpdateVolley;
import com.android.fiby.structures.ItemToPurchase;
import com.android.fiby.structures.Order;
import com.android.fiby.utils.ItemListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavFragment extends ListFragment {

    private View onCreateContainer;

    public FavFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.list_fragment_layout, container, false);

        Bundle args = getArguments();
        final ArrayList<ItemToPurchase> items = args.getParcelableArrayList("items");
        boolean plus = args.getBoolean("plus");

        TextView title = (TextView) v.findViewById(R.id.list_title);
        title.setText(args.getString("title"));

        onCreateContainer = v;

        if(!plus) {
            updateTotalPrice();
        }

        LinearLayout buyCash = (LinearLayout) v.findViewById(R.id.buy_now);
        // TextView buyCash = (TextView) v.findViewById(R.id.buy_now);
        if (args.getString("title").equals("סל קניות") ) {
            buyCash.setVisibility(View.VISIBLE);
            buyCash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    TextView myMsg = new TextView(getContext());
                    myMsg.setText("האם לבצע את ההזמנה?");
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                    myMsg.setTextSize(20);
                    myMsg.setPadding(0,8,0,0);

                    builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SimpleDateFormat formatter= new SimpleDateFormat("dMMyyyy");
                            String currentDate = formatter.format(Calendar.getInstance().getTime());
                            Order newOrder = new Order(-1, items, currentDate);
                            UpdateVolley.buy(newOrder, getContext());
                            successfulOrder(newOrder, view);
                        }
                    });
                    builder.setNegativeButton("לא", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setView(myMsg);

                    final AlertDialog dialog = builder.create();
                    dialog.show(); //show() should be called before dialog.getButton().


                    Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                    layoutParams.weight = 10;
                    btnPositive.setLayoutParams(layoutParams);
                    btnNegative.setLayoutParams(layoutParams);
                }
            });
        } else {
            buyCash.setVisibility(View.GONE);
        }


        Collections.sort(items, new ComparatorItemToPurchase());

        // Create the adapter to convert the array to views
        ItemListAdapter adapter = new ItemListAdapter(getActivity().getApplicationContext(), items, plus, buyCash, this);
        setListAdapter(adapter);

        return v;
    }

    private void successfulOrder(Order newOrder, View view){

        Snackbar.make(view, "הזמנתך בוצעה בהצלחה", Snackbar.LENGTH_LONG).show();
        MainActivity.shoppingChart = new ArrayList<>();
        MainActivity.fab.setVisibility(View.GONE);
        MainActivity.history.add(newOrder);

        FragmentManager manager = getFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void updateTotalPrice(){
        final TextView totalPrice = (TextView) onCreateContainer.findViewById(R.id.text_price);
        double price = 0;
        for (ItemToPurchase itemToPurchase : MainActivity.shoppingChart){
            price += itemToPurchase.getFoodItem().getPrice();
        }
        totalPrice.setText(Double.toString(price));
    }
}
