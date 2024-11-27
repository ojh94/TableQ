package com.itschool.tableq.service;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.repository.OwnerRepository;
import com.itschool.tableq.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    private final OwnerRepository ownerRepository;


    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    public boolean isAuthor(UserDetails userDetails, Long userId) {
        if(userDetails instanceof User){
            User findUser = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User ID "+userId+" Not Found"));

            if(findUser.getId() == ((User) userDetails).getId()) return true;
        }
        return false;
    }
}