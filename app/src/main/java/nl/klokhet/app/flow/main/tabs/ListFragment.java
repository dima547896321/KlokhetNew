package nl.klokhet.app.flow.main.tabs;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.klokhet.app.R;
import nl.klokhet.app.data.network.responce.Lesson;
import nl.klokhet.app.data.network.responce.UserInGroup;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    UserAdapter adapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private RecyclerView rvList;
    private TextView tvCount;
    private TextView tvAmount;
    private List<UserInGroup> listInAdapter = new ArrayList<>();

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScannerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        adapter = new UserAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvList = view.findViewById(R.id.rvList);
        tvCount = view.findViewById(R.id.tvCount);
        tvAmount = view.findViewById(R.id.tvAmount);

        rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvList.setHasFixedSize(true);
        rvList.setAdapter(adapter);
    }

    public void updateUser(UserInGroup user) {
//        StringBuffer sb = new StringBuffer();


        for(UserInGroup u : listInAdapter){
//            sb.append(String.valueOf(u.getId()) + " ");
        }
//        sb.append("\n");
        for (UserInGroup userInGroup : listInAdapter) {
            if (userInGroup.getId().equals(user.getId())) {
                userInGroup.setStatus(user.getStatus());
//                Timber.d("Was IN");
            }
        }
        for(UserInGroup u : listInAdapter){
//            sb.append(String.valueOf(u.getId()) + " ");
        }
//        Timber.d(sb.toString());
        showGroupInfo(listInAdapter);
    }

    public void showGroupInfo(List<UserInGroup> list) {
        if (!isResumed()){
            return;
        }
        int in = 0;
        List<UserInGroup> chekList = new ArrayList<>();
        for (UserInGroup u : list) {
            Log.d("USER", u.toString());
            if (u.getStatus().equals("checkin")) {
                chekList.add(u);
                in++;
            }
        }
        List<UserInGroup> uncheckList = new ArrayList<>();
        for (UserInGroup u : list) {
            Log.d("USER", u.toString());
            if (!u.getStatus().equals("checkin")) {
                uncheckList.add(u);
            }
        }
        List<UserInGroup> returnList = new ArrayList<>();
        returnList.addAll(chekList);
        returnList.addAll(uncheckList);
        listInAdapter.clear();
        listInAdapter.addAll(returnList);
        tvAmount.setText(String.valueOf("/" + list.size()));
        tvCount.setText(String.valueOf(in));
        adapter.setData(returnList);
    }

    public void showCurrentLesson(Lesson lesson) {
        if(isResumed() && adapter!=null) {
            adapter.setLessonsResponce(lesson);
        }
    }

}
