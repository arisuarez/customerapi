package com.test.customerapi.services;

import org.ajbrown.namemachine.Name;
import org.ajbrown.namemachine.NameGenerator;
import org.ajbrown.namemachine.NameGeneratorOptions;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.test.customerapi.models.User;
import com.test.customerapi.filters.UserFilter;
import com.test.customerapi.filters.UserFilter.OrderTypeEnum;

/**
 * Users service
 */
@Component
public class UserService {

    // Necessary data to generate random users
    private final int LENGTH = 100;
    private final String[] cities = {"Santa Cruz de Tenerife", "Barcelona", "Madrid", "Las Palmas", "Sevilla", "La Laguna", "Valencia"};

    private HashMap<Long, User> customers;

    public UserService() {
        generateDummyData();
    }
    
    /**
     * Method to allow perform several search types
     * @param filter Added only requested filters in the exercise
     */
    public List<User> find(final UserFilter filter) {
        Stream<User> stream = customers.values().stream();

        // Filter active only
        if (filter.isActiveOnly()) {
            stream = stream.filter(p -> p.getActive());
        }

        // Filter the user list with the city start text
        if (filter.getCity() != null) {
            final String text = filter.getCity().toLowerCase();
            stream = stream.filter(p -> p.getCity() != null && p.getCity().toLowerCase().startsWith(text));
        }

        // Order by creation date
        if ("createdAt".equals(filter.getOrderBy())) {
            if (filter.getOrderType() == OrderTypeEnum.ASC) {
                stream = stream.sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
            } else {
                stream = stream.sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
            }
        }
        return stream.collect(Collectors.toList());
    }

    /**
     * Method to add a new user to the repository
     * 
     * @param user
     */
    public void add(final User user) {

        // Generate unique id: at this moment we can't remove customers. So the
        // generated number can't repeat
        user.setId((long) customers.size() + 1);
        user.setCreatedAt(new Date());

        // Adds it to the user list
        customers.put(user.getId(), user);
    }

    /**
     * Generate dummy data for testing
     */
    private void generateDummyData() {
        customers = new HashMap<>();

        final Random r = new Random();
        final Date d1 = new GregorianCalendar(1945, 0, 1).getTime();
        final Date d2 = new GregorianCalendar(2015, 11, 31).getTime();
        final Date d3 = new GregorianCalendar(2015, 0, 1).getTime();
        final Date d4 = new GregorianCalendar(2019, 0, 30).getTime();
        final NameGenerator generator = new NameGenerator(new NameGeneratorOptions());

        for (long i = 0; i < this.LENGTH; i++) {
            final Name name = generator.generateName();

            final User item = new User();
            item.setId(i + 1);
            item.setName(name.getFirstName());
            item.setSurname(name.getLastName());
            item.setActive(generateRandomBoolean());
            item.setEmail(String.format("%s@company.com", item.getName().toLowerCase()));
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