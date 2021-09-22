package edu.aku.hassannaqvi.foodfortificationsurvey.ui;

import static edu.aku.hassannaqvi.foodfortificationsurvey.core.MainApp.PROJECT_NAME;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.aku.hassannaqvi.foodfortificationsurvey.R;
import id.zelory.compressor.Compressor;

public class TakePhoto extends Activity implements SurfaceHolder.Callback, Camera.PictureCallback {

    /**
     * FILES:
     * - TakePhoto.java
     * - activity_camera.xml
     * <p>
     * GRADLE:
     * implementation 'id.zelory:compressor:2.1.0'
     * <p>
     * REQUIRE:
     * Intent Extra:
     * - picID
     * - picView ("front" or "back")
     * - childName
     * <p>
     * RETURNS:
     * onActivityResult(resultCode) 0= Photo Cancel, 1=Photo Taken
     * if resultCode = 1 than also returns -> Intent Extra (FileName)
     * <p>
     * USAGE:
     * <p>
     * Intent intent=new Intent(MainActivity.this,TakePhoto.class);
     * intent.putExtra("picID",editText1.getText().toString());
     * intent.putExtra("picView","front".toUpperCase());
     * intent.putExtra("childName",editText2.getText().toString());
     * startActivityForResult(intent, 2);
     **/

    private static final String TAG = "Photo Capture";
    Camera mCamera;
    //Context context;
    LinearLayout btnGrp;
    String picID;
    String picView;
    String childName;
    TextView picInfo;
    ImageView cameraLense;
    ImageView cameraFrame;
    private boolean previewFlag;
    private String tmpFile = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Intent intent = getIntent();
        picID = intent.getStringExtra("picID");
        picView = intent.getStringExtra("picView");
        childName = intent.getStringExtra("personName");

        picInfo = findViewById(R.id.picInfo);
        cameraLense = findViewById(R.id.CameraLense);
        cameraFrame = findViewById(R.id.CameraFrame);
        btnGrp = findViewById(R.id.btnGrp);
        btnGrp.setVisibility(View.GONE);
        hideSystemUI();

