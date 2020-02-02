package martin.sms.Professor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
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
import java.util.List;
import java.util.Map;

import martin.sms.R;
import martin.sms.Student.Student;
import martin.sms.Subject;

public class ProfessorEvidenceList extends AppCompatActivity {

    private ListView listView;
    DatabaseReference myRef;
    List<Student> evidenceList;
    private String userID;
    private FirebaseAuth auth;
    String subjectID;
    String maxNumOfClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_evidence_list);

        listView = findViewById(R.id.evidence_listview);
        evidenceList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userID = auth.getCurrentUser().getUid();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
            subjectID = bundle.getString("subjectID");
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("subjects")
                .child(subjectID)
                .child("students");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Student student = singleSnapshot.getValue(Student.class);
                    evidenceList.add(student);
                }

                Query query = FirebaseDatabase.getInstance().getReference()
                        .child("subjects")
                        .child(subjectID);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        maxNumOfClasses = dataSnapshot.child("fond_casova").getValue().toString();
                        ProfessorEvidenceListAdapter adapter = new ProfessorEvidenceListAdapter(ProfessorEvidenceList.this, evidenceList, maxNumOfClasses);
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMaxNumOfClasses(){
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("subjects")
                .child(subjectID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                maxNumOfClasses = dataSnapshot.child("fond_casova").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
