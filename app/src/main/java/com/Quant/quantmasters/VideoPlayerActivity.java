package com.Quant.quantmasters;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ankushgrover.hourglass.Hourglass;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class VideoPlayerActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {
    ProgressBar progressBar;
    SurfaceView videoSurface;
    MediaPlayer player;
    int rating_val;
    String videoid;
    VideoControllerView controller;
    private SessionManager session;
    private DatabaseHandler db;
    private ProgressDialog pDialog;
    String link, token, type;
    int rate_flag = 0;
    AlertDialog alertDialog;
    int w, h;
    long Total_Time;
    long Remaing_Time = 0;


    Hourglass hourglass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        Intent intent = getIntent();
        videoid = Objects.requireNonNull(intent.getExtras()).getString("v_id");
        link = intent.getExtras().getString("v_link");
        type = intent.getExtras().getString("type");
        pDialog = new ProgressDialog(this);

        db = new DatabaseHandler(getApplicationContext());
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        session = new SessionManager(getApplicationContext());
        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        player = new MediaPlayer();
        controller = new VideoControllerView(this);


        if (session.isLoggedIn()) {
            HashMap<String, String> user = db.getUserDetails();
            token = user.get("token");
            String email = user.get("email");
            Log.d("link", link);
            Check_Rated(videoid, email);



            try {
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(this, Uri.parse(link));
                player.setOnPreparedListener(this);
                 hourglass = new Hourglass(18000000, 1000) {
                    @Override
                    public void onTimerTick(long timeRemaining) {
                     //   Log.d("times",""+String.format("%02d:%02d:%02d",
                              //  TimeUnit.MILLISECONDS.toHours(timeRemaining),
                             //   TimeUnit.MILLISECONDS.toMinutes(timeRemaining) -
                              //          TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeRemaining)),
                              //  TimeUnit.MILLISECONDS.toSeconds(timeRemaining) -
                                //        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeRemaining))));

                        Remaing_Time = timeRemaining;
                       // Log.d("remian",String.valueOf(timeRemaining));


                    }

                    @Override
                    public void onTimerFinish() {
                        // Timer finished



                    }
                };



            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        player.pause();
      //  player.release();
    }


    @Override
    public void onBackPressed() {

        player.pause();



        HashMap<String, String> user = db.getUserDetails();
        String email = user.get("email");

        Total_Time = 18000000 - Remaing_Time;
        Date date2 = new Date(Total_Time);

        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss", Locale.US);
        formatter2.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formatted2 = formatter2.format(date2);
        Log.d("total time watched",formatted2);
        Date date = new Date(player.getCurrentPosition());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formatted = formatter.format(date);

        Track_Video(videoid,email,formatted2,formatted);



        if (!player.isPlaying() && rate_flag == 0 && type.equals("group")) {

            ViewGroup viewGroup = findViewById(android.R.id.content);
            Typeface typeface = Typeface.createFromAsset(getAssets(), "SourceSansProBold.otf");
            Typeface typeface1 = Typeface.createFromAsset(getAssets(), "sourcesans.otf");
            Typeface typeface2 = Typeface.createFromAsset(getAssets(), "SourceSansProRegular.otf");
            //then we will inflate the custom alert dialog xml that we created
            View dialogView = LayoutInflater.from(this).inflate(R.layout.rate_dialog, viewGroup, false);
            final ImageView star1 = dialogView.findViewById(R.id.star1);
            final ImageView star2 = dialogView.findViewById(R.id.star2);
            final ImageView star3 = dialogView.findViewById(R.id.star3);
            final ImageView star4 = dialogView.findViewById(R.id.star4);
            final ImageView star5 = dialogView.findViewById(R.id.star5);
            TextView later = dialogView.findViewById(R.id.later);
            final TextView submit = dialogView.findViewById(R.id.submit);
            TextView rate_text = dialogView.findViewById(R.id.rate_text);
            TextView title = dialogView.findViewById(R.id.title);

            title.setTypeface(typeface);
            rate_text.setTypeface(typeface1);
            submit.setTypeface(typeface2);
            later.setTypeface(typeface2);

            later.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    finish();
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rating_val != 0) {
                        HashMap<String, String> user = db.getUserDetails();
                        final String email = user.get("email");
                        Rate_Video(videoid, email, String.valueOf(rating_val));
                    }
                }
            });


            star1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    star1.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.like), PorterDuff.Mode.SRC_IN);
                    star2.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                    star3.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                    star4.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                    star5.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                    // 10b483
                    submit.setTextColor(Color.parseColor("#10b483"));
                    rating_val = 1;
                }
            });

            star2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    star1.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.like), PorterDuff.Mode.SRC_IN);
                    star2.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.like), PorterDuff.Mode.SRC_IN);
                    star3.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                    star4.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                    star5.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                    rating_val = 2;
                    submit.setTextColor(Color.parseColor("#10b483"));
                }
            });

            star3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    star1.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.like), PorterDuff.Mode.SRC_IN);
                    star2.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.like), PorterDuff.Mode.SRC_IN);
                    star3.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.like), PorterDuff.Mode.SRC_IN);
                    star4.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                    star5.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                    submit.setTextColor(Color.parseColor("#10b483"));
                    rating_val = 3;
                }
            });

            star4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    star1.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.like), PorterDuff.Mode.SRC_IN);
                    star2.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.like), PorterDuff.Mode.SRC_IN);
                    star3.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.like), PorterDuff.Mode.SRC_IN);
                    star4.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.like), PorterDuff.Mode.SRC_IN);
                    star5.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.unlike), PorterDuff.Mode.SRC_IN);
                    submit.setTextColor(Color.parseColor("#10b483"));
                    rating_val = 4;
                }
            });
            star5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    star1.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.like), PorterDuff.Mode.SRC_IN);
                    star2.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.like), PorterDuff.Mode.SRC_IN);
                    star3.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.like), PorterDuff.Mode.SRC_IN);
                    star4.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.like), PorterDuff.Mode.SRC_IN);
                    star5.setColorFilter(ContextCompat.getColor(VideoPlayerActivity.this, R.color.like), PorterDuff.Mode.SRC_IN);
                    submit.setTextColor(Color.parseColor("#10b483"));
                    rating_val = 5;
                }
            });


            //Now we need an AlertDialog.Builder object
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            //setting the view of the builder to our custom view that we already inflated
            builder.setView(dialogView);

            //finally creating the alert dialog and displaying it
            alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setCancelable(false);
        } else {
            finish();
        }



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    // Implement SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        player.prepareAsync();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    @Override
    public void onPrepared(MediaPlayer mp) {

        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        player.start();
        hourglass.startTimer();
        progressBar.setVisibility(View.GONE);

        int videoWidth = player.getVideoWidth();
        int videoHeight = player.getVideoHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;
        android.view.ViewGroup.LayoutParams lp = videoSurface.getLayoutParams();

        if (videoProportion > screenProportion) {
            w = screenWidth;
            h = (int) ((float) screenWidth / videoProportion);

            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
        } else {
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
            w = (int) (videoProportion * (float) screenHeight);
            h = screenHeight;
        }
        videoSurface.setLayoutParams(lp);

        if (!player.isPlaying()) {

            player.start();
        }




    }
    // End MediaPlayer.OnPreparedListener

    // Implement VideoMediaController.MediaPlayerControl
    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void pause() {
        hourglass.pauseTimer();
        player.pause();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public void start() {
        hourglass.resumeTimer();
        player.start();
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // Here you should do the same check with MediaRecorder.
        // And you can be sure that user does not
        // start audio recording through notifications.
        // Or user stops recording through notifications.
    }


    @Override
    public void toggleFullScreen() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            int videoWidth = w;
            int videoHeight = h;
            android.view.ViewGroup.LayoutParams lp = videoSurface.getLayoutParams();


            lp.width = videoWidth;
            lp.height = videoHeight;

            videoSurface.setLayoutParams(lp);


        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


            int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
            int screenHeight = getWindowManager().getDefaultDisplay().getHeight();

            android.view.ViewGroup.LayoutParams lp = videoSurface.getLayoutParams();


            lp.width = 1779;
            lp.height = 1000;

            videoSurface.setLayoutParams(lp);

            if (!player.isPlaying()) {
                player.start();
            }
        }
    }


    public void Rate_Video(final String video_id, final String email, final String rate_count) {
        pDialog.setMessage("Please Wait..");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.quantmasters.in/learn/video/" + video_id + "/rating",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("REZ", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            if (jObj.has("msg")) {
                                hideDialog();
                                if (jObj.getString("msg").equals("rating added")) {
                                    alertDialog.cancel();
                                    new LovelyStandardDialog(VideoPlayerActivity.this)
                                            .setButtonsColorRes(R.color.text_color)
                                            .setTitle("Thank You")
                                            .setMessage("Thank you for rating this video")
                                            .setCancelable(false)
                                            .setPositiveButton("Close", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    finish();
                                                }
                                            })
                                            .show();

                                } else {
                                    new LovelyStandardDialog(VideoPlayerActivity.this)
                                            .setButtonsColorRes(R.color.text_color)
                                            .setTitle("Error")
                                            .setMessage("Something Went Wrong..Please Retry")
                                            .setCancelable(false)
                                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(VideoPlayerActivity.this, New_Main_Activity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .show();
                                }


                            } else {
                                hideDialog();
                                new LovelyStandardDialog(VideoPlayerActivity.this)
                                        .setButtonsColorRes(R.color.text_color)
                                        .setTitle("Error")
                                        .setMessage("Something Went Wrong..Please Retry")
                                        .setCancelable(false)
                                        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(VideoPlayerActivity.this, New_Main_Activity.class);
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
                        new LovelyStandardDialog(VideoPlayerActivity.this)
                                .setButtonsColorRes(R.color.text_color)
                                .setTitle("Error")
                                .setCancelable(false)
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("rating_given", rate_count);
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


    public void Check_Rated(final String video_id, final String email) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.quantmasters.in/learn/video/" + video_id + "/rating/" + email + "/check",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("checking", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            if (jObj.has("rating_given")) {

                                if (jObj.getString("rating_given").equals("0")) {

                                    rate_flag = 0;
                                } else {
                                    rate_flag = 1;
                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("Error", "Server Error");
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


    public void Track_Video(final String video_id, final String email, final String totallength, final String Lastwatch) {
        Log.d("data", totallength + " len " + Lastwatch);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.quantmasters.in/track/video/usage",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("trackZ", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            if (jObj.has("msg")) {

                                if (jObj.getString("msg").equals("Recorded")) {
                                    Log.d("error", "success");

                                } else {
                                    Log.d("error", "error");
                                }


                            } else {
                                Log.d("error", "error");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", "error");

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("videoId", video_id);
                params.put("email", email);
                params.put("totalLength", totallength);
                params.put("lastWatch", Lastwatch);
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}