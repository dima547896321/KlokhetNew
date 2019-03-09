package nl.klokhet.app.flow.main.tabs;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import nl.klokhet.app.data.network.responce.Lesson;
import nl.klokhet.app.data.network.responce.LessonsResponce;
import nl.klokhet.app.data.network.responce.UserInGroup;
import nl.klokhet.app.flow.base.recyclerview_holders.LesonsHolder;
import nl.klokhet.app.flow.base.recyclerview_holders.UserHolder;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_PICTURE = 1;
    Lesson lessonsResponce;
    public List<UserInGroup> list = new ArrayList<>();


    public UserAdapter() {
//        this.lessonsResponce=lessonsResponce;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER: {
                return LesonsHolder.newInstance(parent);
            }
            default: {
                return UserHolder.newInstance(parent);
            }
        }
    }

    public void setLessonsResponce(Lesson lessonsResponce) {
        this.lessonsResponce = lessonsResponce;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return VIEW_TYPE_HEADER;
            default:
                return VIEW_TYPE_PICTURE;
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_HEADER:
                ((LesonsHolder) holder).bind(lessonsResponce);
                break;
            default:
                ((UserHolder) holder).bind(list.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size()+1;
        } else {
            return 0;
        }
    }


    public void setData(List<UserInGroup> data) {
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    public void setMoreData(List<UserInGroup> data) {
        list.addAll(data);
        notifyDataSetChanged();
    }

}
