package ru.taximaster.mariobit.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import ru.taximaster.mariobit.CameraPreview;
import ru.taximaster.mariobit.R;
import ru.taximaster.mariobit.ScanningUnit;

public class MainActivity extends CommonActivity {
    private Camera mCamera;
    private Handler autoFocusHandler;
    private FrameLayout preview;
    private ImageScanner scanner;
    private boolean previewing = true;
    private Image codeImage;

    // Mimic continuous auto-focusing
    final AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing && mCamera != null) {
                mCamera.autoFocus(autoFocusCB);
            }
        }
    };

    static {
        System.loadLibrary("iconv");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView caption = (TextView) findViewById(R.id.caption);
        caption.setTypeface(getTypefaceContent());

        TextView scan = (TextView) findViewById(R.id.scanText);
        scan.setTypeface(getTypefaceContent());

        autoFocusHandler = new Handler();

        preview = (FrameLayout) findViewById(R.id.cameraPreview);

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        initControl();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatusText(R.string.scan_process_label);
        resumeCamera();
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            //
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.cancelAutoFocus();
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    private void initControl() {
        new Handler() {
            public void handleMessage(Message msg) {
                if (preview.getWidth() > 0) {
                    ViewGroup.LayoutParams lp = preview.getLayoutParams();
                    lp.height = preview.getWidth();
                    preview.setLayoutParams(lp);
                } else {
                    this.sendEmptyMessageDelayed(0, 50);
                }
            }
        }.sendEmptyMessage(0);
    }

    private void resumeCamera() {
        mCamera = getCameraInstance();
        CameraPreview mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        preview.removeAllViews();
        preview.addView(mPreview);
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            Size size = parameters.getPreviewSize();
            codeImage = new Image(size.width, size.height, "Y800");
            previewing = true;
            mPreview.refreshDrawableState();
        }
    }

    PreviewCallback previewCb = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            codeImage.setData(data);
            int result = scanner.scanImage(codeImage);
            if (result != 0) {
                SymbolSet symbolSet = scanner.getResults();
                for (Symbol sym : symbolSet) {
                    String lastScannedCode = sym.getData();

                    if (lastScannedCode != null &&
                            ScanningUnit.isExist(MainActivity.this, lastScannedCode)) {
                        showSuccessDialog(lastScannedCode);
                    }
                }
            }
            camera.addCallbackBuffer(data);
        }
    };

    private void setStatusText(int res) {
        TextView textView = (TextView) findViewById(R.id.scanText);
        textView.setText(res);
    }

    private void showSuccessDialog(final String code) {
        SuccessActivity.show(this, code);
    }

    public static boolean show(final Context context) {
        try {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

}
