package br.com.vote.service;

import br.com.vote.repository.UserRepository;
import br.com.vote.repository.entity.User;
import br.com.vote.security.UserSS;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Recuperar usuario por email - inicio service {}", email);
        User user = userRepository.findByEmail(email);

        if(Objects.isNull(user)){
            logger.error("Usuario nao encontrado.");
            throw new UsernameNotFoundException(email);
        }
        logger.info("Recuperar usuario por email - fim service {}", user);
        return UserSS.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword()).build();

    }
}
