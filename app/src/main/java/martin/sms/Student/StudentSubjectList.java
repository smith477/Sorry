package martin.sms.Student;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.Map;

import martin.sms.Professor.ProfessorAllSubjectList;
import martin.sms.Professor.ProfessorSubjectList;
import martin.sms.Professor.ProfileActivityProfessor;
import martin.sms.R;

public class StudentSubjectList extends AppCompatActivity {

    ListView listview;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> keyList = new ArrayList<>();
    private String userID;
    private FirebaseAuth auth;
    int backButtonCount = 0;
    Button btnChoseSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_subject_list);
        btnChoseSubject = (Button) findViewById(R.id.btn_chose_subject_student);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userID = auth.getCurrentUser().getUid();
        }

        listview = (ListView) findViewById(R.id.listview);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list);
        listview.setAdapter(adapter);

        btnChoseSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(StudentSubjectList.this, AllSubjectsList.class);
                startActivity(myIntent);
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("students")
                .child(userID)
                .child("subjects")
                .orderByChild("naziv");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                    String naziv = (objectMap.get("naziv").toString());
                    list.add(naziv);
                    keyList.add(singleSnapshot.getKey());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(StudentSubjectList.this, ProfileActivityStudent.class);
                intent.putExtra("subjectID", keyList.get(i));
                startActivity(intent);
            }
        });

        onBackPressed();

    }


    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 2)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            backButtonCount++;
        }
    }
}
