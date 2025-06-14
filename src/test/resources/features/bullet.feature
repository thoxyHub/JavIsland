Feature: Bullet mechanics

  Background:
    Given a new game
    And the player starts at 10,10
    And the player has at least 1 Ammo in inventory

  Scenario: Fire a bullet east into empty space
    And the player is facing "EAST"
    When the player presses "G"
    Then there should be a Bullet at 11,10
    And the player's Ammo inventory should decrease by 1

  Scenario: Bullet disappears on water
    And the player starts at 12,5
    Given a water Tile exists at 13,5
    And the player is facing "EAST"
    When the player presses "G"
    Then no Bullet should exist at 11,10

  Scenario: Bullet damages mob
    Given a Mob exists at 13,10
    And the player is facing "EAST"
    And a Tree exists at 13,9
    And a Tree exists at 14,10
    And a Tree exists at 13,11
    And a Tree exists at 12,9
    And a Tree exists at 12,11
    When the player presses "G"
    And I advance the game by 1 seconds
    Then the mob's health should decrease by 50
    And the Bullet at 11,10 should no longer exist
