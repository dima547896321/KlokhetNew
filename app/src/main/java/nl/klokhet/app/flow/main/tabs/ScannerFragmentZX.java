package nl.klokhet.app.flow.main.tabs;


import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.LocationRequest;
import com.google.zxing.Result;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.disposables.CompositeDisposable;
import jp.wasabeef.blurry.Blurry;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import nl.klokhet.app.App;
import nl.klokhet.app.R;
import nl.klokhet.app.data.network.providers.ProviderModule;
import nl.klokhet.app.data.network.responce.Lesson;
import nl.klokhet.app.data.network.responce.UserInGroup;
import nl.klokhet.app.flow.login.LoginActivity;
import nl.klokhet.app.flow.main.MainActivity;
import nl.klokhet.app.utils.RxUtils;
import timber.log.Timber;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScannerFragmentZX#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScannerFragmentZX extends Fragment implements ZXingScannerView.ResultHandler {
    public boolean showShowDialog = false;
    //    public CodeScanner mCodeScanner;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    CompositeDisposable compositeDisposable;
    Lesson lesson;
    //    CodeScannerView scannerView;
    View layChekinUser;
    ImageView layUserImage;
    TextView layUserName;
    TextView tvUserLastName;
    TextView layUserIn;
    Button layBtnChekin;
    Button layBtnCancel;
    Handler handler = new Handler();
    AlertDialog dialog;  //dialog for show empty lesson
    private TextView tvLesson;
    private TextView tvGroup;
    private TextView tvOn;
    private TextView tvLocation;
    private TextView tvTeacher;
    private ImageView ivFlipCam;
    private LocationManager locationManager;
    private int camera = 0; // 0-back  1-front
    private ScannerFragmentZX fragment = this;
    private boolean tabSelected = false;
    private View layShowOk;
    private ZXingScannerView mScannerView;

    public ScannerFragmentZX() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScannerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScannerFragmentZX newInstance() {
        ScannerFragmentZX fragment = new ScannerFragmentZX();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_zx, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvLesson = view.findViewById(R.id.tvLesson);
        tvGroup = view.findViewById(R.id.tvGroup);
        tvOn = view.findViewById(R.id.tvOn);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvTeacher = view.findViewById(R.id.tvTeacher);
        ivFlipCam = view.findViewById(R.id.ivFlipCam);
        layChekinUser = view.findViewById(R.id.layChekinUser);
        layUserImage = layChekinUser.findViewById(R.id.tvUserPhoto);
        layUserName = layChekinUser.findViewById(R.id.tvUserName);
        tvUserLastName = layChekinUser.findViewById(R.id.tvUserLastName);
        layUserIn = layChekinUser.findViewById(R.id.tvOn);
        layBtnChekin = layChekinUser.findViewById(R.id.btnChekin);
//        layBtnChekin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        layBtnCancel = layChekinUser.findViewById(R.id.btnLogout);
        layBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layChekinUser.getVisibility() == View.VISIBLE) {
                    layChekinUser.setVisibility(View.GONE);
//                    mCodeScanner.startPreview();
                }
            }
        });
        ivFlipCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (camera == 1) {
                    camera = 0;
                } else {

                    camera = 1;
                }

                mScannerView.stopCamera();
                mScannerView.startCamera(camera);
            }
        });
        if (lesson != null) {
            tvLesson.setText(lesson.getLessonName());
            tvGroup.setText(String.valueOf(lesson.getGroupId()));

            try {
                SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                SimpleDateFormat sdfOut1 = new SimpleDateFormat("HH:mm");
                Date date = sdfIn.parse(lesson.getDateTimeStart().getDate());

                tvOn.setText(sdfOut.format(date) + " - " + sdfOut1.format(date.getTime() + lesson.getDuration() * 60));
            } catch (ParseException e) {
                Log.d("Error", e.getMessage());
            }

            tvLocation.setText(lesson.getLocationName());
            tvTeacher.setText(lesson.getTeacherName());
        }
//        scannerView = getView().findViewById(R.id.scanner_view);
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5000);
        layShowOk = getView().findViewById(R.id.layShowOk);
        mScannerView = new ZXingScannerView(getActivity());
//        loadCamera();
    }

    public void tabSelected() {
        tabSelected = true;
//        if (mCodeScanner != null) {
//            mCodeScanner.startPreview();
//        }
    }

    public void tabUnSelected() {
        tabSelected = false;
    }

