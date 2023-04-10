package br.com.vote.service;

import br.com.vote.repository.AgendaRepository;
import br.com.vote.repository.entity.Agenda;
import br.com.vote.resources.dto.request.AgendaRequestDTO;
import br.com.vote.resources.dto.response.AgendaResponseDTO;
import br.com.vote.service.exception.AlreadyInUseException;
import br.com.vote.service.exception.NotFoundException;
import br.com.vote.service.mapper.AgendaMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AgendaService {

    private static final Logger logger = LoggerFactory.getLogger(AgendaService.class);

    private AgendaRepository agendaRepository;
    private AgendaMapper agendaMapper;

    public Agenda getAgendaByDescription(String description) {
        logger.info("Recuperar pauta - inicio service {}", description);
        Agenda agenda = agendaRepository.findByDescription(description);
        logger.info("Recuperar pauta - fim service {}", agenda);
        return agenda;
    }

    public AgendaResponseDTO saveAgenda(AgendaRequestDTO agendaDTO) {
        logger.info("Cadastro de pauta - inicio service {}", agendaDTO);
        Agenda agenda = getAgendaByDescription(agendaDTO.getDescription());
        if(Objects.isNull(agenda)){
            Agenda newAgenda = agendaMapper.dtoToEntity(agendaDTO);
            newAgenda = agendaRepository.save(newAgenda);
            logger.info("Cadastro de pauta - fim service {}", newAgenda);
            return agendaMapper.entityToDTO(newAgenda);
        }else{
            logger.error("Pauta já existente.");
            throw new AlreadyInUseException("Pauta já existente.");
        }
    }

    public AgendaResponseDTO getAgenda(Integer id) {
        logger.info("Recuperar pauta - inicio service {}", id);
        Optional<Agenda> agenda = agendaRepository.findById(id);
        if (agenda.isEmpty()) {
            logger.error("Pauta não encontrada.");
            throw new NotFoundException("Pauta não encontrada");
        }
        logger.info("Recuperar pauta - fim service {}", agenda);
        return agendaMapper.entityToDTO(agenda.orElse(null));
    }
}
