Feature: Trainee management

  Background:
    Given password encoder returns "encodedPassword"
    And jwt token generated is "jwt-token"

  Scenario: Create trainee successfully
    Given username generated for "John" "Doe" is available
    And trainee creation returns the new trainee
    When the client POSTs to "/trainee" with first name "John", last name "Doe", address "123 Main St" and birth date "1990-01-01"
    Then the response status is 201
    And the response JSON has "$.username" equal to "John.Doe"
    And the response JSON has "$.password" equal to "encodedPassword"
    And the response JSON has "$.token" equal to "jwt-token"
    And the response JSON has "$._links.self.href" equal to "http://localhost/trainee/John.Doe"
    And trainee creation is requested

  Scenario: Creating trainee with invalid data returns bad request
    Given username generated for "Jane" "Smith" is available
    And trainee creation fails with message "Invalid Trainee"
    When the client POSTs to "/trainee" with first name "Jane", last name "Smith", address "456 Main St" and birth date "1992-05-10"
    Then the response status is 400
    And the response body contains "Invalid Trainee"

  Scenario: Update trainee successfully
    Given trainee update returns trainee with username "john.doe", first name "John", last name "Doe", address "123 Main St", birth date "1990-01-01" and active "true"
    When the client PUTs to "/trainee" with username "john.doe", first name "John", last name "Doe", address "123 Main St", birth date "1990-01-01" and active "true"
    Then the response status is 200
    And the response JSON has "$.username" equal to "john.doe"
    And trainee trainer list is requested

  Scenario: Get trainee by username when exists
    Given trainee lookup for "john.doe" returns first name "John", last name "Doe", address "123 Main St", birth date "1990-01-01" and active "true"
    And trainee trainer list for "john.doe" returns trainers "trainer1.yoga"
    When the client GETs "/trainee/john.doe"
    Then the response status is 200
    And the response JSON has "$.username" equal to "john.doe"
    And the response JSON has "$.trainersList[0].username" equal to "trainer1.yoga"

  Scenario: Get trainee by username when not found
    Given trainee lookup for "missing" returns nothing
    When the client GETs "/trainee/missing"
    Then the response status is 404
    And trainee trainer list is not requested

  Scenario: Delete trainee by username
    When the client DELETEs "/trainee/john.doe" trainee
    Then the response status is 200
    And trainee deletion is requested for "john.doe"

  Scenario: Get not assigned trainers for trainee
    Given trainee lookup for "john.doe" returns first name "John", last name "Doe", address "123 Main St", birth date "1990-01-01" and active "true"
    And not assigned trainers for "john.doe" returns trainers "trainer1.yoga,trainer2.cardio"
    When the client GETs "/trainee/john.doe/get-not-assigned-trainers"
    Then the response status is 200
    And the response JSON has "$[0].username" equal to "trainer1.yoga"

  Scenario: Update trainee trainers list successfully
    Given updating trainee "john.doe" trainers succeeds returning trainers "trainer1.yoga,trainer2.cardio"
    When the client PUTs to "/trainee/john.doe/update-trainers" with trainer usernames "trainer1.yoga,trainer2.cardio"
    Then the response status is 200
    And update trainee trainer list is requested for "john.doe"
    And the response JSON has "$[1].username" equal to "trainer2.cardio"

  Scenario: Update trainee trainers list fails when trainee absent
    Given updating trainee "missing" trainers fails with no such element
    When the client PUTs to "/trainee/missing/update-trainers" with trainer usernames "trainer1.yoga"
    Then the response status is 404