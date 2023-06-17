package sg.edu.np.mad.pawgress.Tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;

import sg.edu.np.mad.pawgress.MyDBHandler;
import sg.edu.np.mad.pawgress.R;
import sg.edu.np.mad.pawgress.UserData;

public class CreateTask extends AppCompatActivity {

    MyDBHandler myDBHandler = new MyDBHandler(this,null,null,1);
    String title = "Create Task";
    // is there a limit to how many tasks users can have at one time?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);
        Log.i(title, "Creating task");

        Intent receivingEnd = getIntent();
        UserData user = receivingEnd.getParcelableExtra("User");
        ArrayList<Task> taskList = myDBHandler.findTaskList(user);
        Button createButton = findViewById(R.id.button6);
        Button cancelButton = findViewById(R.id.button5);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etname = findViewById(R.id.editTitle);
                String name = etname.getText().toString();
                EditText etcat = findViewById(R.id.editCat);
                String cat = etcat.getText().toString();
                Task task = new Task(1, name, "In Progress", cat);
                myDBHandler.addTask(task, user);
                Intent newTask = new Intent(CreateTask.this, TaskList.class);
                newTask.putExtra("New Task List", user);
                startActivity(newTask);
                Log.i(title, "task added");
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(title, "discarding");
                Intent back = new Intent(CreateTask.this, TaskList.class);
                back.putExtra("New Task List", user);
                startActivity(back);
                finish();
            }
        });
    }
}