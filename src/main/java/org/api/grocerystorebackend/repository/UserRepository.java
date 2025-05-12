package org.api.grocerystorebackend.repository;

import org.api.grocerystorebackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndAccount_IsActiveTrue(Long id);
}
