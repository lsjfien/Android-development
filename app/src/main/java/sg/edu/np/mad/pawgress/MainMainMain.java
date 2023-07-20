package sg.edu.np.mad.pawgress;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import sg.edu.np.mad.pawgress.Fragments.Game.GameFragment;
import sg.edu.np.mad.pawgress.Fragments.Home.HomeFragment;
import sg.edu.np.mad.pawgress.Fragments.Profile.ProfileFragment;
import sg.edu.np.mad.pawgress.Fragments.Tasks.TasksFragment;
import sg.edu.np.mad.pawgress.Tasks.Task;
import sg.edu.np.mad.pawgress.databinding.ActivityMainMainMainBinding;

public class MainMainMain extends AppCompatActivity {
    ActivityMainMainMainBinding binding;
    UserData user;
    public static String NOTIFICATION_CHANNEL_ID = "1001";
    public static String default_notification_id = "default";

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Exit the app
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setting content view
        setContentView(R.layout.activity_main_main_main);
        // setTheme(R.style.Theme_Pawgress_LightBeigeMode);
        // Binding of navigation bar and fragments
        binding = ActivityMainMainMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        scheduleNoonNotification(getNotification("Keep up with the Pawgress, you're halfway through the day! Remember to stay hydrated and on top of tasks!"));
        Log.i("MainMainMain","Notification Scheduled and Created!");

        // Update the bottomNavigationView selection based of page of origin
        Intent receivingEnd = getIntent();
        String tab = receivingEnd.getExtras().getString("tab");
        user = receivingEnd.getParcelableExtra("User");
        Log.i("MainMainMain", "Tab: " + tab);
        if (tab.equals("tasks_tab")){
            replaceFragment(new TasksFragment());

            // Sets navigation bar item to tasks
            binding.bottomNavigationView.setSelectedItemId(R.id.tasks_tab);

        } else if (tab.equals("profile_tab")) {
            replaceFragment(new ProfileFragment());

            // Sets navigation bar item to profile
            binding.bottomNavigationView.setSelectedItemId(R.id.profile_tab);

        } else{
            replaceFragment(new HomeFragment());
        }

        // Switching of navigation bar tabs
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();

            if (itemID == R.id.home_tab){
                //Replace fragment with HomeFragment
                replaceFragment(new HomeFragment());
            }
            if (itemID == R.id.game_tab){
                //Replace fragment with GameFragment
                replaceFragment(new GameFragment());
            }
            if (itemID == R.id.tasks_tab){
                //Replace fragment with TaskFragment
                replaceFragment(new TasksFragment());
            }
            if (itemID == R.id.profile_tab){
                //Replace fragment with ProfileFragment
                replaceFragment(new ProfileFragment());
            }

            return true;
        });
    }

    // Replaces current fragment with new fragment
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pawgress-c1839-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Users");
        UserData fbUser = myDBHandler.findUser(user.getUsername());
        fbUser.setTaskList(myDBHandler.findTaskList(user));
        fbUser.setFriendList(myDBHandler.findFriendList(user));
        fbUser.setFriendReqList(myDBHandler.findFriendReqList(user));

        myRef.child(user.getUsername()).setValue(fbUser);
    }

    private void scheduleNoonNotification(Notification notification) {
        Log.i("MainMainMain", "Received Notification: " + notification);
        // Create an Intent for the MyNotificationPublisher class
        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATIONID, 1);
        notificationIntent.putExtra(MyNotificationPublisher.NOTIFICATION, notification);

        // Use PendingIntent.FLAG_CANCEL_CURRENT to ensure only one pending intent is used
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Add log to inspect the notificationIntent extras
        Bundle extras = notificationIntent.getExtras();
        if (extras != null) {
            for (String key : extras.keySet()) {
                Log.i("MainMainMain", "Extra: " + key + ", Value: " + extras.get(key));
            }
        }

        // Calculate the time for noon
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 40);
        calendar.set(Calendar.SECOND, 0);

        // If the time has already passed today, schedule it for the next day
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Log.i("MainMainMain", "Scheduled Noon Notification time: " + calendar.getTime());

        // Get the AlarmManager and schedule the notification at noon
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }

    private Notification getNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_id);

        builder.setContentTitle("Scheduled Noon Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);

        // Log to check if the notification object is created successfully
        Notification notification = builder.build();
        Log.i("MainMainMain", "Notification: " + notification);

        return builder.build(); // Return the built notification, not the builder itself
    }


}
