package npi.cameravideo;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends Activity {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
 
    //Nombre de directorio de almacenamiento de imágenes y vídeos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
 
    private Uri fileUri;
 
    private ImageView imgPreview;
    private VideoView videoPreview;
    private Button btnCamera, btnRecordVideo;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        videoPreview = (VideoView) findViewById(R.id.videoPreview);
        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnRecordVideo = (Button) findViewById(R.id.btnRecordVideo);
 
        /**
         * Capture image button click event
         * */
        btnCamera.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });
 
        /**
         * Record video button click event
         */
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                recordVideo();
            }
        });
        
    }
    
    /*
     * Capturing Camera Image 
     */
    private void captureImage() {
    	// Dar un nombre a la fotografía para almacenarla en la carpeta por defecto del movil
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    	
    	ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "IMG_" + timeStamp + ".jpg");
 
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
     
        //fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); // store content values
        
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
     
        // start el intent" de captura
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    
    /*
     * Recording video
     */
    private void recordVideo() {
    	
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
     
        //fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
     
        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
     
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
     
        // start el intent" de captura de vídeo
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }
    
    /**
     * Receiving activity result after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Comprobar la solicitud del usuario de guardar la fotografía
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    
    /*
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // hide video preview
            videoPreview.setVisibility(View.GONE);
 
            imgPreview.setVisibility(View.VISIBLE);
 
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();
 
            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;
 
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
 
            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
