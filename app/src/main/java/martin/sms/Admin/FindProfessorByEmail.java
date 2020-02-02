package martin.sms.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import martin.sms.R;

public class FindProfessorByEmail extends AppCompatActivity {

    private EditText etProfessorEmail;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_professor_by_email);

        etProfessorEmail = (EditText) findViewById(R.id.prof_email);
        btnUpdate = (Button) findViewById(R.id.update_professor);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String professorEmail = etProfessorEmail.getText().toString();
                Intent intent = new Intent(FindProfessorByEmail.this, UpdateProfessor.class);
                intent.putExtra("professorEmail", professorEmail);
                startActivity(intent);
            }
        });
    }
}
