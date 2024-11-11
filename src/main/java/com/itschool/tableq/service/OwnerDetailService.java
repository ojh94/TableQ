package com.itschool.tableq.service;

import com.itschool.tableq.domain.Owner;
import com.itschool.tableq.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OwnerDetailService implements UserDetailsService {

    private final OwnerRepository ownerRepository;

    @Override
    public Owner loadUserByUsername(String email) throws UsernameNotFoundException{
        return ownerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }
}
