package com.tungstun.barapi.domain.person;

import com.tungstun.barapi.domain.payment.Bill;
import com.tungstun.security.domain.user.User;

import java.util.ArrayList;
import java.util.List;

public class PersonBuilder {
    private String name;
    private String phoneNumber;
    private User user;
    private List<Bill> bills;

    public PersonBuilder() {
        this.name = null;
        this.phoneNumber = "";
        this.user = null;
        this.bills = new ArrayList<>();
    }

    public PersonBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PersonBuilder setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public PersonBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public PersonBuilder setBills(List<Bill> bills) {
        this.bills = bills;
        return this;
    }

    public Person build() {
        return new Person(
                this.name,
                this.phoneNumber,
                this.user,
                this.bills
        );
    }
}
