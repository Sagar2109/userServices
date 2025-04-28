package com.userservice.request;


import jakarta.validation.constraints.NotNull;

public class UserDeleteRequest {

    @NotNull
    private String id;
    private boolean suspended;

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
