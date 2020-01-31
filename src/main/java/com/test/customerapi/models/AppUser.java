package com.test.customerapi.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Application user model
 * It allows manage guest user data
 */
public class AppUser {
    private List<Date> requests;

    public List<Date> getRequests() {
        if (requests == null) {
            requests = new ArrayList<>();
        }
        return requests;
    }

    public void setRequests(List<Date> requests) {
        this.requests = requests;
    }

}