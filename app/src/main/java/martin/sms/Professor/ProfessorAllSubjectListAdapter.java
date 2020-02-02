package martin.sms.Professor;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import martin.sms.R;
import martin.sms.SelectedSubject;

public class ProfessorAllSubjectListAdapter extends BaseAdapter {

    Activity activity;
    List<SelectedSubject> users;
    LayoutInflater inflater;

    public ProfessorAllSubjectListAdapter(Activity activity) {
        this.activity = activity;
    }

    public ProfessorAllSubjectListAdapter(Activity activity, List<SelectedSubject> users) {
        this.activity = activity;
        this.users = users;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ProfessorAllSubjectListAdapter.ViewHolder holder = null;

        if (view == null){

            view = inflater.inflate(R.layout.activity_all_subjects_list_item, viewGroup, false);

            holder = new ProfessorAllSubjectListAdapter.ViewHolder();

            holder.tvUserName = (TextView)view.findViewById(R.id.tv_subject_name);
            holder.ivCheckBox = (ImageView) view.findViewById(R.id.iv_check_box);

            view.setTag(holder);
        }else
            holder = (ProfessorAllSubjectListAdapter.ViewHolder)view.getTag();

        SelectedSubject model = users.get(i);

        holder.tvUserName.setText(model.getSubjectName());

        if (model.isSelected())
            holder.ivCheckBox.setBackgroundResource(R.drawable.checked);

        else
            holder.ivCheckBox.setBackgroundResource(R.drawable.check);

        return view;

    }

    public void updateRecords(List<SelectedSubject> users){
        this.users = users;

        notifyDataSetChanged();
    }

    class ViewHolder{

        TextView tvUserName;
        ImageView ivCheckBox;

    }
}
