package com.coffeecup.coffeecupadminsystem.repository;

import com.coffeecup.coffeecupadminsystem.model.Authority; //
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByName(String name);
}