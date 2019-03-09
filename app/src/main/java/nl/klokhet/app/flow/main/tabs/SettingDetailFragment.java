package nl.klokhet.app.flow.main.tabs;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import nl.klokhet.app.R;
import nl.klokhet.app.data.network.responce.ItemContextResponce;
import nl.klokhet.app.flow.main.MainActivity;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingDetailFragment extends Fragment implements View.OnClickListener {
    DatePickerDialog.OnDateSetListener d;
    Calendar dateAndTimeNow = Calendar.getInstance();
    Calendar dateAndTime = Calendar.getInstance();
    Calendar timePickerStart = Calendar.getInstance();
    Calendar timePickerEnd = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd / MM / yyyy");
    int DIALOG_TIME = 1;
    private Spinner spLesson;
    private Spinner spLessonType;
    private Spinner spGroup;
    private Spinner spLocation;
    private TextView tvLessonDate;
    private Button btnStart;
    private Date selectedDate;
    private ImageView ivBack;
    private Fragment fragment;
    private TextView tvLessonStart;
    TimePickerDialog.OnTimeSetListener myCallBack = (view, hourOfDay, minute) -> {
        timePickerStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
        timePickerStart.set(Calendar.MINUTE, minute);
        setStartDate();
    };
    private TextView tvLessonEnd;
    TimePickerDialog.OnTimeSetListener myCallBack1 = (view, hourOfDay, minute) -> {
        timePickerEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
        timePickerEnd.set(Calendar.MINUTE, minute);
        Timber.d(timePickerStart.get(Calendar.YEAR) + " " + timePickerStart.get(Calendar.HOUR_OF_DAY) + " " + timePickerStart.get(Calendar.MINUTE));
        Timber.d(timePickerEnd.get(Calendar.YEAR) + " " + timePickerEnd.get(Calendar.HOUR_OF_DAY) + " " + timePickerEnd.get(Calendar.MINUTE));
        if (timePickerEnd.before(timePickerStart) || timePickerEnd.equals(timePickerStart)) {
            if (timePickerStart.get(Calendar.HOUR_OF_DAY) == 23 && timePickerStart.get(Calendar.MINUTE) >= 55) {
                timePickerStart.set(Calendar.MINUTE, 50);
                timePickerEnd.set(Calendar.MINUTE, 55);
                timePickerEnd.set(Calendar.HOUR_OF_DAY, 0);
            } else {
                timePickerEnd.set(Calendar.MINUTE, timePickerStart.get(Calendar.MINUTE) + 1);
                timePickerEnd.set(Calendar.HOUR_OF_DAY, timePickerStart.get(Calendar.HOUR_OF_DAY));
            }
        }
        Timber.d(timePickerStart.get(Calendar.YEAR) + " " + timePickerStart.get(Calendar.HOUR_OF_DAY) + " " + timePickerStart.get(Calendar.MINUTE));
        Timber.d(timePickerEnd.get(Calendar.YEAR) + " " + timePickerEnd.get(Calendar.HOUR_OF_DAY) + " " + timePickerEnd.get(Calendar.MINUTE));
        setStartDate();
        setEndDate();
    };

    public SettingDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingDetailFragment newInstance() {
        SettingDetailFragment fragment = new SettingDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void setStartDate() {
        String hour = String.valueOf(timePickerStart.get(Calendar.HOUR_OF_DAY));//> 9 ? timePickerStart.get(Calendar.HOUR) + "" : "0" + timePickerStart.get(Calendar.HOUR) + "";
        int min = timePickerStart.get(Calendar.MINUTE);
        String minutes = min > 9 ? String.valueOf(timePickerStart.get(Calendar.MINUTE)) : "0" + String.valueOf(timePickerStart.get(Calendar.MINUTE));//> 9 ? timePickerStart.get(Calendar.MINUTE) + "" : "0" + timePickerStart.get(Calendar.MINUTE) + "";
        tvLessonStart.setText(hour + " : " + minutes);
    }

    private void setEndDate() {
        String hour1 = String.valueOf(timePickerEnd.get(Calendar.HOUR_OF_DAY));// > 9 ? timePickerEnd.get(Calendar.HOUR) + "" : "0" + timePickerEnd.get(Calendar.HOUR) + "";
        int min = timePickerEnd.get(Calendar.MINUTE);
        String minutes = min > 9 ? String.valueOf(timePickerEnd.get(Calendar.MINUTE)) : "0" + String.valueOf(timePickerEnd.get(Calendar.MINUTE));
        tvLessonEnd.setText(hour1 + " : " + minutes);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = this;
        selectedDate = new Date();
        d = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String text = "Incorrect start date";

                dateAndTime.set(Calendar.YEAR, year);
                dateAndTime.set(Calendar.MONTH, monthOfYear);
                dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (dateAndTime.before(dateAndTimeNow)) {
                    Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                    dateAndTime.set(Calendar.YEAR, dateAndTimeNow.get(Calendar.YEAR));
                    dateAndTime.set(Calendar.MONTH, dateAndTimeNow.get(Calendar.MONTH));
                    dateAndTime.set(Calendar.DAY_OF_MONTH, dateAndTimeNow.get(Calendar.DAY_OF_MONTH));
                }
                setInitialDateTime();
            }
        };
    }

    private void setInitialDateTime() {
        tvLessonDate.setText(sdf.format(dateAndTime.getTime()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivBack = view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commit();
        });
        if (MainActivity.shedulerContext != null) {
            spLesson = view.findViewById(R.id.spLesson);
//            spLesson.setPrompt("Choose lesson");
            ArrayAdapter spLessonAdapter = new ArrayAdapter(getContext(),
                    R.layout.item_spinner_item, MainActivity.shedulerContext.getLesson().toArray(new ItemContextResponce[MainActivity.shedulerContext.getLesson().size()]));
            spLesson.setAdapter(spLessonAdapter);


            spLessonType = view.findViewById(R.id.spLessonType);
//            spLessonType.setPrompt("Choose lesson type");
            ArrayAdapter spLessonTypeAdapter = new ArrayAdapter(getContext(),
                    R.layout.item_spinner_item, MainActivity.shedulerContext.getLessonType().toArray(new ItemContextResponce[MainActivity.shedulerContext.getLessonType().size()]));
            spLessonType.setAdapter(spLessonTypeAdapter);

            spGroup = view.findViewById(R.id.spGroup);
            spGroup.setPrompt("Choose group");
            ArrayAdapter spGroupAdapter = new ArrayAdapter(getContext(),
                    R.layout.item_spinner_item, MainActivity.shedulerContext.getGroup().toArray(new ItemContextResponce[MainActivity.shedulerContext.getGroup().size()]));
            spGroup.setAdapter(spGroupAdapter);

            spLocation = view.findViewById(R.id.spLocation);
            spLocation.setPrompt("Choose location");
            ArrayAdapter spLocationAdapter = new ArrayAdapter(getContext(),
                    R.layout.item_spinner_item, MainActivity.shedulerContext.getLocation().toArray(new ItemContextResponce[MainActivity.shedulerContext.getLocation().size()]));
            spLocation.setAdapter(spLocationAdapter);


        }
        tvLessonDate = view.findViewById(R.id.tvLessonDate);
        tvLessonDate.setText(sdf.format(dateAndTime.getTime()));
        tvLessonDate.setOnClickListener(v -> new DatePickerDialog(getContext(), d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show()
        );
        tvLessonStart = view.findViewById(R.id.tvLessonStart);
        setStartDate();
        tvLessonStart.setOnClickListener(v -> onCreateDialog(1));
        tvLessonEnd = view.findViewById(R.id.tvLessonEnd);
        timePickerEnd.set(Calendar.HOUR_OF_DAY, timePickerEnd.get(Calendar.HOUR_OF_DAY) + 1);
        setEndDate();
        tvLessonEnd.setOnClickListener(v -> {
            timePickerEnd.set(Calendar.HOUR_OF_DAY, timePickerEnd.get(Calendar.HOUR_OF_DAY));
            onCreateDialog(2);
        });
        btnStart = view.findViewById(R.id.btnStart);
        btnStart.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "PT_Sans-Web-Bold.ttf"));
        btnStart.setOnClickListener(this);
    }

    private void onCreateDialog(int id) {
        TimePickerDialog tpd = new TimePickerDialog(getContext(),
                id == 1 ? myCallBack : myCallBack1,
                id == 1 ? timePickerStart.get(Calendar.HOUR_OF_DAY) : timePickerEnd.get(Calendar.HOUR_OF_DAY),
                id == 1 ? timePickerStart.get(Calendar.MINUTE) : timePickerEnd.get(Calendar.MINUTE),
                true);
        tpd.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnStart.getId()) {
            if (getActivity() != null) {
                if (spLesson.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), MainActivity.shedulerContext.getLesson().get(spLesson.getSelectedItemPosition()).getName(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (spLessonType.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), MainActivity.shedulerContext.getLessonType().get(spLessonType.getSelectedItemPosition()).getName(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (spGroup.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), MainActivity.shedulerContext.getGroup().get(spGroup.getSelectedItemPosition()).getName(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (spLocation.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), MainActivity.shedulerContext.getLocation().get(spLocation.getSelectedItemPosition()).getName(), Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateAndTime.set(Calendar.HOUR_OF_DAY, timePickerStart.get(Calendar.HOUR_OF_DAY));
                dateAndTime.set(Calendar.MINUTE, timePickerStart.get(Calendar.MINUTE));
                dateAndTime.set(Calendar.SECOND, timePickerStart.get(Calendar.SECOND));
                long duration = timePickerEnd.getTime().getTime() - timePickerStart.getTime().getTime();
                Timber.d("Choto tam");
                ((MainActivity) getActivity()).saveSchedule(
                        MainActivity.shedulerContext.getLesson().get(spLesson.getSelectedItemPosition()).getName(),
                        String.valueOf(MainActivity.shedulerContext.getLesson().get(spLesson.getSelectedItemPosition()).getId()),
                        String.valueOf(MainActivity.shedulerContext.getLessonType().get(spLessonType.getSelectedItemPosition()).getId()),
                        String.valueOf(MainActivity.shedulerContext.getGroup().get(spGroup.getSelectedItemPosition()).getId()),
                        String.valueOf(MainActivity.shedulerContext.getLocation().get(spLocation.getSelectedItemPosition()).getId()),
                        sdf.format(dateAndTime.getTime()),
                        String.valueOf(TimeUnit.MILLISECONDS.toMinutes(duration))
                );
            }
        }
    }
}
