package sg.edu.np.mad.pawgress.Tasks;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.mad.pawgress.R;

public class ParentTaskViewHolder extends RecyclerView.ViewHolder {

    String title = "ViewHolder!";
    TextView name, category, duedate;
    RelativeLayout card;
    ImageButton edit,delete;
    CheckBox complete;
    RecyclerView childList;

    public ParentTaskViewHolder(View itemView){
        super(itemView);
        category = itemView.findViewById(R.id.taskCat);
        card = itemView.findViewById(R.id.CategoryCard); // individual task
        childList = itemView.findViewById(R.id.childList);
        Log.i(title, "parent viewholder");
    }
}
