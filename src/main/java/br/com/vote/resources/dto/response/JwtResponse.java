package br.com.vote.resources.dto.response;

import br.com.vote.security.UserSS;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private String token;
    private UserSS user;
}