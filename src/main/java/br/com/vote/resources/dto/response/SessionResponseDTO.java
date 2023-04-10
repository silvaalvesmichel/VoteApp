package br.com.vote.resources.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionResponseDTO {

    private Integer id;

    @JsonProperty("expiration_date")
    private LocalDateTime expirationDate;

    @JsonProperty("quantity_vote_yes")
    private Integer quantityVoteYes;

    @JsonProperty("quantity_vote_no")
    private Integer quantityVoteNo;

    @JsonProperty("associated_vote")
    private List<UserResponseDTO> associatedVote;

    @JsonProperty("agenda")
    private AgendaResponseDTO agenda;

    @JsonProperty("is_active")
    private Boolean isActive;
}
