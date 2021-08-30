package com.zerock.club.security;

import com.zerock.club.entity.ClubMember;
import com.zerock.club.entity.ClubMemberRole;
import com.zerock.club.repository.ClubMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemberTest {

    @Autowired private ClubMemberRepository repository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    void insertDummies() {

        //  1 -  80 까지는 USER
        // 81 -  90 까지는 USER, MANAGER
        // 90 - 100 까지는 USER, MANAGER, ADMIN
        IntStream.rangeClosed(1, 100).forEach(i -> {

            ClubMember clubMember = ClubMember.builder()
                    .email("user" + i + "@zerock.com")
                    .name("사용자" + i)
                    .fromSocial(false)
                    .password(passwordEncoder.encode("1111"))
                    .build();

            // default role
            clubMember.addMemberRole(ClubMemberRole.USER);

            if (i > 80) clubMember.addMemberRole(ClubMemberRole.MANAGER);
            if (i > 90) clubMember.addMemberRole(ClubMemberRole.ADMIN);

            repository.save(clubMember);
        });

    }

    @Test
    void testRead() {

        Optional<ClubMember> result = repository.findByEmail("user95@zerock.com", false);

        ClubMember clubMember = result.get();

        System.out.println(clubMember);
    }

}
