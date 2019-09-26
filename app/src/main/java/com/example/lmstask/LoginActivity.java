package com.example.lmstask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
Button loginBtn;
EditText etusername,etpassword;
    StringRequest stringRequest;
    String username, password,token,accountid;
    CustomProgressDialogTwo customProgressDialogTwo;
    CheckBox checkBox;
    SharedPreferences sharedPreferences;
    Pref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etusername=findViewById(R.id.username);
        etpassword=findViewById(R.id.password);
        loginBtn=findViewById(R.id.login_button);
        checkBox=findViewById(R.id.checkbox);

        customProgressDialogTwo = new CustomProgressDialogTwo(this, R.drawable.loadingicon);
        pref=new Pref(LoginActivity.this);


        CheckSharedPreferencesForValues();





        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                      if(etusername.getText().toString().trim().equals("")||etpassword.getText().toString().trim().equals("")){
                          Toast.makeText(LoginActivity.this, "Please fill credentials", Toast.LENGTH_SHORT).show();
                      }
                      else{
                          pref.setUserName(etusername.getText().toString());
                          pref.setPass(etpassword.getText().toString());

                      }
                }
                else{
                       sharedPreferences=getApplicationContext().getSharedPreferences(getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
                       if(sharedPreferences.contains("username")&&sharedPreferences.contains("pass")){
                           pref.removeUsername();
                           pref.removePass();

                       }
                       else{


                       }


                }
            }
        });



        loginBtn.setOnClickListener(this);



    }

    private void CheckSharedPreferencesForValues() {
        sharedPreferences=getApplicationContext().getSharedPreferences(getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        if(sharedPreferences.contains("username")&&sharedPreferences.contains("pass")){
            etusername.setText(pref.getUserName());
            etpassword.setText(pref.getPass());
        }
        else{


        }



    }

    @Override
    public void onClick(View view) {

        CallLoginApi();


    }

    private void CallLoginApi() {
       username=etusername.getText().toString().trim();
       password=etpassword.getText().toString().trim();
        customProgressDialogTwo.show();
        stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                customProgressDialogTwo.dismiss();
                Log.e("Response_login",response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("code").equals("200")){
                        JSONObject responseJsonObj= jsonObject.getJSONObject("response");
                        token=responseJsonObj.getString("token");
                        JSONObject userJsonObj=responseJsonObj.getJSONObject("user");
                        accountid=userJsonObj.getString("account_id");

                        Log.e("Token",token);
                        Log.e("Account_id",accountid);


                        pref.setToken(token);
                        pref.setAccountId(accountid);

                        Intent intent=new Intent(LoginActivity.this,GetUsersActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                    }





                }//end of try
                catch (JSONException e) {

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        customProgressDialogTwo.dismiss();
//                        Log.e("error", String.valueOf(error));

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            if (networkResponse.statusCode == 500) {
                                customProgressDialogTwo.dismiss();
                                Toast.makeText(getApplicationContext(), "Server Error ", Toast.LENGTH_SHORT).show();
                            }else if(networkResponse.statusCode==401){
                                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();

                            } else if (error instanceof TimeoutError) {
                                customProgressDialogTwo.dismiss();
                                Toast.makeText(getApplicationContext(), "Connnection TimeOut", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("username", username);
                params.put("password", password);
                params.put("usertype", "accountAdmin");
                params.put("_extend", "user");

                Log.e("params==>", String.valueOf(params));


                return params;
            }
        };
        retryPolicy();





    }

    private void retryPolicy() {
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(mRetryPolicy);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
