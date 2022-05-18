package com.ably.auth.repository;

import com.ably.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.username = :key or u.email = :key or u.phone = :key")
    User getUser(@Param("key") String key);

    User findByPhone(String phone);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByPhone(String phone);
}
