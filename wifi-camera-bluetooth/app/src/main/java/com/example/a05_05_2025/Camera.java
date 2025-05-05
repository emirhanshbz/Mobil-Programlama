package com.example.a05_05_2025;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Camera extends AppCompatActivity {

    private ActivityResultLauncher<Intent> takePictureLauncher;
    private ActivityResultLauncher<Intent> recordVideoLauncher;

    private Button b5, b6;
    private ImageView imageView;
    private VideoView videoView;

    private Uri mediaUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        b5 = findViewById(R.id.button5);
        b6 = findViewById(R.id.button6);
        imageView = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);


        setupResultLaunchers();


        b5.setOnClickListener(v -> captureImage());
        b6.setOnClickListener(v -> captureVideo());
    }

    private void setupResultLaunchers() {
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (mediaUri != null) {
                            imageView.setImageURI(mediaUri);
                            imageView.setVisibility(View.VISIBLE);
                            videoView.setVisibility(View.GONE);
                        } else {
                            Intent data = result.getData();
                            if (data != null && data.getExtras() != null) {
                                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                                imageView.setImageBitmap(imageBitmap);
                                imageView.setVisibility(View.VISIBLE);
                                videoView.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(this, "Resim alınamadı.", Toast.LENGTH_SHORT).show();
                            }
                        }


                    } else {
                        Toast.makeText(this, "Resim çekme iptal edildi.", Toast.LENGTH_SHORT).show();
                    }
                });

        recordVideoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Uri videoUri = mediaUri;

                        if (videoUri != null) {
                            videoView.setVideoURI(videoUri);
                            MediaController mediaController = new MediaController(this);
                            videoView.setMediaController(mediaController);
                            mediaController.setAnchorView(videoView);
                            videoView.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.GONE);
                            videoView.requestFocus();
                            videoView.start();
                        } else {
                            Toast.makeText(this, "Video alınamadı.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Video kaydetme iptal edildi.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Resim dosyası oluşturulamadı.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (photoFile != null) {
                mediaUri = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName() + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
                takePictureLauncher.launch(takePictureIntent);
            }
        } else {
            Toast.makeText(this, "Cihazda kamera uygulaması bulunamadı.", Toast.LENGTH_SHORT).show();
        }
    }


    private void captureVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            File videoFile = null;
            try {
                videoFile = createVideoFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Video dosyası oluşturulamadı.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (videoFile != null) {
                mediaUri = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName() + ".provider",
                        videoFile);
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
                recordVideoLauncher.launch(takeVideoIntent);
            }
        } else {
            Toast.makeText(this, "Cihazda video kayıt uygulaması bulunamadı.", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }



    private File createVideoFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String videoFileName = "MP4_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File video = File.createTempFile(
                videoFileName,
                ".mp4",
                storageDir
        );
        return video;
    }
}
