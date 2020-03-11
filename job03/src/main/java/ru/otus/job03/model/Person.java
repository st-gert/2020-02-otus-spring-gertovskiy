package ru.otus.job03.model;

/**
 * Сведения об экзаменующемся.
 */
public class Person {

    private final String firstName;
    private final String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    // generated getters & setters
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
}
