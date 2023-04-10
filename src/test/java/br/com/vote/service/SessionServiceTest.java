package br.com.vote.service;

import br.com.vote.commons.annotation.Unitario;
import br.com.vote.repository.AgendaRepository;
import br.com.vote.repository.SessionRepository;
import br.com.vote.repository.UserRepository;
import br.com.vote.repository.entity.Agenda;
import br.com.vote.repository.entity.Session;
import br.com.vote.repository.entity.User;
import br.com.vote.resources.dto.request.SessionRequestDTO;
import br.com.vote.resources.dto.request.VoteRequestDTO;
import br.com.vote.resources.dto.response.AgendaResponseDTO;
import br.com.vote.resources.dto.response.SessionResponseDTO;
import br.com.vote.resources.dto.response.UserResponseDTO;
import br.com.vote.service.exception.NotFoundException;
import br.com.vote.service.mapper.SessionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@Unitario
@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionService sessionService;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private SessionMapper sessionMapper;
    @Mock
    private AgendaRepository agendaRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SqsMessageService sqsMessageService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sessionService = new SessionService(sessionRepository, sessionMapper, agendaRepository, userRepository, sqsMessageService);
    }

    @Test
    @DisplayName("Service - save session")
    public void deveriaAdicionarSession() {
        Mockito.when(agendaRepository.findById(any(Integer.class))).thenReturn(Optional.of(mockAgenda()));
        Mockito.when(sessionMapper.dtoToEntity(any(SessionRequestDTO.class), any(Agenda.class))).thenReturn(mockSession());
        Mockito.when(sessionRepository.save(any(Session.class))).thenReturn(mockSession());
        Mockito.when(sessionMapper.entityToDTO(any(Session.class))).thenReturn(mockSessionResponse());
        var session = sessionService.saveSession(mockSessionR());
        assertNotNull(session);
    }

    @Test
    @DisplayName("Service - save session - Error")
    public void naodeveriaAdicionarSession() {
        Mockito.when(agendaRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.saveSession(mockSessionR()));
    }

    @Test
    @DisplayName("Service - get session")
    public void deveriaRecuperarSession() {
        Mockito.when(sessionRepository.findById(any(Integer.class))).thenReturn(Optional.of(mockSession()));
        Mockito.when(sessionMapper.entityToDTO(any(Session.class))).thenReturn(mockSessionResponse());
        var session = sessionService.getSession(1);
        assertNotNull(session);
    }

    @Test
    @DisplayName("Service - save session - error")
    public void deveriaRecuperarSessionError() {
        Mockito.when(sessionRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.getSession(1));
    }

    @Test
    @DisplayName("Service - save vote")
    public void deveriaCadastrarVoto() {
        Mockito.when(sessionRepository.findById(any(Integer.class))).thenReturn(Optional.of(mockSession()));
        Mockito.when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(mockUser()));
        Mockito.when(sessionRepository.save(any(Session.class))).thenReturn(mockSession());
        Mockito.when(sessionMapper.entityToDTO(any(Session.class))).thenReturn(mockSessionResponse());

        var session = sessionService.saveVote(mockVoteNew());
        assertNotNull(session);
    }

    @Test
    @DisplayName("Service - save vote - erro")
    public void naodeveriaCadastrarVoto() {
        Mockito.when(sessionRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.saveVote(mockVoteNew()));
    }

    @Test
    @DisplayName("Service - desactive session")
    public void deveriaDesativarSessao() {
        Mockito.when(sessionRepository.findById(any(Integer.class))).thenReturn(Optional.of(mockSession()));
        sessionService.desactivateSession("1");
    }

    @Test
    @DisplayName("Service - get status vote")
    public void deveriaBuscarStatusSession() {
        Mockito.when(sessionRepository.findById(any(Integer.class))).thenReturn(Optional.of(mockSession()));
        var session = sessionService.getSessionStatus(1);
        assertNotNull(session);

        assertThrows(NotFoundException.class, () -> sessionService.saveVote(mockVoteNew()));
    }

    @Test
    @DisplayName("Service - get status vote - erro")
    public void naodeveriaBuscarStatusSession() {
        Mockito.when(sessionRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.getSessionStatus(1));
    }

    private VoteRequestDTO mockVoteNew() {
        return VoteRequestDTO.builder()
                .idSession(1)
                .idAssociated(1)
                .tipoVoto("sim")
                .build();
    }

    private SessionResponseDTO mockSessionResponse() {
        return SessionResponseDTO.builder()
                .id(1)
                .expirationDate(LocalDateTime.now())
                .agenda(mockAgendaResponse())
                .associatedVote(List.of(mockUserResponse()))
                .isActive(Boolean.TRUE)
                .quantityVoteNo(0)
                .quantityVoteYes(0)
                .build();
    }

    private UserResponseDTO mockUserResponse() {
        return UserResponseDTO.builder()
                .id(12)
                .build();
    }

    private AgendaResponseDTO mockAgendaResponse() {
        return AgendaResponseDTO.builder()
                .description("teste")
                .id(1)
                .build();
    }


    private SessionRequestDTO mockSessionR() {
        return SessionRequestDTO.builder()
                .expirationDate(LocalDateTime.now())
                .idAgenda(1)
                .build();
    }

    private Session mockSession() {
        return Session.builder()
                .id(1)
                .expirationDate(LocalDateTime.now())
                .agenda(mockAgenda())
                .associatedVote(List.of(mockUser()))
                .isActive(Boolean.TRUE)
                .quantityVoteNo(0)
                .quantityVoteYes(0)
                .build();
    }

    private User mockUser() {
        return User.builder()
                .cpf("65590367077")
                .id(12)
                .email("teste@teste.com")
                .name("teste")
                .password("asdfgadsf")
                .build();
    }

    private Agenda mockAgenda() {
        return Agenda.builder()
                .description("teste")
                .id(1)
                .build();
    }
}
