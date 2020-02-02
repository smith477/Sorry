package martin.sms.Student;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import martin.sms.SelectedSubject;

public class AllSubjectsList extends AppCompatActivity {

    ArrayList<String> list = new ArrayList<>();
    private String userID;
    private FirebaseAuth auth;
    private Student currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_subjects_list);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userID = auth.getCurrentUser().getUid();
        }

        ListView listView = (ListView) findViewById(R.id.listview);
        final List<SelectedSubject> allSubjectsList = new ArrayList<>();
        final AllSubjectsListAdapter adapter = new AllSubjectsListAdapter(this, allSubjectsList);
        listView.setAdapter(adapter);

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("subjects")
                .orderByChild("naziv");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                    String name = (objectMap.get("naziv").toString());
                    String id = singleSnapshot.getKey();
                    allSubjectsList.add(new SelectedSubject(false, name, id));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SelectedSubject subject = allSubjectsList.get(i);
                if (subject.isSelected())
                    subject.setSelected(false);
                else
                    subject.setSelected(true);

                allSubjectsList.set(i, subject);
                adapter.updateRecords(allSubjectsList);
            }
        });

        returnStudent(userID);

        Button ok = (Button) findViewById(R.id.btnOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectedSubject subject;

                for (int i=0; i<allSubjectsList.size(); i++){
                    if(allSubjectsList.get(i).isSelected) {

                        subject = new SelectedSubject(allSubjectsList.get(i).subjectName);

                        reference.child("students")
                                .child(userID)
                                .child("subjects")
                                .child(allSubjectsList.get(i).id)
                                .child("naziv")
                                .setValue(allSubjectsList.get(i).subjectName);

                        reference.child("subjects")
                                .child(allSubjectsList.get(i).id)
                                .child("students")
                                .child(userID)
                                .setValue(currentStudent);
                    }
                }

                for (int i=0; i<allSubjectsList.size(); i++){
                    allSubjectsList.get(i).isSelected = false;
                }

                startActivity(new Intent(AllSubjectsList.this, StudentSubjectList.class));
            }
        });
    }

    private void returnStudent(String studentID){
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("students")
                .orderByChild("studentID")
                .equalTo(studentID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Student student = new Student();
                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                    student.setIme(objectMap.get("ime").toString());
                    student.setPrezime(objectMap.get("prezime").toString());
                    student.setIndex(objectMap.get("index").toString());
                    student.setBroj_casova(Integer.parseInt(objectMap.get("broj_casova").toString()));

                    currentStudent = student;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
