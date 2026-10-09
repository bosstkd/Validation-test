package com.mnb.projet.presentation;

import com.mnb.projet.application.model.MnbModelDTO;
import com.mnb.projet.application.service.MnbApplicationService;
import com.mnb.projet.domain.common.exceptions.DomainBadRequestCommandException;
import com.mnb.projet.domain.common.exceptions.DomainResourceNotFoundException;
import com.mnb.projet.domain.common.exceptions.DomainInternalException;
import com.mnb.projet.domain.validation.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController()
@CrossOrigin("*")
@RequestMapping("/mnb")
@RequiredArgsConstructor
@Tag(name = "Mnb", description = "Création et recherche d'un mnb, et endpoints d'exemple des erreurs")
public class MnbController {

    private final MnbApplicationService service;

    @Operation(summary = "Hello", description = "Retourne un message de bienvenue")
    @ApiResponse(responseCode = "200", description = "Message de bienvenue")
    @GetMapping("/hello")
    public String getHello() {
        return "Hello Mnb";
    }

    @Operation(summary = "Exemple d'erreur 400", description = "Lève systématiquement une erreur de requête invalide")
    @ApiResponse(responseCode = "400", description = "Requête invalide",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping("/badRequest")
    public String getBadRequestException() {

        throw new DomainBadRequestCommandException(DomainBadRequestCommandException.TEST_ERROR_BadRequest_MESSAGE,
                Stream.of("a", "b", "c").collect(Collectors.toSet()));
    }

    @Operation(summary = "Exemple d'erreur 500", description = "Lève systématiquement une erreur interne")
    @ApiResponse(responseCode = "500", description = "Erreur interne",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping("/serverError")
    public String getServerException() {
        throw new DomainInternalException(DomainInternalException.TEST_ERROR_Server_MESSAGE);
    }

    @Operation(summary = "Exemple d'erreur 404", description = "Lève systématiquement une erreur de ressource introuvable")
    @ApiResponse(responseCode = "404", description = "Ressource introuvable",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping("/notFound")
    public String getNotFoundException() {
        throw new DomainResourceNotFoundException(DomainResourceNotFoundException.TEST_ERROR_NotFound_MESSAGE);
    }

    @Operation(summary = "Créer un mnb", description = "Valide puis enregistre le mnb, et retourne le mnb créé")
    @ApiResponse(responseCode = "200", description = "Mnb créé")
    @ApiResponse(responseCode = "400", description = "Mnb invalide",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping("/create")
    public ResponseEntity<MnbModelDTO> createExample(@RequestBody MnbModelDTO mnbModelDTO) {

        return ResponseEntity.ok(service.createMnb(mnbModelDTO));
    }

    @Operation(summary = "Rechercher un mnb", description = "Retourne le mnb correspondant à l'email")
    @ApiResponse(responseCode = "200", description = "Mnb trouvé")
    @ApiResponse(responseCode = "400", description = "Email invalide",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "Aucun mnb pour cet email",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping("/find")
    public ResponseEntity<MnbModelDTO> findExample(
            @Parameter(description = "Email du mnb recherché", example = "a-ek@hotmail.fr") @RequestParam String email) {

        return ResponseEntity.ok(service.findMnb(email));
    }
}
