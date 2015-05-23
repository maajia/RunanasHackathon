package com.hh.hackathon.runanashackathon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by majajaensch on 23.05.15.
 */
@JsonIgnoreProperties("url")
public class User {

    private String _username;
    private String _email;
    private boolean _isStaff;


    public String getUsername() {
        return _username;
    }

    public void setUsername(String username) {
        _username = username;
    }

    public void setEmail(String email) {
        _email = email;
    }

    public String getEmail(){
        return _email;
    }

    @JsonProperty("is_staff")
    public boolean isStaff(){
        return _isStaff;
    }

    @JsonProperty("is_staff")
    public void setStaff(boolean isStaff) {
        _isStaff = isStaff;
    }

}
