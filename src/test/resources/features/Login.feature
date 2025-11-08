Feature: Login
  As a consumer of the login API
  I want to authenticate with username and password
  So that I can obtain a JWT token or receive an appropriate rejection

  Scenario: Successful login clears brute force attempts and returns JWT
    Given there is a brute force record for user "john" without a lock
    And the credentials for user "john" are valid and produce token "jwt-token-123"
    When the client POSTs to "/login" with username "john" and password "password"
    Then the response status is 200
    And the response body contains a JWT token "jwt-token-123"
    And brute force attempts are cleared for user "john"

  Scenario: Locked user cannot login
    Given user "john" is currently locked until one hour in the future
    When the client POSTs to "/login" with username "john" and password "password"
    Then the response status is 403
    And the login service is not invoked for user "john"

  Scenario: Wrong credentials return 401 and register failed attempt
    Given there is a brute force record for user "john" without a lock
    And the credentials for user "john" are rejected
    When the client POSTs to "/login" with username "john" and password "wrong"
    Then the response status is 401
    And a failed login attempt is recorded for user "john"