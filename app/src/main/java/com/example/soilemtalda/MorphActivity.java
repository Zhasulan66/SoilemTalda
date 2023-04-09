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

public class MorphActivity extends AppCompatActivity {

    EditText message;

    TextView aiAnswer;
    AppCompatButton taldau_btn;

    ProgressBar progressBar;
    ImageView back;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String myKey = "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";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morph);

        getSupportActionBar().hide();

        message = findViewById(R.id.message);
        aiAnswer = findViewById(R.id.answer_id);
        taldau_btn = findViewById(R.id.taldau_btn);
        progressBar = findViewById(R.id.progress_bar);

        back = findViewById(R.id.imageView_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MorphActivity.this, ChooseTypeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

            }
        });

        taldau_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String question = message.getText().toString();

                if(question.isEmpty()){
                    Toast.makeText(MorphActivity.this, "сөйлем жоқ", Toast.LENGTH_SHORT).show();
                }
                else {
                    String text = "саған үлгілер: \n" +
                            "сұрақ: Қаладан \n" +
                            "жауап: қала - түбір, зат есім \\n дан - жалғау, шығыс септік" +
                            "\n" +
                            "сұрақ: аспан \n" +
                            "жауап: аспан - түбір, зат есім " +
                            "\n" +
                            "сұрақ: кеше \n" +
                            "жауап: кеше - түбір, устеу" +
                            "\n" +
                            "сұрақ: жасыл \n" +
                            "жауап: жасыл - түбір, сын есім" +
                            "\n" +
                            "сұрақ: " + question;
                    callAPI(text, myKey);
                    message.setText("");

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
}