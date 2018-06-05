package com.android.fiby.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.android.fiby.R;
import com.android.fiby.fragments.FavFragment;
import com.android.fiby.fragments.MenuFragment;
import com.android.fiby.fragments.OrdersFragment;
import com.android.fiby.net.LoadVolley;
import com.android.fiby.structures.FoodItem;
import com.android.fiby.structures.ItemToPurchase;
import com.android.fiby.structures.Order;
import com.android.fiby.structures.SemiItem;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Activity mainActivity;
    public static ArrayList<FoodItem> foodList;
    public static ArrayList<ItemToPurchase> favourites;
    public static ArrayList<Order> history = new ArrayList<>();
    private LoadVolley loadVolley;
    public static int userId;
    public static ArrayList<ItemToPurchase> shoppingChart = new ArrayList<>();
    public static FloatingActionButton fab;

    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.loading_layout);

        mainActivity = this;

        userId = getIntent().getIntExtra("USER_ID", -1);
        loadData();
    }

    private void afterLoading(){
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        if(shoppingChart.size()==0){
            fab.setVisibility(View.GONE);
        }

        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e35478")));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavFragment favActivity = new FavFragment();

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("items", shoppingChart);
                bundle.putBoolean("plus", false);
                bundle.putString("title", "סל קניות");
                favActivity.setArguments(bundle);

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_container, favActivity);
                transaction.addToBackStack("favActivity");
                transaction.commit();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.nav_user_name)).setText(getIntent().getStringExtra("USER_FIRST_NAME")+" "+getIntent().getStringExtra("USER_LAST_NAME"));

        // Change to the menu
        FragmentManager manager = getSupportFragmentManager();
        MenuFragment menu = new MenuFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, menu);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager manager = getSupportFragmentManager();

        if (id == R.id.nav_order) {
            MenuFragment menu = new MenuFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("items", foodList);
            menu.setArguments(bundle);

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_container, menu);
            transaction.addToBackStack("menu");
            transaction.commit();

        } else if (id == R.id.nav_fav) {
            FavFragment favActivity = new FavFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("items", favourites);
            bundle.putBoolean("plus", true);
            bundle.putString("title", "מועדפים");
            favActivity.setArguments(bundle);

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_container, favActivity);
            transaction.addToBackStack("favActivity");
            transaction.commit();

        } else if (id == R.id.nav_logout) {
            SharedPreferences settings = this.getSharedPreferences("com.android.fiby", Context.MODE_PRIVATE);
            settings.edit().clear().apply();

            Intent signInIntent = new Intent(this, LoginActivity.class);
            signInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            favourites = new ArrayList<>();
            history = new ArrayList<>();

            this.startActivity(signInIntent);
            this.finish();
        } else if (id == R.id.nav_history){
            OrdersFragment orders = new OrdersFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("items", history);
            orders.setArguments(bundle);

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_container, orders);
            transaction.addToBackStack("orders");
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void loadData(){
        loadVolley = new LoadVolley(userId, MainActivity.this, this);

        loadVolley.getItems();
    }

    ////////////////////////////////////////////////////////////
    //////////////////         PARSER         //////////////////
    ////////////////////////////////////////////////////////////

    public void setFoodList(JSONArray foodJASON) {
        this.foodList = new ArrayList<>();
        for(int i = 0; i < foodJASON.length(); ++i){
            try{
                this.foodList.add(new FoodItem(foodJASON.getJSONObject(i).getInt("item_id"),
                        foodJASON.getJSONObject(i).getString("display_name"),
                        foodJASON.getJSONObject(i).getString("image"),
                        foodJASON.getJSONObject(i).getDouble("price"),
                        foodJASON.getJSONObject(i).getString("category"),
                        foodJASON.getJSONObject(i).getBoolean("available")));
            }
            catch (JSONException e) {
                Toast.makeText(MainActivity.this, "אירעה שגיאה בשרת", Toast.LENGTH_SHORT).show();
            }
        }
        loadVolley.getSemiItems();
    }

    public void addSemiItems(JSONArray semiItems){
        for(int i = 0; i < semiItems.length(); ++i) {
            try{
                SemiItem currentSemiItem = new SemiItem(semiItems.getJSONObject(i).getString("display_name"),
                        semiItems.getJSONObject(i).getInt("semi_id"),
                        semiItems.getJSONObject(i).getBoolean("available"));
                int foodItemId = semiItems.getJSONObject(i).getInt("item_id");
                for(FoodItem item : foodList){
                    if(item.getId() == foodItemId){
                        item.addOptionalAddition(currentSemiItem);
                        break;
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        loadVolley.getFavourites();
    }

    public void setFavourites(JSONArray favouritesJSON) {

        favourites = new ArrayList<>();
        if(favouritesJSON.length() == 0) {
            return;
        }

        try {
            mainLoop:
            for (int i = 0; i < favouritesJSON.length(); ++i) {
                int favouriteId = favouritesJSON.getJSONObject(i).getInt("favourite_id");
                // Check if the item already in the favourites
                for (ItemToPurchase item : favourites) {
                    if (item.getFavId() == favouriteId) {
                        int semiId = favouritesJSON.getJSONObject(i).optInt("semi_id", -1);
                        for (SemiItem semi : item.getFoodItem().getOptionalAdditions()) {
                            if (semi.getId() == semiId) {
                                item.addSemiItem(semi);
                                continue mainLoop;
                            }
                        }
                        Toast.makeText(this, "אירעה שגיאה בשרת", Toast.LENGTH_SHORT).show();    // Shouldn't run this line!
                    }
                }

                // Add a new item to favourites
                int foodItemId = favouritesJSON.getJSONObject(i).getInt("item_id");
                for (FoodItem foodItem : this.foodList) {
                    if (foodItem.getId() == foodItemId) {
                        ItemToPurchase item = new ItemToPurchase(foodItem, favouriteId);
                        favourites.add(item);
                        int semiId = favouritesJSON.getJSONObject(i).optInt("semi_id", -1);
                        for (SemiItem semi : item.getFoodItem().getOptionalAdditions()) {
                            if (semi.getId() == semiId) {
                                item.addSemiItem(semi);
                                continue mainLoop;
                            }
                        }
                    }
                }

            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        loadVolley.getHistory();
    }

    public void setHistory(JSONArray historyJSON) {
        this.history = new ArrayList<>();
        if(historyJSON.length() == 0){
            return;
        }

        try{
            mainLoop:
            for (int i = 0; i < historyJSON.length(); ++i) {
                int order_id = historyJSON.getJSONObject(i).getInt("order_id");
                Order currentOrder = null;
                // Check if the order already in the history
                orderLoop:
                for (Order item : this.history) {
                    if (item.getId() == order_id) {
                        currentOrder = item;
                        break orderLoop;
                    }
                }
                if(currentOrder == null) {
                    // Create a new order
                    currentOrder = new Order(order_id, new ArrayList<ItemToPurchase>(), historyJSON.getJSONObject(i).getString("date"));
                    history.add(currentOrder);
                }


                ArrayList<ItemToPurchase> currentItemsToPurchases = currentOrder.getShopList();

                // Find the relevant order item
                int orderItemId = historyJSON.getJSONObject(i).getInt("order_item");
                ItemToPurchase currentItemToPurchase = null;
                for (ItemToPurchase itemToPurchase : currentItemsToPurchases) {
                    if(itemToPurchase.getOrderId() == orderItemId) {
                        currentItemToPurchase = itemToPurchase;
                        break;
                    }
                }
                if(currentItemToPurchase == null) {
                    // Create a new order item
                    int currentFoodItemId = historyJSON.getJSONObject(i).getInt("item_id");
                    for (FoodItem foodItem : foodList) {
                        if(foodItem.getId() == currentFoodItemId) {
                            currentItemToPurchase = new ItemToPurchase(foodItem);
                            currentItemToPurchase.setOrderId(orderItemId);
                            currentItemsToPurchases.add(currentItemToPurchase);
                            break;
                        }
                    }
                }

                // Add the semi item
                int semiItemId = historyJSON.getJSONObject(i).optInt("semi_item_id");
                if(semiItemId != 0) {   // null = 0
                    for(SemiItem semiItem : currentItemToPurchase.getFoodItem().getOptionalAdditions()) {
                        if (semiItem.getId() == semiItemId) {
                            currentItemToPurchase.addSemiItem(semiItem);
                            break;
                        }
                    }
                }

            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        afterLoading();
    }
}
