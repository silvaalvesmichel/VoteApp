package br.com.vote.resources;

import br.com.vote.resources.dto.request.VoteRequestDTO;
import br.com.vote.resources.dto.response.SessionResponseDTO;
import br.com.vote.service.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@Api(value = "Vote")
@RequestMapping(value = "/api")
public class VoteResource {

    private static final Logger logger = LoggerFactory.getLogger(VoteResource.class);

    private SessionService sessionService;

    @ApiOperation(value = "Register a Vote")
    @PostMapping("/vote")
    public ResponseEntity<SessionResponseDTO> vote(@RequestBody VoteRequestDTO voteNewDTO){
        logger.info("Registro de voto - inicio {}", voteNewDTO);
        SessionResponseDTO response =  sessionService.saveVote(voteNewDTO);
        logger.info("Registro de voto - fim {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
