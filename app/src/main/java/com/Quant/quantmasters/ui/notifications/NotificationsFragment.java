package com.Quant.quantmasters.ui.notifications;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.Quant.quantmasters.Edit_Profile;
import com.Quant.quantmasters.Profile_Image;
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
import com.Quant.quantmasters.DatabaseHandler;
import com.Quant.quantmasters.First_Activity;
import com.Quant.quantmasters.Login_Activity;
import com.Quant.quantmasters.R;
import com.Quant.quantmasters.SessionManager;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsFragment extends Fragment {
    private ProgressDialog pDialog;
    private SessionManager session;
    private EditText inputEmail, inputPassword;
    TextView title, sub_title;
    private CircleImageView image_pic;
    String email;
    ImageButton back;
    private RelativeLayout logout, edit_rel, rello;
    private TextView profile_text, name, support, share, privacy, update, about, website_text, linkedin_text, facebook_text, title_note, profile_pic_two;
    ;
    DatabaseHandler db;
    LinearLayout facebook, linkedin, website;
    private NotificationsViewModel notificationsViewModel;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "ImagePref";
    public static final String IMAGE = "bitmap";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "SourceSansProBold.otf");
        Typeface typeface2 = Typeface.createFromAsset(getContext().getAssets(), "SourceSansProRegular.otf");
        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        name = root.findViewById(R.id.Name);
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        logout = root.findViewById(R.id.logout);
        session = new SessionManager(getContext());
        db = new DatabaseHandler(getContext());
        HashMap<String, String> user = db.getUserDetails();
        email = user.get("email");
        rello = root.findViewById(R.id.rello);
        image_pic = root.findViewById(R.id.image_pic);
        profile_pic_two = root.findViewById(R.id.profile_pic_two);


        website_text = root.findViewById(R.id.website_text);
        linkedin_text = root.findViewById(R.id.linkedin_text);
        facebook_text = root.findViewById(R.id.facebook_text);
        title_note = root.findViewById(R.id.title_note);


        facebook = root.findViewById(R.id.facebook);
        linkedin = root.findViewById(R.id.linkedin);
        website = root.findViewById(R.id.website);

        support = root.findViewById(R.id.support);
        share = root.findViewById(R.id.share);
        privacy = root.findViewById(R.id.privacy);
        update = root.findViewById(R.id.update);
        about = root.findViewById(R.id.about);


        website_text.setTypeface(typeface2);
        linkedin_text.setTypeface(typeface2);
        facebook_text.setTypeface(typeface2);
        name.setTypeface(typeface);

        support.setTypeface(typeface2);
        share.setTypeface(typeface2);
        privacy.setTypeface(typeface2);
        update.setTypeface(typeface2);
        about.setTypeface(typeface2);


        edit_rel = root.findViewById(R.id.edit_rel);

         profile_pic_two.setOnClickListener(new View.OnClickListener() {
          @Override
             public void onClick(View view) {
              Intent intent = new Intent(getContext(), Edit_Profile.class);
              startActivity(intent);

              }
           });

        // image_pic.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //       Intent intent = new Intent(getContext(), Profile_Image.class);
        //      intent.putExtra("image_link", "https://api.quantmasters.in/user/"+email+"/profile/image");
        //      startActivity(intent);

        //     }
        //  });


         edit_rel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                Intent intent = new Intent(getContext(), Edit_Profile.class);
                startActivity(intent);
            }
        });


        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.linkedin.com/in/himanshu-sharma-4626268b/";
                try {
                    Intent i = new Intent("android.intent.action.MAIN");
                    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                    i.addCategory("android.intent.category.LAUNCHER");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {

                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
            }
        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.quantmasters.in/home";
                try {
                    Intent i = new Intent("android.intent.action.MAIN");
                    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                    i.addCategory("android.intent.category.LAUNCHER");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {

                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
            }
        });


        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.quantmasters.in/home";
                try {
                    Intent i = new Intent("android.intent.action.MAIN");
                    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                    i.addCategory("android.intent.category.LAUNCHER");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {

                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
            }
        });


        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.quantmasters.in/home";
                try {
                    Intent i = new Intent("android.intent.action.MAIN");
                    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                    i.addCategory("android.intent.category.LAUNCHER");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {

                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.quantmasters.in/home";
                try {
                    Intent i = new Intent("android.intent.action.MAIN");
                    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                    i.addCategory("android.intent.category.LAUNCHER");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {

                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.BluCoastInnovations.Envision2k19";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://play.google.com/store/apps/details?id=com.Quant.QuantMastersTrainingServices";
                try {
                    Intent i = new Intent("android.intent.action.MAIN");
                    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                    i.addCategory("android.intent.category.LAUNCHER");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {

                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/pg/Quant-Masters-239562816724244/";
                try {
                    Intent i = new Intent("android.intent.action.MAIN");
                    i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                    i.addCategory("android.intent.category.LAUNCHER");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (ActivityNotFoundException e) {

                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
            }
        });


        if (session.isLoggedIn()) {

            profile_pic_two.setTypeface(typeface2);
            String id = user.get("id");
            String names = user.get("name");
            String token = user.get("token");
            email = user.get("email");
            Log.d("res", names);
            char first = names.charAt(0);
            profile_pic_two.setText(String.valueOf(first));
            name.setText(names);
            profile_pic_two.setVisibility(View.VISIBLE);

            // if (sharedpreferences.contains(IMAGE)) {
            //   image_pic.setImageBitmap(StringToBitMap(sharedpreferences.getString(IMAGE, "")));
            //   Glide.with(getContext())
            //          .load(sharedpreferences.getString(IMAGE, ""))
            //         .centerCrop()
            //         .skipMemoryCache(true)
            //         .override(500,500)
            //         .into(image_pic);
            //   profile_pic_two.setVisibility(View.INVISIBLE);
            // }else{
            //     image_pic.setVisibility(View.INVISIBLE);
            //      profile_pic_two.setVisibility(View.VISIBLE);
            //  }


            title_note.setText("SIGN OUT");

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Logout_User(email);
                }
            });


        } else {
            rello.setVisibility(View.GONE);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), Login_Activity.class);
                    startActivity(intent);

                }
            });

            title_note.setText("SIGN IN");
        }


        back = root.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });


        return root;
    }


    private void Logout_User(final String email) {
        pDialog.setMessage("Logging Out ...");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.quantmasters.in/user/logout",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            if (jObj.has("text")) {
                                String text = jObj.getString("text");

                                session.setLogin(false);
                                db.deleteall();
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.clear();
                                editor.apply();
                                Intent intent = new Intent(getContext(), First_Activity.class);
                                startActivity(intent);
                                Objects.requireNonNull(getActivity()).finish();


                            } else {
                                new LovelyStandardDialog(getContext())
                                        .setButtonsColorRes(R.color.red)
                                        .setTitle("Error")
                                        .setMessage("Oops!! Something Went Wrong.")
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
                        Log.d("res", String.valueOf(error));

                        hideDialog();
                        new LovelyStandardDialog(getContext())
                                .setButtonsColorRes(R.color.red)
                                .setTitle("Error")
                                .setMessage("Oops!! Something Went Wrong.")
                                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                })
                                .show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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


    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}