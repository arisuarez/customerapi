package com.test.customerapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.test.customerapi.exceptions.ResourceNotFoundException;
import com.test.customerapi.models.User;
import com.test.customerapi.services.UserFilter;
import com.test.customerapi.services.UserService;
import com.test.customerapi.services.UserFilter.OrderTypeEnum;

@RestController
class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/api/users")
	public List<User> getUsers(
        @RequestParam(value = "activeOnly", defaultValue = "") String activeOnly,
        @RequestParam(value = "cityStartsWith", defaultValue = "") String cityStartsWith,
        @RequestParam(value = "orderByCreationDate", defaultValue = "") String orderByCreationDate,
        @RequestParam(value = "orderType", defaultValue = "asc") String orderType) {

        UserFilter filter = new UserFilter();

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

    @RequestMapping(value="/api/users", method = RequestMethod.POST)
	public User addUsers(@RequestBody User user) {
        this.validate(user);
        this.service.add(user);
        return user;
    }

	@GetMapping("/api/cities")
	public List<String> getCities(@RequestParam(value = "startsWith", defaultValue = "") String startsWith) {
        return service.getCitiesStartsWith(startsWith);
    }

    private void validate(User user) {
        if (user.getName().isEmpty() || user.getSurname().isEmpty()) {
            
        }
    }

}
