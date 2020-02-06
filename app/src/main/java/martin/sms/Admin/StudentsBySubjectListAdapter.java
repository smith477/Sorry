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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import martin.sms.Professor.Profesor;
import martin.sms.R;
import martin.sms.Student.Student;

public class StudentsBySubjectListAdapter extends ArrayAdapter<Student> {

    private Activity context;
    private List<Student> studentList;
    private DatabaseReference myRef;
    private String subjectID;



    public StudentsBySubjectListAdapter(Activity context, List<Student> studentList, String subjectID) {
        super(context, R.layout.activity_admin_all_students_by_subject_list_item, studentList);
        this.context = context;
        this.studentList = studentList;
        this.subjectID = subjectID;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.activity_admin_all_students_by_subject_list_item, null, true);

        TextView studentName = (TextView)listView.findViewById(R.id.student_name);
        TextView studentSurname = (TextView)listView.findViewById(R.id.student_surname);
        Button removeStudent = (Button)listView.findViewById(R.id.remove);






        Student student = studentList.get(position);
        studentName.setText(student.getIme());
        studentSurname.setText(student.getPrezime());

        removeStudent.setOnClickListener((view) -> {

            myRef = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("subjects")
                    .child(subjectID)
                    .child("students")
                    .child(student.getStudentID());

            myRef.removeValue();

            myRef = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("students")
                    .child(student.getStudentID())
                    .child("subjects")
                    .child(subjectID);

            myRef.removeValue();
            Intent intent = new Intent(context, AdminSubjectListAdapter.class);
            context.startActivity(intent);

        });




        return listView;
    }


}
