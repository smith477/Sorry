package martin.sms.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import martin.sms.LoginActivity;
import martin.sms.Professor.ProfileActivityProfessor;
import martin.sms.R;
import martin.sms.Student.MainActivity;

public class ProfileActivityAdmin extends AppCompatActivity {

    Button btnUpdateStudent, btnUpdateProfessor, btnAddSubject, btnSignOut, btnUpdateSubject;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_admin);

        btnUpdateStudent = (Button) findViewById(R.id.update_student);
        btnUpdateProfessor = (Button) findViewById(R.id.update_profesor);
        btnAddSubject = (Button) findViewById(R.id.add_subject);
        btnSignOut = (Button)findViewById(R.id.btn_sign_out);
        btnUpdateSubject = (Button)findViewById(R.id.update_subject);

        setupFirebaseAuth();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(ProfileActivityAdmin.this, LoginActivity.class));
                    finish();
                }
            }
        };

        btnUpdateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivityAdmin.this, FindByIndex.class));
            }
        });

        btnUpdateProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivityAdmin.this, FindProfessorByEmail.class));
            }
        });

        btnUpdateSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivityAdmin.this, FindSubjectByName.class));
            }
        });

        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivityAdmin.this, AddSubject.class));
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
            }
        });
    }

      /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    private void checkCurrentUser(FirebaseUser user){

        if(user == null){
            Intent intent = new Intent(ProfileActivityAdmin.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void setupFirebaseAuth(){

        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                checkCurrentUser(user);

                if (user != null) {
                } else {
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
        FirebaseUser user = auth.getCurrentUser();
        checkCurrentUser(user);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
