Feature: User management operations through login controller

  Scenario: Change password delegates to the service
    When the client PUTs to "/change-password" with username "john", old password "oldPass" and new password "newPass"
    Then the response status is 200
    And the password change is delegated for "john" with "oldPass" and "newPass"

  Scenario: Change active status toggles the user
    When the client PATCHes to "/change-active" with username "john"
    Then the response status is 200
    And the active flag is toggled for user "john"