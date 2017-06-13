package com.mooo.sestus.indoor_locator.locate;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.mooo.sestus.indoor_locator.Injection;
import com.mooo.sestus.indoor_locator.R;

public class LocateActivity extends AppCompatActivity {

    public static final String FLOOR_PLAN_ID = "FP_ID";
    public static final String POINT_ID = "POINT_ID";
    public static final String LOCATE_FRAGMENT = "LOCATE_FRAGMENT";
    private LocateContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnetic_scan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String floorPlanId = getIntent().getStringExtra(FLOOR_PLAN_ID);
        FragmentManager fragManager = this.getSupportFragmentManager();
        LocateFragment fragment = (LocateFragment) fragManager.findFragmentByTag(LOCATE_FRAGMENT);

        if (fragment == null) {
            fragment = LocateFragment.newInstance(floorPlanId);
        }

        fragManager.beginTransaction()
                .replace(R.id.cont_locate, fragment, LOCATE_FRAGMENT)
                .commit();
        presenter = new LocatePresenter(Injection.provideFloorPlanRepository(this), Injection.provideSensorRepository(this), fragment, floorPlanId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}