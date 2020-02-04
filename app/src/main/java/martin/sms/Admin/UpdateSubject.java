package martin.sms.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import martin.sms.Professor.Profesor;
import martin.sms.Professor.ProfessorEvidenceList;
import martin.sms.R;
import martin.sms.Subject;

public class UpdateSubject extends AppCompatActivity {

    private EditText etSbject_number, etSubjectName, etSubjectMaxNumOfClasses, etSubjectCode;
    private Button btnUpdateSubject, btnDeleteSubject;
    private Spinner btnGodina, btnSmer;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private String subjectName, subjectID;
    private Subject updateSubject, newSubject;
    ArrayList<String> studentsList = new ArrayList<>();
    ArrayList<String> professorsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_subject);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
            subjectName = bundle.getString("subjectName");

        etSbject_number = (EditText) findViewById(R.id.subject_number);
        etSubjectName = (EditText) findViewById(R.id.subject_name);
        etSubjectMaxNumOfClasses = (EditText) findViewById(R.id.subject_maxNumOfClasses);
        etSubjectCode = (EditText) findViewById(R.id.subject_code);
        btnUpdateSubject = (Button) findViewById(R.id.btn_update_subject);
        btnDeleteSubject = (Button) findViewById(R.id.btn_delete_subject);
        btnGodina = (Spinner) findViewById(R.id.godina);
        btnSmer = (Spinner) findViewById(R.id.smer);

        Spinner dropdownGodina = findViewById(R.id.godina);
        String[] itemsGodina = new String[]{"1", "2", "3", "4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsGodina);
        dropdownGodina.setAdapter(adapter);

        Spinner dropdownSmer = findViewById(R.id.smer);
        String[] itemsSmer = new String[]{"Opšti", "RII", "EKM", "US", "Elektroenergetika", "E", "T"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsSmer);
        dropdownSmer.setAdapter(adapter1);

        findSubject(subjectName);

        btnUpdateSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newSubjectNumber = etSbject_number.getText().toString();
                String newSubjectName = etSubjectName.getText().toString();
                String newSubjectMaxNumOfClasses = etSubjectMaxNumOfClasses.getText().toString();
                String newSubjectCode = etSubjectCode.getText().toString();
                String newSubjectGodina = btnGodina.getSelectedItem().toString();
                String newSubjectSmer = btnSmer.getSelectedItem().toString();

                if(!updateSubject.getSifra().equals(newSubjectNumber)){

                    myRef.child("subjects")
                            .child(subjectID)
                            .child("sifra")
                            .setValue(newSubjectNumber);
                }

                if(!updateSubject.getNaziv().equals(newSubjectName)){

                    myRef.child("subjects")
                            .child(subjectID)
                            .child("naziv")
                            .setValue(newSubjectName);
                }

                if(!updateSubject.getFond_casova().equals(newSubjectMaxNumOfClasses)){

                    myRef.child("subjects")
                            .child(subjectID)
                            .child("fond_casova")
                            .setValue(newSubjectMaxNumOfClasses);
                }

                if(!updateSubject.getKod().equals(newSubjectCode)){

                    myRef.child("subjects")
                            .child(subjectID)
                            .child("kod")
                            .setValue(newSubjectCode);
                }

                if(!updateSubject.getGodina().equals(newSubjectGodina)){

                    myRef.child("subjects")
                            .child(subjectID)
                            .child("godina")
                            .setValue(newSubjectGodina);
                }

                if(!updateSubject.getSmer().equals(newSubjectSmer)){

                    myRef.child("subjects")
                            .child(subjectID)
                            .child("smer")
                            .setValue(newSubjectSmer);
                }

                newSubject = new Subject(newSubjectNumber,newSubjectName,newSubjectCode,newSubjectMaxNumOfClasses,subjectID, newSubjectGodina, newSubjectSmer);

                updateStudentNode();
                updateProfessorNode();

                finish();
            }
        });

        btnDeleteSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child("subjects")
                        .child(subjectID)
                        .removeValue();

                deleteFromStudentNode();
                deleteFromProfessorNode();

                finish();
            }
        });
    }

    private void setSubjectData(Subject subject){
        updateSubject = subject;
        etSbject_number.setText(subject.getSifra());
        etSubjectName.setText(subject.getNaziv());
        etSubjectMaxNumOfClasses.setText(subject.getFond_casova());
        etSubjectCode.setText(subject.getKod());
        subjectID = subject.getSubjectID();
        if(!subjectID.equals("")){
            getStudentsList();
            getProfessorsList();
        }
    }

    public void findSubject(final String name){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("subjects")
                .orderByChild("naziv")
                .equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Subject subject = singleSnapshot.getValue(Subject.class);
                    //subjectID = singleSnapshot.getKey();
                    setSubjectData(subject);
                }
                if (!dataSnapshot.exists()){
                    Toast.makeText(UpdateSubject.this, "Ne postoji predmet sa ovim imenom!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getStudentsList(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("subjects")
                .child(subjectID)
                .child("students")
                .orderByChild("index");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    studentsList.add(singleSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void deleteFromStudentNode(){
        for (int i = 0; i < studentsList.size(); i++){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("students")
                    .child(studentsList.get(i))
                    .child("subjects")
                    .child(subjectID)
                    .removeValue();

        }
    }

    private void getProfessorsList(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("subjects")
                .child(subjectID)
                .child("professors")
                .orderByChild("ime");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    professorsList.add(singleSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void deleteFromProfessorNode(){
        for (int i = 0; i < professorsList.size(); i++){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("professors")
                    .child(professorsList.get(i))
                    .child("subjects")
                    .child(subjectID)
                    .removeValue();
        }
    }

    private void updateStudentNode(){
        for (int i = 0; i < studentsList.size(); i++){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("students")
                    .child(studentsList.get(i))
                    .child("subjects")
                    .child(subjectID)
                    .child("naziv")
                    .setValue(newSubject.getNaziv());
        }
    }

    private void updateProfessorNode(){
        for (int i = 0; i < professorsList.size(); i++){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("professors")
                    .child(professorsList.get(i))
                    .child("subjects")
                    .child(subjectID)
                    .child("naziv")
                    .setValue(newSubject.getNaziv());
        }
    }
}
