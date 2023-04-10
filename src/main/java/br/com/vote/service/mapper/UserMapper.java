package br.com.vote.service.mapper;

import br.com.vote.repository.entity.User;
import br.com.vote.resources.dto.response.UserDTO;
import br.com.vote.resources.dto.request.UserRequestDTO;
import br.com.vote.resources.dto.response.UserResponseDTO;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserMapper {


    public static List<UserResponseDTO> listEntityToDTO(List<User> associatedVote) {
        if (Objects.isNull(associatedVote)) {
            return null;
        }
        return associatedVote.stream()
                .map(UserMapper::entityToResponseDTO)
                .collect(Collectors.toList());
    }

    private static UserResponseDTO entityToResponseDTO(User user) {
        if (Objects.isNull(user)) {
            return null;
        }
        return UserResponseDTO.builder()
                .id(user.getId())
                .build();
    }

    public User dtoToEntity(UserRequestDTO userRequestDTO) {
        return User.builder()
                .id(userRequestDTO.getId())
                .name(userRequestDTO.getName())
                .email(userRequestDTO.getEmail())
                .password(userRequestDTO.getPassword())
                .cpf(userRequestDTO.getCpf())
                .build();
    }

    public static UserDTO entityToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .cpf(user.getCpf())
                .build();
    }
}