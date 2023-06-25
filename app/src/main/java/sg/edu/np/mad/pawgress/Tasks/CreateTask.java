package sg.edu.np.mad.pawgress.Tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import sg.edu.np.mad.pawgress.CreateAccount;
import sg.edu.np.mad.pawgress.Fragments.Home.HomeFragment;
import sg.edu.np.mad.pawgress.MainMainMain;
import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class CreateTask extends AppCompatActivity {
    UserData user;
    MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);
    String title = "Create Task";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);
        Log.i(title, "Creating task");

        Intent receivingEnd = getIntent();
        user = receivingEnd.getParcelableExtra("User");
        Button createButton = findViewById(R.id.button6);
        Button cancelButton = findViewById(R.id.button5);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etname = findViewById(R.id.editTitle);
                String name = etname.getText().toString();
                EditText etcat = findViewById(R.id.editCat);
                // do not accept blank task title or category
                if (etname.length() > 0 && etcat.length() > 0) {
                    String cat = etcat.getText().toString();

                    EditText ethr = findViewById(R.id.editHrs);
                    int hr = 0;

                    try {
                        hr = Integer.parseInt(ethr.getText().toString());
                    } catch (NumberFormatException e) {
                        // do nth
                    }

                    EditText etmin = findViewById(R.id.editMins);
                    int min = 0;

                    try {
                        min = Integer.parseInt(etmin.getText().toString());
                    } catch (NumberFormatException e) {
                        // do nth
                    }

                    EditText etsec = findViewById(R.id.editSec);
                    int sec = 0;

                    try {
                        sec = Integer.parseInt(etsec.getText().toString());
                    } catch (NumberFormatException e) {
                        // do nth
                    }

                    int totalSeconds = (hr * 3600) + (min * 60) + sec;

                    Task task = new Task(1, name, "In Progress", cat ,0, totalSeconds);
                    myDBHandler.addTask(task, user);
                    Intent newTask = new Intent(CreateTask.this, MainMainMain.class);
                    newTask.putExtra("New Task List", user);
                    newTask.putExtra("User", user);
                    newTask.putExtra("tab", "tasks_tab");
                    startActivity(newTask);
                    Log.i(title, "task added");
                    Log.i(title, String.valueOf(totalSeconds));
                    finish();
                }
                else{
                    Toast.makeText(CreateTask.this, "Invalid Title & Category", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTask(user);
            }
        });
    }
    private void cancelTask(UserData user){
        Log.i(title, "discarding");
        Intent back = new Intent(CreateTask.this, MainMainMain.class);
        back.putExtra("New Task List", user);
        back.putExtra("User", user);
        back.putExtra("tab", "tasks_tab");
        startActivity(back);
        finish();
    }

    @Override
    public void onBackPressed(){
        cancelTask(user);
    }
}