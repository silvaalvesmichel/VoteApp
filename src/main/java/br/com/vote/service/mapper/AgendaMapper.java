package br.com.vote.service.mapper;

import br.com.vote.repository.entity.Agenda;
import br.com.vote.resources.dto.request.AgendaRequestDTO;
import br.com.vote.resources.dto.response.AgendaResponseDTO;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AgendaMapper {

    public Agenda dtoToEntity(AgendaRequestDTO agendaRequestDTO) {
        return Agenda.builder()
                .description(agendaRequestDTO.getDescription())
                .isActive(Boolean.TRUE)
                .build();
    }

    public static AgendaResponseDTO entityToDTO(Agenda agenda) {
        return AgendaResponseDTO.builder()
                .id(agenda.getId())
                .description(agenda.getDescription())
                .isActive(agenda.getIsActive())
                .build();
    }
}
