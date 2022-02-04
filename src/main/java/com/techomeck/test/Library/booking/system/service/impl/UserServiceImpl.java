package com.techomeck.test.Library.booking.system.service.impl;

import com.techomeck.test.Library.booking.system.entity.User;
import com.techomeck.test.Library.booking.system.repository.UserRepository;
import com.techomeck.test.Library.booking.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepo;


    @Autowired
    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }


    @Override
    @Transactional
    public void addUser(User user) {
        userRepo.save(user);
    }


    @Override
    public boolean deleteUser(int id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            userRepo.delete(user.get());
            return true;
        }
        return false;
    }
}
