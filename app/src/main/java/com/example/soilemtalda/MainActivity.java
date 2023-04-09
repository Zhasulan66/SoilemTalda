package com.example.soilemtalda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    EditText message;

    TextView aiAnswer;
    AppCompatButton taldau_btn;

    ProgressBar progressBar;
    ImageView back;

    ImageView imageView;
    ProgressBar progressBar2;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String myKey = "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";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        message = findViewById(R.id.message);
        aiAnswer = findViewById(R.id.answer_id);
        taldau_btn = findViewById(R.id.taldau_btn);
        progressBar = findViewById(R.id.progress_bar);

        imageView = findViewById(R.id.image_view);
        progressBar2 = findViewById(R.id.progress_bar2);

        back = findViewById(R.id.imageView_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChooseTypeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

            }
        });

        taldau_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String question = message.getText().toString();

                if(question.isEmpty()){
                    Toast.makeText(MainActivity.this, "сөйлем жоқ", Toast.LENGTH_SHORT).show();
                }
                else {
                    String text = "саған үлгілер: \n" +
                            "сұрақ: Аяулым тауға шықты \n" +
                            "жауап: Аяулым - бастауыш \\n тауға - пысықтауыш \\n шықты - баяндауыш\n" +
                            "\n" +
                            "сұрақ: Мен қалың кітап сатып алдым\n" +
                            "жауап: Мен  - бастауыш \\n қалың - анықтауыш \\n кітап - толықтауыш \\n сатып  - баяндауыш\\n алдым - баяндауыш\n" +
                            "\n" +
                            "сұрақ: Ол заттарын жинады да, далаға жүгіріп кетті \n" +
                            "жауап: Ол - бастауыш \\n заттарын  - толықтауыш \\n жинады - баяндауыш \\n далаға - пысықтауыш  \\n  жүгіріп - баяндауыш \\n кетті - баяндауыш \n" +
                            "\n" + "сұрақ: " + question;
                    callAPI(text, myKey);
                    message.setText("");
                    callAPI2(question);

                }
            }
        });
    }

    public void callAPI(String question, String key) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                aiAnswer.setVisibility(View.GONE);
            }
        });


        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("model", "text-davinci-003");
            jsonObject.put("prompt", question);
            jsonObject.put("max_tokens", 3000);
            jsonObject.put("temperature", 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer " + key)
                .post(body)
                .build();

        Log.d("test", "request created");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                //addResponse("Failed to load " +  e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        aiAnswer.setText("Failed to load " +  e.getMessage());
                    }
                });

                Log.d("test", "call on failure");

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        aiAnswer.setVisibility(View.VISIBLE);
                    }
                });

                Log.d("test", "call on response");

                if(response.isSuccessful()){
                    JSONObject jsonObject1 = null;

                    try {
                        String responseString = response.body().string();
                        jsonObject1 = new JSONObject(responseString);
                        JSONArray jsonArray = jsonObject1.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                aiAnswer.setText(result.trim());
                            }
                        });


                        Log.d("test", "success!");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            aiAnswer.setText("Failed to load + " +  response.body().toString());
                        }
                    });

                    Log.d("test", "else fail!");
                }
            }
        });

    }

    private void callAPI2(String text) {
        //API CALL
        setInProgress(true);

        JSONObject jsonBodyImage = new JSONObject();
        try {
            jsonBodyImage.put("prompt", text);
            jsonBodyImage.put("size", "256x256");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        RequestBody requestBodyImage = RequestBody.create(jsonBodyImage.toString(), JSON);
        Request request2 = new Request.Builder()
                .url("https://api.openai.com/v1/images/generations")
                .header("Authorization", "Bearer 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")
                .post(requestBodyImage)
                .build();

        client.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(MainActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONObject jsonObjectImage = new JSONObject(response.body().string());
                    String imageUrl = jsonObjectImage.getJSONArray("data").getJSONObject(0).getString("url");
                    setInProgress(false);
                    loadImage(imageUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void setInProgress(boolean inProgress){
        runOnUiThread(()->{
            if(inProgress){
                taldau_btn.setVisibility(View.GONE);
                progressBar2.setVisibility(View.VISIBLE);
            }
            else {
                taldau_btn.setVisibility(View.VISIBLE);
                progressBar2.setVisibility(View.GONE);
            }
        });
    }

    private void loadImage(String imageUrl) {
        //load image
        runOnUiThread(() -> {
            Picasso.get().load(imageUrl).into(imageView);
        });

    }
}