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

import martin.sms.R;
import martin.sms.Subject;


public class AdminSubjectListAdapter extends ArrayAdapter<Subject> {

    private Activity context;
    private List<Subject> subjectList;

    public AdminSubjectListAdapter(Activity context, List<Subject> subjectList) {
        super(context, R.layout.activity_all_subjects_list_item, subjectList);
        this.context = context;
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.activity_admin_all_subject_list_item, null, true);

        TextView subjectGodina = (TextView)listView.findViewById(R.id.godina);
        TextView subjectSmer = (TextView)listView.findViewById(R.id.smer);
        TextView subjectPredmet = (TextView)listView.findViewById(R.id.predmet);
        final TextView subjectId = (TextView)listView.findViewById(R.id.idPredmet);


        Subject subject = subjectList.get(position);
        subjectGodina.setText(subject.getGodina());
        subjectSmer.setText(subject.getSmer());
        subjectPredmet.setText(subject.getNaziv());
        subjectId.setText(subject.getSubjectID());

        Button professor = (Button) listView.findViewById(R.id.profesori);
        professor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AllProfessorsBySubject.class);
                intent.putExtra("subjectID", subjectId.getText());
                view.getContext().startActivity(intent);
            }
        });

        return listView;
    }
}

/*
* Button professor = (Button) findViewById(R.id.profesori);
        professor.setOnClickListener((view) -> {

            Intent intent = new Intent(this, Page.class);
            intent.putExtra("subjectID", subjectID); // getText() SHOULD NOT be static!!!
            startActivity(intent);
        });

String passedArg = getIntent().getExtras().getString("arg");
enteredValue.setText(passedArg); Ovo je za drugu aktivnost
*  */

