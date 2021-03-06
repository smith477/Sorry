package martin.sms.Professor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import martin.sms.LoginActivity;
import martin.sms.R;
import martin.sms.Student.MainActivity;
import martin.sms.Student.ProfileActivityStudent;

public class ProfileActivityProfessor extends AppCompatActivity {

    private TextView tvCode, tvTimer;
    private Button btnGetCode, btnShowEvidence, btnSignOut, btnStartTimer, btnExitSubject;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference myRef;
    String subjectID;
    String professorID;
    String randomString;
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    LocationListener locationListener;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_professor);


        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        setupFirebaseAuth();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(ProfileActivityProfessor.this, LoginActivity.class));
                    finish();
                }
            }
        };

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null) {
            subjectID = bundle.getString("subjectID");
            professorID = bundle.getString("professorID");
        }
        tvCode = (TextView)findViewById(R.id.view_code);
        tvTimer = (TextView)findViewById(R.id.tvTimer);
        btnGetCode = (Button)findViewById(R.id.btn_get_code);
        btnShowEvidence = (Button)findViewById(R.id.btn_show_evidence);
        btnSignOut = (Button)findViewById(R.id.btn_sign_out);
        btnStartTimer = (Button) findViewById(R.id.btn_start_timer);
        btnExitSubject = (Button) findViewById(R.id.btn_delete_subject);
        btnStartTimer.setEnabled(false);

        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomString = getRandomString(10);
                tvCode.setText(randomString);
                //reverseTimer(30, tvTimer); //Ovo treba da se veze za neko dugme "Start"

                btnGetCode.setEnabled(false);
                btnStartTimer.setEnabled(true);
            }
        });

        btnStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reverseTimer(30, tvTimer); //Ovo treba da se veze za neko dugme "Start"

                myRef.child("subjects")
                        .child(subjectID)
                        .child("kod")
                        .setValue(randomString);

                myRef.child("subjects")
                        .child(subjectID)
                        .child("latitude")
                        .setValue(latitude);

                myRef.child("subjects")
                        .child(subjectID)
                        .child("longitude")
                        .setValue(longitude);

                btnStartTimer.setEnabled(false);

            }
        });

        btnShowEvidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivityProfessor.this, ProfessorEvidenceList.class);
                intent.putExtra("subjectID", subjectID);
                startActivity(intent);
            }
        });

        subjectID = bundle.getString("subjectID");
        btnExitSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSubject(professorID, subjectID);
                Intent intent = new Intent(ProfileActivityProfessor.this, ProfessorSubjectList.class);
                startActivity(intent);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
            }
        });


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void deleteSubject(String idP, String idS){

        myRef = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("professors")
                    .child(idP)
                    .child("subjects")
                    .child(idS);

//        Toast.makeText(this, myRef.toString(), Toast.LENGTH_LONG).show();
        myRef.removeValue();

        myRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("subjects")
                .child(idS)
                .child("professors")
                .child(idP);

        myRef.removeValue();
        Toast.makeText(this, "Napustili ste predmet", Toast.LENGTH_LONG).show();
    }

    public void reverseTimer(int Seconds,final TextView tv){

        new CountDownTimer(Seconds* 1000+1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tv.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                btnGetCode.setEnabled(true);
                tv.setText("Completed");
                myRef.child("subjects")
                        .child(subjectID)
                        .child("kod")
                        .setValue("");

                myRef.child("subjects")
                        .child(subjectID)
                        .child("latitude")
                        .setValue(0);

                myRef.child("subjects")
                        .child(subjectID)
                        .child("longitude")
                        .setValue(0);
            }
        }.start();
    }

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for(int i=0 ; i < sizeOfRandomString ; ++i )
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private void checkCurrentUser(FirebaseUser user){

        if(user == null){
            Intent intent = new Intent(ProfileActivityProfessor.this, LoginActivity.class);
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
        locationManager.removeUpdates(locationListener);
    }
}
