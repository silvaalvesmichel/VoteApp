package br.com.vote.service;

import br.com.vote.repository.UserRepository;
import br.com.vote.repository.entity.User;
import br.com.vote.resources.dto.response.UserDTO;
import br.com.vote.resources.dto.request.UserRequestDTO;
import br.com.vote.resources.dto.response.UserResponseDTO;
import br.com.vote.service.dto.ValidationCpfDTO;
import br.com.vote.service.enumerator.StatusValidationEnum;
import br.com.vote.service.exception.AlreadyInUseException;
import br.com.vote.service.exception.InvalidParamException;
import br.com.vote.service.exception.NotFoundException;
import br.com.vote.service.exception.ServiceUnavailableException;
import br.com.vote.service.mapper.UserMapper;
import br.com.vote.service.util.Util;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final String END_POINT_VALIDATION_CPF = "http://localhost:8082/api/users/valid/";

    public User getUserByCPF(String cpf) {
        logger.info("Recuperar usuario por cpf - inicio service {}", cpf);
        User user = userRepository.findByCPF(cpf);
        logger.info("Recuperar usuario por cpf - fim service {}", cpf);
        return user;
    }

    public UserResponseDTO saveUser(UserRequestDTO userRequestDTO) {
        logger.info("Cadastrar usuario - inicio service {}", userRequestDTO);
        userValidate(userRequestDTO);
        User user = getUserByCPF(userRequestDTO.getCpf());
        if (Objects.isNull(user)) {
            if (userRequestDTO.getPassword().equals(userRequestDTO.getPasswordConfirmation())) {
                User newUser = userMapper.dtoToEntity(userRequestDTO);
                newUser.setPassword(bCryptPasswordEncoder.encode(userRequestDTO.getPassword()));
                newUser = userRepository.save(newUser);
                logger.info("Cadastrar usuario - fim service {}", newUser);
                return UserResponseDTO.builder().id(newUser.getId()).build();
            } else {
                logger.error("A confirmacao da senha precisa ser igual a senha");
                throw new AlreadyInUseException("A confirmacao da senha precisa ser igual a senha");
            }
        } else {
            logger.error("O CPF recebido já se encontra cadastrado");
            throw new AlreadyInUseException("O CPF recebido já se encontra cadastrado");
        }
    }

    private void userValidate(UserRequestDTO userRequestDTO) {
        if (Util.isnull(userRequestDTO.getName())) {
            logger.error("Favor informe o nome do usuario");
            throw new InvalidParamException("Favor informe o nome do usuario");
        }
        if (Util.isnull(userRequestDTO.getEmail())) {
            logger.error("Favor informe o email");
            throw new InvalidParamException("Favor informe o email");
        }
        if (Util.isnull(userRequestDTO.getCpf())) {
            logger.error("Favor informe o cpj");
            throw new InvalidParamException("Favor informe o cpj");
        }
        if (userRequestDTO.getCpf() != null || !userRequestDTO.getCpf().equals("")) {
            if (!validaCPF(userRequestDTO.getCpf())) {
                logger.error("Favor informe o CPF valido");
                throw new InvalidParamException("Favor informe o CPF valido");
            }
        }
        if (Util.isnull(userRequestDTO.getPassword())) {
            logger.error("Favor informe o password");
            throw new InvalidParamException("Favor informe o password");
        }
        if (Util.isnull(userRequestDTO.getPasswordConfirmation())) {
            logger.error("Favor informe a confirmacao do password");
            throw new InvalidParamException("Favor informe a confirmacao do password");
        }
    }

    public boolean validaCPF(String cpf) {
        logger.info("Integracao validacao CPF - inicio service ");
        try{
        var reponseStatus = restTemplate.getForObject(END_POINT_VALIDATION_CPF + cpf, ValidationCpfDTO.class);
        var status = StatusValidationEnum.toEnum(reponseStatus.getStatus());
        if (status.equals(StatusValidationEnum.VALID)) {
            logger.info("Integracao validacao CPF - fim service");
            return Boolean.TRUE;
        }
        logger.info("Integracao validacao CPF - fim service");
        return Boolean.FALSE;
        }catch (ResourceAccessException e){
            logger.error("Erro ao integrar com sistema de validacao cpf");
            throw new ServiceUnavailableException("Erro ao integrar com sistema de validacao cpf");
        }

    }

    public UserDTO getUser(Integer id) {
        logger.info("Obter usuario por id - inicio service {}",id);
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            logger.error("Usuario nao encontrado");
            throw new NotFoundException("Usuario nao encontrado");
        }
        logger.info("Obter usuario por id - fim service {}",user);
        return userMapper.entityToDTO(user.orElse(null));
    }
}
