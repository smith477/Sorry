package martin.sms.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import martin.sms.R;
import martin.sms.Subject;

public class AddSubject extends AppCompatActivity {

    private EditText etSubjectKey, etMaxNumOfClasses, etSubjectName;
    private Button btnAddSubject;
    private String subjectKey, maxNumOfClasses, subjectName;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        auth = FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        etSubjectKey = (EditText) findViewById(R.id.sifra_predmeta);
        etSubjectName = (EditText) findViewById(R.id.naziv_predmeta);
        etMaxNumOfClasses = (EditText) findViewById(R.id.fond_casova);

        btnAddSubject = (Button) findViewById(R.id.add_subject);

        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                subjectKey = etSubjectKey.getText().toString();
                subjectName = etSubjectName.getText().toString();
                maxNumOfClasses = etMaxNumOfClasses.getText().toString();

                String id = myRef.push().getKey();
                //Subject subject = new Subject(subjectKey, subjectName, "", maxNumOfClasses, id);
                Subject subject = new Subject(subjectKey, subjectName, "", maxNumOfClasses, id, 0, 0);

                myRef.child("subjects")
                        .child(id)
                        .setValue(subject);

                finish();
            }
        });
    }
}
