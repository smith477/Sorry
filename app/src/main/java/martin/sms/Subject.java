package martin.sms;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import martin.sms.Professor.Profesor;
import martin.sms.Student.Student;

public class Subject implements Parcelable {

    private String sifra;
    private String naziv;
    private String kod;
    private String fond_casova;
    private String tr_broj_casova;
    private String subjectID;
    private String godina;
    private String smer;
    private double latitude;
    private double longitude;

    public Subject() {
    }

    public Subject(String sifra, String naziv, String kod, String fond_casova, String subjectID, String godina, String smer) {
        this.sifra = sifra;
        this.naziv = naziv;
        this.kod = kod;
        this.fond_casova = fond_casova;
        this.subjectID = subjectID;
        this.godina = godina;
        this.smer = smer;
    }

    protected Subject(Parcel in) {
        sifra = in.readString();
        naziv = in.readString();
        kod = in.readString();
        fond_casova = in.readString();
        tr_broj_casova = in.readString();
        subjectID = in.readString();
        godina = in.readString();
        smer = in.readString();
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    public Subject(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Subject(String sifra, String naziv, String kod, String fond_casova, String subjectID, double latitude, double longitude, String godina, String smer) {
        this.sifra = sifra;
        this.naziv = naziv;
        this.kod = kod;
        this.fond_casova = fond_casova;
        this.subjectID = subjectID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.godina = godina;
        this.smer = smer;
    }

    public static Creator<Subject> getCREATOR() {
        return CREATOR;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getKod() {
        return kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public String getFond_casova() {
        return fond_casova;
    }

    public void setFond_casova(String fond_casova) {
        this.fond_casova = fond_casova;
    }

    public String getTr_broj_casova() {
        return tr_broj_casova;
    }

    public void setTr_broj_casova(String tr_broj_casova) {
        this.tr_broj_casova = tr_broj_casova;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sifra);
        parcel.writeString(naziv);
        parcel.writeString(kod);
        parcel.writeString(fond_casova);
        parcel.writeString(tr_broj_casova);
        parcel.writeString(subjectID);
        parcel.writeString(godina);
        parcel.writeString(smer);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getGodina () {return godina;}

    public void setGodina (String godina) {this.godina = godina;}

    public String getSmer () {return smer;}

    public void setSmer (String smer) {this.smer = smer;}
}
