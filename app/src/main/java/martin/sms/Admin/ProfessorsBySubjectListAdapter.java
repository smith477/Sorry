package martin.sms.Admin;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import martin.sms.Professor.Profesor;
import martin.sms.R;
import martin.sms.Subject;

public class ProfessorsBySubjectListAdapter extends ArrayAdapter<Profesor> {
    private Activity context;
    private List<Profesor> professorList;

    public ProfessorsBySubjectListAdapter(Activity context, List<Profesor> professorList) {
        super(context, R.layout.activity_admin_all_professors_by_subject_list_item, professorList);
        this.context = context;
        this.professorList = professorList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.activity_admin_all_professors_by_subject_list_item, null, true);

        TextView professorName = (TextView)listView.findViewById(R.id.professor_name);
        TextView professorSurname = (TextView)listView.findViewById(R.id.professor_surname);


        Profesor professor = professorList.get(position);
        professorName.setText(professor.getIme());
        professorSurname.setText(professor.getPrezime());

        return listView;
    }
}

