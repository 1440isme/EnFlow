package vn.enflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.enflow.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
}
