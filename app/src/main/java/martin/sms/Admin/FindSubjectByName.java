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

import martin.sms.R;

public class FindSubjectByName extends AppCompatActivity {

    private EditText etSubjectName;
    private Button btnAddSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_subject_by_name);

        etSubjectName = (EditText) findViewById(R.id.et_subject_name);
        btnAddSubject = (Button) findViewById(R.id.btn_add_subject);

        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subjectName = etSubjectName.getText().toString();
                Intent intent = new Intent(FindSubjectByName.this, UpdateSubject.class);
                intent.putExtra("subjectName", subjectName);
                startActivity(intent);
            }
        });
    }
}
