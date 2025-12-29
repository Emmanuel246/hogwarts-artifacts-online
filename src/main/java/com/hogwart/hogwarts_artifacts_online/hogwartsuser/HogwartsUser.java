package com.hogwart.hogwarts_artifacts_online.hogwartsuser;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

@Entity
public class HogwartsUser implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;

    @NotEmpty(message = "username is required.")
    private String username;

    @NotEmpty(message = "password is required.")
    private String password;

    private boolean enabled;

    @NotEmpty(message = "roles are required.")
    private String roles; // Space separated string


    public HogwartsUser() {
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public @NotEmpty(message = "username is required.") String getUsername() {
        return username;
    }

    public void setUsername(@NotEmpty(message = "username is required.") String username) {
        this.username = username;
    }

    public @NotEmpty(message = "password is required.") String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty(message = "password is required.") String password) {
        this.password = password;
    }

    public @NotEmpty(message = "roles are required.") String getRoles() {
        return roles;
    }

    public void setRoles(@NotEmpty(message = "roles are required.") String roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
