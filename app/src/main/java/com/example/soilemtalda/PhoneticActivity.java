package com.example.soilemtalda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PhoneticActivity extends AppCompatActivity {
    KazakhLetter[] letters;

    EditText message;
    TextView answer;
    AppCompatButton taldau_btn;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonetic);

        letters = initPhonetic();

        getSupportActionBar().hide();

        message = findViewById(R.id.message);
        answer = findViewById(R.id.answer_id);
        taldau_btn = findViewById(R.id.taldau_btn);
        back = findViewById(R.id.imageView_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer.setText(" ");
                answer.setVisibility(View.GONE);
                message.setText(" ");
                Intent intent = new Intent(PhoneticActivity.this, ChooseTypeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

            }
        });

        taldau_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String word = message.getText().toString();

                if (word.isEmpty()) {
                    Toast.makeText(PhoneticActivity.this, "сөз жоқ :D", Toast.LENGTH_SHORT).show();
                } else {

                    List<KazakhLetter> phoneticRepresentation = new ArrayList<>();
                    for (char c : word.toCharArray()) {
                        for (KazakhLetter letter : letters) {
                            if (letter.getLetter().equals(Character.toString(c))) {
                                phoneticRepresentation.add(letter);
                                break;
                            }
                        }
                    }

                    StringBuilder result = new StringBuilder(" ");

                    for (KazakhLetter letter : phoneticRepresentation) {
                        result.append(letter.toString() + ", \n ");

                    }
                    answer.setVisibility(View.VISIBLE);
                    answer.setText(result.toString());
                }

            }
        });

    }


    public static KazakhLetter[] initPhonetic(){
        KazakhLetter[] letters = {
                new KazakhLetter("а", "a", true),
                new KazakhLetter("ә", "ae", true),
                new KazakhLetter("б", "b", false),
                new KazakhLetter("в", "v", false),
                new KazakhLetter("г", "g", false),
                new KazakhLetter("ғ", "gh", false),
                new KazakhLetter("д", "d", false),
                new KazakhLetter("е", "e", true),
                new KazakhLetter("ё", "yo", true),
                new KazakhLetter("ж", "zh", false),
                new KazakhLetter("з", "z", false),
                new KazakhLetter("и", "i", true),
                new KazakhLetter("й", "y", false),
                new KazakhLetter("к", "k", false),
                new KazakhLetter("қ", "q", false),
                new KazakhLetter("л", "l", false),
                new KazakhLetter("м", "m", false),
                new KazakhLetter("н", "n", false),
                new KazakhLetter("ң", "ng", false),
                new KazakhLetter("о", "o", true),
                new KazakhLetter("ө", "oe", true),
                new KazakhLetter("п", "p", false),
                new KazakhLetter("р", "r", false),
                new KazakhLetter("с", "s", false),
                new KazakhLetter("т", "t", false),
                new KazakhLetter("у", "u", true),
                new KazakhLetter("ұ", "u'", true),
                new KazakhLetter("ү", "ue", true),
                new KazakhLetter("ф", "f", false),
                new KazakhLetter("х", "h", false),
                new KazakhLetter("һ", "h'", false),
                new KazakhLetter("ц", "ts", false),
                new KazakhLetter("ч", "ch", false),
                new KazakhLetter("ш", "sh", false),
                new KazakhLetter("щ", "shch", false),
                new KazakhLetter("ъ", "", false),
                new KazakhLetter("ы", "y", true),
                new KazakhLetter("і", "i", true),
                new KazakhLetter("ь", "", false),
                new KazakhLetter("э", "e'", true),
                new KazakhLetter("ю", "yu", true),
                new KazakhLetter("я", "ya", true)
        };
        return letters;
    }
}