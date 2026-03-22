package com.mnb.projet.domain.validation.message;

public class GenericValidatorPatterns {

  private GenericValidatorPatterns() {
  }

  public static final String REG_EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
  public static final String REG_NAME = "[\u00C0-\u017Fa-zA-Z']+([- ][\u00C0-\u017Fa-zA-Z']+)*";
  public static final String REG_FRANCE_CODE_POSTAL = "([0-9]{5})";
}
