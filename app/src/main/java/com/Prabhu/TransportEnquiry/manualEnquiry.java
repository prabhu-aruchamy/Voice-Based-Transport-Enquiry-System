package com.Prabhu.TransportEnquiry;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class manualEnquiry extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    private String optionSelected;
    private EditText userId,b_from,b_to,b_timing;
    private Button fetchButton,button_fetchinfo;
    String ttype;
    private TextToSpeech tTextToSpeech;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private TextView displayOutput;
    //private String UserValidateURL = "https://thelordtech.in/dbms/TransportHistory.php";
    String JSON_STRING;
    HttpResponse response;
    String str = "";
    int qw = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_enquiry);
        getSupportActionBar().hide();

        userId = (EditText)findViewById(R.id.userid_input);
        b_from = (EditText)findViewById(R.id.b_from);
        b_to = (EditText)findViewById(R.id.b_to);
        b_timing = (EditText)findViewById(R.id.b_timing);
        fetchButton = (Button)findViewById(R.id.button_fetch);
        displayOutput = (TextView)findViewById(R.id.opt);
        button_fetchinfo = (Button) findViewById(R.id.button_fetchinfo);

        InitializeTextToSpeech();

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchFromPHP();

            }
        });

        button_fetchinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchFromPHPBusEnquiry();
            }
        });

        Spinner spinner = findViewById(R.id.spinner1);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.avaliable_opt, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(manualEnquiry.this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        optionSelected = adapterView.getItemAtPosition(i).toString();

        if(optionSelected.matches("Booking Details")){
            qw++;
            userId.setVisibility(View.VISIBLE);
            b_from.setVisibility(View.GONE);
            b_to.setVisibility(View.GONE);
            b_timing.setVisibility(View.GONE);
            fetchButton.setVisibility(View.VISIBLE);
            button_fetchinfo.setVisibility(View.INVISIBLE);
            ttype = "TransportHistory";
           // Toast.makeText(this, ttype, Toast.LENGTH_SHORT).show();


        }else if(optionSelected.matches("Bus Enquiry")){

            userId.setVisibility(View.GONE);
            b_from.setVisibility(View.VISIBLE);
            b_to.setVisibility(View.VISIBLE);
            b_timing.setVisibility(View.VISIBLE);
            fetchButton.setVisibility(View.INVISIBLE);
            button_fetchinfo.setVisibility(View.VISIBLE);


            ttype = "EnquiryBooking";

            //Toast.makeText(this, ttype, Toast.LENGTH_SHORT).show();

        }else if(optionSelected.matches("Check Bus From")){

            userId.setVisibility(View.GONE);
            b_from.setVisibility(View.VISIBLE);
            b_to.setVisibility(View.GONE);
            b_timing.setVisibility(View.GONE);

            ttype = "CheckBusFrom";
            //Toast.makeText(this, ttype, Toast.LENGTH_SHORT).show();



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
                        System.out.println("INITIALIZATION SUCCESS!");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
    }


    private void FetchFromPHP() {

        progressDialog = new ProgressDialog(manualEnquiry.this);
        progressDialog.setMessage("Please Wait, Fetching Details");
        progressDialog.show();

        // Calling method to get value from EditText.
        //GetValueFromEditText();

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thelordtech.in/dbms/BookingEnquiryN.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        // Showing response message coming from server.
                        Toast.makeText(manualEnquiry.this, ServerResponse, Toast.LENGTH_LONG).show();
                        displayOutput.setText(ServerResponse);
                        SpeechVoice(ServerResponse);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        // Showing error message if something goes wrong.
                        Toast.makeText(manualEnquiry.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                       // SpeechVoice(volleyError.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                String idofuser;
                idofuser = userId.getText().toString();

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
//                params.put("userid",idofuser);
                //params.put("userID", userId.getText().toString());

                params.put("userID", userId.getText().toString());


                System.out.println("qwerty.. "+userId);
                return params;
            }

        };

        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(manualEnquiry.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

        //UserValidateURL = null;

    }

    private void SpeechVoice(String serverResponse) {

        tTextToSpeech.speak(serverResponse,TextToSpeech.QUEUE_FLUSH,null,null);
    }

    private void FetchFromPHPBusEnquiry() {

        progressDialog = new ProgressDialog(manualEnquiry.this);
        progressDialog.setMessage("Please Wait, Fetching Details");
        progressDialog.show();

        // Calling method to get value from EditText.
        //GetValueFromEditText();

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thelordtech.in/dbms/NBusEnquiry.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        // Showing response message coming from server.
                        Toast.makeText(manualEnquiry.this, ServerResponse, Toast.LENGTH_LONG).show();
                        displayOutput.setText(ServerResponse);
                        SpeechVoice(ServerResponse);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        // Showing error message if something goes wrong.
                        Toast.makeText(manualEnquiry.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                String idofuser;
                idofuser = userId.getText().toString();

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
//                params.put("userid",idofuser);

                params.put("bfrom", b_from.getText().toString());
                params.put("bto",b_to.getText().toString());
                params.put("timing",b_timing.getText().toString());

                System.out.println("qwerty.. "+userId);
                return params;
            }

        };

        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(manualEnquiry.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

        //UserValidateURL = null;

    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
