package ru.netology.carddeliverypatterns.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {
    }

    private static Faker faker;

    static void makeFaker(String locale) {
        faker = new Faker(new Locale(locale));
    }

    public static String generateDate(int shift) {
        LocalDate date = LocalDate.now().plusDays(shift);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(formatter);
    }

    public static String generateCity() {
        Random random = new Random();
        return Cities.cities[random.nextInt(Cities.cities.length)];
    }

    public static String generateName(String locale) {
        makeFaker(locale);
        return faker.name().fullName();
    }

    public static String generatePhone(String locale) {
        makeFaker(locale);
        return faker.phoneNumber().phoneNumber();
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
            return new UserInfo(generateCity(), generateName(locale), generatePhone(locale));
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}
