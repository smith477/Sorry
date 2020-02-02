package martin.sms;

public class SelectedSubject {

    public boolean isSelected;
    public String subjectName;
    public String id;

    public SelectedSubject(boolean isSelected, String subjectName) {
        this.isSelected = isSelected;
        this.subjectName = subjectName;
    }

    public SelectedSubject() {
    }

    public SelectedSubject(boolean isSelected, String subjectName, String id) {
        this.isSelected = isSelected;
        this.subjectName = subjectName;
        this.id = id;
    }

    public SelectedSubject(String subjectName) {
        this.subjectName = subjectName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
