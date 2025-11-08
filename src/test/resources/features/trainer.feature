Feature: Trainer management

  Scenario: Create trainer successfully
    Given training type "Yoga" exists
    And username generated for "John" "Doe" is available
    And password encoder returns "encodedPassword"
    And trainer creation returns the new trainer
    And jwt token generated is "jwt-token"
    When the client POSTs to "/trainer" with first name "John", last name "Doe" and specialization "Yoga"
    Then the response status is 201
    And the response JSON has "$.username" equal to "John.Doe"
    And the response JSON has "$.password" equal to "encodedPassword"
    And the response JSON has "$.token" equal to "jwt-token"
    And trainer creation is requested

  Scenario: Creating trainer with invalid data returns bad request
    Given training type "Yoga" exists
    And username generated for "Jane" "Smith" is available
    And password encoder returns "encodedPassword"
    And trainer creation fails with message "Invalid Trainer"
    When the client POSTs to "/trainer" with first name "Jane", last name "Smith" and specialization "Yoga"
    Then the response status is 400
    And the response body contains "Invalid Trainer"

  Scenario: Update trainer successfully
    Given training type "Yoga" exists
    And trainer update returns trainer with username "john.doe" and specialization "Yoga"
    And trainer has no trainees
    When the client PUTs to "/trainer" with username "john.doe", first name "John", last name "Doe", specialization "Yoga" and active "true"
    Then the response status is 200
    And the response JSON has "$.username" equal to "john.doe"
    And the response JSON has "$.specialization.name" equal to "Yoga"

  Scenario: Get trainer by username when exists
    Given trainer lookup for "john.doe" returns specialization "Yoga"
    And trainer has no trainees
    When the client GETs "/trainer/john.doe" trainer
    Then the response status is 200
    And the response JSON has "$.username" equal to "john.doe"

  Scenario: Get trainer by username when not found
    Given trainer lookup for "missing" returns nothing
    When the client GETs "/trainer/missing"
    Then the response status is 404
    And trainer trainees are not requested