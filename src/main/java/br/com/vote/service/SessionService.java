package br.com.vote.service;

import br.com.vote.repository.AgendaRepository;
import br.com.vote.repository.SessionRepository;
import br.com.vote.repository.UserRepository;
import br.com.vote.repository.entity.Agenda;
import br.com.vote.repository.entity.Session;
import br.com.vote.repository.entity.User;
import br.com.vote.resources.dto.request.SessionRequestDTO;
import br.com.vote.resources.dto.request.VoteRequestDTO;
import br.com.vote.resources.dto.response.SessionResponseDTO;
import br.com.vote.resources.dto.response.SessionStatusResponseDTO;
import br.com.vote.service.enumerator.TipoVotoEnum;
import br.com.vote.service.exception.NotFoundException;
import br.com.vote.service.exception.RoleException;
import br.com.vote.service.mapper.SessionMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SessionService {

    private static final Logger logger = LoggerFactory.getLogger(SessionService.class);

    private SessionRepository sessionRepository;
    private SessionMapper sessionMapper;
    private AgendaRepository agendaRepository;
    private UserRepository userRepository;
    private SqsMessageService sqsMessageService;

    public SessionResponseDTO saveSession(SessionRequestDTO sessionDTO) {
        logger.info("Cadastro de sessao - inicio service {}", sessionDTO);
        Optional<Agenda> agenda = agendaRepository.findById(sessionDTO.getIdAgenda());
        if (!agenda.isEmpty()) {
            Session newSession = sessionMapper.dtoToEntity(sessionDTO, agenda.get());

            newSession = sessionRepository.save(newSession);
            SessionResponseDTO retorno = sessionMapper.entityToDTO(newSession);
            logger.info("Envio de mensagem sqs");
            sqsMessageService.sendMessage(newSession.getId().toString());
            logger.info("Cadastro de sessao - fim service {}", retorno);
            return retorno;
        } else {
            logger.error("Pauta não encontrada, favor informe uma Pauta valida.");
            throw new NotFoundException("Pauta não encontrada, favor informe uma Pauta valida.");
        }
    }

    public SessionResponseDTO getSession(Integer id) {
        logger.info("Recuperar sessao - inicio service {}", id);
        Optional<Session> session = sessionRepository.findById(id);
        if (session.isEmpty()) {
            logger.error("Sessão não encontrada");
            throw new NotFoundException("Sessão não encontrada");
        }
        logger.info("Recuperar sessao - fim service ");
        return sessionMapper.entityToDTO(session.orElse(null));
    }

    public SessionResponseDTO saveVote(VoteRequestDTO voteNewDTO) {
        logger.info("Cadastro de voto - inicio service {}", voteNewDTO);
        fieldsRequired(voteNewDTO);
        Optional<Session> session = sessionRepository.findById(voteNewDTO.getIdSession());
        if (session.isEmpty()) {
            logger.error("Sessão não encontrada");
            throw new NotFoundException("Sessão não encontrada");
        }
        Optional<User> user = userRepository.findById(voteNewDTO.getIdAssociated());
        if (user.isEmpty()) {
            logger.error("Usuário não encontrado");
            throw new NotFoundException("Usuário não encontrado");
        }

        Session sessionVote = session.get();
        sessionVote.getAssociatedVote().forEach(userV -> {
            if (userV.getId() == voteNewDTO.getIdAssociated()) {
                logger.error("Usuário já votou");
                throw new RoleException("Usuário já votou");
            }
        });


        if (!sessionVote.getIsActive()) {
            logger.error("Sessão já foi finalizada.");
            throw new RoleException("Sessão já foi finalizada.");
        }

        if (Objects.isNull(sessionVote.getAssociatedVote())) {
            sessionVote.setAssociatedVote(List.of(user.get()));
        } else {
            ArrayList<User> users = new ArrayList<>();
            users.addAll(sessionVote.getAssociatedVote());
            var userAdd = user.get();
            users.add(userAdd);
            sessionVote.setAssociatedVote(users);
        }

        if (voteNewDTO.getTipoVoto().equals(TipoVotoEnum.SIM.getCodigo())) {
            sessionVote.setQuantityVoteYes(Objects.isNull(sessionVote.getQuantityVoteYes()) ? 1 : sessionVote.getQuantityVoteYes() + 1);
        } else {
            sessionVote.setQuantityVoteNo(Objects.isNull(sessionVote.getQuantityVoteNo()) ? 1 : sessionVote.getQuantityVoteNo() + 1);
        }

        sessionVote = sessionRepository.save(sessionVote);
        logger.info("Cadastro de voto sessao - fim service {}", sessionVote);
        return sessionMapper.entityToDTO(sessionVote);
    }

    private void fieldsRequired(VoteRequestDTO voteNewDTO) {
        if (Objects.isNull(voteNewDTO.getIdSession())) {
            logger.error("Identificador da sessão é obrigatório.");
            throw new RoleException("Identificador da sessão é obrigatório.");
        }
        if (Objects.isNull(voteNewDTO.getIdAssociated())) {
            logger.error("Identificador do associado é obrigatório.");
            throw new RoleException("Identificador do associado é obrigatório.");
        }
        if (Objects.isNull(voteNewDTO.getTipoVoto())) {
            logger.error("Tipo de voto é obrigatório.");
            throw new RoleException("Tipo de voto é obrigatório.");
        }
        if (Objects.isNull(TipoVotoEnum.toEnum(voteNewDTO.getTipoVoto()))) {
            logger.error("Tipo de voto inválido, informe sim ou nao.");
            throw new RoleException("Tipo de voto inválido, informe sim ou nao.");
        }
    }

    public void desactivateSession(String idSession) {
        logger.info("Desativar sessao - inicio service {}", idSession);
        Integer id = Integer.parseInt(idSession);
        Optional<Session> session = sessionRepository.findById(id);
        if (!session.isEmpty()) {
            Session sessionActivate = session.get();
            LocalDateTime dateNow = LocalDateTime.now();
            if (sessionActivate.getExpirationDate().isBefore(dateNow)) {
                sessionActivate.setIsActive(Boolean.FALSE);
                logger.info("Sessao desativada");
                sessionRepository.save(sessionActivate);
            } else {
                logger.info("Sessao nao desativada");
                sqsMessageService.sendMessage(sessionActivate.getId().toString());
            }
        }

    }

    public SessionStatusResponseDTO getSessionStatus(Integer id) {
        logger.info("Recuperar status de sessao - inicio service {}", id);
        Optional<Session> session = sessionRepository.findById(id);
        if (!session.isEmpty()) {
            Session sessionVote = session.get();
            var response = new SessionStatusResponseDTO();
            if (sessionVote.getQuantityVoteYes() > sessionVote.getQuantityVoteNo()) {
                response.setResultado("Aprovada");
            } else if(sessionVote.getQuantityVoteYes() < sessionVote.getQuantityVoteNo()) {
                response.setResultado("Reprovada");
            }else{
                response.setResultado("Empate");
            }
            if (sessionVote.getIsActive()) {
                response.setSituacao("Em votação");
            } else {
                response.setSituacao("Votação finalizada");
            }
            logger.info("Cadastro de sessao - fim service {}",response);
            return response;
        } else {
            logger.error("Sessão não encontrada");
            throw new NotFoundException("Sessão não encontrada");
        }
    }
}
