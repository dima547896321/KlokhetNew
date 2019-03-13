package nl.klokhet.app.flow.main.tabs;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import nl.klokhet.app.App;
import nl.klokhet.app.R;
import nl.klokhet.app.data.network.responce.TeacherInfoResponce;
import nl.klokhet.app.flow.login.LoginActivity;
import nl.klokhet.app.flow.main.MainActivity;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsMainFragment extends Fragment implements View.OnClickListener {
    TeacherInfoResponce teacher;
    Typeface typeface;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ImageView tvUserPhoto;
    private TextView tvUserName;
    private TextView tvSchool;
    private TextView tvLesson;
    private TextView tvOn;
    private Button btnManualMode;
    private Button btnLogout;
    private FrameLayout fragmentContainer;

    public SettingsMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsMainFragment newInstance() {
        SettingsMainFragment fragment = new SettingsMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "PT_Sans-Web-Bold.ttf");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_main, container, false);
    }

    public void setTeacher(TeacherInfoResponce teacher) {
        this.teacher = teacher;
        if (isResumed())
            bindTeacher();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvUserPhoto = view.findViewById(R.id.tvUserPhoto);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvSchool = view.findViewById(R.id.tvSchool);
        tvLesson = view.findViewById(R.id.tvLesson);
        tvOn = view.findViewById(R.id.tvOn);
        btnManualMode = view.findViewById(R.id.btnManualMode);
        btnManualMode.setTypeface(typeface);

        btnLogout = view.findViewById(R.id.btnLogout);
        fragmentContainer = view.findViewById(R.id.fragmentContainer);

        btnLogout.setOnClickListener(v -> {
            LoginActivity.start(getContext(), true);
            getActivity().finish();
        });
        btnManualMode.setOnClickListener(this);
//        bindTeacher();
    }

    @Override
    public void onResume() {
        super.onResume();
        bindTeacher();
    }

    public void bindTeacher() {
        if (tvUserPhoto == null) {
            Timber.d("Cannot bind teacher: tvUserPhoto is null");
            return;
        }
        if (teacher != null) {
            if (!teacher.getTeacher().getPhoto().contains("no_photo")) {
                Glide.with(App.getInstance().getApplicationContext())
                        .load("https://klokhet.sapient.pro" + teacher.getTeacher().getPhoto())
                        .apply(RequestOptions.circleCropTransform())
                        .into(tvUserPhoto);
            } else {
                tvUserPhoto.setImageResource(R.drawable.ic_user);
            }
            tvUserName.setText(teacher.getTeacher().getFirstName() + " " + teacher.getTeacher().getLastName());
        }
        if (MainActivity.lessonsResponce != null) {
            if (MainActivity.lessonsResponce.getCurrent() != null && MainActivity.lessonsResponce.getCurrent().getLocationName() != null) {
                tvSchool.setText(" " + MainActivity.lessonsResponce.getCurrent().getLocationName());
            } else {
                tvSchool.setText("");
            }
            if (MainActivity.lessonsResponce.getCurrent() != null && MainActivity.lessonsResponce.getCurrent().getName() != null) {
                tvLesson.setText(" " + MainActivity.lessonsResponce.getCurrent().getName());
                StringBuffer stringBuffer = new StringBuffer();
                if (MainActivity.lessonsResponce.getCurrent().getName() != null && !MainActivity.lessonsResponce.getCurrent().getName().isEmpty()) {
                    stringBuffer.append(MainActivity.lessonsResponce.getCurrent().getName() + " ");
                }
                if (MainActivity.lessonsResponce.getCurrent().getLessonName() != null && !MainActivity.lessonsResponce.getCurrent().getLessonName().isEmpty()) {
                    stringBuffer.append(MainActivity.lessonsResponce.getCurrent().getLessonName() + " ");
                }
                if (MainActivity.lessonsResponce.getCurrent().getLessonTypeName() != null && !MainActivity.lessonsResponce.getCurrent().getLessonTypeName().isEmpty()) {
                    stringBuffer.append("(" + MainActivity.lessonsResponce.getCurrent().getLessonTypeName() + ")");
                }
                tvLesson.setText(stringBuffer.toString());
            }
            if (MainActivity.lessonsResponce.getCurrent() != null && MainActivity.lessonsResponce.getCurrent().getDateTimeStart() != null) {
                try {
                    SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    SimpleDateFormat sdfOut1 = new SimpleDateFormat("HH:mm");
                    Date date = sdfIn.parse(MainActivity.lessonsResponce.getCurrent().getDateTimeStart().getDate());

                    tvOn.setText(sdfOut.format(date) + " - " + sdfOut1.format(date.getTime() + MainActivity.lessonsResponce.getCurrent().getDuration() * 60 * 1000));
                } catch (ParseException e) {
                    Log.d("Error", e.getMessage());
                }
            } else {
                tvLesson.setText("");
                tvOn.setText("");
            }
        } else {
            tvLesson.setText("");
            tvOn.setText("");
        }
    }

    public void hideContainer() {
        fragmentContainer.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnManualMode.getId()) {
            showManualModeDialog();
        }
    }

    public void showManualModeDialog() {
        if (!isResumed()) {
            return;
        }
        fragmentContainer.setVisibility(View.VISIBLE);
        SettingDetailFragment fragment = (SettingDetailFragment) getChildFragmentManager().findFragmentByTag(SettingDetailFragment.class.getSimpleName());
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (fragment != null) {
            ft.remove(fragment);
        }

        SettingDetailFragment fragmentNew = SettingDetailFragment.newInstance();
        ft.add(R.id.fragmentContainer, fragmentNew, SettingDetailFragment.class.getSimpleName());
        ft.addToBackStack(this.getClass().getSimpleName());
        ft.commit();

    }
}
