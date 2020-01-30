package com.test.customerapi.services;

import org.ajbrown.namemachine.Name;
import org.ajbrown.namemachine.NameGenerator;
import org.ajbrown.namemachine.NameGeneratorOptions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.test.customerapi.models.User;
import com.test.customerapi.services.UserFilter.OrderTypeEnum;

@Component
public class UserService {
    private final int LENGTH = 100;
    private final String[] cities = {"Santa Cruz de Tenerife", "Barcelona", "Madrid", "Las Palmas", "Sevilla"};
    private HashMap<Long, User> customers;

    public UserService() {
        generateDummyData();
    }
    
    public List<User> all() {
        return customers.values().stream()
            .collect(Collectors.toList());
    }

    public List<User> find(UserFilter filter) {
        Stream<User> stream = customers.values().stream();

        if (filter.isActiveOnly()) {
            stream = stream.filter(p -> p.getActive());
        }
        if (filter.getCity() != null) {
            String text = filter.getCity().toLowerCase();
            stream = stream.filter(p -> p.getCity() != null && p.getCity().toLowerCase().startsWith(text));
        }
        if ("createdAt".equals(filter.getOrderBy())) {
            if (filter.getOrderType() == OrderTypeEnum.ASC) {
                stream = stream.sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
            } else {
                stream = stream.sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
            }
        }
        return stream.collect(Collectors.toList());
    }

    public Optional<User> get(long id) {
        return customers.values().stream()
            .filter(p -> p.getId() == id)
            .findFirst();
    }

    public void add(User user) {

        // customers.add(user);
    }
    public void update(User customer) {
    }
    public void remove(long id) {
    }

    public List<String> getCitiesStartsWith(String text) {
        return customers.values().stream()
            .filter(p -> p.getCity() != null && p.getCity().toLowerCase().startsWith(text.toLowerCase()))
            .map(p -> p.getCity())
            .distinct()
            .collect(Collectors.toList());
    }

    public String[] getCities() {
        return cities;
    }

    /**
     * Generate dummy data for testing
     */
    private void generateDummyData() {
        customers = new HashMap<>();

        Random r = new Random();
        Date d1 = new GregorianCalendar(1945, 0, 1).getTime();
        Date d2 = new GregorianCalendar(2015, 11, 31).getTime();
        Date d3 = new GregorianCalendar(2015, 0, 1).getTime();
        Date d4 = new GregorianCalendar(2019, 0, 30).getTime();
        NameGenerator generator = new NameGenerator(new NameGeneratorOptions());

        for (long i = 0; i < this.LENGTH; i++) {
            Name name = generator.generateName();
            
            User item = new User();
            item.setId(i + 1);
            item.setName(name.getFirstName());
            item.setSurname(name.getLastName());
            item.setActive(generateRandomBoolean());
            item.setEmail(String.format("%s@testcompany.com", item.getName().toLowerCase()));
            item.setCity(cities[r.nextInt(cities.length)]);
            item.setBirthday(new Date(ThreadLocalRandom.current().nextLong(d1.getTime(), d2.getTime())));
            item.setCreatedAt(new Date(ThreadLocalRandom.current().nextLong(d3.getTime(), d4.getTime())));

            customers.put(item.getId(), item);
        }
    }

    /**
     * Generate random value for active property (75% is active)
     */
    private boolean generateRandomBoolean() {
        return Math.random() < 0.75;
    }

}