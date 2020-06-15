package com.Quant.quantmasters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Shreyas on 09-04-2017.
 */


class Navigation_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Navigation_List> navigation_lists;
    private Context context;
    private ProgressDialog pDialog;
    private DatabaseHandler db;
    private String email;
    private SessionManager session;

    Navigation_Adapter(Context context, List<Navigation_List> navigation_lists) {
        this.navigation_lists = navigation_lists;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_list, parent, false);

        return new Navigation_Card(view);

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        session = new SessionManager(context);
        pDialog = new ProgressDialog(context);
        db = new DatabaseHandler(context);
        pDialog.setCancelable(false);
        final Navigation_List feeds = navigation_lists.get(position);
        ((Navigation_Card) holder).List.setText(feeds.Get_Menu());

        ((Navigation_Card) holder).image.setImageResource(feeds.Get_Image());
        Typeface fontz = Typeface.createFromAsset(context.getAssets(), "SourceSansProRegular.otf");
        Typeface bold = Typeface.createFromAsset(context.getAssets(), "SourceSansProBold.otf");
        ((Navigation_Card) holder).List.setTypeface(fontz);

        TextPaint paints = ((Navigation_Card) holder).List.getPaint();
        float widths = paints.measureText("Quant Masters");
        Shader textShaders = new LinearGradient(0, 0, widths, ((Navigation_Card) holder).List.getTextSize(),
                new int[]{
                        Color.parseColor("#177ED5"),
                        Color.parseColor("#16B1E9"),
                }, null, Shader.TileMode.CLAMP);
        ((Navigation_Card) holder).List.getPaint().setShader(textShaders);
        ((Navigation_Card) holder).List.setTypeface(fontz);
        HashMap<String, String> user = db.getUserDetails();

        email = user.get("email");
        ((Navigation_Card) holder).List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (feeds.Get_Id()) {
                    case 1:
                        Intent intent = new Intent(context, New_Profile.class);
                        context.startActivity(intent);
                        break;
                    case 2:
                        Intent intent1 = new Intent(context, New_Dashboard.class);
                        context.startActivity(intent1);
                        break;
                    case 3:
                        String url = "https://www.quantmasters.in/home";
                        try {
                            Intent i = new Intent("android.intent.action.MAIN");
                            i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                            i.addCategory("android.intent.category.LAUNCHER");
                            i.setData(Uri.parse(url));
                            context.startActivity(i);
                        } catch (ActivityNotFoundException e) {

                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            context.startActivity(i);
                        }
                        break;
                    case 4:
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                            String shareMessage = "\nLet me recommend you this application\n\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.Quant.QuantMastersTrainingServices";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch (Exception e) {
                            //e.toString();
                        }
                        break;

                    case 5:
                        String urls = "https://play.google.com/store/apps/details?id=com.Quant.QuantMastersTrainingServices";
                        try {
                            Intent i = new Intent("android.intent.action.MAIN");
                            i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                            i.addCategory("android.intent.category.LAUNCHER");
                            i.setData(Uri.parse(urls));
                            context.startActivity(i);
                        } catch (ActivityNotFoundException e) {

                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(urls));
                            context.startActivity(i);
                        }
                        break;

                    case 6:
                        Intent intents = new Intent(context, Payment_Activity.class);
                        context.startActivity(intents);
                        break;
                    case 7:
                        Logout_User(email);
                        break;
                    case 8:

                        new LovelyStandardDialog(context)
                                .setButtonsColorRes(R.color.text_color)
                                .setTitle("Note")
                                .setMessage(R.string.whatsapp)
                                .setPositiveButton("Continue", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String contact = "+91 8217088163"; //
                                        String urlz = "https://api.whatsapp.com/send?phone=" + contact;
                                        try {
                                            PackageManager pm = context.getPackageManager();
                                            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(urlz));
                                            context.startActivity(i);
                                        } catch (PackageManager.NameNotFoundException e) {
                                            Toast.makeText(context, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }

                                    }
                                })
                                .show();


                        break;

                }

            }
        });

        ((Navigation_Card) holder).rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (feeds.Get_Id()) {
                    case 1:
                        Intent intent = new Intent(context, New_Profile.class);
                        context.startActivity(intent);
                        break;
                    case 2:
                        Intent intent1 = new Intent(context, New_Dashboard.class);
                        context.startActivity(intent1);
                        break;
                    case 3:
                        String url = "https://www.quantmasters.in/home";
                        try {
                            Intent i = new Intent("android.intent.action.MAIN");
                            i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                            i.addCategory("android.intent.category.LAUNCHER");
                            i.setData(Uri.parse(url));
                            context.startActivity(i);
                        } catch (ActivityNotFoundException e) {

                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            context.startActivity(i);
                        }
                        break;
                    case 4:
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                            String shareMessage = "\nLet me recommend you this application\n\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.Quant.QuantMastersTrainingServices";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch (Exception e) {
                            //e.toString();
                        }
                        break;
                    case 5:
                        String urls = "https://play.google.com/store/apps/details?id=com.Quant.QuantMastersTrainingServices";
                        try {
                            Intent i = new Intent("android.intent.action.MAIN");
                            i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                            i.addCategory("android.intent.category.LAUNCHER");
                            i.setData(Uri.parse(urls));
                            context.startActivity(i);
                        } catch (ActivityNotFoundException e) {

                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(urls));
                            context.startActivity(i);
                        }
                        break;
                    case 6:
                        Intent intents = new Intent(context, Payment_Activity.class);
                        context.startActivity(intents);
                        break;
                    case 7:
                        Logout_User(email);
                        break;

                    case 8:


                        new LovelyStandardDialog(context)
                                .setButtonsColorRes(R.color.text_color)
                                .setTitle("Note")
                                .setMessage(R.string.whatsapp)
                                .setPositiveButton("Continue", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String contact = "+91 8217088163"; //
                                        String urlz = "https://api.whatsapp.com/send?phone=" + contact;
                                        try {
                                            PackageManager pm = context.getPackageManager();
                                            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(urlz));
                                            context.startActivity(i);
                                        } catch (PackageManager.NameNotFoundException e) {
                                            Toast.makeText(context, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }

                                    }
                                })
                                .show();


                        break;
                }

            }
        });


        ((Navigation_Card) holder).image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (feeds.Get_Id()) {
                    case 1:
                        Intent intent = new Intent(context, New_Profile.class);
                        context.startActivity(intent);
                        break;
                    case 2:
                        Intent intent1 = new Intent(context, New_Dashboard.class);
                        context.startActivity(intent1);
                        break;
                    case 3:
                        String url = "https://www.quantmasters.in/home";
                        try {
                            Intent i = new Intent("android.intent.action.MAIN");
                            i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                            i.addCategory("android.intent.category.LAUNCHER");
                            i.setData(Uri.parse(url));
                            context.startActivity(i);
                        } catch (ActivityNotFoundException e) {

                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            context.startActivity(i);
                        }
                        break;
                    case 4:
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                            String shareMessage = "\nLet me recommend you this application\n\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.Quant.QuantMastersTrainingServices";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch (Exception e) {
                            //e.toString();
                        }
                        break;
                    case 5:
                        String urls = "https://play.google.com/store/apps/details?id=com.Quant.QuantMastersTrainingServices";
                        try {
                            Intent i = new Intent("android.intent.action.MAIN");
                            i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                            i.addCategory("android.intent.category.LAUNCHER");
                            i.setData(Uri.parse(urls));
                            context.startActivity(i);
                        } catch (ActivityNotFoundException e) {

                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(urls));
                            context.startActivity(i);
                        }
                        break;
                    case 6:
                        Intent intents = new Intent(context, Payment_Activity.class);
                        context.startActivity(intents);
                        break;
                    case 7:
                        Logout_User(email);
                        break;
                    case 8:


                        new LovelyStandardDialog(context)
                                .setButtonsColorRes(R.color.text_color)
                                .setTitle("Note")
                                .setMessage(R.string.whatsapp)
                                .setPositiveButton("Continue", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String contact = "+91 8217088163"; //
                                        String urlz = "https://api.whatsapp.com/send?phone=" + contact;
                                        try {
                                            PackageManager pm = context.getPackageManager();
                                            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(urlz));
                                            context.startActivity(i);
                                        } catch (PackageManager.NameNotFoundException e) {
                                            Toast.makeText(context, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }

                                    }
                                })
                                .show();


                        break;
                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return navigation_lists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    private static class Navigation_Card extends RecyclerView.ViewHolder {


        TextView List;
        ImageView image;
        RelativeLayout List_Layout, rel;


        Navigation_Card(View itemView) {

            super(itemView);


            List_Layout = (RelativeLayout) itemView.findViewById(R.id.List_Layout);
            image = (ImageView) itemView.findViewById(R.id.image);
            rel = (RelativeLayout) itemView.findViewById(R.id.rel);
            List = (TextView) itemView.findViewById(R.id.List);


        }
    }


    private void Logout_User(final String email) {
        pDialog.setMessage("Logging Out ...");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.quantmasters.in/user/logout",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jObj = new JSONObject(response);
                            if (jObj.has("text")) {
                                String text = jObj.getString("text");

                                session.setLogin(false);
                                db.deleteall();
                                hideDialog();
                                Intent intent = new Intent(context, First_Activity.class);
                                context.startActivity(intent);
                                ((Activity) context).finish();


                            } else {
                                hideDialog();
                                new LovelyStandardDialog(context)
                                        .setButtonsColorRes(R.color.text_color)
                                        .setTitle("Error")
                                        .setMessage("Something Went Wrong...Retry")
                                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Logout_User(email);
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
                        Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                return params;
            }

        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}