package com.mooo.sestus.indoor_locator.locate;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.mooo.sestus.indoor_locator.BasePresenter;
import com.mooo.sestus.indoor_locator.BaseView;

public interface LocateContract {

    interface View extends BaseView<Presenter> {
        void updateAzimuth(float azimuth);

        void updatePitch(float pitch);

        void updateRoll(float roll);

        void updateMagneticFieldX(float value);

        void updateMagneticFieldY(float value);

        void updateMagneticFieldZ(float value);

        void updateMagneticAccuracy(int accuracy);

        void updateRotationVectorAccuracy(int accuracy);

        void showLocatedPointId(int pointId, PointF point);

        void showFloorPlanImage(Bitmap image);
    }

    interface Presenter extends BasePresenter {
        void startRecording();

        void stopRecording();
    }
}
