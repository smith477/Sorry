package martin.sms.Student;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import martin.sms.Admin.UpdateStudent;
import martin.sms.LoginActivity;
import martin.sms.Professor.ProfileActivityProfessor;
import martin.sms.R;
import martin.sms.Subject;

public class ProfileActivityStudent extends AppCompatActivity {

    private EditText inputCode;
    private Button btnSubmit, btnDelete;
    private String code;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    String subjectID;
    String userID;
    String studentID;
    boolean present;
    public int currentNum;

    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    LocationListener locationListener;
    double latitude;
    double longitude;

    double x;
    double y;

    double rez;
    float[] gps = new float[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_student);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userID = auth.getCurrentUser().getUid();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
            subjectID = bundle.getString("subjectID");

        btnSubmit = (Button) findViewById(R.id.btn_submit);
        inputCode = (EditText) findViewById(R.id.enter_code);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSubjectCoordinates();
                code = inputCode.getText().toString();
                if(code.isEmpty()){
                    Toast.makeText(ProfileActivityStudent.this, "This field cannot be blank! Try again!" ,
                            Toast.LENGTH_SHORT).show();}
                else{
                    checkCode(code);
                }

                startActivity(new Intent(ProfileActivityStudent.this, StudentSubjectList.class));
                //startActivity(new Intent(ProfileActivityStudent.this, MainActivity.class));
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSubject(userID, subjectID);
                Intent intent = new Intent(ProfileActivityStudent.this, StudentSubjectList.class);
                startActivity(intent);
            }
        });

        getCurrentNumOfClasses(userID);



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

    public void deleteSubject(String idU, String idS){
        myRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("students")
                .child(idU)
                .child("subjects")
                .child(idS);

        myRef.removeValue();
        myRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("subjects")
                .child(idS)
                .child("students")
                .child(idU);

        myRef.removeValue();
        Toast.makeText(this, "Napustili ste predmet", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    private double distance(Location currentUser, Location prof)
    {
        double theta = currentUser.getLongitude() - prof.getLongitude();
        double dist = Math.sin(deg2rad(currentUser.getLatitude()))
                * Math.sin(deg2rad(prof.getLatitude()))
                * Math.cos(deg2rad(currentUser.getLatitude()))
                * Math.cos(deg2rad(prof.getLatitude()))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return (dist);

    }

    private double rad2deg(double rad)
    {
        return (rad * 180 / Math.PI);
    }

    private double deg2rad(double deg)
    {
        return (deg * Math.PI / 180.0);
    }

    private void getSubjectCoordinates() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("subjects")
                .orderByChild("subjectID")
                .equalTo(subjectID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Subject subject = singleSnapshot.getValue(Subject.class);
                    x = subject.getLatitude();
                    y = subject.getLongitude();
                }

                Location professor = new Location("");
                professor.setLatitude(x);
                professor.setLongitude(y);

                Location currentUser = new Location("");
                currentUser.setLatitude(latitude);
                currentUser.setLongitude(longitude);

                Location.distanceBetween(x,y,latitude,longitude,gps);
                rez = distance(currentUser, professor);
                String dist = "Distance is " + new DecimalFormat("#.#").format((currentUser.distanceTo(professor))) + "m";
                Toast.makeText(ProfileActivityStudent.this, gps[0] + " m", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkCode(String code){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("subjects")
                .orderByChild("kod")
                .equalTo(code);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && gps[0] <= 50.0) {
                    addNumOfClasses(userID);
                    Toast.makeText(ProfileActivityStudent.this,  "Successful check in!",
                            Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(ProfileActivityStudent.this, "Error! Try agan!" ,
                            Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getCurrentNumOfClasses(String studentID){
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("subjects")
                .child(subjectID)
                .child("students")
                .child(studentID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentNum = (Integer.parseInt(dataSnapshot.child("broj_casova").getValue().toString()));
                currentNum++;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addNumOfClasses(String studentID){
        myRef.child("subjects")
                .child(subjectID)
                .child("students")
                .child(studentID)
                .child("broj_casova")
                .setValue(currentNum);

    }
}
