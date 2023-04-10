package br.com.vote.resources.dto.request;

import br.com.vote.resources.dto.response.UserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO extends UserDTO {

    private String password;

    @JsonProperty("password_confirmation")
    private String passwordConfirmation;
}
