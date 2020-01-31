package com.test.customerapi.services;

import org.springframework.stereotype.Component;

import java.util.Date;

import com.test.customerapi.models.AppUser;

/**
 * Application users service
 */
@Component
public class AppUserService {

    private AppUser user;

    public AppUserService() {
        user = new AppUser();
    }

    public AppUser get() {
        return this.user;
    }

    public void update(AppUser user) {
        this.user = user;
    }

    /**
     * Adds a new request date to the list. Limits the list to the minimum neccesary
     * requests
     * 
     * @param t
     * @param maxRequests
     */
    public void addNewRequest(Date t, int maxRequests) {
        this.user.getRequests().add(t);
        while (this.user.getRequests().size() > maxRequests) {
            this.user.getRequests().remove(0);
        }
    }

}