package com.example.user.domain.user.repository;


import com.example.user.domain.user.repository.enums.UserStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByPhoneAndIdNot(String phone, Long id);

    Optional<UserEntity> findFirstByIdAndStatusOrderByIdDesc(Long userId, UserStatus status);

    boolean existsByEmail(String email);

    boolean existsByName(String name);

    Optional<UserEntity> findFirstByEmailAndStatusNotOrderByEmailDesc(String email, UserStatus status);

    Optional<UserEntity> findFirstByEmailAndStatusOrderByIdDesc(String email, UserStatus status);

}
