package com.example.admin.cameratesting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.example.admin.camera.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;


public class MainActivity extends ActionBarActivity {
    int CAMERA_PIC_REQUEST = 1337;
    private Bitmap bitmap;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.result);

        Button ButtonClick;

        ButtonClick =(Button) findViewById(R.id.cameraButton);
        ButtonClick.setOnClickListener(new OnClickListener (){
            @Override
            public void onClick(View view)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if( requestCode == 1337) {
            //  data.getExtras()
            //Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            /*Now you have received the bitmap..you can pass that bitmap to other activity
            and play with it in this activity or pass this bitmap to other activity
            and then upload it to server.*/
            // recyle unused bitmaps

            bitmap = (Bitmap) data.getExtras().get("data");

            imageView.setImageBitmap(bitmap);
            SaveImage(bitmap);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
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
