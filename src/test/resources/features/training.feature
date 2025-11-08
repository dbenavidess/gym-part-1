Feature: Training operations

  Scenario: Create a training successfully
    Given training creation will succeed
    When the client POSTs to "/training" with name "Morning Workout", date "2024-05-01", duration "60", training type "Cardio", trainer username "trainer.john" and trainee username "trainee.tom"
    Then the response status is 200
    And training creation is invoked with name "Morning Workout", date "2024-05-01", duration "60", training type "Cardio", trainer username "trainer.john" and trainee username "trainee.tom"

  Scenario: Search trainings by trainer returns list
    Given training search will return 1 result
    When the client GETs "/training" with trainer username "trainer.john", training type "Cardio", from "2024-05-01", to "2024-05-31", trainee username "trainee.tom"
    Then the response status is 200
    And the response JSON array size is 1
    And training search is invoked with trainer username "trainer.john", training type "Cardio", from "2024-05-01", to "2024-05-31", trainee username "trainee.tom"

  Scenario: Search trainings without trainer or trainee returns bad request
    When the client GETs "/training" with trainer username "", training type "", from "", to "", trainee username ""
    Then the response status is 400
    And the response body contains "Invalid Trainer/Trainee"
    And training search is not invoked

  Scenario: Delete training by id
    When the client DELETEs "/training/11111111-2222-3333-4444-555555555555" training
    Then the response status is 204
    And training deletion is invoked for id "11111111-2222-3333-4444-555555555555"

  Scenario: Delete training that does not exist returns not found
    Given deleting training with id "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee" throws not found
    When the client DELETEs "/training/aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee" training
    Then the response status is 404