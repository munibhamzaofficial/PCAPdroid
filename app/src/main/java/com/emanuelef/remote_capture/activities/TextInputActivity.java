package com.emanuelef.remote_capture.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.emanuelef.remote_capture.R;
import com.emanuelef.remote_capture.TinyDB.TinyDB;
import com.emanuelef.remote_capture.Utils;
import com.emanuelef.remote_capture.model.DomainModel;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;


public class TextInputActivity extends AppCompatActivity {

    private TextView tvPickJSON;
    private EditText etInputText;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.input_file_text);
        setContentView(R.layout.activity_text_input);

        tvPickJSON = findViewById(R.id.tvPickJSON);
        etInputText = findViewById(R.id.etInputText);
        btnSubmit = findViewById(R.id.btnSaveInput);

        //isForView file
        boolean isForView = getIntent().getBooleanExtra("isForView",false);
        if (isForView){
            TinyDB tinydb = new TinyDB(this);
            String kodexDomains = tinydb.getString("kodexDomain");
            etInputText.setText(kodexDomains.trim());
        }


        tvPickJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputData = etInputText.getText().toString();
                if(inputData.isEmpty()){
                    Toast.makeText(TextInputActivity.this,"Text could not be empty ",Toast.LENGTH_SHORT).show();
                }else if(!isJSONValid(inputData)){
                    Toast.makeText(TextInputActivity.this,"Must be a valid JSON ",Toast.LENGTH_SHORT).show();
                }else{
//                    MainActivity.kodexJsonData = inputData;
//                    Gson gson = new Gson();
//                    Type listUserType = new TypeToken<ArrayList<JsonModelClass>>() { }.getType();
//                    ArrayList<JsonModelClass> data = gson.fromJson(inputData, listUserType);
//                    MainActivity.kodexJsonArray = data;
//                    for (int i = 0; i < MainActivity.kodexJsonArray.size(); i++) {
//                        Log.i("data", "kodex " + i + "\n" + MainActivity.kodexJsonArray.get(i).getDomain());
//                    }


                    //kodex
                    try {
                        JSONObject jsonObject = new JSONObject(inputData);
                        JSONArray jsonArray = jsonObject.getJSONArray("domains");

                        ArrayList<DomainModel> listdata = new ArrayList<DomainModel>();
                        if (jsonArray != null) {

                            //Iterating JSON array
                            for (int i=0;i<jsonArray.length();i++){

                                //Adding each element of JSON array into ArrayList
                                JSONObject jObj = jsonArray.getJSONObject(i);
                                Log.d("Kodex Network: ", String.valueOf(jsonArray.get(i)));
                                DomainModel dataModel = new DomainModel();
                                dataModel.setName(jObj.getString("name"));
                                dataModel.setTitle(jObj.getString("title"));
                                dataModel.setDescription(jObj.getString("description"));
                                dataModel.setLink(jObj.getString("link"));
                                dataModel.setImg(jObj.getString("img"));
                                listdata.add((dataModel));
                            }
                        }
                        for (DomainModel item : listdata){
                            Log.d("Kodex Network: ",item.getName());
                        }
//                        Save data into LocalPreferances
                        TinyDB tinydb = new TinyDB(getApplicationContext());
                        tinydb.putString("kodexDomain",inputData);
                        // MY_PREFS_NAME - a static String variable like:

//                        SharedPreferences.Editor editor = getSharedPreferences(MainActivity.KODEX_PREFS_NAME, MODE_PRIVATE).edit();
//                        editor.putString("kodexDomains", inputData);
//                        editor.apply();

                        MainActivity.kodexJsonArray = listdata;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }
        });
    }

    //Kodex
    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> fileActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                        //Do something with files

//                        Uri uri = data.getData();
                        Uri uri = files.get(0).getUri();
                        String content = "";
                        try {
                            InputStream in = getContentResolver().openInputStream(uri);


                            BufferedReader r = new BufferedReader(new InputStreamReader(in));
                            StringBuilder total = new StringBuilder();
                            for (String line; (line = r.readLine()) != null; ) {
                                total.append(line).append('\n');
                            }

                            content = total.toString();
                            Log.d("Kodex Network Content",content);


                        }catch (Exception e) {
                            Log.d("Kodex Network Exception",e.toString());
                        }

                        Log.d("Kodex Network JSON",content);
                        etInputText.setText(content);
                    }
                }
            });

    public void openFilePicker() {
        Intent intent = new Intent(TextInputActivity.this, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(false)
                .setShowFiles(true)
                .setShowAudios(false)
                .setShowVideos(false)
                .setMaxSelection(1)
                .setSingleChoiceMode(true)
                .setSkipZeroSizeFiles(false)
                .setSuffixes(".json")
                .build());
        fileActivityResultLauncher.launch(intent);
    }

    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}