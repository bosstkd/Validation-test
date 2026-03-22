package com.mnb.projet.presentation;

import com.mnb.projet.application.MnbUseCase;
import com.mnb.projet.domain.common.exceptions.MnbBadRequestException;
import com.mnb.projet.domain.common.exceptions.MnbNotFoundException;
import com.mnb.projet.domain.common.exceptions.MnbServerException;
import com.mnb.projet.domain.model.MnbModel;
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

    private final MnbUseCase service;

    @GetMapping("/hello")
    public String getHello() {
        return "Hello Mnb";
    }

    @GetMapping("/badRequest")
    public String getBadRequestException() {

        throw new MnbBadRequestException(MnbBadRequestException.TEST_ERROR_BadRequest_MESSAGE,
                Stream.of("a", "b", "c").collect(Collectors.toSet()));
    }

    @GetMapping("/serverError")
    public String getServerException() {
        throw new MnbServerException(MnbServerException.TEST_ERROR_Server_MESSAGE);
    }

    @GetMapping("/notFound")
    public String getNotFoundException() {
        throw new MnbNotFoundException(MnbNotFoundException.TEST_ERROR_NotFound_MESSAGE);
    }

    @PostMapping("/create")
    public ResponseEntity<MnbModel> createExample(@RequestBody MnbModel MnbModel) {

        return ResponseEntity.ok(service.createMnb(MnbModel));
    }

    @GetMapping("/find")
    public ResponseEntity<MnbModel> createExample(@RequestParam String email) {

        return ResponseEntity.ok(service.findMnb(email));
    }
}
