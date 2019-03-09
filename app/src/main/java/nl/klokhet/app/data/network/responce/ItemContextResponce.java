package nl.klokhet.app.data.network.responce;

import android.content.Intent;

import java.io.Serializable;

public class ItemContextResponce implements Serializable {
    private Integer id;
    private String name;

    public ItemContextResponce() {
    }

    public ItemContextResponce(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return  name ;
    }
}
