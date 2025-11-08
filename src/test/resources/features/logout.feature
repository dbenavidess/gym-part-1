Feature: Logout
  In order to invalidate current tokens
  Consumers must be able to logout

  Scenario: Logout with valid bearer token succeeds
    Given a token "Bearer token-abc" belongs to user "john"
    When the client POSTs to "/logout" with header "Authorization" set to "Bearer token-abc"
    Then the response status is 200
    And the token for user "john" is invalidated

  Scenario: Logout without bearer header fails
    When the client POSTs to "/logout" without an Authorization header
    Then the response status is 400