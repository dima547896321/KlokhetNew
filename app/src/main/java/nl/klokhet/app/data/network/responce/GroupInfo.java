package nl.klokhet.app.data.network.responce;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupInfo implements Serializable {
    private ArrayList<UserInGroup> list = new ArrayList<>();

    public GroupInfo() {
    }

    public ArrayList<UserInGroup> getList() {
        return list;
    }

    public void setList(ArrayList<UserInGroup> list) {
        this.list = list;
    }
}
