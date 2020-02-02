package martin.sms.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import martin.sms.R;

public class FindByIndex extends AppCompatActivity {

    private EditText etStudentIndex;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_by_index);

        etStudentIndex = (EditText) findViewById(R.id.student_index);
        btnUpdate = (Button) findViewById(R.id.update_student);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String studentIndex = etStudentIndex.getText().toString();
                Intent intent = new Intent(FindByIndex.this, UpdateStudent.class);
                intent.putExtra("studentIndex", studentIndex);
                startActivity(intent);
            }
        });
    }
}
