package martin.sms.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import martin.sms.Professor.Profesor;
import martin.sms.R;
import martin.sms.Student.Student;

public class UpdateProfessor extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private String professorEmail;
    private Profesor professorUpdate, newProfessor;
    private String professorID;
    private EditText professorName, professorLastName;
    private Button btnUpdateStudent, btnAddSubject;
    private ArrayList<String> subjectsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_professor);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
            professorEmail = bundle.getString("professorEmail");

        professorName = (EditText) findViewById(R.id.prof_name);
        professorLastName = (EditText) findViewById(R.id.prof_lastname);
        btnUpdateStudent = (Button) findViewById(R.id.update_prof);
        btnAddSubject = (Button) findViewById(R.id.add_subject);

        findProfessor(professorEmail);

        btnUpdateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newProfessorName = professorName.getText().toString();
                String newProfessorLastName = professorLastName.getText().toString();

                if(!professorUpdate.getIme().equals(newProfessorName)){

                    myRef.child("professors")
                            .child(professorID)
                            .child("ime")
                            .setValue(newProfessorName);
                }

                if(!professorUpdate.getPrezime().equals(newProfessorLastName)){

                    myRef.child("professors")
                            .child(professorID)
                            .child("prezime")
                            .setValue(newProfessorLastName);
                }

                newProfessor = new Profesor(newProfessorName, newProfessorLastName);
                updateSubjectsNode();
                finish();
            }
        });

        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateProfessor.this, AddSubjectToProfessor.class);
                intent.putExtra("professorID", professorID);
                startActivity(intent);
            }
        });
    }

    private void setProfessorData(Profesor profesor){
        professorUpdate = profesor;
        professorName.setText(profesor.getIme());
        professorLastName.setText(profesor.getPrezime());
        professorID = profesor.getProfesorID();
        if (!professorLastName.equals("")){
            getSubjectsList();
        }
    }

    private void findProfessor(String email){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("professors")
                .orderByChild("email")
                .equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()) {
                    Profesor profesor = singleSnapshot.getValue(Profesor.class);
                    setProfessorData(profesor);
                }
                if (!dataSnapshot.exists()){
                    Toast.makeText(UpdateProfessor.this, "Ne postoji profesor sa ovom email adresom!", Toast.LENGTH_SHORT).show();
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
                .child("professors")
                .child(professorID)
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
                    .child("professors")
                    .child(professorID)
                    .setValue(newProfessor);
        }
    }
}
