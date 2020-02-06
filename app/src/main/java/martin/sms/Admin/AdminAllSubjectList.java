package martin.sms.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import martin.sms.LoginActivity;
import martin.sms.Professor.ProfessorEvidenceList;
import martin.sms.Professor.ProfessorEvidenceListAdapter;
import martin.sms.Professor.ProfileActivityProfessor;
import martin.sms.R;
import martin.sms.Student.MainActivity;
import martin.sms.Student.Student;
import martin.sms.Subject;

public class AdminAllSubjectList extends AppCompatActivity {

    private ListView listView;
    List<Subject> subjectList;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
   // private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference myRef;
    String subjectID;
    //Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_subject_list);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        //Button ok = (Button) findViewById(R.id.btnOk);
        listView = findViewById(R.id.listview);
        subjectList = new ArrayList<>();

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
                .orderByChild("smer");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Subject subject = singleSnapshot.getValue(Subject.class);
                    subjectList.add(subject);
                }

                Query query = FirebaseDatabase.getInstance().getReference()
                        .child("subjects");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AdminSubjectListAdapter adapter = new AdminSubjectListAdapter(AdminAllSubjectList.this, subjectList);
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