package br.com.vote.resources;

import br.com.vote.resources.dto.request.SessionRequestDTO;
import br.com.vote.resources.dto.response.SessionResponseDTO;
import br.com.vote.resources.dto.response.SessionStatusResponseDTO;
import br.com.vote.service.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@Api(value = "Session")
@RequestMapping(value = "/api")
public class SessionResource {

    private static final Logger logger = LoggerFactory.getLogger(SessionResource.class);

    private SessionService sessionService;

    @ApiOperation(value = "Register a session")
    @PostMapping("/session")
    public ResponseEntity<SessionResponseDTO> session(@RequestBody SessionRequestDTO sessionRequestDTO){
        logger.info("Cadastro de sessao - inicio {}", sessionRequestDTO);
        SessionResponseDTO response =  sessionService.saveSession(sessionRequestDTO);
        logger.info("Cadastro de sessao - fim {}",response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @ApiOperation(value = "Perform session ID search")
    @GetMapping("/session/{id}")
    public ResponseEntity<SessionResponseDTO> getSession(@PathVariable Integer id){
        logger.info("Recuperar sessao - inicio {}",id);
        SessionResponseDTO response =sessionService.getSession(id);
        logger.info("Recuperar sessao - fim {}",response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @ApiOperation(value = "Perform session results ID search")
    @GetMapping("/session/status/{id}")
    public ResponseEntity<SessionStatusResponseDTO> getSessionStatus(@PathVariable Integer id){
        logger.info("Recuperar status da sessao - inicio {}",id);
        SessionStatusResponseDTO response =sessionService.getSessionStatus(id);
        logger.info("Recuperar status da sessao sessao - fim {}",response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
