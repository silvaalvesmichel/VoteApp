package br.com.vote.resources.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteRequestDTO {

    @JsonProperty("id_session")
    private Integer idSession;

    @JsonProperty("id_associated")
    private Integer idAssociated;

    @JsonProperty("tipo_voto")
    private String tipoVoto;

}
