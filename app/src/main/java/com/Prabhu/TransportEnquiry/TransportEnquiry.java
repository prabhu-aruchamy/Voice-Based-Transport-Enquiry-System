package com.Prabhu.TransportEnquiry;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

public class TransportEnquiry extends AppCompatActivity {

    private TextToSpeech tTextToSpeech;
    private SpeechRecognizer tSpeechRecognizer;
    private String tWelcomeText = "Welcome To Voice Based Transport Enquiry System" ;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    LinearLayout tCommandsTextLayout;
    private TextView welcomeUser;
    private TextView ProjectTitle;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_enquiry);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestAudioPermissions();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });


        count = 0;
        tCommandsTextLayout = (LinearLayout)findViewById(R.id.commandsTextLayout);
        welcomeUser = (TextView)findViewById(R.id.welcomeUser);
        ProjectTitle = (TextView)findViewById(R.id.ProjectTitle);
        String NameofUser = getIntent().getStringExtra("UserName");
        welcomeUser.setText("Welcome "+NameofUser+"!");
        tCommandsTextLayout.setVisibility(View.GONE);

        ProjectTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = count + 1;
                if(count >=5){
                    Intent intent = new Intent(TransportEnquiry.this,manualEnquiry.class);
                    startActivity(intent);
                    count=0;
                }

            }
        });



        getSupportActionBar().hide();
        InitializeTextToSpeech();
        GetDetailsAsVoiceFromUser();

    }

    private void GetDetailsAsVoiceFromUser() {

        if(SpeechRecognizer.isRecognitionAvailable(this)){
            tSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            tSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int i) {

                }

                @Override
                public void onResults(Bundle bundle) {

                    List<String> results = bundle.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                    );
                    processResult(results != null ? results.get(0) : null);

                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });

        }

    }

    private void processResult(String command) {


        Toast.makeText(TransportEnquiry.this, "Command begin: "+command, Toast.LENGTH_SHORT).show();
        command = command.toLowerCase();

        if(command.indexOf("open")!= -1) {
            if (command.indexOf("transport") != -1 || command.indexOf("enquiry")!=-1) {

                //SpeechVoice("The Word is "+command);
                Intent intent = new Intent(TransportEnquiry.this, manualEnquiry.class);
                startActivity(intent);


            }
//        }else if(command.contains("bus")){
//            if(command.contains("services")){
//                //MainOption2 - Booking Enquiry
//
//                Toast.makeText(TransportEnquiry.this, "Command: "+command, Toast.LENGTH_SHORT).show();
//                SpeechVoice("The word is "+command);
//            }
//        }
        }

    }

    private void InitializeTextToSpeech() {

        tTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tTextToSpeech.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        //if no error call Text To Speech
                        SpeechVoice(tWelcomeText);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
    }

    private void SpeechVoice(String readWords) {

        tTextToSpeech.speak(readWords,TextToSpeech.QUEUE_FLUSH,null,null);

    }

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {

            //Go ahead with recording audio now

            tCommandsTextLayout.setVisibility(View.VISIBLE);

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
            tSpeechRecognizer.startListening(intent);
        }
    }


    @Override
    protected void onDestroy() {
        if (tTextToSpeech != null) {
            tTextToSpeech.stop();
            tTextToSpeech.shutdown();
        }

        super.onDestroy();
    }

}
