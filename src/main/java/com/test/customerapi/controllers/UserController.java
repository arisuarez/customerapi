package com.test.customerapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import com.test.customerapi.exceptions.TooManyRequestsException;
import com.test.customerapi.exceptions.UserValidationException;
import com.test.customerapi.models.AppUser;
import com.test.customerapi.models.User;
import com.test.customerapi.filters.UserFilter;
import com.test.customerapi.filters.UserFilter.OrderTypeEnum;
import com.test.customerapi.services.AppUserService;
import com.test.customerapi.services.UserService;

/**
 * Rest controller to dispatch all user requests
 */
@RestController
class UserController {

    // Necesary data to limit the number of requests
    private final int SECONDS = 10;
    private final int MAX_REQUESTS = 3;

    @Autowired
    private UserService service;

    @Autowired
    private AppUserService appUserService;

    /**
     * Gets the list of users that can be fitered by the following fields
     * 
     * @param activeOnly
     * @param cityStartsWith
     * @param orderByCreationDate
     * @param orderType
     * @return
     */
    @GetMapping("/api/users")
    public List<User> getUsers(@RequestParam(value = "activeOnly", defaultValue = "") String activeOnly,
            @RequestParam(value = "cityStartsWith", defaultValue = "") String cityStartsWith,
            @RequestParam(value = "orderByCreationDate", defaultValue = "") String orderByCreationDate,
            @RequestParam(value = "orderType", defaultValue = "asc") String orderType) {

        // Limit the number of requests
        this.checkLastVisit();

        UserFilter filter = new UserFilter();

        // Prepare the filter for the users service
        filter.setActiveOnly("true".equals(activeOnly));
        if (!"".equals(cityStartsWith)) {
            filter.setCity(cityStartsWith);
        }
        filter.setOrderBy("true".equals(orderByCreationDate) ? "createdAt" : null);
        if ("desc".equals(orderType.toLowerCase())) {
            filter.setOrderType(OrderTypeEnum.DESC);
        }

        return service.find(filter);
    }

    /**
     * Create a new user
     * 
     * @param user User data mapped with the json data from the front
     * @return
     */
    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public User addUsers(@RequestBody User user) {

        // Limit the number of requests
        this.checkLastVisit();

        this.validate(user);
        this.service.add(user);
        return user;
    }

    /**
     * Validate user: All fields are required
     * 
     * @param user
     */
    private void validate(User user) throws UserValidationException {
        if (user.getName() == null || user.getName().isEmpty() || user.getSurname() == null
                || user.getSurname().isEmpty() || user.getEmail() == null || user.getEmail().isEmpty()
                || user.getCity() == null || user.getCity().isEmpty() || user.getBirthday() == null) {
            throw new UserValidationException();
        }
    }

    /**
     * Checks the requests dates and limits the number of call to MAX_REQUESTS=3
     * every SECONDS=10 seconds
     */
    private void checkLastVisit() throws TooManyRequestsException {
        Date t = new Date();
        AppUser appUser = appUserService.get();
        List<Date> requests = appUser.getRequests();
        if (requests.size() >= MAX_REQUESTS) {

            // Third previous request before this call
            Date d = requests.get(requests.size() - MAX_REQUESTS);

            // Difference in seconds between third previous request and the actual request
            long seconds = (t.getTime() - d.getTime()) / 1000;

            // If the difference is lower than SECONDS=10, throw an exception
            if (seconds < SECONDS) {
                throw new TooManyRequestsException();
            }
        }

        // Saves the last request
        this.appUserService.addNewRequest(t, MAX_REQUESTS);
    }

}
