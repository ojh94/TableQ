package com.itschool.tableq.repository;

import com.itschool.tableq.ApplicationTests;
import com.itschool.tableq.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest extends ApplicationTests {
    @Autowired
    private UserRepository userRepository;

    private String sampleEmail = "mykd2012@gmail.com";

    @BeforeEach
    void deleteEntities() {
        // 테스트 실행 전에 데이터베이스를 초기화하여 테스트 간에 데이터가 격리되도록 한다.
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void create() {
        User entity = User.builder()
                .id(1L)
                .email(sampleEmail)
                .password("1234")
                .phoneNumber("010-0000-9999")
                .name("홍길동")
                .nickname("닉네임")
                .lastLoginAt(LocalDateTime.now())
                .build();

        User savedEntity = userRepository.save(entity);

        System.out.println("---------------- savedEntity = " + savedEntity);

        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getEmail()).isEqualTo(entity.getEmail());
    }

    @Test
    @Transactional
    void read() {
        // create()를 호출하지 않고 직접 데이터를 생성하여 독립적인 테스트가 되도록 함
        User entity = User.builder()
                .email(sampleEmail)
                .password("1234")
                .phoneNumber("010-0000-9999")
                .name("홍길동")
                .nickname("닉네임")
                .lastLoginAt(LocalDateTime.now())
                .build();

        userRepository.save(entity);  // 직접 저장하여 테스트

        // 해당 이메일로 사용자를 찾는다.
        User findUser = userRepository.findByEmail(sampleEmail)
                .orElseThrow(() -> new NoSuchElementException("해당 유저를 찾을 수 없습니다."));

        assertThat(findUser).isNotNull();
        assertThat(findUser.getEmail()).isEqualTo(sampleEmail);
    }

    @Test
    @Transactional
    void update() {

        // 새로운 사용자 생성
        User entity = User.builder()
                .email(sampleEmail)
                .password("1234")
                .phoneNumber("010-0000-9999")
                .name("홍길동")
                .nickname("닉네임")
                .lastLoginAt(LocalDateTime.now())
                .build();

        User savedEntity = userRepository.save(entity);

        System.out.println("---------------- savedEntity = " + savedEntity);
        System.out.println("---------------- savedEntity.getLastModifiedAt() = " + savedEntity.getLastModifiedAt());

        // 저장된 엔티티 업데이트
        savedEntity.setPassword("0000");

        // saveAndFlush()를 사용하여 즉시 데이터베이스에 반영
        User updatedEntity = userRepository.saveAndFlush(savedEntity);

        System.out.println("---------------- updatedEntity = " + updatedEntity);
        System.out.println("---------------- updatedEntity.getLastModifiedAt() = " + updatedEntity.getLastModifiedAt());

        assertThat(updatedEntity).isNotNull();
        assertThat(updatedEntity.getPassword()).isEqualTo("0000");
    }

    @Test
    @Transactional
    void delete() {
        Long beforeCount = userRepository.countBy().orElseThrow();

        // 사용자 생성 후 삭제하는 테스트
        User entity = User.builder()
                .email(sampleEmail)
                .password("1234")
                .phoneNumber("010-0000-9999")
                .name("홍길동")
                .nickname("닉네임")
                .lastLoginAt(LocalDateTime.now())
                .build();

        User savedEntity = userRepository.save(entity);

        // 삭제
        userRepository.delete(savedEntity);

        Long afterCount = userRepository.countBy().orElseThrow();

        assertThat(beforeCount).isEqualTo(afterCount);
    }
}