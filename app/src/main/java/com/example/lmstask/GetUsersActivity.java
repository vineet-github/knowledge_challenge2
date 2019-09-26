package com.example.lmstask;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetUsersActivity extends AppCompatActivity implements View.OnClickListener {
    StringRequest stringRequest;
    RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    CustomProgressDialogTwo customProgressDialogTwo;
    Pref pref;
    int countimitial = 0, countfinal = 5;
    UserAdapter recyclerViewAdapter;
    List<UserModel> userModelList = new ArrayList<>();
    LinearLayout linearLayoutlogout;
    public int NUM_ITEMS_PAGE = 5;
    int no_of_pages = 0;
    LinearLayout paginationll;
    TextView firsttv, secondtv, thirdtv, fourthtv, nexttv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_users);

        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutlogout = findViewById(R.id.linear_logout);
        paginationll = findViewById(R.id.pagination_ll);
        firsttv = findViewById(R.id.first);
        secondtv = findViewById(R.id.second);
        thirdtv = findViewById(R.id.third);
        fourthtv = findViewById(R.id.fourth);
        nexttv = findViewById(R.id.next);
        pref = new Pref(GetUsersActivity.this);
        customProgressDialogTwo = new CustomProgressDialogTwo(GetUsersActivity.this, R.drawable.loadingicon);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_layout);

        linearLayoutlogout.setOnClickListener(this);


        GetUsersListApi();

    }

    private void GetUsersListApi() {
        customProgressDialogTwo.show();
        userModelList.clear();
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.BASE_URL + pref.getAccountId() + "/students?" + "_start=" + countimitial + "&_limit=" + countfinal + "&token=" + pref.getToken(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Students Response", response);
                        Log.e("Token Response", pref.getToken());

                        customProgressDialogTwo.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("200")) {
                                JSONArray jsonArray = jsonObject.optJSONArray("response");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    UserModel userModel = new UserModel();
                                    userModel.setFull_name(jsonArray.getJSONObject(i).getString("first_name") + " " + jsonArray.getJSONObject(i).getString("last_name"));
                                    userModel.setLast_name(jsonArray.getJSONObject(i).getString("username"));
                                    userModel.setGroup(jsonArray.getJSONObject(i).getString("group"));

                                    userModelList.add(userModel);

                                }

                                no_of_pages = userModelList.size() / NUM_ITEMS_PAGE;
                                DisplayPageNumbersAccordingToSize(no_of_pages);
                                if (userModelList.size() % 10 != 0) {
                                    no_of_pages = no_of_pages + 1;
                                    DisplayPageNumbersAccordingToSize(no_of_pages);
                                }



                                setRecyclerViewAdapter();

                            }


                        } catch (Exception e) {

                        }
//                        Log.e("awaitingLeavesPostRequest login response==>", response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Crashlytics.logException(error);
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            if (networkResponse.statusCode == 500) {
                                Toast.makeText(GetUsersActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

//                                Log.e("Logged_out", String.valueOf(networkResponse.statusCode));
                            } else {

                            }
                        }
                    }
                }) {


        };
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(mRetryPolicy);
        VolleySingleton.getInstance(GetUsersActivity.this).addToRequestQueue(stringRequest);


    }

    private void DisplayPageNumbersAccordingToSize(int no_of_pages) {
        paginationll.setVisibility(View.VISIBLE);
        switch (no_of_pages) {
            case 1:
                firsttv.setVisibility(View.VISIBLE);
                nexttv.setVisibility(View.VISIBLE);
                firsttv.setOnClickListener(this);
                break;

            case 2:
                firsttv.setVisibility(View.VISIBLE);
                secondtv.setVisibility(View.VISIBLE);
                nexttv.setVisibility(View.VISIBLE);
                firsttv.setOnClickListener(this);
                secondtv.setOnClickListener(this);
                break;

            case 3:
                firsttv.setVisibility(View.VISIBLE);
                secondtv.setVisibility(View.VISIBLE);
                thirdtv.setVisibility(View.VISIBLE);
                nexttv.setVisibility(View.VISIBLE);
                firsttv.setOnClickListener(this);
                secondtv.setOnClickListener(this);
                thirdtv.setOnClickListener(this);
                break;

            case 4 :
                firsttv.setVisibility(View.VISIBLE);
                secondtv.setVisibility(View.VISIBLE);
                thirdtv.setVisibility(View.VISIBLE);
                fourthtv.setVisibility(View.VISIBLE);
                nexttv.setVisibility(View.VISIBLE);
                firsttv.setOnClickListener(this);
                secondtv.setOnClickListener(this);
                thirdtv.setOnClickListener(this);
                fourthtv.setOnClickListener(this);
                break;

        }


    }


    private void setRecyclerViewAdapter() {

        layoutManager = new LinearLayoutManager(GetUsersActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new UserAdapter(GetUsersActivity.this, userModelList);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.linear_logout :
                CallLogOutApi();
                break;
            case R.id.first :
                 countimitial=0;
                 countfinal=5;
                 GetUsersListApi();
                 break;
            case R.id.second :
                 countimitial=5;
                 countfinal=5;
                GetUsersListApi();
                break;
            case R.id.third :
                countimitial=10;
                countfinal=5;
                GetUsersListApi();
                break;
            case R.id.fourth :
                countimitial=15;
                countfinal=5;
                GetUsersListApi();
                break;


        }

    }

    private void CallLogOutApi() {
        customProgressDialogTwo.show();
        stringRequest = new StringRequest(Request.Method.DELETE, Config.LOGIN_API + "?token=" + pref.getToken(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                customProgressDialogTwo.dismiss();
                Log.e("Response_logout", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("code").equals("200")) {


                        Intent intent = new Intent(GetUsersActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                        Toast.makeText(GetUsersActivity.this, "Logout Successful", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(GetUsersActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
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
                            } else if (networkResponse.statusCode == 401) {
                                Toast.makeText(GetUsersActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();

                            } else if (error instanceof TimeoutError) {
                                customProgressDialogTwo.dismiss();
                                Toast.makeText(getApplicationContext(), "Connnection TimeOut", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }) {


        };
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(mRetryPolicy);
        VolleySingleton.getInstance(GetUsersActivity.this).addToRequestQueue(stringRequest);


    }

}
