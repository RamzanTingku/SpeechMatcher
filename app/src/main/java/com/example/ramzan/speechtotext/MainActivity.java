package com.example.ramzan.speechtotext;

import java.util.ArrayList;
        import java.util.Locale;

        import android.app.Activity;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
        import android.os.Bundle;
        import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
        import android.view.View;
        import android.widget.ImageButton;
        import android.widget.TextView;
        import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Context context;
    TinyDB tinydb;
    private TextView txtSpeechInput;
    private TextView txtSpeechInput2;
    private ImageButton btnSpeak;
    private ImageButton btnSpeak2;
    private final int REQ_CODE_NEW_SPEECH_INPUT = 100;
    private final int REQ_CODE_SAVE_SPEECH_INPUT = 200;
    ArrayList<String>SavedSpeechList = new ArrayList<>();
    ArrayList<String>NewSpeechList = new ArrayList<>();
    boolean isSpeechMatched;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        tinydb = new TinyDB(context);

       newPromptSpeechInput();

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        txtSpeechInput2 = (TextView) findViewById(R.id.txtSpeechInput2);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnSpeak2 = (ImageButton) findViewById(R.id.btnSpeak2);

        SavedSpeechList = tinydb.getListString("SavedList");

        // hide the action bar
//        getActionBar().hide();

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                newPromptSpeechInput();
            }
        });


        btnSpeak2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                savePromptSpeechInput();
            }
        });


    }

    /**
     * Showing google speech input dialog
     * */
    private void savePromptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        /*intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());*/
        /*intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));*/
        try {
            startActivityForResult(intent, REQ_CODE_SAVE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void newPromptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        /*intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());*/
        /*intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));*/
        try {
            startActivityForResult(intent, REQ_CODE_NEW_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_NEW_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if(!NewSpeechList.isEmpty()){
                        NewSpeechList.clear();
                    }

                    for (int i = 0; i < result.size(); i++) {
                        NewSpeechList.add(result.get(i));

                    }

                checkloop:
                    for (int i = 0; i < NewSpeechList.size(); i++) {

                        for (int j = 0; j < SavedSpeechList.size(); j++) {
                            if(NewSpeechList.get(i).equals(SavedSpeechList.get(j))){
                                txtSpeechInput.setText("Speech Matched");
                                break checkloop;
                            }else if(i == (NewSpeechList.size())-1){
                                if(NewSpeechList.get(i).equals(SavedSpeechList.get(j))){
                                   txtSpeechInput.setText("Speech Matched");
                                    break checkloop;
                                }else {
                                   txtSpeechInput.setText("Speech Didn't Match");

                                }
                            }
                        }

                    }


                   // txtSpeechInput.setText(SavedSpeechList.get(0)+"\n"+SavedSpeechList.get(1)+"\n"+SavedSpeechList.get(2)+"\n"+SavedSpeechList.get(3)+"\n"+SavedSpeechList.get(4));
                  //  txtSpeechInput2.setText(NewSpeechList.get(0)+"\n"+NewSpeechList.get(1)+"\n"+NewSpeechList.get(2)+"\n"+NewSpeechList.get(3)+"\n"+NewSpeechList.get(4));


                }
                break;
            }

            case REQ_CODE_SAVE_SPEECH_INPUT:{

                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if(!SavedSpeechList.isEmpty()){
                        SavedSpeechList.clear();
                    }


                    for (int i = 0; i < result.size(); i++) {

                        SavedSpeechList.add(result.get(i));

                    }

                    txtSpeechInput.setText("New Word Saved");

                    tinydb.putListString("SavedList", SavedSpeechList);


                }
                break;

            }

        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

}
