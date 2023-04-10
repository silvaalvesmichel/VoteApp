package br.com.vote.service;

import br.com.vote.commons.annotation.Unitario;
import br.com.vote.repository.UserRepository;
import br.com.vote.repository.entity.Agenda;
import br.com.vote.repository.entity.Session;
import br.com.vote.repository.entity.User;
import br.com.vote.resources.dto.request.SessionRequestDTO;
import br.com.vote.service.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@Unitario
@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    UserDetailsServiceImpl service;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        service = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    @DisplayName("Service - get user")
    public void deveriaRecuperarUser() {
        Mockito.when(userRepository.findByEmail(any(String.class))).thenReturn(mockUser());
        var user = service.loadUserByUsername("teste@teste.com");
        assertNotNull(user);
    }

    @Test
    @DisplayName("Service - get user - error")
    public void naodeveriaRecuperarUser() {
        Mockito.when(userRepository.findByEmail(any(String.class))).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("teste@teste.com"));
    }

    private User mockUser() {
        return User.builder()
                .cpf("65590367077")
                .id(12)
                .email("teste@teste.com")
                .name("teste")
                .password("asdfgadsf")
                .build();
    }
}
