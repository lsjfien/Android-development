package sg.edu.np.mad.pawgress.Tasks;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.SaveSharedPreference;
import sg.edu.np.mad.pawgress.UserData;

public class TaskGame extends AppCompatActivity {

    private int seconds = 0;
    private boolean running;
    private boolean wasRunning;

    private ImageButton buttonStart;
    private ImageButton buttonReset;
    private Button buttonFinish;

    private TextView timeView;
    private Handler handler;
    MyDBHandler myDBHandler = new MyDBHandler(this, null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_game);

        MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);
        UserData user1 = myDBHandler.findUser(SaveSharedPreference.getUserName(this));
        ImageView pet_picture = findViewById(R.id.corgi_1);
        if (user1.getPetDesign() == R.drawable.grey_cat){pet_picture.setImageResource(R.drawable.grey_cat);}
        else if (user1.getPetDesign() == R.drawable.orange_cat){pet_picture.setImageResource(R.drawable.orange_cat);}
        else if (user1.getPetDesign() == R.drawable.grey_cat){pet_picture.setImageResource(R.drawable.corgi);}
        else{pet_picture.setImageResource(R.drawable.golden_retriever);}
        pet_picture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        int random = new Random().nextInt(3);
                        MediaPlayer mediaPlayer;

                        if (random == 0){
                            mediaPlayer = MediaPlayer.create(TaskGame.this, R.raw.corgi_down_sound);
                        } else if (random == 1) {
                            mediaPlayer = MediaPlayer.create(TaskGame.this, R.raw.corgi_up_sound);
                        }
                        else {
                            mediaPlayer = MediaPlayer.create(TaskGame.this, R.raw.corgi_3_sound);
                        }

                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                mp.reset();
                                mp.release();
                            };
                        });
                        Animation anim = new ScaleAnimation(
                                1f, 1f, // Start and end values for the X axis scaling
                                1f, 0.85f, // Start and end values for the Y axis scaling
                                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
                        anim.setFillAfter(true); // Needed to keep the result of the animation
                        anim.setDuration(150);
                        v.startAnimation(anim);

                        break;

                    case MotionEvent.ACTION_UP:
                        Animation anim2 = new ScaleAnimation(
                                1f, 1f, // Start and end values for the X axis scaling
                                0.85f, 1f, // Start and end values for the Y axis scaling
                                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
                        anim2.setFillAfter(true); // Needed to keep the result of the animation
                        anim2.setDuration(150);
                        v.startAnimation(anim2);
                        break;
                }

                return true;
            }
        });

        Intent receivingEnd = getIntent();
        UserData user = receivingEnd.getParcelableExtra("User");
        Task task = receivingEnd.getParcelableExtra("Task");
        ArrayList<Task> taskList = myDBHandler.findTaskList(user);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonStart = findViewById(R.id.start_timer_imagebutton);
        buttonReset = findViewById(R.id.reset_timer_imagebutton);
        buttonFinish = findViewById(R.id.finish_timer);
        timeView = findViewById(R.id.text_view_Countdown);

        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        handler = new Handler();
        runTimer();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TIMER", "Start/Stop button has been pressed!");
                if (running) {
                    pauseTimer();
                } else {
                    startTimer();
                }
                updateButtonUI();
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TIMER", "Reset button has been pressed!");
                if (!running){
                    showResetConfirmationDialog();
                }
            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running) { pauseTimer(); }
                showFinishConfirmationDialog(user, task);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        pauseTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            startTimer();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", seconds);
        outState.putBoolean("running", running);
        outState.putBoolean("wasRunning", wasRunning);
    }

    private void startTimer() {
        running = true;
        wasRunning = true;
    }

    private void pauseTimer() {
        running = false;
    }

    private void resetTimer() {
        running = false;
        seconds = 0;
    }
    private void showResetConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Timer");
        builder.setMessage("Are you sure you want to reset the timer?");

        // Set the positive button and its click listener
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetTimer();
            }
        });

        // Set the negative button and its click listener
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing, return back
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showFinishConfirmationDialog(UserData user, Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Finish Task?");
        builder.setMessage("Has the task been completed?");

        // Set the positive button and its click listener
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent activityName = new Intent(TaskGame.this, TaskCompletion.class);
                activityName.putExtra("User", user);
                activityName.putExtra("seconds", seconds);
                activityName.putExtra("Task", task);
                startActivity(activityName);
            }
        });

        // Set the negative button and its click listener
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!running) { startTimer(); }
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void runTimer() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);

                if (running) {
                    seconds++;
                }

                handler.postDelayed(this, 1000);
            }
        });
    }

    private void updateButtonUI() {
        if (running) {
            buttonStart.setImageResource(R.drawable.baseline_pause_24);
            buttonReset.setAlpha(0.5F);
        }
        else {
            buttonStart.setImageResource(R.drawable.baseline_play_arrow_24);
            buttonReset.setAlpha(1);
        }
    }
}