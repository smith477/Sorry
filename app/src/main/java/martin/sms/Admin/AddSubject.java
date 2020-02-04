package martin.sms.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import martin.sms.R;
import martin.sms.Subject;

public class AddSubject extends AppCompatActivity {

    private EditText etSubjectKey, etMaxNumOfClasses, etSubjectName;
    private Button btnAddSubject;
    private Spinner btnGodina, btnSmer;
    private String subjectKey, maxNumOfClasses, subjectName, godina, smer;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        Spinner dropdownGodina = findViewById(R.id.godina);
        String[] itemsGodina = new String[]{"1", "2", "3", "4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsGodina);
        dropdownGodina.setAdapter(adapter);

        Spinner dropdownSmer = findViewById(R.id.smer);
        String[] itemsSmer = new String[]{"Opšti", "RII", "EKM", "US", "Elektroenergetika", "E", "T"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsSmer);
        dropdownSmer.setAdapter(adapter1);



        auth = FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        etSubjectKey = (EditText) findViewById(R.id.sifra_predmeta);
        etSubjectName = (EditText) findViewById(R.id.naziv_predmeta);
        etMaxNumOfClasses = (EditText) findViewById(R.id.fond_casova);

        btnAddSubject = (Button) findViewById(R.id.add_subject);
        btnGodina = (Spinner) findViewById(R.id.godina);
        btnSmer = (Spinner) findViewById(R.id.smer);

        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                subjectKey = etSubjectKey.getText().toString();
                subjectName = etSubjectName.getText().toString();
                maxNumOfClasses = etMaxNumOfClasses.getText().toString();
                godina = btnGodina.getSelectedItem().toString();
                smer = btnSmer.getSelectedItem().toString();

                String id = myRef.push().getKey();
                //Subject subject = new Subject(subjectKey, subjectName, "", maxNumOfClasses, id);
                Subject subject = new Subject(subjectKey, subjectName, "", maxNumOfClasses, id, 0, 0, godina, smer);

                myRef.child("subjects")
                        .child(id)
                        .setValue(subject);

                finish();
            }
        });
    }
}
