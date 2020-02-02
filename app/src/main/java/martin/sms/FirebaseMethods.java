package martin.sms;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import martin.sms.Admin.Admin;
import martin.sms.Professor.Profesor;
import martin.sms.Student.Student;

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference myRef;
    private Context mContext;
    private String userID;

    public FirebaseMethods(Context context) {
        auth = FirebaseAuth.getInstance();
        mContext = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        if (auth.getCurrentUser() != null) {
            userID = auth.getCurrentUser().getUid();
        }
    }

    public void registerNewEmail(final String email, String password, final String index) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, R.string.auth_failed, Toast.LENGTH_SHORT).show();

                        } else if (task.isSuccessful()) {
                            sendVerificationEmail();
                            userID = auth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                        }

                    }
                });
    }

    public void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(mContext, "We have sent an email with a confirmation link to your email address!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(mContext, "Couldn't send verification email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void addNewStudent(String index, String ime, String prezime, String email) {

        Student student = new Student(index, ime, prezime, email, userID, 0);

       myRef.child("students")
               .child(userID)
               .setValue(student);

    }

    public void addNewProfessor(String firstname, String lastname, String email) {

        Profesor profesor = new Profesor(firstname, lastname, email, userID);

        myRef.child("professors")
                .child(userID)
                .setValue(profesor);

    }

    public void addNewAdmin(String firstname, String lastname, String email) {

        Admin admin = new Admin(firstname, lastname, email, userID);

        myRef.child("admins")
                .child(userID)
                .setValue(admin);

    }
}




















