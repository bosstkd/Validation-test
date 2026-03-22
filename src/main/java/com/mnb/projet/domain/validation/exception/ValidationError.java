package com.mnb.projet.domain.validation.exception;

public record ValidationError(String message, String champ) {

  @Override
  public String toString() {
    return "ValidationError{" +
        "message=" + message +
        ", champ=" + champ +
        '}';
  }
}
