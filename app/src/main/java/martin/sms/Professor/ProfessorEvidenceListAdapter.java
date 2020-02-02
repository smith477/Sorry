package martin.sms.Professor;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import martin.sms.R;
import martin.sms.Student.Student;

public class ProfessorEvidenceListAdapter extends ArrayAdapter<Student> {

    private Activity context;
    private List<Student> evidenceList;
    private String maxNumOfClasses;

    public ProfessorEvidenceListAdapter(Activity context, List<Student> evidenceList, String maxNumOfClasses) {
        super(context, R.layout.activity_professor_evidence_list_item, evidenceList);
        this.context = context;
        this.evidenceList = evidenceList;
        this.maxNumOfClasses = maxNumOfClasses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.activity_professor_evidence_list_item, null, true);

        TextView studentName = (TextView)listView.findViewById(R.id.student_ime);
        TextView studentLastName = (TextView)listView.findViewById(R.id.student_prezime);
        TextView studentIndex = (TextView)listView.findViewById(R.id.student_index);
        TextView studentNumOfClasses = (TextView)listView.findViewById(R.id.student_numOfClasses);
        TextView subjectMaxNumOfClasses = (TextView)listView.findViewById(R.id.subject_maxNumOfClasses);

        Student student = evidenceList.get(position);
        studentIndex.setText(student.getIndex());
        studentName.setText(student.getIme());
        studentLastName.setText(student.getPrezime());
        studentNumOfClasses.setText(Integer.toString(student.getBroj_casova()));
        subjectMaxNumOfClasses.setText(maxNumOfClasses);

        return listView;
    }
}

