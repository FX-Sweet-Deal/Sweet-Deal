package com.example.user.domain.user.controller.model.update;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class UserUpdateEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    private LocalDate birth;

    @Column(length = 200, nullable = false)
    private String address;

    @Column(length = 20, unique = true)
    private String phone;

    // == 변경 메서드(엔티티 규칙 캡슐화) ==
    public void changeName(String name) {
        this.name = name;
    }

    public void changeBirth(LocalDate birth) {
        this.birth = birth;
    }

    public void changeAddress(String address) {
        this.address = address;
    }

    public void changePhone(String phone) {
        this.phone = phone;
    }
}
