package com.coffeecup.coffeecup.repository;

import com.coffeecup.coffeecup.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByName(String name);
}