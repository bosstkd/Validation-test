## Generated Back-End-Socle by (Amine):
Generator version 1.0,
> This software platform adheres to the Domain-Driven Design (DDD) architecture, which includes four layers: Domain, Infrastructure, Application, and Presentation. The platform's version 1.0 is designed to be user-friendly and easy to use, as it comes equipped with various features such as a validation mechanism to ensure correct data inputs, a structural database configuration for efficient data storage and retrieval, an exception management mechanism to handle errors, and a simple example to demonstrate how to use the platform

### How the validation mechanism works:
On the embedded example once the payload comes from TestController.getTestModel() to TestService.testServiceValidationCall(testModel) --> 
this method call's the validation mechanism on ( TestValidator.validate(myModel) ) as follow:
- Validate method take's an Object as parameter then it can be called with any Object or model and confirms that the object is not null,
- Once in the validate method this one instantiate a Set<ValidationError> where ValidationError has the following body:
```
{
    "message": "Error message",
    "champ": "The attribute that make's the problem"
}
```
- And call's ObjectConstraintValidator.validate(testModel), if this one detect a problem it catch them and put all errors in the Set<ValidationError>;
- ObjectConstraintValidator.validate(testModel) Call the javax.validation.ValidatorFactory and validate the object based on the javax.validation.constraints.*;
- If there's a violation of the constraints then Set<ConstraintViolation<T>> should be not empty then it makes throw of :
    >  MyProjectNameValidationException.creerValidationException(constraintViolations);

### How the exception management mechanism works:
- The exception management mechanism is directly related to the validation mechanism and it contains other exceptions not related to the validation,
- In the domain.common.exceptions package we can find the default exposed exceptions classes (BadRequest, NotFound and Server) exceptions.
- This classes can be called for the HTTP status exceptions and they are handled in (presentation.common.error.mapper.ExceptionMapper class) called as a controllerAdvice,
- For ValidationException this one has a badRequest status and when we call it for example as :
  > MyProjectNameValidationException.creerValidationException(constraintViolations)
  + It call's the MnbValidationException constructor with the general error message and the Set of ValidationError;
  + And as MnbValidationException extends RuntimeException and it's provided in the ExceptionMapper then it's mapped as an ErrorMessage body:
    + ```
      {
        "code": 400,
        "timestamp": "2023-05-07 T 13:25:02",
        "error": "My general message",
        "details": [
          {
            "message": "Le nom ne peut etre null",
            "champ": "domain.model.TestModel.nom"
          },
          {
            "message": "Le prenom n'est pas valide",
            "champ": "domain.model.TestModel.prénom"
          }
        ]
      }
      ```
### The database configuration:
- The database by default is configured as H2 structural DB but if we cant to change the configuration simply we can make it on application.yaml and updates the pom.xml file with informations of the DB that we want to use.