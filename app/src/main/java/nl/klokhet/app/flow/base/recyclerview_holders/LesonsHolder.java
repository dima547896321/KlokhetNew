package nl.klokhet.app.flow.base.recyclerview_holders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import nl.klokhet.app.R;
import nl.klokhet.app.data.network.responce.Lesson;

public class LesonsHolder extends RecyclerView.ViewHolder {

    private TextView tvLesson;
    private TextView tvGroup;
    private TextView tvOn;
    private TextView tvLocation;
    private TextView tvTeacher;

    public LesonsHolder(View itemView) {
        super(itemView);
        tvLesson = itemView.findViewById(R.id.tvLesson);
        tvGroup = itemView.findViewById(R.id.tvGroup);
        tvOn = itemView.findViewById(R.id.tvOn);
        tvLocation = itemView.findViewById(R.id.tvLocation);
        tvTeacher = itemView.findViewById(R.id.tvTeacher);
    }

    public static LesonsHolder newInstance(View itemView) {
        View view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_lessons_info, null, true);
        return new LesonsHolder(view);
    }

    public void bind(Lesson lesson) {
        if (lesson == null) {
            tvGroup.setText("");
            tvLesson.setText("");
            tvOn.setText("");
            tvLocation.setText("");
            tvTeacher.setText("");
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            if(lesson.getName()!=null && !lesson.getName().isEmpty()){
                stringBuffer.append(lesson.getName() + " ");
            }
            if(lesson.getLessonName()!=null && !lesson.getLessonName().isEmpty()){
                stringBuffer.append(lesson.getLessonName() + " ");
            }
            if(lesson.getLessonTypeName()!=null && !lesson.getLessonTypeName().isEmpty()){
                stringBuffer.append("("+lesson.getLessonTypeName() + ")");
            }
            tvLesson.setText(stringBuffer.toString());
            tvGroup.setText(lesson.getGroupName());
            try {
                SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                SimpleDateFormat sdfOut1 = new SimpleDateFormat("HH:mm");
                Date date = sdfIn.parse(lesson.getDateTimeStart().getDate());

                tvOn.setText(sdfOut.format(date) + " - " +sdfOut1.format(date.getTime()+lesson.getDuration()*60*1000));
            } catch (ParseException e) {
                Log.d("Error", e.getMessage());
            }
            tvLocation.setText(lesson.getLocationName());
            tvTeacher.setText(lesson.getTeacherName());
        }
    }

}
