package martin.sms.Admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import martin.sms.Professor.Profesor;
import martin.sms.Professor.ProfessorSubjectList;
import martin.sms.Professor.ProfileActivityProfessor;
import martin.sms.R;
import martin.sms.Subject;

import static android.content.Intent.getIntent;

public class ProfessorsBySubjectListAdapter extends ArrayAdapter<Profesor> {
    private Activity context;
    private List<Profesor> professorList;
    private DatabaseReference myRef;
    private String subjectID;



    public ProfessorsBySubjectListAdapter(Activity context, List<Profesor> professorList, String subjectID) {
        super(context, R.layout.activity_admin_all_professors_by_subject_list_item, professorList);
        this.context = context;
        this.professorList = professorList;
        this.subjectID = subjectID;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.activity_admin_all_professors_by_subject_list_item, null, true);

        TextView professorName = (TextView)listView.findViewById(R.id.professor_name);
        TextView professorSurname = (TextView)listView.findViewById(R.id.professor_surname);
        Button removeProfessor = (Button)listView.findViewById(R.id.remove);






        Profesor professor = professorList.get(position);
        professorName.setText(professor.getIme());
        professorSurname.setText(professor.getPrezime());

        removeProfessor.setOnClickListener((view) -> {

            myRef = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("subjects")
                    .child(subjectID)
                    .child("professors")
                    .child(professor.getProfesorID());

            myRef.removeValue();

            myRef = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("professors")
                    .child(professor.getProfesorID())
                    .child("subjects")
                    .child(subjectID);

            myRef.removeValue();
            Intent intent = new Intent(context, AdminSubjectListAdapter.class);
            context.startActivity(intent);

        });



        return listView;
    }
}

