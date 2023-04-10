package br.com.vote.resources.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendaResponseDTO {

    private Integer id;

    private String description;

    @JsonProperty("is_active")
    private Boolean isActive;
}