//    public void loadCamera() {
//
//        if (checkPermissionsCamera() && getActivity() != null) {
//
////            if (mCodeScanner != null) {
////                return;
////            }
////            mCodeScanner = new CodeScanner(getActivity(), scannerView);
////            mCodeScanner.setFlashEnabled(false);
//            mCodeScanner.setDecodeCallback(new DecodeCallback() {
//                @Override
//                public void onDecoded(@NonNull final Result result) {
//
//
//                }
//            });
//            scannerView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mCodeScanner.startPreview();
//                }
//            });
//            mCodeScanner.startPreview();
//        } else {
//            if (getActivity() != null)
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 555);
//        }
//    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.setAutoFocus(true);
        mScannerView.startCamera();
    }


    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    private boolean checkPermissionsCamera() {
        if (getActivity() == null) {
            return false;
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
            LoginActivity.start(getActivity(), true);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
    }

    public void showToast(String message) {
        if (getActivity() != null)
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void showCurrentLesson(Lesson lesson) {
        this.lesson = lesson;
        if (lesson == null) {
            tvLesson.setText("");
            tvGroup.setText("");
            tvOn.setText("");
            tvLocation.setText("");
            tvTeacher.setText("");
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            if (lesson.getName() != null && !lesson.getName().isEmpty()) {
                stringBuffer.append(lesson.getName() + " ");
            }
            if (lesson.getLessonName() != null && !lesson.getLessonName().isEmpty()) {
                stringBuffer.append(lesson.getLessonName() + " ");
            }
            if (lesson.getLessonTypeName() != null && !lesson.getLessonTypeName().isEmpty()) {
                stringBuffer.append("(" + lesson.getLessonTypeName() + ")");
            }
            tvLesson.setText(stringBuffer.toString());
            tvGroup.setText(lesson.getGroupName());
            try {
                SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                SimpleDateFormat sdfOut1 = new SimpleDateFormat("HH:mm");
                Date date = sdfIn.parse(lesson.getDateTimeStart().getDate());
                Date date1 = new Date(date.getTime() + (lesson.getDuration() * 60 * 1000));
                tvOn.setText(sdfOut.format(date) + " - " + sdfOut1.format(date1));
            } catch (ParseException e) {
                tvOn.setText("");
            }
            tvLocation.setText(lesson.getLocationName());
            tvTeacher.setText(lesson.getTeacherName());
        }
    }

    private boolean CheckStartTime(UserInGroup user) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startDaate = sdf.parse(user.getTime().getDate());
            Date now = new Date();
            if (startDaate.before(now)) {
                return true;
            } else {
                showToast("Lesson not start");
                playSoundError();
                vibrateDevice();
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean checkLocation(UserInGroup user) {
        if (getActivity() != null) {
            if (MainActivity.location == null) {
                Toast.makeText(getActivity(), "Current position not found", Toast.LENGTH_LONG).show();
                return true;
            }
            if (user.getRadius() == null || user.getRadius().isEmpty() || user.getRadius().equals("0")) {
                Timber.d("Radius is null or empty or 0");
                return true;
            }
            if (lesson == null || lesson.getLocationLat() == null || lesson.getLocationLon() == null) {
                Timber.d("Lesson is null or empty ");
                return true;
            }
            float[] result = new float[2];
            Location.distanceBetween(MainActivity.location.getLatitude(), MainActivity.location.getLongitude(), lesson.getLocationLat(), lesson.getLocationLon(), result);
            Timber.d("My location : " + MainActivity.location.toString());
            Timber.d("School location : " + lesson.getLocationLat() + " " + lesson.getLocationLon());
            Timber.d("Radius is: " + user.getRadius());
            if ((result[0] / 1000.0) <= Double.parseDouble(user.getRadius())) {
                Timber.d("Distance is: " + result[0]);
                return true;
            } else {
                playSoundError();
                vibrateDevice();
                showToast("Wrong location");
                return false;
            }
        } else {
            Timber.d("Activity null ");
            return false;
        }
    }

    public void showOkView() {
        layChekinUser.setVisibility(View.GONE);
        layShowOk.setVisibility(View.VISIBLE);
        ImageView ivTransparent = layShowOk.findViewById(R.id.ivTransparent);
        Blurry.with(getContext()).
                color(Color.argb(100, 200, 200, 200))
                .animate()
                .async()
                .capture(mScannerView).into(ivTransparent);
        layShowOk.postDelayed(() -> {
            layShowOk.setVisibility(View.GONE);
            mScannerView.resumeCameraPreview(this);
        }, 3000);

    }

    public void vibrateDevice() {
        if (getActivity() != null) {
            Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createWaveform(new long[]{150, 250, 500}, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(500);
            }
        }
    }

    private void playSoundOk() {
        if (getActivity() != null) {
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getActivity(), notification);
                r.play();
            } catch (Exception e) {
                Timber.d(e.getMessage());
            }
        }
    }

    private void playSoundError() {
        if (getActivity() != null) {
            MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.erro);
            mediaPlayer.start();
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        if (!tabSelected) {
            Timber.d("Not in tub");
            return;
        }
        if (MainActivity.lessonsResponce != null && MainActivity.location != null && compositeDisposable != null) {
            if (layChekinUser.getVisibility() == View.VISIBLE || layShowOk.getVisibility() == View.VISIBLE) {
                Timber.d("Not start, because other items visible");
                return;
            }
            compositeDisposable.add(
                    ProviderModule
                            .getUserProvider()
                            .getUserProfile(rawResult.getText(), String.valueOf(MainActivity.lessonsResponce.getCurrent().getId()))
                            .compose(RxUtils.ioToMainTransformerSingle())
                            .subscribe(done -> {
                                        if (done.getStatus().getSuccess()) {
                                            vibrateDevice();
                                            playSoundOk();
                                            layChekinUser.setVisibility(View.VISIBLE);
                                            layUserName.setText(done.getData().getStudent().getFirst_name());
                                            tvUserLastName.setText(done.getData().getStudent().getLast_name());
                                            try {

                                                if (fragment != null && getActivity() != null) {
                                                    UserInGroup user = ((MainActivity) fragment.getActivity()).getUserFromList(done.getData().getStudent().getId());
                                                    UserInGroup returnUser = done.getData().getStudent();
                                                    returnUser.setStatus(user.getStatus());
                                                    if (returnUser != null) {
                                                        if (returnUser.getStatus().equalsIgnoreCase("Not checkin")) {
                                                            layBtnChekin.setBackground(App.getInstance().getResources().getDrawable(R.drawable.btn_shape_round_green));
                                                            layBtnChekin.setText(App.getInstance().getResources().getString(R.string.check_in));
                                                            SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                                            SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                                            SimpleDateFormat sdfOut1 = new SimpleDateFormat("HH:mm");
                                                            Date date = sdfIn.parse(done.getData().getStudent().getTime().getDate());
                                                            layUserIn.setText(sdfOut.format(date));
                                                            layBtnChekin.setOnClickListener(
                                                                    v -> {
                                                                        if (checkLocation(returnUser) && CheckStartTime(returnUser)) {
                                                                            compositeDisposable.add(ProviderModule.getUserProvider().checkin(MainActivity.location.getLatitude(), MainActivity.location.getLongitude(), rawResult.getText(), String.valueOf(MainActivity.lessonsResponce.getCurrent().getId()))
                                                                                    .compose(RxUtils.ioToMainTransformerSingle())
                                                                                    .subscribe(done12 -> {
                                                                                        if (done12.getStatus().getSuccess()) {
                                                                                            layChekinUser.setVisibility(View.GONE);
                                                                                            if (fragment != null) {
//                                                                                                            fragment.showToast(done12.getStatus().getMessage());
                                                                                                if (fragment.getActivity() != null) {
                                                                                                    returnUser.setStatus("checkin");
                                                                                                    ((MainActivity) getActivity()).updateUserInList(returnUser);
                                                                                                }
                                                                                                showOkView();
                                                                                                playSoundOk();
                                                                                                vibrateDevice();
                                                                                            }
//                                                                                                        Timber.d("Done");
                                                                                            layChekinUser.setVisibility(View.GONE);
                                                                                            mScannerView.resumeCameraPreview(fragment);
                                                                                        } else {
                                                                                            layChekinUser.setVisibility(View.GONE);
                                                                                            if (fragment != null) {
                                                                                                fragment.showToast(done12.getStatus().getMessage());
                                                                                                if (fragment.getActivity() != null) {
                                                                                                    ((MainActivity) getActivity()).updateUserInList(returnUser);
                                                                                                }
                                                                                            }
                                                                                            Timber.d(done12.getStatus().getMessage());
                                                                                            layChekinUser.setVisibility(View.GONE);
                                                                                            mScannerView.resumeCameraPreview(fragment);
                                                                                            vibrateDevice();
                                                                                            playSoundError();
                                                                                        }
                                                                                    }, error -> {
                                                                                        Timber.d(error.getMessage());
                                                                                        if (fragment != null) {
                                                                                            fragment.showToast(error.getMessage());
                                                                                        }
                                                                                        layChekinUser.setVisibility(View.GONE);
                                                                                        mScannerView.resumeCameraPreview(fragment);
                                                                                        vibrateDevice();
                                                                                        playSoundError();
                                                                                    }));
                                                                        } else {
                                                                            mScannerView.resumeCameraPreview(fragment);
                                                                        }


                                                                    });
                                                        } else if (returnUser.getStatus().equalsIgnoreCase("checkin")) {
                                                            layBtnChekin.setBackground(App.getInstance().getResources().getDrawable(R.drawable.btn_shape_round_orange));
                                                            layBtnChekin.setText(App.getInstance().getResources().getString(R.string.check_out));
                                                            SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                            SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                                            SimpleDateFormat sdfOut1 = new SimpleDateFormat("HH:mm");
                                                            Date date = sdfIn.parse(done.getData().getStudent().getTime().getDate());
//                                                                        if (!returnUser.getTime_out().isEmpty()) {
//                                                                            Date dateOut = sdfIn.parse(returnUser.getTime_out());
//                                                                            layUserIn.setText(sdfOut.format(date) + " - " + sdfOut1.format(dateOut));
//                                                                        } else {
                                                            layUserIn.setText(sdfOut.format(date));
//                                                                        }
                                                            layBtnChekin.setOnClickListener(v -> {
                                                                if (checkLocation(returnUser) && CheckStartTime(returnUser)) {
                                                                    compositeDisposable.add(ProviderModule.getUserProvider().checkOut(rawResult.getText(), String.valueOf(MainActivity.lessonsResponce.getCurrent().getId()))
                                                                            .compose(RxUtils.ioToMainTransformerSingle())
                                                                            .subscribe(done1 -> {
                                                                                if (done1.getStatus().getSuccess()) {
                                                                                    layChekinUser.setVisibility(View.GONE);
                                                                                    Timber.d("Done");
                                                                                    if (fragment != null) {
                                                                                        fragment.showToast(done1.getStatus().getMessage());
                                                                                        if (fragment.getActivity() != null) {
                                                                                            returnUser.setStatus("checkout");
                                                                                            returnUser.setTime_out(sdfIn.format(new Date()));
                                                                                            ((MainActivity) getActivity()).updateUserInList(returnUser);
                                                                                        }
                                                                                        showOkView();
                                                                                        playSoundOk();
                                                                                        vibrateDevice();
                                                                                    }
                                                                                    layChekinUser.setVisibility(View.GONE);
                                                                                    mScannerView.resumeCameraPreview(fragment);
                                                                                } else {
                                                                                    layChekinUser.setVisibility(View.GONE);
                                                                                    if (fragment != null) {
                                                                                        fragment.showToast(done1.getStatus().getMessage());
                                                                                        if (fragment.getActivity() != null) {
                                                                                            ((MainActivity) getActivity()).updateUserInList(returnUser);
                                                                                        }
                                                                                    }
                                                                                    Timber.d(done1.getStatus().getMessage());
                                                                                    layChekinUser.setVisibility(View.GONE);
                                                                                    mScannerView.resumeCameraPreview(fragment);
                                                                                    vibrateDevice();
                                                                                    playSoundError();
                                                                                }
                                                                            }, error -> {
                                                                                Timber.d(error.getMessage());
                                                                                layChekinUser.setVisibility(View.GONE);
                                                                                mScannerView.resumeCameraPreview(fragment);
                                                                                vibrateDevice();
                                                                                playSoundError();
                                                                            })
                                                                    );
                                                                } else {
                                                                    mScannerView.resumeCameraPreview(fragment);
                                                                }
                                                            });

                                                        } else if (returnUser.getStatus().equalsIgnoreCase("checkout")) {
                                                            layBtnChekin.setBackground(App.getInstance().getResources().getDrawable(R.drawable.btn_shape_round_green));
                                                            layBtnChekin.setText(App.getInstance().getResources().getString(R.string.check_in));
                                                            SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                                            SimpleDateFormat sdfIn1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                            SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                                            SimpleDateFormat sdfOut1 = new SimpleDateFormat("HH:mm");
                                                            Date date = sdfIn.parse(done.getData().getStudent().getTime().getDate());
                                                            layUserIn.setText(sdfOut.format(date));
                                                            layBtnChekin.setOnClickListener(v ->
                                                            {
                                                                if (checkLocation(returnUser) && CheckStartTime(returnUser)) {
                                                                    compositeDisposable.add(ProviderModule.getUserProvider().checkin(MainActivity.location.getLatitude(), MainActivity.location.getLongitude(), rawResult.getText(), String.valueOf(MainActivity.lessonsResponce.getCurrent().getId()))
                                                                            .compose(RxUtils.ioToMainTransformerSingle())
                                                                            .subscribe(done13 -> {
                                                                                if (done13.getStatus().getSuccess()) {
                                                                                    layChekinUser.setVisibility(View.GONE);
                                                                                    if (fragment != null) {
                                                                                        fragment.showToast(done13.getStatus().getMessage());
                                                                                        if (fragment.getActivity() != null) {
                                                                                            returnUser.setStatus("checkin");
                                                                                            returnUser.setTime_in(sdfIn1.format(new Date()));
                                                                                            ((MainActivity) getActivity()).updateUserInList(returnUser);

                                                                                        }
                                                                                        showOkView();
                                                                                        playSoundOk();
                                                                                        vibrateDevice();
                                                                                    }
                                                                                    Timber.d("Done");
                                                                                    layChekinUser.setVisibility(View.GONE);
                                                                                    mScannerView.resumeCameraPreview(fragment);
                                                                                } else {
                                                                                    layChekinUser.setVisibility(View.GONE);
                                                                                    Timber.d(done13.getStatus().getMessage());
                                                                                    if (fragment != null) {
                                                                                        fragment.showToast(done13.getStatus().getMessage());
                                                                                        if (fragment.getActivity() != null) {
                                                                                            ((MainActivity) getActivity()).updateUserInList(returnUser);

                                                                                        }
                                                                                    }
                                                                                    layChekinUser.setVisibility(View.GONE);
                                                                                    mScannerView.resumeCameraPreview(fragment);
                                                                                    vibrateDevice();
                                                                                    playSoundError();
                                                                                }
                                                                            }, error -> {
                                                                                Timber.d(error.getMessage());
                                                                                if (fragment != null) {
                                                                                    fragment.showToast(error.getMessage());
                                                                                }
                                                                                layChekinUser.setVisibility(View.GONE);
                                                                                mScannerView.resumeCameraPreview(fragment);
                                                                                vibrateDevice();
                                                                                playSoundError();
                                                                            }));
                                                                } else {
                                                                    mScannerView.resumeCameraPreview(fragment);
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            } catch (ParseException e) {
                                                Timber.d(e.getMessage());
                                                if (fragment != null) {
                                                    fragment.showToast(done.getData().getStudent().getTime().getDate().replace(".000", ""));
                                                }
                                                mScannerView.resumeCameraPreview(fragment);
                                                vibrateDevice();
                                            }
                                            if (done.getData().getStudent().getPhoto().isEmpty() || done.getData().getStudent().getPhoto().contains("no_photo")) {
                                                layUserImage.setImageResource(R.drawable.ic_user);
                                            } else {
                                                Glide.with(App.getInstance().getApplicationContext())
                                                        .load("https://klokhet.sapient.pro" + done.getData().getStudent().getPhoto())
                                                        .apply(RequestOptions.circleCropTransform())
                                                        .into(layUserImage);
                                            }
                                        } else {
                                            Timber.d(done.getStatus().getMessage());
                                            if (fragment != null) {
                                                fragment.showToast(done.getStatus().getMessage());
                                            }
                                            mScannerView.resumeCameraPreview(fragment);
                                            vibrateDevice();
                                            playSoundError();
                                        }
                                    }, error -> {
                                        Timber.d(error.getMessage());
                                        if (fragment != null) {
                                            fragment.showToast(error.getMessage().replace(".000", ""));
                                        }
                                        mScannerView.resumeCameraPreview(fragment);
                                        vibrateDevice();
                                        playSoundError();
                                    }

                            )
            );
        } else {
            handler.postDelayed(() -> {
                Timber.d("Location not found yet");
                if (fragment != null && fragment.isVisible()) {
                    if (MainActivity.location == null) {
                        fragment.showToast("Location not found yet");
                        playSoundError();
                        vibrateDevice();
                    }
                }
                mScannerView.resumeCameraPreview(fragment);
            }, 150);
        }
    }
}
