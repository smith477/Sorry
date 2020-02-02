package martin.sms.Admin;

import android.os.Parcel;
import android.os.Parcelable;

public class Admin {

    private String ime;
    private String prezime;
    private String email;
    private String adminID;


    public Admin(String ime, String prezime, String email, String adminID) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.adminID = adminID;
    }

    public Admin() {
    }

    public Admin(String ime, String prezime, String email) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
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

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }
}
