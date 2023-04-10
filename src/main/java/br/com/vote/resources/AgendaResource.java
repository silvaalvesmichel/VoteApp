package br.com.vote.resources;

import br.com.vote.resources.dto.request.AgendaRequestDTO;
import br.com.vote.resources.dto.response.AgendaResponseDTO;
import br.com.vote.service.AgendaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@Api(value = "Agenda")
@RequestMapping(value = "/api")
public class AgendaResource {

    private static final Logger logger = LoggerFactory.getLogger(AgendaResource.class);

    private AgendaService agendaService;

    @ApiOperation(value = "Register a agenda")
    @PostMapping("/agenda")
    public ResponseEntity<AgendaResponseDTO> agenda(@RequestBody AgendaRequestDTO agendaDTO){
        logger.info("Cadastro de pauta - inicio {}", agendaDTO);
        AgendaResponseDTO response =  agendaService.saveAgenda(agendaDTO);
        logger.info("Cadastro de pauta - fim {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @ApiOperation(value = "Perform agenda ID search")
    @GetMapping("/agenda/{id}")
    public ResponseEntity<AgendaResponseDTO> getAgenda(@PathVariable Integer id){
        logger.info("Recuperar pauta - inicio {}", id);
        AgendaResponseDTO response =agendaService.getAgenda(id);
        logger.info("Recuperar pauta - fim {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
