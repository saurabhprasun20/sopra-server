package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;

import java.sql.Date;

public class UserPutDTO {

    private Long id;
    private String username;
    private UserStatus status;
    private String token;
    private java.sql.Date creationDate;
    private java.sql.Date birthDate;
    private Boolean loginCheckStatus;

    public Boolean getLoginCheckStatus() {
        return loginCheckStatus;
    }

    public void setLoginCheckStatus(Boolean loginCheckStatus) {
        this.loginCheckStatus = loginCheckStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