        picInfo.setText(picView + "\r\n For: " + childName);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG + ":");
        wl.acquire();
        //


        previewFlag = false;

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

        SurfaceView surfaceView = findViewById(R.id.CameraView);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        surfaceView.setFocusable(true);
        surfaceView.setFocusableInTouchMode(true);
        surfaceView.setClickable(true);

        surfaceView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (previewFlag == false) {
                    cameraFrame.setVisibility(View.GONE);
                    cameraLense.setVisibility(View.VISIBLE);

                    mCamera.cancelAutoFocus();
                    cameraLense.setElevation(50);
                    cameraLense.setAlpha(0.8f);
                    Camera.Parameters parameters = mCamera.getParameters();
                    //parameters.setJpegQuality(88);

                    parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
                    //parameters.setFlashMode(Camera.Parameters.WHITE_BALANCE_AUTO);
                    parameters.set("rotation", 90);
                    parameters.set("iso", "auto");
                    //parameters.setPreviewSize(640, 480);
                    //parameters.setPictureSize(2448, 3264);
                    //parameters.setPictureSize(2576, 1932);
                    parameters.setPictureFormat(ImageFormat.JPEG);
                    mCamera.setParameters(parameters);
                    mCamera.autoFocus(new Camera.AutoFocusCallback() {

                        @Override
                        public void onAutoFocus(boolean b, Camera camera) {
                            previewFlag = true;
                            camera.takePicture(null, null, null, TakePhoto.this);
                            btnGrp.setVisibility(View.VISIBLE);
                            cameraLense.setVisibility(View.GONE);

                        }
                    });

                } else {
                    /*camera.startPreview();
                    previewFlag = false;*/
                }
                return false;
            }


        });



      /*  surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previewFlag == false) {
                    mCamera.cancelAutoFocus();
                    Camera.Parameters parameters = mCamera.getParameters();
                    //parameters.setJpegQuality(88);

                    parameters.setAutoWhiteBalanceLock(true);
                    //parameters.setFlashMode(Camera.Parameters.WHITE_BALANCE_AUTO);
                    parameters.set("rotation", 90);
                    parameters.set("iso", "auto");
                    //parameters.setPreviewSize(640, 480);
                    //parameters.setPictureSize(640, 480);
                    //parameters.setPictureSize(2576, 1932);
                    parameters.setPictureFormat(ImageFormat.JPEG);



                    mCamera.setParameters(parameters);
                    mCamera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean b, Camera camera) {
                            previewFlag = true;
                            camera.takePicture(null, null, null, TakePhoto.this);
                            btnGrp.setVisibility(View.VISIBLE);
                        }
                    });

                } else {
                    *//*camera.startPreview();
                    previewFlag = false;*//*
                }
            }
        });*/

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (previewFlag == false) {
            int newOrientation = newConfig.orientation;
            Toast.makeText(this, String.valueOf(newOrientation), Toast.LENGTH_SHORT).show();

            switch (newOrientation) {
                case Configuration.ORIENTATION_PORTRAIT:
                    mCamera.setDisplayOrientation(90);
                    Log.v("TakePhoto", "Orientation = 90");
                    break;
                case Configuration.ORIENTATION_LANDSCAPE:
                    mCamera.setDisplayOrientation(180);
                    Log.v("TakePhoto", "Orientation = 180");
                    break;
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        mCamera = Camera.open();
        Camera.Parameters parameters = mCamera.getParameters();
        List<String> focusModes = parameters.getSupportedFocusModes();
        for (String p : focusModes) {
            Log.d("focusModes", "surfaceCreated: " + p + "\r\n");
        }

        List<String> whiteBalance = parameters.getSupportedWhiteBalance();
        for (String p : whiteBalance) {
            Log.d("whiteBalance", "surfaceCreated: " + p + "\r\n");
        }

        List<Integer> picFormat = parameters.getSupportedPictureFormats();
        for (Integer p : picFormat) {
            Log.d("picFormat", "surfaceCreated: " + p + "\r\n");
        }
//
        List<String> colorEffects = parameters.getSupportedColorEffects();
        for (String effect : colorEffects) {
            Log.d("colorEffects", effect);
        }
        //parameters.setColorEffect(Camera.Parameters.WHITE_BALANCE_AUTO);
        //parameters.setPreviewSize(640, 480);
        //parameters.setPictureSize(640, 480);
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        int orientation = this.getResources().getConfiguration().orientation;
        Toast.makeText(this, String.valueOf(orientation), Toast.LENGTH_SHORT).show();

        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                mCamera.setDisplayOrientation(90);
                Log.v("TakePhoto", "Orientation = 90");
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                mCamera.setDisplayOrientation(180);
                Log.v("TakePhoto", "Orientation = 0");
                break;
        }
        //camera.setDisplayOrientation(180);

        List<Camera.Size> previewSize = parameters.getSupportedPreviewSizes();
        List<Camera.Size> picSize = parameters.getSupportedPictureSizes();
        if (previewSize.size() > 1) {
            for (Camera.Size size : previewSize) {
                Log.d("TAG", size.height + " * " + size.width);
            }
        }
        if (picSize.size() > 1) {
            for (Camera.Size size : picSize) {
                Log.d("TAG", size.height + " * " + size.width);
            }
        }
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (Exception e) {
            mCamera.release();
        }
        mCamera.setParameters(parameters);

        mCamera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.d(TAG, "onPictureTaken: Start");
        File pictureFileDir = getDir(0);
        Log.d(TAG, "onPictureTaken: Directory Created");
        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

            Log.d(TAG, "Can't create directory to save image.");
            Toast.makeText(this, "Can't create directory to save image.",
                    Toast.LENGTH_LONG).show();
            return;

        } else {
            Log.d(TAG, "Directory created" + pictureFileDir);

        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_mm_dd_hh_mm_ss");
        String date = dateFormat.format(new Date());

        //TODO: PhotoID will be sent from calling Activity as StringExtra(). Replace "Cipture"
        String photoFile = picID + "_" + date + "_" + picView + ".jpg";

        String filename = pictureFileDir.getPath() + File.separator + photoFile;

        File pictureFile = new File(filename);

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            tmpFile = photoFile;
            Toast.makeText(this, "New Image saved:" + photoFile,
                    Toast.LENGTH_LONG).show();
        } catch (Exception error) {
            Log.d(TAG, "File" + filename + "not saved: "
                    + error.getMessage());
            Toast.makeText(this, "Image could not be saved.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void dropPhoto(View view) {
        if (tmpFile != null) {
            btnGrp.setVisibility(View.GONE);
            deleteTempFile(tmpFile);
            tmpFile = null;
            Intent intent = new Intent();
            intent.putExtra("FileName", "");
            intent.putExtra("FileType", picView);
            setResult(RESULT_CANCELED, intent);
            finish();//finishing activity
        }
    }

    public void savePhoto(View view) {
        if (tmpFile != null) {
            btnGrp.setVisibility(View.GONE);
            //moveFile(tmpFile);
            //compressAndMove(tmpFile);

            //camera.startPreview();
            String fileName = compressAndMove(tmpFile);
            tmpFile = null;
            Intent intent = new Intent();
            intent.putExtra("FileName", fileName);
            intent.putExtra("FileType", picView);
            setResult(RESULT_OK, intent);
            finish();//finishing activity
            //previewFlag = false;
        }
    }

/*    private File getDir(int i) {
        String appFolder = PROJECT_NAME;

        if (i == 1) {
            File sdDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            return new File(sdDir, appFolder);
        } else {
            File sdDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            return new File(sdDir, appFolder + File.separator + "temp");
        }
    }*/

    private File getDir(int i) {
        String albumName = PROJECT_NAME;

        if (i == 1) {
            /*File sdDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);*/
            File sdDir = new File(this.getExternalFilesDir(
                    Environment.DIRECTORY_PICTURES), albumName);
            return sdDir;
        } else {
            /*File sdDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);*/
            File sdDir = new File(this.getExternalFilesDir(
                    Environment.DIRECTORY_PICTURES), albumName + File.separator + "temp");
            return sdDir;
        }
    }

    private void deleteTempFile(String inputFile) {

        File inputPath = getDir(0);

        new File(inputPath + File.separator + inputFile).delete();
        Toast.makeText(this, "Cancelled Photo...", Toast.LENGTH_LONG).show();

    }

    private String compressAndMove(String inputFile) {
        File inputPath = getDir(0);
        File outputPath = getDir(1);
        File actualImage = new File(inputPath + File.separator + inputFile);
        /*try {
            *//*File compressedImgFile = new Compressor(this)
                    .setDestinationDirectoryPath(outputPath + File.separator)
//                    .setMaxWidth(2576)
//                    .setMaxHeight(1932)
//
                    *//**//*      .setMaxWidth(640)
                          .setMaxHeight(480)*//**//*
                    .setQuality(88)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToFile(actualImage);*//*

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("tag", e.getMessage());

        }*/
        // delete the original file
        new File(inputPath + File.separator + inputFile).delete();
        Toast.makeText(this, "Photo Saved in " + outputPath + File.separator + inputFile, Toast.LENGTH_SHORT).show();
        return inputFile;
    }

    /*
        private void moveFile(String inputFile) {
            Toast.makeText(this, "Saving Photo...", Toast.LENGTH_LONG).show();

            InputStream in = null;
            OutputStream out = null;
            File inputPath = getDir(0);
            File outputPath = getDir(1);
            try {

                //create output directory if it doesn't exist (not needed, just a precaution)
                //File dir = getDir(1);
                if (!outputPath.exists()) {
                    outputPath.mkdirs();
                }

                in = new FileInputStream(inputPath + File.separator + inputFile);
                File actualImage = new File(inputPath + File.separator + inputFile);
                File compressedImgFile = new Compressor(this).compressToFile(actualImage);
                out = new FileOutputStream(outputPath + File.separator + inputFile);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;

                // write the output file
                out.flush();
                out.close();
                out = null;

                // delete the original file
                new File(inputPath + File.separator + inputFile).delete();
                Toast.makeText(this, "Photo Saved in " + outputPath + File.separator + inputFile, Toast.LENGTH_SHORT).show();

            } catch (FileNotFoundException fnfe1) {
                Log.e("tag", fnfe1.getMessage());
            } catch (Exception e) {
                Log.e("tag", e.getMessage());
            }

        }
    */
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onBackPressed() {
        if (tmpFile != null) {
            deleteTempFile(tmpFile);
            tmpFile = null;
        }
        Intent intent = new Intent();
        intent.putExtra("FileName", "");
        setResult(0, intent);
        finish();//finishing activity
    }

}