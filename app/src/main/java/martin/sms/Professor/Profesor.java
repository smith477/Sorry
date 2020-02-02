package martin.sms.Professor;

import android.os.Parcel;
import android.os.Parcelable;

public class Profesor  {

    private String ime;
    private String prezime;
    private String email;
    private String profesorID;

    public Profesor(String ime, String prezime, String email, String profesorID) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.profesorID = profesorID;
    }

    public Profesor(String ime, String prezime, String email) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
    }

    public Profesor() {
    }

    public Profesor(String ime, String prezime) {
        this.ime = ime;
        this.prezime = prezime;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfesorID() {
        return profesorID;
    }

    public void setProfesorID(String profesorID) {
        this.profesorID = profesorID;
    }
}
