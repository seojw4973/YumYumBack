package org.baratie.yumyum.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.baratie.yumyum.domain.member.domain.Member;
import org.baratie.yumyum.domain.member.domain.Role;
import org.baratie.yumyum.domain.member.repository.MemberRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Configuration
@Service
@RequiredArgsConstructor
public class MemberDetails implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername....");
        System.out.println("email : " + email);

        try {
            Optional<Member> member = memberRepository.findByEmail(email);
            if (member.isEmpty()) {
                System.out.println("User not found with email: " + email);
                throw new UsernameNotFoundException("User not found with email: " + email);
            }

            System.out.println("member: " + member.get());

            return User.builder()
                    .username(member.get().getEmail())
                    .password(member.get().getPassword())
                    .authorities(new SimpleGrantedAuthority(Role.USER.getRole()))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
