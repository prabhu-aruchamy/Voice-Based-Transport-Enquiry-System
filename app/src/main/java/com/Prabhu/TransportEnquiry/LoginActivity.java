package com.Prabhu.TransportEnquiry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText luserName,lpassword;
    private Button tloginbtn;
    private TextView registertext;
    private String uname,upass;
    private ProgressDialog progressDialog;
    private String UserValidateURL="https://thelordtech.in/dbms/userValidate.php";
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        luserName = (EditText)findViewById(R.id.luserName);
        lpassword = (EditText) findViewById(R.id.lpassword);
        tloginbtn = (Button) findViewById(R.id.tloginbtn);
        registertext = (TextView)findViewById(R.id.registertext);


        tloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname = luserName.getText().toString();
                upass = lpassword.getText().toString();
                System.out.println("qwertyuiop: "+uname+".."+upass);

//                HashMap postData = new HashMap();
//
//                postData.put("txtUserid", user_id);
//                postData.put("txtUserName",tname);
                //postData.put("btnSubmit","Register");
                if(!uname.isEmpty() && !upass.isEmpty()){

                    Validateuser(uname, upass);


                }
                else {
                    Snackbar.make(view,"Enter UserName and Password",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        registertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterUser.class);
                startActivity(intent);
            }
        });

    }

    private void Validateuser(final String uname, final String upass) {

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please Wait, Logging in...");
        progressDialog.show();

        // Calling method to get value from EditText.
        //GetValueFromEditText();

        // Creating string request with post method.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserValidateURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        // Showing response message coming from server.
                        Toast.makeText(LoginActivity.this, ServerResponse, Toast.LENGTH_LONG).show();

                        if(ServerResponse.contains("login success")){
                            Intent intent = new Intent(LoginActivity.this,TransportEnquiry.class);
                            intent.putExtra("UserName",uname);
                            startActivity(intent);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        // Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        // Showing error message if something goes wrong.
                        Toast.makeText(LoginActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("user_name", uname);
                params.put("password", upass);

                return params;
            }

        };

        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(LoginActivity.this);
        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);
    }

}
