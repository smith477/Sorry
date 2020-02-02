package martin.sms.Student;

import android.os.Parcel;
import android.os.Parcelable;

public class Student{

    private String index;
    private String ime;
    private String prezime;
    private String email;
    private String studentID;
    private int broj_casova;

    public Student() {
    }

    public Student(String index, String ime, String prezime, String email, String studentID, int broj_casova) {
        this.index = index;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.studentID = studentID;
        this.broj_casova = broj_casova;
    }

    public Student(String index, String ime, String prezime, String email, String studentID) {
        this.index = index;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.studentID = studentID;
    }

    public Student(String index, String ime, String prezime, int broj_casova) {
        this.index = index;
        this.ime = ime;
        this.prezime = prezime;
        this.broj_casova = broj_casova;
    }

    public Student(String index, String ime, String prezime) {
        this.index = index;
        this.ime = ime;
        this.prezime = prezime;}

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
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

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public int getBroj_casova() {
        return broj_casova;
    }

    public void setBroj_casova(int broj_casova) {
        this.broj_casova = broj_casova;
    }
}
