package com.sports.model;

/**
 * Abstract base class representing a Person.
 * Demonstrates Abstraction and Encapsulation in Core Java.
 */
public abstract class Person {
    // Encapsulated fields
    protected String id;
    protected String name;
    protected int age;

    public Person() {
    }

    public Person(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    // Getters and Setters demonstrating encapsulation with validation
    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be empty");
        }
        this.id = id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name.trim();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 5 || age > 100) {
            throw new IllegalArgumentException("Age must be between 5 and 100");
        }
        this.age = age;
    }

    // Abstract methods to enforce polymorphic implementation in subclasses
    public abstract String getRole();

    public abstract String displayDetails();

    public abstract String toDataString();
}
