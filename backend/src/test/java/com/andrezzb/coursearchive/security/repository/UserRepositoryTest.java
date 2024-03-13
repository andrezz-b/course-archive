package com.andrezzb.coursearchive.security.repository;

import com.andrezzb.coursearchive.security.SecurityConfig;
import com.andrezzb.coursearchive.security.models.UserEntity;
import com.andrezzb.coursearchive.security.services.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({SecurityConfig.class, CustomUserDetailsService.class})
public class UserRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0-alpine");

    @Autowired
    private UserRepository userRepository;

    @Test
    void givenUsername_whenFindByUsername_shouldReturnUser() {
        final String username = "testuser";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("testpassword");
        user.setEmail("testemail");
        user.setFirstName("testfirst");
        user.setLastName("testlast");
        userRepository.save(user);

        Optional<UserEntity> found = userRepository.findByUsername(username);

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getUsername()).isEqualTo(username);
    }

    @Test
    void givenUsername_whenFindByUsername_shouldNotReturnUser() {
        UserEntity found = userRepository.findByUsername("testuser").orElse(null);

        assertThat(found).isNull();
    }

    @Test
    void givenEmail_whenFindByEmail_shouldReturnUser() {
        final String email = "testuser@test.com";
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setEmail(email);
        user.setFirstName("testfirst");
        user.setLastName("testlast");
        userRepository.save(user);

        var found = userRepository.findByEmail(email);

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getEmail()).isEqualTo(email);
    }
    @Test
    void givenEmail_whenFindByEmail_shouldNotReturnUser() {
        UserEntity found = userRepository.findByEmail("testuser@test.com").orElse(null);

        assertThat(found).isNull();
    }
}
