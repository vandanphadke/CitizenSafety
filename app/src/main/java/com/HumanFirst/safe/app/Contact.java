package com.HumanFirst.safe.app;

/**
 * Created by Vandan on 06-06-2014.
 */
public class Contact {
    public String name , phone , label ;
    public int id ;

    public Contact(int id, String name, String phone, String label) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.label = label;
    }

    public Contact(String name, String phone, String label) {
        this.name = name;
        this.phone = phone;
        this.label = label;
    }

    public Contact() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


}
