package martin.sms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import martin.sms.Admin.RegisterActivityAdmin;
import martin.sms.Professor.RegisterActivityProfessor;
import martin.sms.Student.RegisterActivity;

public class RegisterTypes extends AppCompatActivity {

    private Button btnSignupProfessor, btnSignupStudent, btnSignupAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_types);

        btnSignupStudent = (Button) findViewById(R.id.sign_up_student);
        btnSignupProfessor = (Button) findViewById(R.id.sign_up_professor);
        btnSignupAdmin = (Button) findViewById(R.id.sign_up_admin);

        btnSignupStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterTypes.this, RegisterActivity.class));
            }
        });

        btnSignupProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterTypes.this, RegisterActivityProfessor.class));
            }
        });

        btnSignupAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterTypes.this, RegisterActivityAdmin.class));
            }
        });
    }
}
