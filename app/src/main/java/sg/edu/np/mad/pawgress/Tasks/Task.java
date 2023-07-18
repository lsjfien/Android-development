package sg.edu.np.mad.pawgress.Tasks;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task implements Parcelable {
    private int dailyChallenge;
    private int taskID;
    private String taskName;
    private String status;
    private String category;
    private int timeSpent;
    private int targetSec;
    private String dueDate;
    private String dateCreated;
    private String dateStart;
    private String dateComplete;
    private int priority;

    public Task() {
    }

    // add in date of creation and duedate(can accept null)
    public Task(int taskID, String taskName, String status, String category, int timeSpent, int targetSec, String dueDate, String dateCreated, String dateStart, String dateComplete, int dailyChallenge, int priority) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.status = status;
        this.category = category;
        this.timeSpent = timeSpent;
        this.targetSec = targetSec;
        this.dueDate = dueDate;
        this.dateCreated = dateCreated;
        this.dateStart = dateStart;
        this.dateComplete = dateComplete;
        this.dailyChallenge = dailyChallenge;
        this.priority = priority;
    }

    protected Task(Parcel in) {
        taskID = in.readInt();
        taskName = in.readString();
        status = in.readString();
        category = in.readString();
        timeSpent = in.readInt();
        targetSec = in.readInt();
        dueDate = in.readString();
        dateCreated = in.readString();
        dateStart = in.readString();
        dateComplete = in.readString();
        dailyChallenge = in.readInt();
        priority = in.readInt();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(taskID);
        dest.writeString(taskName);
        dest.writeString(status);
        dest.writeString(category);
        dest.writeInt(timeSpent);
        dest.writeInt(targetSec);
        dest.writeString(dueDate);
        dest.writeString(dateCreated);
        dest.writeString(dateStart);
        dest.writeString(dateComplete);
        dest.writeInt(dailyChallenge);
        dest.writeInt(priority);
    }
    public int getTaskID() {
        return taskID;
    }
    public void setTaskID(int taskID) { this.taskID = taskID; }
    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public int getTimeSpent() { return timeSpent; }
    public void setTimeSpent(int timeSpent) { this.timeSpent = timeSpent; }
    public int getTargetSec() { return targetSec; }
    public void setTargetSec(int targetSec) { this.targetSec = targetSec; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public String getDateCreated() { return dateCreated; }
    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }
    public String getDateComplete() { return dateComplete; }
    public void setDateComplete(String dateComplete) { this.dateComplete = dateComplete; }
    public int getDailyChallenge() { return dailyChallenge; }
    public void setDailyChallenge(int dailyChallenge) { this.dailyChallenge = dailyChallenge;}
    public int getPriority() { return priority;}
    public void setPriority(int priority) { this.priority = priority;}
    public String getDateStart() { return dateStart; }
    public void setDateStart(String dateStart) { this.dateStart = dateStart; }

    public List<String> getColorCode() {
        List<String> colorCodes = new ArrayList<>();

        switch (taskName) {
            case "Drink Water":
                colorCodes.add("#bde0fe"); // water blue
                colorCodes.add("#FFFFFF");
                break;
            case "Read (books/news, etc) for 10 mins":
                colorCodes.add("#ffddd2"); // skin color
                colorCodes.add("#FFFFFF");
                break;
            case "Stretch for 5 minutes":
                colorCodes.add("#ffc8dd"); // light cute pink
                colorCodes.add("#FFFFFF");
                break;
            case "Take a 10 minutes walk":
                colorCodes.add("#cdb4db"); // some light purple
                colorCodes.add("#FFFFFF");
                break;
            case "Meditate for 15 minutes":
                colorCodes.add("#ffcad4"); // almost skin color
                colorCodes.add("#FFFFFF");
                break;
            default:
                colorCodes.add("#d0f4de"); // mint green
                colorCodes.add("#FFFFFF");
                break;
        }

        return colorCodes;
    }
}