package br.com.vote.service.mapper;

import br.com.vote.repository.entity.Agenda;
import br.com.vote.repository.entity.Session;
import br.com.vote.resources.dto.request.SessionRequestDTO;
import br.com.vote.resources.dto.response.SessionResponseDTO;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SessionMapper {

    public Session dtoToEntity(SessionRequestDTO sessionDTO, Agenda agenda) {
        return Session.builder()
                .agenda(agenda)
                .expirationDate(Objects.isNull(sessionDTO.getExpirationDate()) ?
                        LocalDateTime.now().plusMinutes(1) :
                        sessionDTO.getExpirationDate())
                .isActive(Boolean.TRUE)
                .quantityVoteYes(0)
                .quantityVoteNo(0)
                .build();
    }

    public SessionResponseDTO entityToDTO(Session session) {
        return SessionResponseDTO.builder()
                .id(session.getId())
                .agenda(AgendaMapper.entityToDTO(session.getAgenda()))
                .associatedVote(UserMapper.listEntityToDTO(session.getAssociatedVote()))
                .quantityVoteNo(Objects.isNull(session.getQuantityVoteNo()) ? 0 : session.getQuantityVoteNo())
                .quantityVoteYes(Objects.isNull(session.getQuantityVoteYes()) ? 0 : session.getQuantityVoteYes())
                .isActive(session.getIsActive())
                .expirationDate(session.getExpirationDate())
                .build();
    }
}
