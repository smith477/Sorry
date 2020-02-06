package martin.sms.Professor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import martin.sms.R;

public class ProfessorSubjectList extends AppCompatActivity {

    ListView listview;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> keyList = new ArrayList<>();
    private String userID;
    private FirebaseAuth auth;
    int backButtonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_subject_list);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userID = auth.getCurrentUser().getUid();
        }

        listview = (ListView) findViewById(R.id.listview);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list);
        listview.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("professors")
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
                Intent intent = new Intent(ProfessorSubjectList.this, ProfileActivityProfessor.class);
                intent.putExtra("subjectID", keyList.get(i));
                intent.putExtra("professorID", userID);
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
