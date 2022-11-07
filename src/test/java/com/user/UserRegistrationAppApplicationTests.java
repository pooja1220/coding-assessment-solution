package com.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pooja.repository.UserRepository;

@SpringBootTest
class UserRegistrationAppApplicationTests {

	@Autowired
	private UserRepository repo;
}
