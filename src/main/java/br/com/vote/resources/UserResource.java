package br.com.vote.resources;

import br.com.vote.resources.dto.response.UserDTO;
import br.com.vote.resources.dto.request.UserRequestDTO;
import br.com.vote.resources.dto.response.UserResponseDTO;
import br.com.vote.service.UserService;
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
@Api(value = "User")
@RequestMapping(value = "/api")
public class UserResource {

    private static final Logger logger = LoggerFactory.getLogger(UserResource.class);

    private UserService userService;

    @ApiOperation(value = "Register a user")
    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> users(@RequestBody UserRequestDTO userRequestDTO){
        logger.info("Cadastro de usuario - inicio {}", userRequestDTO);
        UserResponseDTO response =  userService.saveUser(userRequestDTO);
        logger.info("Cadastro de usuario - fim {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @ApiOperation(value = "Perform user ID search")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Integer id){
        logger.info("Recuperar usuario - inicio {}", id);
        UserDTO response =userService.getUser(id);
        logger.info("Recuperar usuario - fim {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}