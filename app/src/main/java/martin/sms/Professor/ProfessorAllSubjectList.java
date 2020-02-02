package martin.sms.Professor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.List;
import java.util.Map;

import martin.sms.Student.AllSubjectsListAdapter;
import martin.sms.R;
import martin.sms.SelectedSubject;
import martin.sms.Student.MainActivity;

public class ProfessorAllSubjectList extends AppCompatActivity {

    ArrayList<String> list = new ArrayList<>();
    private String userID;
    private FirebaseAuth auth;
    private Profesor currentProfessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_all_subject_list);

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

        returnProfessor(userID);

        Button ok = (Button) findViewById(R.id.btnOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectedSubject subject;

                for (int i=0; i<allSubjectsList.size(); i++){
                    if(allSubjectsList.get(i).isSelected) {

                        subject = new SelectedSubject(allSubjectsList.get(i).subjectName);

                        reference.child("professors")
                                .child(userID)
                                .child("subjects")
                                .child(allSubjectsList.get(i).id)
                                .child("naziv")
                                .setValue(allSubjectsList.get(i).subjectName);

                        reference.child("subjects")
                                .child(allSubjectsList.get(i).id)
                                .child("professors")
                                .child(userID)
                                .setValue(currentProfessor);
                    }
                }

                for (int i=0; i<allSubjectsList.size(); i++){
                    allSubjectsList.get(i).isSelected = false;
                }

                startActivity(new Intent(ProfessorAllSubjectList.this, ProfessorSubjectList.class));
            }
        });
    }

    private void returnProfessor(String profesorID){
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("professors")
                .orderByChild("profesorID")
                .equalTo(profesorID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
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

}