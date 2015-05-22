package com.example.admin.cameratesting;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends ActionBarActivity {
    int CAMERA_PIC_REQUEST = 1337;
    int GALLERY_PIC_REQUEST = 9090;
    private Bitmap bitmap;
    private ImageView imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButton = (ImageView) findViewById(R.id.result);

        Button CameraButtonClick;

        CameraButtonClick =(Button) findViewById(R.id.cameraButton);
        CameraButtonClick.setOnClickListener(new OnClickListener (){
            @Override
            public void onClick(View view)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });

        Button GalleryButtonClick;

        GalleryButtonClick = (Button) findViewById(R.id.galleryButton);
        GalleryButtonClick.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, GALLERY_PIC_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent myIntent)
    {
        if( requestCode == CAMERA_PIC_REQUEST) {
            //  data.getExtras()
            //Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            /*Now you have received the bitmap..you can pass that bitmap to other activity
            and play with it in this activity or pass this bitmap to other activity
            and then upload it to server.*/
            // recyle unused bitmaps

            bitmap = (Bitmap) myIntent.getExtras().get("data");

            imageButton.setImageBitmap(bitmap);
            SaveImage(bitmap);
            super.onActivityResult(requestCode, resultCode, myIntent);
        }
        else if(requestCode == GALLERY_PIC_REQUEST && resultCode == RESULT_OK
                && myIntent != null){
            Uri selectedImage = myIntent.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            if (cursor == null || cursor.getCount() < 1) {
                return; // no cursor or no record. DO YOUR ERROR HANDLING
            }

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            if(columnIndex < 0) // no column index
                return; // DO YOUR ERROR HANDLING

            String picturePath = cursor.getString(columnIndex);

            cursor.close(); // close cursor

            Bitmap photo = (Bitmap) BitmapFactory.decodeFile(picturePath);

            List<Bitmap> bitmap = new ArrayList<Bitmap>();
            bitmap.add(photo);

            imageButton.setImageBitmap(photo);
            /*
            ImageAdapter imageAdapter = new ImageAdapter(
                    AddIncidentScreen.this, bitmap);
            imageAdapter.notifyDataSetChanged();
            newTagImage.setAdapter(imageAdapter);
*/
            super.onActivityResult(requestCode, resultCode, myIntent);
        }
    }

    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString() + "/CameraTesting";
        File myDir = new File(root);
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file.exists())
            System.out.println("it exists");
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

   /* public static Camera isCameraAvailable(View v){
        Camera object = null;
        try {
            object = Camera.open(1);
        }
        catch (Exception e){
        }
            System.out.println("Camera Exception");
            return object;
        }
*/
}
