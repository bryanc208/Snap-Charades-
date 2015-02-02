package kikhack.snapcharades;

/**
 * Created by Tanatorn on 15-01-31.
 */

       import android.content.Context;
        import android.hardware.Camera;
       import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
       import android.view.Gravity;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.SurfaceHolder;
        import android.view.SurfaceView;
        import android.view.View;
        import android.view.ViewGroup;
       import android.widget.Button;
        import android.widget.FrameLayout;
        import android.hardware.Camera.PictureCallback;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
       import java.util.ArrayList;
       import java.util.List;
        import java.util.Random;


public class CameraActivity extends ActionBarActivity {
    protected boolean released = false;
    protected int idList = 0;
    protected ArrayList<Prompt> prompts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_camera);
        setTheme(R.style.AppThemeNoActionBar);
        openCamera();
        loadPrompt();
        String prompt = pickPrompt();
        FrameLayout layout = (FrameLayout) findViewById(R.id.layout);
        mView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        RelativeLayout rLayout = new RelativeLayout(layout.getContext());
        TextView thisPrompt = new TextView(layout.getContext());
        thisPrompt.setText(prompt);
        thisPrompt.setBackgroundColor(0xFF000000);
        thisPrompt.setTextColor(0xFFFFFFFF);
        thisPrompt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
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
                String prompt = pickPrompt();
                TextView thisPrompt = (TextView) findViewById(1);
                thisPrompt.setText(prompt);
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
        layout.addView(mView);
        layout.addView(thisPrompt);
        layout.addView(rLayout);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        camera = Camera.open(0);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        camera.release();
    }

    protected void afterPhotoTaken(){
        final FrameLayout layout = (FrameLayout) findViewById(R.id.layout);
        RelativeLayout rLayout = new RelativeLayout(layout.getContext());
        rLayout.setGravity(Gravity.TOP | Gravity.END);
        rLayout.setPadding(0,75,0,0);
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
        layout.addView(rLayout);
    }
    @SuppressWarnings( "deprecation" )
    PictureCallback mPicture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }
            try {
                camera.stopPreview();
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                afterPhotoTaken();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
    };

    private File getOutputMediaFile() {

        File newFile = new File(getApplicationContext().getFilesDir(),"image.jpg");
        /*
         File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");*/

        return newFile;
    }

    @SuppressWarnings( "deprecation" )
    public Camera camera;
    public CameraView mView;
    protected class CameraView extends SurfaceView implements SurfaceHolder.Callback{
        private SurfaceHolder mHolder;
        public CameraView(Context context){
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

            for(int i = 1; i < sizeList.size(); i++){
                if((sizeList.get(i).width * sizeList.get(i).height) >
                        (bestSize.width * bestSize.height)){
                    bestSize = sizeList.get(i);
                }
            }

            mParameters.setPreviewSize(bestSize.width, bestSize.height);
            camera.setParameters(mParameters);
            camera.startPreview();

        }
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            try {
                if (released)
                    openCamera();
                camera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                camera.release();
            }
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

            camera.stopPreview();
            camera.release();
            released = true;

        }
    }

    protected void openCamera(){
        camera = Camera.open(0);
        camera.setDisplayOrientation(90);
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

    class Prompt{
        public String action;
        public int id;
        public Prompt(String action){
            this.action = action;
            this.id = idList;
            idList++;
        }
    }

    protected void loadPrompt(){
        Prompt prompt1 = new Prompt("Silly face");
        Prompt prompt2 = new Prompt("This works");
        Prompt prompt3 = new Prompt("This really works");
        prompts.add(prompt1);
        prompts.add(prompt2);
        prompts.add(prompt3);
    }
    protected String pickPrompt(){
        Random rand = new Random();
        int sizeOfArrayList = prompts.size();
        int randomInt = rand.nextInt(sizeOfArrayList);
        return prompts.get(randomInt).action;
    }
}