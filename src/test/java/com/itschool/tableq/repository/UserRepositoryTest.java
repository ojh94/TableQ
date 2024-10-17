package com.itschool.tableq.repository;

import com.itschool.tableq.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void create(){
        try{
            User entity = User.builder()
                    .email("mykd2012@gmail.com")
                    .password("1234")
                    .phoneNumber("010-6564-1047")
                    .name("오재헌")
                    .nickName("nik")
                    .build();

            userRepository.save(entity);

            Optional<User> findUser =  userRepository.findByEmail("mykd2012@gmail.com");

            if(!findUser.isEmpty()){
                User finduser = findUser.get();
                assertThat(finduser.getEmail()).isEqualTo(entity.getEmail());
            }else{
                assertThat(0).isEqualTo(1);
                throw new RuntimeException("해당 유저를 찾지 못 했음.");
            }

        }catch (RuntimeException e){
            assertThat(0).isEqualTo(1);
            e.printStackTrace();
        }
    }

}