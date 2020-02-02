package martin.sms.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import martin.sms.Professor.Profesor;
import martin.sms.R;
import martin.sms.Student.Student;

public class AddSubjectToProfessor extends AppCompatActivity {

    private EditText etSubjectName;
    private Button btnAddSubject, btnDeleteSubject;
    private String subjectID, professorID;
    private Profesor currentProfessor;
    private boolean addBtnClicked, removeBtnClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject_to_professor);

        etSubjectName = (EditText) findViewById(R.id.et_subject_name);
        btnAddSubject = (Button) findViewById(R.id.btn_add_subject);
        btnDeleteSubject = (Button) findViewById(R.id.btn_delete_subject);

        addBtnClicked = false;
        removeBtnClicked = false;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
            professorID = bundle.getString("professorID");

        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBtnClicked = true;
                String subjectName = etSubjectName.getText().toString();
                findProfessor();
                findSubject(subjectName);
                finish();
            }
        });

        btnDeleteSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeBtnClicked = true;
                String subjectName = etSubjectName.getText().toString();
                findProfessor();
                findSubject(subjectName);
                finish();
            }
        });
    }

    public void removeSubject(String name){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("professors")
                .child(professorID)
                .child("subjects")
                .child(subjectID)
                .removeValue();

        reference.child("subjects")
                .child(subjectID)
                .child("professors")
                .child(professorID)
                .removeValue();
    }

    public void addSubject(String name){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("professors")
                .child(professorID)
                .child("subjects")
                .child(subjectID)
                .child("naziv")
                .setValue(name);

        reference.child("subjects")
                .child(subjectID)
                .child("professors")
                .child(professorID)
                .setValue(currentProfessor);
    }

    private void findProfessor(){
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("professors")
                .orderByChild("profesorID")
                .equalTo(professorID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Profesor profesor = new Profesor();
                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                    profesor.setIme(objectMap.get("ime").toString());
                    profesor.setPrezime(objectMap.get("prezime").toString());

                    currentProfessor = profesor;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                    subjectID = singleSnapshot.getKey();

                    if (addBtnClicked) {
                        addSubject(name);
                        addBtnClicked = false;
                    }
                    if(removeBtnClicked){
                        removeSubject(name);
                        removeBtnClicked = false;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
