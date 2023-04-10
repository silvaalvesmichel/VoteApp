package br.com.vote.service;

import br.com.vote.commons.annotation.Unitario;
import br.com.vote.repository.AgendaRepository;
import br.com.vote.repository.entity.Agenda;
import br.com.vote.resources.dto.request.AgendaRequestDTO;
import br.com.vote.service.exception.AlreadyInUseException;
import br.com.vote.service.exception.NotFoundException;
import br.com.vote.service.mapper.AgendaMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@Unitario
@ExtendWith(MockitoExtension.class)
public class AgendaServiceTest {

    @Mock
    AgendaRepository agendaRepository;

    @Mock
    private AgendaService agendaService;

    @Spy
    private AgendaMapper agendaMapper;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        agendaService = new AgendaService(agendaRepository, agendaMapper);
    }

    @Test
    @DisplayName("Service - save agenda")
    public void deveriaAdicionarPauta(){
        Mockito.when(agendaRepository.findByDescription(any(String.class))).thenReturn(null);
        Mockito.when(agendaRepository.save(any(Agenda.class))).thenReturn(mockAgenda());
        var agenda = agendaService.saveAgenda(mockAgendaR());

        assertNotNull(agenda);
    }

    @Test
    @DisplayName("Service - save agenda - error")
    public void naodeveriaAdicionarPauta(){
        Mockito.when(agendaRepository.findByDescription(any(String.class))).thenReturn(mockAgenda());
        assertThrows(AlreadyInUseException.class, ()->agendaService.saveAgenda(mockAgendaR()));
    }

    @Test
    @DisplayName("Service - find agenda")
    public void deveriabuscarPauta(){
        Mockito.when(agendaRepository.findByDescription(any(String.class))).thenReturn(mockAgenda());
        var agenda = agendaService.getAgendaByDescription("teste");

        assertNotNull(agenda);
    }

    @Test
    @DisplayName("Service - find agenda id")
    public void deveriabuscarPautaPorId(){
        Mockito.when(agendaRepository.findById(any(Integer.class))).thenReturn(mockAgendaO());
        var agenda = agendaService.getAgenda(1);

        assertNotNull(agenda);
    }

    @Test
    @DisplayName("Service - find agenda id - error")
    public void naodeveriabuscarPautaPorId(){
        Mockito.when(agendaRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, ()->agendaService.getAgenda(1));
    }

    private Agenda mockAgenda() {
        return Agenda.builder()
                .description("teste")
                .id(1)
                .build();
    }

    private Optional<Agenda> mockAgendaO() {
        return Optional.of(Agenda.builder()
                .description("teste")
                .id(1)
                .build());
    }

    private AgendaRequestDTO mockAgendaR() {
        return AgendaRequestDTO.builder()
                .description("Aumento de salario")
                .build();
    }
}
