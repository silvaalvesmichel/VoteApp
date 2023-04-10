package br.com.vote.resources.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionRequestDTO {

    @JsonProperty("expiration_date")
    private LocalDateTime expirationDate;

    @JsonProperty("id_agenda")
    private Integer idAgenda;
}
