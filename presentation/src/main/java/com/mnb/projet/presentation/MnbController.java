package com.mnb.projet.presentation;

import com.mnb.projet.application.model.MnbModelDTO;
import com.mnb.projet.application.service.MnbApplicationService;
import com.mnb.projet.domain.common.exceptions.DomainBadRequestCommandException;
import com.mnb.projet.domain.common.exceptions.DomainResourceNotFoundException;
import com.mnb.projet.domain.common.exceptions.DomainInternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController()
@CrossOrigin("*")
@RequestMapping("/mnb")
@RequiredArgsConstructor
public class MnbController {

    private final MnbApplicationService service;

    @GetMapping("/hello")
    public String getHello() {
        return "Hello Mnb";
    }

    @GetMapping("/badRequest")
    public String getBadRequestException() {

        throw new DomainBadRequestCommandException(DomainBadRequestCommandException.TEST_ERROR_BadRequest_MESSAGE,
                Stream.of("a", "b", "c").collect(Collectors.toSet()));
    }

    @GetMapping("/serverError")
    public String getServerException() {
        throw new DomainInternalException(DomainInternalException.TEST_ERROR_Server_MESSAGE);
    }

    @GetMapping("/notFound")
    public String getNotFoundException() {
        throw new DomainResourceNotFoundException(DomainResourceNotFoundException.TEST_ERROR_NotFound_MESSAGE);
    }

    @PostMapping("/create")
    public ResponseEntity<MnbModelDTO> createExample(@RequestBody MnbModelDTO mnbModelDTO) {

        return ResponseEntity.ok(service.createMnb(mnbModelDTO));
    }

    @GetMapping("/find")
    public ResponseEntity<MnbModelDTO> findExample(@RequestParam String email) {

        return ResponseEntity.ok(service.findMnb(email));
    }
}
