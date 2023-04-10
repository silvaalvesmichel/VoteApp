package br.com.vote.service;

import br.com.vote.commons.annotation.Unitario;
import br.com.vote.repository.UserRepository;
import br.com.vote.repository.entity.User;
import br.com.vote.resources.dto.request.UserRequestDTO;
import br.com.vote.service.dto.ValidationCpfDTO;
import br.com.vote.service.exception.AlreadyInUseException;
import br.com.vote.service.exception.InvalidParamException;
import br.com.vote.service.exception.NotFoundException;
import br.com.vote.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@Unitario
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, userMapper, bCryptPasswordEncoder, restTemplate);
    }

    @Test
    @DisplayName("Service - save user")
    public void deveriaAdicionarUser() {
        Mockito.when(userRepository.findByCPF(any(String.class))).thenReturn(null);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(mockUser());
        Mockito.when(userMapper.dtoToEntity(any(UserRequestDTO.class))).thenReturn(mockUser());
        Mockito
                .when(restTemplate.getForObject(
                        "http://localhost:8082/api/users/valid/65590367077", ValidationCpfDTO.class))
                .thenReturn(mockValidationCPF());
        var user = userService.saveUser(mockUserR());

        assertNotNull(user);
    }
    @Test
    @DisplayName("Service - save user cpf - error")
    public void naodeveriaAdicionarUserCpf() {
        var userMock = mockUserR();
        userMock.setCpf(null);
        assertThrows(InvalidParamException.class, ()->userService.saveUser(userMock));
    }
    @Test
    @DisplayName("Service - save user nome - error")
    public void naodeveriaAdicionarUserNome() {
        var userMock = mockUserR();
        userMock.setName(null);
        assertThrows(InvalidParamException.class, ()->userService.saveUser(userMock));
    }
    @Test
    @DisplayName("Service - save user email - error")
    public void naodeveriaAdicionarUserEmail() {
        var userMock = mockUserR();
        userMock.setEmail(null);
        assertThrows(InvalidParamException.class, ()->userService.saveUser(userMock));
    }
    @Test
    @DisplayName("Service - save user password - error")
    public void naodeveriaAdicionarUserPassword() {
        var userMock = mockUserR();
        userMock.setPassword(null);
        Mockito
                .when(restTemplate.getForObject(
                        "http://localhost:8082/api/users/valid/65590367077", ValidationCpfDTO.class))
                .thenReturn(mockValidationCPF());
        assertThrows(InvalidParamException.class, ()->userService.saveUser(userMock));
    }
    @Test
    @DisplayName("Service - save user password confirm - error")
    public void naodeveriaAdicionarUserPasswordConfirm() {
        var userMock = mockUserR();
        userMock.setPasswordConfirmation(null);
        Mockito
                .when(restTemplate.getForObject(
                        "http://localhost:8082/api/users/valid/65590367077", ValidationCpfDTO.class))
                .thenReturn(mockValidationCPF());
        assertThrows(InvalidParamException.class, ()->userService.saveUser(userMock));
    }

    @Test
    @DisplayName("Service - save user-Error")
    public void naodeveriaAdicionarUserError() {
        Mockito.when(userRepository.findByCPF(any(String.class))).thenReturn(mockUser());
        Mockito
                .when(restTemplate.getForObject(
                        "http://localhost:8082/api/users/valid/65590367077", ValidationCpfDTO.class))
          .thenReturn(mockValidationCPF());
        assertThrows(AlreadyInUseException.class, ()->userService.saveUser(mockUserR()));
    }

    private ValidationCpfDTO mockValidationCPF() {
        return ValidationCpfDTO.builder().status("ABLE_TO_VOTE").build();
    }

    @Test
    @DisplayName("Service - find user")
    public void deveriaRecuperarUser() {
        Mockito.when(userRepository.findByCPF(any(String.class))).thenReturn(mockUser());
        var user = userService.getUserByCPF("65590367077");
        assertNotNull(user);
    }

    @Test
    @DisplayName("Service - find user")
    public void deveriaRecuperarUserId() {
        Mockito.when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(mockUser()));
        var user = userService.getUser(12);
        assertNotNull(user);
    }

    @Test
    @DisplayName("Service - find user")
    public void deveriaRecuperarUserIdError() {
        Mockito.when(userRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, ()->userService.getUser(12));
    }

    private UserRequestDTO mockUserR() {
        var userR = new UserRequestDTO();
        userR.setPassword("12345");
        userR.setPasswordConfirmation("12345");
        userR.setCpf("65590367077");
        userR.setEmail("teste@teste.com");
        userR.setName("teste");
        return userR;
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
