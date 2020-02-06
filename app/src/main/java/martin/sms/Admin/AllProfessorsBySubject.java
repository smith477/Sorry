package martin.sms.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import java.util.List;

import martin.sms.Professor.Profesor;
import martin.sms.R;
import martin.sms.Subject;

public class AllProfessorsBySubject extends AppCompatActivity {

    private ListView listView;
    List<Profesor> professorList;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    // private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference myRef;
    String subjectID;
    //Button ok;

     //String subjectID = getIntent().getExtras().getString("subjectID");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_professors_by_subject_list);
        //Toast.makeText(this, "SubjectID je: " + subjectID, Toast.LENGTH_LONG).show();

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        //Button ok = (Button) findViewById(R.id.btnOk);
        listView = findViewById(R.id.listview);
        professorList = new ArrayList<>();

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
                .child("professors");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Profesor professor = singleSnapshot.getValue(Profesor.class);
                    professorList.add(professor);
                }

                Query query = FirebaseDatabase.getInstance().getReference()
                        .child("subjects")
                        .child(subjectID);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ProfessorsBySubjectListAdapter adapter = new ProfessorsBySubjectListAdapter(AllProfessorsBySubject.this, professorList);
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

}
