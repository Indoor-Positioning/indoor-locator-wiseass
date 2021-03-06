package com.mooo.sestus.indoor_locator.viewfloorplan;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.mooo.sestus.indoor_locator.R;
import com.mooo.sestus.indoor_locator.locate.LocateActivity;
import com.mooo.sestus.indoor_locator.scan.MagneticScanActivity;

import java.util.Collection;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewFloorPlanFragment extends Fragment implements ViewFloorPlanContract.View {
    private ViewFloorPlanContract.Presenter presenter;
    private int floorPlanId;
    private PinView floorPlanImage;
    private FloatingActionButton confirmPinFab;
    private FloatingActionButton scanPointFab;


    public ViewFloorPlanFragment() {
        // Required empty public constructor
    }

    public static ViewFloorPlanFragment newInstance(int floorPlanId) {
        ViewFloorPlanFragment floorPlanFragment = new ViewFloorPlanFragment();
        floorPlanFragment.floorPlanId = floorPlanId;
        return floorPlanFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_floor_plan, container, false);
        floorPlanImage = (PinView) v.findViewById(R.id.imageView);
        confirmPinFab = (FloatingActionButton) getActivity().findViewById(R.id.fab_confirm);
        scanPointFab = (FloatingActionButton) getActivity().findViewById(R.id.fab_scan_pin);
        Button locateButton = (Button) v.findViewById(R.id.btn_locate);
        locateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LocateActivity.class);
                intent.putExtra(LocateActivity.FLOOR_PLAN_ID, floorPlanId);
                startActivity(intent);
            }
        });

        confirmPinFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onPointConfirmed();
                confirmPinFab.setVisibility(GONE);
            }
        });
        scanPointFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.scanSelectedPoint();
            }
        });
        final GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (floorPlanImage.isReady()) {
                    presenter.onUserClickedFloorPlan(floorPlanImage.viewToSourceCoord(e.getX(), e.getY()));
                } else {
                    Toast.makeText(getContext(), "Single tap: Image not ready", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (floorPlanImage.isReady()) {
                    presenter.onUserLongClickedFloorPlan(floorPlanImage.viewToSourceCoord(e.getX(), e.getY()));
                } else {
                    Toast.makeText(getContext(), "Long tap: Image not ready", Toast.LENGTH_SHORT).show();
                }
            }
        });

        floorPlanImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        return v;
    }

    @Override
    public void setPresenter(ViewFloorPlanContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showFloorPlanImage(final String resourceName, final Collection<PointF> pinnedLocations, final Collection<PointF> floorPlanPois) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int resourceId = getContext().getResources().getIdentifier(resourceName, "drawable", getContext().getPackageName());
                floorPlanImage.setImage(ImageSource.resource(resourceId));
                floorPlanImage.setPins(pinnedLocations);
                floorPlanImage.setPois(floorPlanPois);
            }
        });
    }

    @Override
    public void showConfirmAddPinToFloorPlan(PointF pin) {
        floorPlanImage.addNewPin(pin);
        scanPointFab.setVisibility(View.GONE);
        confirmPinFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void showConfirmAddPoiToFloorPlan(PointF pin) {
        floorPlanImage.addNewPoi(pin);
        scanPointFab.setVisibility(View.GONE);
        confirmPinFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void showStartScanningActivity(int pinnedLocationId) {
        Intent intent = new Intent(getContext(), MagneticScanActivity.class);
        intent.putExtra(MagneticScanActivity.POINT_ID, pinnedLocationId);
        startActivity(intent);
    }

    @Override
    public void showPin(PointF pin) {
        floorPlanImage.setAddedPin(pin);
    }

    @Override
    public void showPoi(PointF pin) {
        floorPlanImage.setAddedPoi(pin);
    }

    @Override
    public void showSelectedPin(PointF pin) {
        floorPlanImage.setSelectedPin(pin);
        confirmPinFab.setVisibility(View.GONE);
        scanPointFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSelectedPoi(PointF pin) {
        floorPlanImage.setSelectedPoi(pin);
        confirmPinFab.setVisibility(View.GONE);
        scanPointFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void removePin(PointF pin) {
        floorPlanImage.removePin(pin);
    }

    @Override
    public void removePoi(PointF pin) {
        floorPlanImage.removePoi(pin);
    }
}
