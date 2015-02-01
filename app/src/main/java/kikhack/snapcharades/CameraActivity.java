package kikhack.snapcharades;

/**
 * Created by Tanatorn on 15-01-31.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class CameraActivity extends ActionBarActivity {
    @SuppressWarnings("deprecation")
    public Camera camera;
    public CameraView mView;
    protected boolean released = false;
    protected int idList = 0;
    protected ArrayList<Prompt> prompts = new ArrayList<>();
    protected String selectedPrompt;
    @SuppressWarnings("deprecation")
    PictureCallback mPicture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            if (data != null) {
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                int screenHeight = getResources().getDisplayMetrics().heightPixels;
                Bitmap bm = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // Notice that width and height are reversed
                    Display display = getWindowManager().getDefaultDisplay();
                    Bitmap scaled = Bitmap.createScaledBitmap(bm, display.getHeight(), display.getHeight(), true);
                    int w = scaled.getWidth();
                    int h = scaled.getHeight();
                    // Setting post rotate to 90
                    Matrix mtx = new Matrix();
                    mtx.postRotate(270);
                    // Rotating Bitmap
                    bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
                } else {// LANDSCAPE MODE
                    //No need to reverse width and height
                    Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth, screenHeight, true);
                    bm = scaled;
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, stream);

                byte[] file = stream.toByteArray();
                try {
                    camera.stopPreview();
                    File pictureFile = getOutputMediaFile();
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(file);
                    fos.close();
                    afterPhotoTaken();
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }
            }
        }
    };

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_camera);
        openCamera();
        loadPrompt();
        selectedPrompt = pickPrompt();
        FrameLayout layout = (FrameLayout) findViewById(R.id.layout);
        mView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        RelativeLayout rLayout = new RelativeLayout(layout.getContext());
        TextView thisPrompt = new TextView(layout.getContext());
        thisPrompt.setText(selectedPrompt);
        thisPrompt.setBackgroundColor(0xFF000000);
        thisPrompt.setTextColor(0xFFFFFFFF);
        thisPrompt.setGravity(Gravity.CENTER_HORIZONTAL);
        thisPrompt.setId(1);
        thisPrompt.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        thisPrompt.setTextSize(20.0F);
        rLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        final Button thisButton = new Button(layout.getContext());
        thisButton.setId(2);
        thisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, mPicture);
                thisButton.setVisibility(View.INVISIBLE);


            }
        });
        rLayout.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        thisButton.setText("Go!");
        thisButton.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        rLayout.addView(thisButton);
        mView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.addView(mView);
        layout.addView(thisPrompt);
        layout.addView(rLayout);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        camera = Camera.open(1);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        camera.release();
    }

    protected void afterPhotoTaken() {
        final FrameLayout layout = (FrameLayout) findViewById(R.id.layout);
        RelativeLayout rLayout = new RelativeLayout(layout.getContext());
        rLayout.setGravity(Gravity.TOP | Gravity.START);
        rLayout.setPadding(0, 75, 0, 0);
        RelativeLayout rLayout2 = new RelativeLayout(layout.getContext());
        rLayout2.setGravity(Gravity.BOTTOM | Gravity.CENTER);
        final Button sendBtn = new Button(rLayout2.getContext());
        sendBtn.setText("Send!");
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.startPreview();
                Intent intent = new Intent(CameraActivity.this, MainActivity.class);
                intent.setAction("PHOTO_RECIEVED");
                intent.putExtra("prompt", selectedPrompt);
                startActivity(intent);


            }
        });
        final Button cancelBtn = new Button(rLayout.getContext());
        cancelBtn.setText("Cancel");
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.startPreview();
                Button goButton = (Button) findViewById(2);
                goButton.setVisibility(View.VISIBLE);
                View parentView = (View) cancelBtn.getParent();
                layout.removeView(parentView);
            }
        });
        camera.stopPreview();
        rLayout.addView(cancelBtn);
        rLayout2.addView(sendBtn);
        layout.addView(rLayout2);
        layout.addView(rLayout);

    }

    private File getOutputMediaFile() {

        File newFile = new File(getApplicationContext().getFilesDir(), "image.jpg");
        return newFile;
    }

    protected void openCamera() {
        camera = Camera.open(1);

        /*camera.setDisplayOrientation(90);
        Camera.Parameters params = camera.getParameters();
        params.setJpegQuality(100);
        params.set("orientation", "portrait");
        params.set("rotation", );

        camera.setParameters(params);*/
        camera.startPreview();
        mView = new CameraView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void loadPrompt() {
        Prompt prompt4 = new Prompt("Hug the Opposite Gender!");
        Prompt prompt5 = new Prompt("Study Selfie!");
        Prompt prompt6 = new Prompt("Food Selfie!");
        Prompt prompt7 = new Prompt("Capture an Awesome Scenery!");
        Prompt prompt8 = new Prompt("Hug a Stranger!");
        Prompt prompt9 = new Prompt("Funny Face!");
        Prompt prompt10 = new Prompt("Take a Selfie with a Hot Guy/Girl!");
        Prompt prompt11 = new Prompt("Group Selfie!");
        Prompt prompt12 = new Prompt("Stike a Pose!");
        Prompt prompt13 = new Prompt("Wink!");
        Prompt prompt14 = new Prompt("Bathroom Selfie!");
        Prompt prompt15 = new Prompt("High-Five a Stranger!!");
        Prompt prompt16 = new Prompt("Sexy Pose!");
        Prompt prompt17 = new Prompt("Napping Selfie");
        Prompt prompt18 = new Prompt("Make a Heart!");
        Prompt prompt19 = new Prompt("School/Work Pride!");
        Prompt prompt20 = new Prompt("No Make-Up!");
        Prompt prompt21 = new Prompt("Hug the Same Gender!");
        Prompt prompt22 = new Prompt("Duck Face!");
        Prompt prompt23 = new Prompt("Act Cute!");
        Prompt prompt24 = new Prompt("Lick Something New!");
        Prompt prompt25 = new Prompt("Kiss a Stranger!");
        Prompt prompt26 = new Prompt("Show off that Outfit!");
        prompts.add(prompt4);
        prompts.add(prompt5);
        prompts.add(prompt6);
        prompts.add(prompt7);
        prompts.add(prompt8);
        prompts.add(prompt9);
        prompts.add(prompt10);
        prompts.add(prompt11);
        prompts.add(prompt12);
        prompts.add(prompt13);
        prompts.add(prompt14);
        prompts.add(prompt15);
        prompts.add(prompt16);
        prompts.add(prompt17);
        prompts.add(prompt18);
        prompts.add(prompt19);
        prompts.add(prompt20);
        prompts.add(prompt21);
        prompts.add(prompt22);
        prompts.add(prompt23);
        prompts.add(prompt24);
        prompts.add(prompt25);
        prompts.add(prompt26);

    }

    protected String pickPrompt() {
        Random rand = new Random();
        int sizeOfArrayList = prompts.size();
        int randomInt = rand.nextInt(sizeOfArrayList);
        return prompts.get(randomInt).action;
    }

    protected class CameraView extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;

        public CameraView(Context context) {
            super(context);
            mHolder = this.getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            setFocusable(true);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            Camera.Parameters mParameters = camera.getParameters();
            Camera.Size bestSize = null;
            List<Camera.Size> sizeList = camera.getParameters().getSupportedPreviewSizes();
            bestSize = sizeList.get(0);

            for (int i = 1; i < sizeList.size(); i++) {
                if ((sizeList.get(i).width * sizeList.get(i).height) >
                        (bestSize.width * bestSize.height)) {
                    bestSize = sizeList.get(i);
                }
            }

            mParameters.setPreviewSize(bestSize.width, bestSize.height);
            camera.setParameters(mParameters);
            //camera.startPreview();
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            try {
                if (released)
                    openCamera();
                setCameraDisplayOrientation(CameraActivity.this, Camera.CameraInfo.CAMERA_FACING_BACK, camera);
                camera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                camera.release();
            }
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

            camera.release();
            released = true;
        }
    }

    class Prompt {
        public String action;
        public int id;

        public Prompt(String action) {
            this.action = action;
            this.id = idList;
            idList++;
        }
    }
}