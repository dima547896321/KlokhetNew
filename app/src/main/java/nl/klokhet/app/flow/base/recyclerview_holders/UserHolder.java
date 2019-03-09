package nl.klokhet.app.flow.base.recyclerview_holders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import nl.klokhet.app.App;
import nl.klokhet.app.R;
import nl.klokhet.app.data.network.responce.UserInGroup;
import timber.log.Timber;

public class UserHolder extends RecyclerView.ViewHolder {

    public ImageView ivUserPhoto;
    public TextView tvUserName;
    public TextView tvUserChekin;
    public ImageView ivUserStatus;
    SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    SimpleDateFormat sdfOut1 = new SimpleDateFormat("HH:mm");

    public UserHolder(View itemView) {
        super(itemView);
        ivUserPhoto = itemView.findViewById(R.id.ivUserPhoto);
        tvUserName = itemView.findViewById(R.id.tvUserName);
        tvUserChekin = itemView.findViewById(R.id.tvUserChekin);
        ivUserStatus = itemView.findViewById(R.id.ivUserStatus);

    }

    public static UserHolder newInstance(View itemView) {
        View view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_user, null, true);
        return new UserHolder(view);
    }

    public void bind(UserInGroup user) {
        if (!user.getPhoto().contains("no_photo")) {
            Glide.with(App.getInstance().getApplicationContext())
                    .load("https://klokhet.sapient.pro" + user.getPhoto())
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivUserPhoto);
        } else {
            ivUserPhoto.setImageResource(R.drawable.ic_user);
        }
        tvUserName.setText(user.getFirst_name() + " " + user.getLast_name());
        if (user.getStatus().equalsIgnoreCase("Not checkin")) {
            tvUserChekin.setText("awaiting");
            ivUserStatus.setBackground(App.getInstance().getResources().getDrawable(R.drawable.ic_clock));
        } else if (user.getStatus().equalsIgnoreCase("checkin")) {
            try {
                tvUserChekin.setText(sdfOut1.format(sdfIn.parse(user.getTime_in())));
            } catch (ParseException e) {
                tvUserChekin.setText(user.getTime_in());
            }
            ivUserStatus.setBackground(App.getInstance().getResources().getDrawable(R.drawable.ic_checked));
        } else if (user.getStatus().equalsIgnoreCase("checkout")) {
            try {
                Timber.d("USER " + new Gson().toJson(user));
                tvUserChekin.setText(sdfOut1.format(sdfIn.parse(user.getTime_in())) + " - " + sdfOut1.format(sdfIn.parse(user.getTime_out())));
            } catch (ParseException e) {
                Timber.d("Error parse data: " +user.getTime_out());
                try {
                    tvUserChekin.setText(sdfOut1.format(sdfIn.parse(user.getTime_in())));
                } catch (ParseException e1) {
                    Timber.d("Error parse data: " +user.getTime_in());
                }
            }
            ivUserStatus.setBackground(App.getInstance().getResources().getDrawable(R.drawable.ic_clock));
        }
    }

}
