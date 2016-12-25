package com.technovanzahackathon.tripistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectedListener;
import com.technovanzahackathon.tripistant.Expense.ExpenseActivity;
import com.technovanzahackathon.tripistant.TripChecklists.ChecklistFragment;
import com.technovanzahackathon.tripistant.TripChecklists.Trips;

/**
 * Created by Pooja S on 12/24/2016.
 */

public class TripActivity extends AppCompatActivity {

    BottomBar bottomBar;
    CoordinatorLayout coordinatorLayout;
    Trips note;
    private static final String EXTRA_NOTE = "EXTRA_CHECK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_activity);

/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_trip);
        setSupportActionBar(toolbar);
*/

        note = (Trips) getIntent().getSerializableExtra(EXTRA_NOTE);
//        toolbar.setTitle(note.getTripname());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatelayout);

        final Bundle args = new Bundle();
        args.putString("someInt", note.getTripdate());
        args.putString("someInt1", note.getTriptime());

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(args);
        switchFragment(homeFragment);

        bottomBar = BottomBar.attachShy(coordinatorLayout, findViewById(R.id.fragment_holder), savedInstanceState);
        bottomBar.noTabletGoodness();
        bottomBar.setItems(
                new BottomBarTab(R.drawable.home, "Assistant"),
                new BottomBarTab(R.drawable.currency_usd, "Expense"),
                new BottomBarTab(R.drawable.check, "Checklist")
        );

        bottomBar.setOnItemSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                switch (position) {
                    case 0:
                        HomeFragment homeFragment = new HomeFragment();
                        homeFragment.setArguments(args);
                        switchFragment(homeFragment);
                        break;

                    case 1:
                        Intent intente = new Intent(getApplicationContext(), ExpenseActivity.class);
                        startActivity(intente);
                        //switchFragment(new ExpenseFragment());
                        break;

                    case 2:
                        Intent intent = new Intent(getApplicationContext(), ChecklistFragment.class);
                        intent.putExtra("NEW_TRIP", note);
                        startActivity(intent);
                        //switchFragment(new ChecklistFragment());
                        break;
                }
            }
        });

    }

    public void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_holder, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return true;
    }

}
