package com.techomeck.test.Library.booking.system.service;

import com.techomeck.test.Library.booking.system.entity.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    void addUser(User user);

    boolean deleteUser(int id);
}
