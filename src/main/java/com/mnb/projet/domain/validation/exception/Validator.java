package com.mnb.projet.domain.validation.exception;

import static com.mnb.projet.domain.validation.exception.ValidationException.DONNEES_INCORRECTES_EXCEPTION;

import com.mnb.projet.domain.validation.ObjectConstraintValidator;
import java.util.HashSet;
import java.util.Set;

public class Validator {

  public static void validate(Object testModel) {
    if (testModel == null) {
      throw new ValidationException(DONNEES_INCORRECTES_EXCEPTION,
          Set.of(new ValidationError("L'objet à valider ne peut etre null",
              "object")));
    }

    try {
      ObjectConstraintValidator.validate(testModel);
    } catch (ValidationException ex) {
      // if ObjectConstraintValidator.validate returns validation errors they will be in ex.getErreurs()
      // that we use to overload ValidationException
      throw new ValidationException(DONNEES_INCORRECTES_EXCEPTION, ex.getErreurs());
    }
  }

  public static void validate(Object testModel, Class<?> group) {
    Validator.validate(testModel);
  }
}
