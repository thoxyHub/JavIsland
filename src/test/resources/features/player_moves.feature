Feature: Player movement on the island

  Scenario: Move north from starting position
    Given a new game
    And the player starts at 10,10
    And the player is facing "SOUTH"
    When the player presses "DOWN"
    And I advance the game by 1 seconds
    Then the player's position should be 10,11

  Scenario: Move east from starting position
    Given a new game
    And the player starts at 10,10
    And the player is facing "EAST"
    When the player presses "RIGHT"
    Then the player's position should be 11,10

  Scenario: Look East
    Given a new game
    And the player starts at 10,10
    When the player presses "RIGHT"
    Then the player is facing "EAST"
    And the player's position should be 10,10

  Scenario: Move east then look south
    Given a new game
    And the player starts at 10,10
    And the player is facing "EAST"
    When the player presses "RIGHT"
    And the player presses "DOWN"
    Then the player's position should be 11,10
    And the player is facing "SOUTH"

  Scenario: Move west from starting position
    Given a new game
    And the player starts at 10,10
    And the player is facing "WEST"
    When the player presses "LEFT"
    Then the player's position should be 9,10

  Scenario: Move south from starting position
    Given a new game
    And the player starts at 10,10
    And the player is facing "SOUTH"
    When the player presses "DOWN"
    Then the player's position should be 10,11

  Scenario: Look North
    Given a new game
    And the player starts at 10,10
    When the player presses "UP"
    Then the player is facing "NORTH"
    And the player's position should be 10,10

  Scenario: Look West
    Given a new game
    And the player starts at 10,10
    When the player presses "LEFT"
    Then the player is facing "WEST"
    And the player's position should be 10,10

  Scenario: Move north then look east
    Given a new game
    And the player starts at 10,10
    And the player is facing "NORTH"
    When the player presses "UP"
    And the player presses "RIGHT"
    Then the player's position should be 10,9
    And the player is facing "EAST"

  Scenario: Move south then look west
    Given a new game
    And the player starts at 10,10
    And the player is facing "SOUTH"
    When the player presses "DOWN"
    And the player presses "LEFT"
    Then the player's position should be 10,11
    And the player is facing "WEST"

  Scenario: Move east twice
    Given a new game
    And the player starts at 10,10
    And the player is facing "EAST"
    When the player presses "RIGHT"
    And the player presses "RIGHT"
    Then the player's position should be 12,10

  Scenario: Move west then look east
    Given a new game
    And the player starts at 10,10
    And the player is facing "WEST"
    When the player presses "LEFT"
    And the player presses "RIGHT"
    Then the player's position should be 9,10
    And the player is facing "EAST"