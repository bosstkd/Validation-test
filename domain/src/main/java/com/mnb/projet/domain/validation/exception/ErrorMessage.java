package com.mnb.projet.domain.validation.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;

@JsonInclude(Include.NON_NULL)
public record ErrorMessage(int code, Date timestamp, String error, Object details) {

}
