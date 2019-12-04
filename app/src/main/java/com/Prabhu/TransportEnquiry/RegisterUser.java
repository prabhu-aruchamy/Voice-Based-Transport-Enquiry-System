package com.Prabhu.TransportEnquiry;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RegisterUser extends AppCompatActivity {

    private EditText Username,email,password,cpassword;
    private String uname,uemail,upass,ucpass;
    private Button registerbtn;
    private String ServerURL = "https://thelordtech.in/dbms/registerUser.php" ;
    private String uid;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        Username = (EditText) findViewById(R.id.newuser_username);
        email = (EditText) findViewById(R.id.newuser_email);
        password = (EditText) findViewById(R.id.newuser_password);
        cpassword = (EditText) findViewById(R.id.newuser_conformpass);
        registerbtn = (Button)findViewById(R.id.registerbtn);

        progressDialog = new ProgressDialog(RegisterUser.this);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Random rand = new Random();
                uid = String.valueOf(rand.nextInt(5000));
                uname = Username.getText().toString();
                uemail = email.getText().toString();
                upass = password.getText().toString();
                ucpass = cpassword.getText().toString();

                if(uname.isEmpty() || uemail.isEmpty() || upass.isEmpty() || ucpass.isEmpty()){
                    Toast.makeText(RegisterUser.this, "All Fields are mandatory", Toast.LENGTH_SHORT).show();
                }else if(!uemail.contains("@") || !uemail.contains(".com")){
                    Toast.makeText(RegisterUser.this, "Not an Valid E-mail", Toast.LENGTH_SHORT).show();
                }else if(!(upass.matches(ucpass))){
                    Toast.makeText(RegisterUser.this, "Both passwords must be same", Toast.LENGTH_SHORT).show();
                }else {

                    progressDialog.setMessage("Please Wait, Registering...");
                    progressDialog.show();

                    // Calling method to get value from EditText.
                    //GetValueFromEditText();

                    // Creating string request with post method.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerURL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String ServerResponse) {

                                    // Hiding the progress dialog after all task complete.
                                    progressDialog.dismiss();

                                    // Showing response message coming from server.
                                    Toast.makeText(RegisterUser.this, ServerResponse, Toast.LENGTH_LONG).show();

                                    if(ServerResponse.contains("Registration Success")){
                                        finish();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                    // Hiding the progress dialog after all task complete.
                                    progressDialog.dismiss();

                                    // Showing error message if something goes wrong.
                                    Toast.makeText(RegisterUser.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {

                            // Creating Map String Params.
                            Map<String, String> params = new HashMap<String, String>();

                            // Adding All values to Params.
                            params.put("uid", uid);
                            params.put("uname", uname);
                            params.put("uemail", uemail);
                            params.put("upass", upass);

                            return params;
                        }

                    };

                    // Creating RequestQueue.
                    requestQueue = Volley.newRequestQueue(RegisterUser.this);
                    // Adding the StringRequest object into requestQueue.
                    requestQueue.add(stringRequest);

                }

                }

        });
    }



//       public class addToDataBase extends AsyncTask<String , Void, String> {
//
//
//
//           @Override
//            protected void onPreExecute() {
//               super.onPreExecute();
//               progressDialog = new ProgressDialog(RegisterUser.this);
//                progressDialog.setMessage("Registering...");
//                progressDialog.setCancelable(false);
//                progressDialog.show();
//            }
//
//            @Override
//            protected String doInBackground(String... strings) {
//
//                String idHolder = user_id;
//                String nameHolder= uname;
//                String emailHolder = uemail;
//                String passwordHolder= upass;
//
//                try {
//                    URL url = new URL(ServerURL);
//                    HttpURLConnection httpURLConnection =
//                            (HttpURLConnection)url.openConnection();
//                    httpURLConnection.setRequestMethod("POST");
//                    httpURLConnection.setDoOutput(true);
//                    OutputStream OS = httpURLConnection.getOutputStream();
//                    BufferedWriter bufferedWriter = new BufferedWriter(new
//                            OutputStreamWriter(OS, "UTF-8"));
//                    String data= URLEncoder.encode("userid","UTF-8")+"="+URLEncoder.encode(idHolder,"UTF-8")+"&"+URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(nameHolder,"UTF-8")+"&"+ URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(emailHolder,"UTF-8")
//                            +"&"+ URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(passwordHolder,"UTF-8");
//                    bufferedWriter.write(data);
//                    bufferedWriter.flush();
//                    bufferedWriter.close();
//                    OS.close();
//                    InputStream IS = httpURLConnection.getInputStream();
//                    IS.close();
//                    return "Registration Success!!";
//                }catch (MalformedURLException e){
//                    e.printStackTrace();
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//            return null;
//            }
//
//            @Override
//            protected void onPostExecute(String aVoid) {
//                super.onPostExecute(aVoid);
//                progressDialog.dismiss();
//                Toast.makeText(RegisterUser.this, "Registration Sucessfull", Toast.LENGTH_SHORT).show();
//
//            }
        }




