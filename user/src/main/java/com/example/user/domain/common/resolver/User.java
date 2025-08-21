package com.example.user.domain.common.resolver;


import com.example.user.domain.user.repository.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class User {

    private Long id;

    private String email;

    private UserRole role;


}
