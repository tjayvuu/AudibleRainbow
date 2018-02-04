package com.example.acer.savehinhve;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class MainActivity extends AppCompatActivity implements OnInitListener {

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Uri fileUri; // file url to store image/video

    private ImageView imgPreview;
    private Button btnCapturePicture;
    private Bitmap bitmap_2;
    private String imageString;
    private String colorDescription;


    //TTS object
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MediaPlayer mp = MediaPlayer.create(this,R.raw.tapbottom);
        mp.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        btnCapturePicture = (Button) findViewById(R.id.button);

        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView theTextviewV1 = (TextView)findViewById(R.id.textView2);
                TextView theTextviewV2 = (TextView)findViewById(R.id.textView3);
                TextView theTextviewV3 = (TextView)findViewById(R.id.textView5);
                TextView theTextviewV4 = (TextView)findViewById(R.id.textView6);
                theTextviewV1.setText(" ");
                theTextviewV2.setText(" ");
                theTextviewV3.setText(" ");
                theTextviewV4.setText(" ");
                btnCapturePicture.setBackgroundColor(Color.TRANSPARENT);
                btnCapturePicture.setText(" ");
                // capture picture
                captureImage();
                MediaPlayer ma = MediaPlayer.create(getApplicationContext(),R.raw.taptap);
                ma.start();




            }
        });
        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);


    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                myTTS = new TextToSpeech(this, this );
            }
            else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();

                // convert image bitmapo to base64 string

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = bitmap_2;
                Log.d("TAG", ""+ bitmap);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                // process image analysis using APIs
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        // All your networking logic
                        // should be here
                        // Create URL

                        // call ColorNameAPI
                        try {
                            Log.d("myTag00", "This is my message");
                            String rgb = getImageAnalysis();
                            String colorName = getName(rgb);
                            String toSpeak = fullSpeech(colorName, colorDescription);
                            speakWords(toSpeak);
                            setText(colorName);
//                            String bra = "0";
//                            String zero= "0";
//                            bra = colorName;
//                            TextView theTextviewV23 = (TextView)findViewById(R.id.textViewTitle);
//                            theTextviewV23.setText(colorName);
//                            theTextviewV23.postInvalidate();
//                            TextView theTextviewv2323 = (TextView)findViewById(R.id.textView4);
//                            theTextviewv2323.setText("cat");
                            Log.d("myTag01", "This is my message");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    private void previewCapturedImage() {
        try {
            // hide video preview

            imgPreview.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
            bitmap_2 = bitmap;

            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    /*
     * Here we restore the fileUri again
     */


    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name

        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    // yellow
    public String getImageAnalysis() throws Exception {
        final String TARGET_URL =
                "https://vision.googleapis.com/v1/images:annotate?";
        final String API_KEY =
                "key=***REMOVED***";

        URL serverUrl = new URL(TARGET_URL + API_KEY);
//		URLConnection urlConnection = serverUrl.openConnection();

        HttpURLConnection httpConnection = (HttpURLConnection) serverUrl.openConnection();
        System.setProperty("http.agent", "Mozilla/5.0");

//		HttpURLConnection httpConnection = (HttpURLConnection)urlConnection;

        httpConnection.setRequestMethod("POST");
        httpConnection.setRequestProperty("Content-Type", "application/json");
        httpConnection.setDoOutput(true);
        BufferedWriter httpRequestBodyWriter = new BufferedWriter(new
                OutputStreamWriter(httpConnection.getOutputStream()));
        Log.d("imageString", imageString);
        String toWrite = "{\"requests\":  [{ \"features\":  [ {\"type\": \"IMAGE_PROPERTIES\""
                +"}], \"image\": {\"content\":\"" + imageString +
                "\"}}]}";
        Log.d("toWrite", toWrite);
        httpRequestBodyWriter.write
                (toWrite);
        httpRequestBodyWriter.close();
        String resp = httpConnection.getResponseMessage();

        if (httpConnection.getInputStream() == null) {
            System.out.println("No stream");
            return "";
        }

        Scanner httpResponseScanner = new Scanner(httpConnection.getInputStream());
        String resp1 = "";
        while (httpResponseScanner.hasNext()) {
            String line = httpResponseScanner.nextLine();
            resp1 += line;
//			   System.out.println(line);  //  alternatively, print the line of response
        }
        httpResponseScanner.close();
        // parse json
        JSONObject obj = new JSONObject(resp1);


        JSONObject res = obj.getJSONArray("responses").getJSONObject(0).getJSONObject("imagePropertiesAnnotation")
                .getJSONObject("dominantColors").getJSONArray("colors").getJSONObject(0).getJSONObject("color")
                ;
        int r = 0;
        int g = 0;
        int b = 0;
        r = res.getInt("red");
        g = res.getInt("green");
        b = res.getInt("blue");
        int rgb_int = ((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
        colorDescription = BasicColors.getClosestColorDescription(rgb_int);
        String rgb = "rgb" + "(" + r + "," + g + "," + b + ")";
        Log.d("myTagYellow", rgb);
        return rgb;
//        System.out.println(rgb);
    }

    // Use ColorNameAPI to get color nam from RGB code
    public static String getName(String rgb) throws Exception {

        String url = "http://thecolorapi.com/id?rgb=" + rgb;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
//		con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        Log.d("myTagCN11", "" + responseCode);
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
        String str = response.toString();
        JSONObject j_obj = new JSONObject(str);


        JSONObject res = j_obj.getJSONObject("name");
        String colorName = res.getString("value");
        Log.d("myTag99", colorName);
        return colorName;


    }

    private void speakWords(String speech) {

        //speak straight away
        Log.d("MyTag100", speech);
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    // full text to speech
    private String fullSpeech(String colorName, String colorDescription){
        return "Your picture has " + colorName + " as the dominant color. Its nearest basic color " +
                "is " + BasicColors.getClosestColorName() + ". " + colorDescription;
    }

    //setup TTS
    public void onInit(int initStatus) {

        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.US);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }
//    TextToSpeech t1;
//    private void speakColor(String toSpeak){
//
//        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if(status != TextToSpeech.ERROR) {
//                    t1.setLanguage(Locale.US);
//                }
//            }
//        });
////        Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
//        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
//    }
    private Handler mHandler = new Handler();
    public void setText(final String colorName){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
//                String bra = "0";
//                String zero= "0";
//                bra = colorName;
                final String cNAme = colorName;
                TextView theTextviewV23 = (TextView)findViewById(R.id.textViewTitle);
                theTextviewV23.setText(cNAme);
                theTextviewV23.postInvalidate();
                TextView theTextviewv2323 = (TextView)findViewById(R.id.textView4);
                theTextviewv2323.setText(colorDescription);
                theTextviewv2323.postInvalidate();
            }
        });
    }


}
