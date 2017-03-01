/*
 * Barebones implementation of displaying camera preview.
 *
 * Created by lisah0 on 2012-02-24
 */
package ru.taximaster.mariobit;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * A basic Camera preview class
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private PreviewCallback previewCallback;
    private AutoFocusCallback autoFocusCallback;
    private byte[] cameraBuffer;

    public CameraPreview(Context context, Camera camera,
                         PreviewCallback previewCb,
                         AutoFocusCallback autoFocusCb) {
        super(context);
        mCamera = camera;
        previewCallback = previewCb;
        autoFocusCallback = autoFocusCb;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
            }
        } catch (IOException e) {
            Log.d("DBG", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Camera preview released in activity
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mHolder.getSurface() == null) {
            return;
        }

        if (mCamera != null) {
            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
            }

            try {
                Camera.Parameters parameters = mCamera.getParameters();
                Camera.Size previewSize = parameters.getPreviewSize();
                int imageFormat = parameters.getPreviewFormat();
                int bufferSize = previewSize.width * previewSize.height * ImageFormat.getBitsPerPixel(imageFormat) / 8;
                cameraBuffer = new byte[bufferSize];

                mCamera.setDisplayOrientation(90);

                mCamera.setPreviewDisplay(mHolder);
                mCamera.setPreviewCallbackWithBuffer(null);
                mCamera.setPreviewCallbackWithBuffer(previewCallback);
                mCamera.addCallbackBuffer(cameraBuffer);
                mCamera.startPreview();
                mCamera.autoFocus(autoFocusCallback);
            } catch (Exception e) {
                Log.d("DBG", "Error starting camera preview: " + e.getMessage());
            }
        }
    }


}
