package martin.sms.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import martin.sms.LoginActivity;
import martin.sms.R;
import martin.sms.Student.Student;

public class UpdateStudent extends AppCompatActivity {

    private EditText studentName, studentLastName, studentEmail, studenrNumOfClasses;
    private Button btnUpdateStudent, btnAddSubject;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private Student studentUpdate, newStudent;
    private String studentIndex, studentID;
    private ArrayList<String> subjectsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
            studentIndex = bundle.getString("studentIndex");

        studentName = (EditText) findViewById(R.id.student_name);
        studentLastName = (EditText) findViewById(R.id.student_lastname);
        //studentEmail = (EditText) findViewById(R.id.student_email);
        studenrNumOfClasses = (EditText) findViewById(R.id.student_numOfClasses);
        btnUpdateStudent = (Button) findViewById(R.id.update_student);
        btnAddSubject = (Button) findViewById(R.id.add_subject);

        findStudent(studentIndex);

        btnUpdateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newStudentName = studentName.getText().toString();
                String newStudentLastName = studentLastName.getText().toString();
                //String newStudentEmail = studentEmail.getText().toString();
                int newNumOfClasses = Integer.parseInt(studenrNumOfClasses.getText().toString());

                if(!studentUpdate.getIme().equals(newStudentName)){

                    myRef.child("students")
                            .child(studentID)
                            .child("ime")
                            .setValue(newStudentName);
                }

                if(!studentUpdate.getPrezime().equals(newStudentLastName)){

                    myRef.child("students")
                            .child(studentID)
                            .child("prezime")
                            .setValue(newStudentLastName);
                }

                if(studentUpdate.getBroj_casova() != newNumOfClasses){

                    myRef.child("students")
                            .child(studentID)
                            .child("broj_casova")
                            .setValue(newNumOfClasses);
                }

                newStudent = new Student(studentIndex, newStudentName, newStudentLastName, newNumOfClasses);
                updateSubjectsNode();

                finish();
            }
        });

        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateStudent.this, AddSubjectToStudent.class);
                intent.putExtra("studentID", studentID);
                startActivity(intent);
            }
        });
    }

    private void setStudentData(Student student){
        studentUpdate = student;
        studentName.setText(student.getIme());
        studentLastName.setText(student.getPrezime());
       // studentEmail.setText(student.getEmail());
        studenrNumOfClasses.setText(Integer.toString(student.getBroj_casova()));
        studentID = student.getStudentID();
        if (!studentID.equals("")){
            getSubjectsList();
        }
    }

    private void findStudent(String index){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("students")
                .orderByChild("index")
                .equalTo(index);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()) {
                    Student student = singleSnapshot.getValue(Student.class);
                    setStudentData(student);
                }
                if (!dataSnapshot.exists()){
                    Toast.makeText(UpdateStudent.this, "Ne postoji student sa ovim indexom!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getSubjectsList() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("students")
                .child(studentID)
                .child("subjects")
                .orderByChild("naziv");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    subjectsList.add(singleSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void updateSubjectsNode(){
        for (int i = 0; i < subjectsList.size(); i++){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("subjects")
                    .child(subjectsList.get(i))
                    .child("students")
                    .child(studentID)
                    .setValue(newStudent);
        }
    }
}
