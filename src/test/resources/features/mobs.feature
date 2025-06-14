Feature: Mob behavior

  Background:
    Given a new game
    And the player starts at 10,10

  Scenario: Mob spawns adjacent and immediately attacks
    Given a Mob exists at 10,9
    When I advance the game by 0.1 seconds
    Then the player's health should decrease by 10

  Scenario: Mob chases player when far away
    Given a Mob exists at 10,8
    When I advance the game by 1 seconds
    Then the mob's position should be closer to the player

  Scenario: Mob stops chasing if player is dead
    Given the player's health is 0
    And a Mob exists at 11,10
    When I advance the game by 0.5 seconds
    Then the mob should not move
