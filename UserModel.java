package com.uic.clouddatabase;

public class UserModel {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserModel() {
        this.id = "";
        this.name = "";
        this.email = "";
    }

    @Override
    public String toString() {
        return this.name;
    }

    private String id;
    private String name;
    private String email;


}
