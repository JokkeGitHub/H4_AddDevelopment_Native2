package com.example.h4_adddevelopment_native2;

import static android.os.Environment.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
        {



            ImageView imageView;
            ImageView imageView2;
            ImageView imageView3;
    Button cameraButton;
    Button clearImagesButton;

            private float xCoOrdinate, yCoOrdinate;
            public int imageAmount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        cameraButton = findViewById(R.id.cameraBtn);
        clearImagesButton = findViewById(R.id.clearBtn);



        asynchronousGet = (Button) findViewById(R.id.asynchronousGet);
        synchronousGet = (Button) findViewById(R.id.synchronousGet);
        asynchronousPOST = (Button) findViewById(R.id.asynchronousPost);

        asynchronousGet.setOnClickListener(this);
        synchronousGet.setOnClickListener(this);
        asynchronousPOST.setOnClickListener(this);

        txtString = (TextView) findViewById(R.id.txtString);


        clearImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageView.setImageDrawable(null);
                imageView2.setImageDrawable(null);
                imageView3.setImageDrawable(null);
            }
        });


        //Request for camera runtime permission
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {
                            Manifest.permission.CAMERA
                    }, 100);
        }

        cameraButton.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);

        });


        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xCoOrdinate = view.getX() - event.getRawX();
                        yCoOrdinate = view.getY() - event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        imageView2.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    xCoOrdinate = view.getX() - event.getRawX();
                    yCoOrdinate = view.getY() - event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                    break;
                default:
                    return false;
            }
            return true;
        }
    });

        imageView3.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    xCoOrdinate = view.getX() - event.getRawX();
                    yCoOrdinate = view.getY() - event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                    break;
                default:
                    return false;
            }
            return true;
        }
    });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        Bitmap bitmap = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100)
        {
            bitmap = (Bitmap) data.getExtras().get("data");
            saveToInternalStorage(bitmap);

        }

        if (imageAmount == 1)
        {
            imageView.setImageBitmap(bitmap);
            imageAmount = 2;
        }
        else if (imageAmount == 2)
        {
            imageView2.setImageBitmap(bitmap);
            imageAmount = 3;
        }
        else if (imageAmount == 3)
        {
            imageView3.setImageBitmap(bitmap);
            imageAmount = 1;
        }
    }




            private void saveToInternalStorage(Bitmap bitmapImage){String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/Camera/";
                File myDir = new File(root);
                myDir.mkdirs();
                String fname = "Image-" + "image_name" + ".png";
                File file = new File(myDir, fname);
                System.out.println(file.getAbsolutePath());
                if (file.exists()) file.delete();
                Log.i("LOAD", root + fname);
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmapImage.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);
            }

    TextView txtString;
    Button asynchronousGet, synchronousGet, asynchronousPOST;

    public String url = "https://reqres.in/api/users/2";
    public String postUrl = "https://reqres.in/api/users/";
    public String postBody = "{\n" +
            "    \"name\": \"morpheus\",\n" +
            "    \"job\": \"leader\"\n" +
            "}";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");



    void postRequest(String postUrl, String postBody) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);

        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d("TAG", response.body().string());
            }
        });
    }


    void run() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONObject json = new JSONObject(myResponse);
                            txtString.setText("First Name: "+json.getJSONObject("data").getString("first_name") + "\nLast Name: " + json.getJSONObject("data").getString("last_name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.asynchronousGet:
                try {
                    run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.synchronousGet:
                OkHttpHandler okHttpHandler = new OkHttpHandler();
                okHttpHandler.execute(url);
                break;
            case R.id.asynchronousPost:
                try {
                    postRequest(postUrl, postBody);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    public class OkHttpHandler extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {

            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);
            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            txtString.setText(s);
        }
    }







}

