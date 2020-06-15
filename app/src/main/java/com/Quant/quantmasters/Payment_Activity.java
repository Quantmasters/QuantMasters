package com.Quant.quantmasters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Payment_Activity extends AppCompatActivity implements PaymentResultListener {
    RecyclerView recycler_views;
    ProgressBar progressBar;
    private static final String TAG = Payment_Activity.class.getSimpleName();
    String email;
    private DatabaseHandler db;
    private ProgressDialog pDialog;
    String token, orderId;
    private SessionManager session;
    TextView buy_now;
    RelativeLayout button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_activity);
         buy_now = findViewById(R.id.buy_now);
        button = findViewById(R.id.button);
        db = new DatabaseHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String id = user.get("id");
        String names = user.get("name");
        email = user.get("email");
        token = user.get("token");
        pDialog = new ProgressDialog(this);
        ImageView packege_details = findViewById(R.id.packege_details);
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);

      button.setVisibility(View.VISIBLE);

        Glide.with(getApplication())
                .load("https://www.quantmasters.in/assets/paid_plans.png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        
                        return false;
                    }
                })
                .into(packege_details);




        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pDialog.setCancelable(false);
                pDialog.setMessage("Initiating Payment..");
                showDialog();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/product/course/quote",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jObj = new JSONObject(response);
                                    if (jObj.has("amount")) {
                                        int amount = jObj.getInt("amount");
                                        amount = amount * 100;
                                        Start(String.valueOf(amount));
                                    } else {
                                        hideDialog();
                                        new LovelyStandardDialog(Payment_Activity.this)
                                                .setButtonsColorRes(R.color.text_color)
                                                .setTitle("Error")
                                                .setMessage("Something Went Wrong...Retry")
                                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                    }
                                                })
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                hideDialog();
                                new LovelyStandardDialog(Payment_Activity.this)
                                        .setButtonsColorRes(R.color.text_color)
                                        .setTitle("Error")
                                        .setMessage("Something Went Wrong..Please Retry")
                                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        })
                                        .show();
                            }
                        }) {


                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headerMap = new HashMap<String, String>();
                        headerMap.put("Authorization", "Bearer " + token);
                        return headerMap;
                    }
                };
                int socketTimeout = 30000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);


            }
        });


    }

    public void startPayment() {

        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();

            options.put("name", "Quant Masters");
            options.put("description", "Course Fee");
            options.put("order_id", orderId);
            options.put("email", email);
            options.put("currency", "INR");
            JSONObject preFill = new JSONObject();

            options.put("prefill", preFill);
            co.open(activity, options);
            hideDialog();
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        new LovelyStandardDialog(Payment_Activity.this)
                .setButtonsColorRes(R.color.text_color)
                .setTitle("Cancel Payment?")
                .setCancelable(false)
                .setMessage("Are you sure you want cancel?")
                .setPositiveButton("No", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setNegativeButton("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .show();

    }


    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Payment_verification(razorpayPaymentID);
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
            finish();
        }
    }


    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            new LovelyStandardDialog(Payment_Activity.this)
                    .setButtonsColorRes(R.color.text_color)
                    .setTitle("Cancel Payment?")
                    .setCancelable(false)
                    .setMessage("Are you sure you want cancel?")
                    .setPositiveButton("No", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setNegativeButton("Yes", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })
                    .show();

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
            finish();
        }
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    public void Payment_verification(final String PayId) {
        pDialog.setMessage("Confirming..");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.quantmasters.in/product/payment/confirm",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("REZ", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            if (jObj.has("msg")) {
                                Update(email);

                            } else {
                                new LovelyStandardDialog(Payment_Activity.this)
                                        .setButtonsColorRes(R.color.text_color)
                                        .setTitle("Error")
                                        .setMessage("Something Went Wrong")
                                        .setCancelable(false)
                                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(Payment_Activity.this, New_Main_Activity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        new LovelyStandardDialog(Payment_Activity.this)
                                .setButtonsColorRes(R.color.text_color)
                                .setTitle("Error")
                                .setCancelable(false)
                                .setMessage("Something Went Wrong..Please Retry")
                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Payment_verification(PayId);
                                    }
                                })
                                .show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("amount", "250000");
                params.put("paymentId", PayId);
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Authorization", "Bearer " + token);
                return headerMap;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void Update(final String email) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/login/" + email + "/refresh",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            Log.d("Login", response);
                            if (jObj.has("token")) {
                                String token = jObj.getString("token");
                                Log.d("new token", token);
                                String subscribed = jObj.getString("subscribed");
                                db.updateToken(token, email,subscribed);
                                session.setLogin(true);
                                hideDialog();
                                Intent intent = new Intent(Payment_Activity.this, Payment_Done.class);
                                startActivity(intent);
                                finish();

                            } else {
                                new LovelyStandardDialog(Payment_Activity.this)
                                        .setButtonsColorRes(R.color.text_color)
                                        .setTitle("Oops!!!")
                                        .setCancelable(false)
                                        .setMessage("Something Went Wrong..Please Retry")
                                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                HashMap<String, String> user = db.getUserDetails();
                                                String email = user.get("email");
                                                Update(email);
                                            }
                                        })
                                        .show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        new LovelyStandardDialog(Payment_Activity.this)
                                .setButtonsColorRes(R.color.text_color)
                                .setTitle("Oops!!!")
                                .setCancelable(false)
                                .setMessage("Something Went Wrong..Please Retry")
                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        HashMap<String, String> user = db.getUserDetails();
                                        String email = user.get("email");
                                        Update(email);
                                    }
                                })
                                .show();
                    }
                });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void Start(final String amount) {
        pDialog.setMessage("Initiating Payment..");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.quantmasters.in/product/order",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);

                            Log.d("res", response);
                            if (jObj.has("orderId")) {
                                orderId = jObj.getString("orderId");
                                startPayment();
                            } else {
                                new LovelyStandardDialog(Payment_Activity.this)
                                        .setButtonsColorRes(R.color.text_color)
                                        .setTitle("Error")
                                        .setCancelable(false)
                                        .setMessage("Something Went Wrong")
                                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new LovelyStandardDialog(Payment_Activity.this)
                                .setButtonsColorRes(R.color.text_color)
                                .setTitle("Error")
                                .setCancelable(false)
                                .setMessage("Something Went Wrong...Retry")
                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                       Start(amount);
                                    }
                                })
                                .show();
                        hideDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("amount", amount);
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("Authorization", "Bearer " + token);
                return headerMap;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}