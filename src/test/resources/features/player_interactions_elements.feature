Feature: Player interaction with elements

  Background:
    Given a new game
    And the player starts at 10,10

  Scenario: Hit a tree once and farm wood
    And a Tree exists at 11,10
    And the player is facing "EAST"
    When the player presses "S" 1 times
    And a Tree still exists at 11,10
    Then the player's inventory should contain 7 Wood

  Scenario: Chop down a tree and farm wood
    And a Tree exists at 11,10
    And the player is facing "EAST"
    When the player presses "S" 2 times
    And I advance the game by 0 seconds
    Then the Tree at 11,10 should no longer exist
    And the player's inventory should contain 9 Wood

  Scenario: Mine a rock and farm stone
    And a Rock exists at 10,11
    And the player is facing "SOUTH"
    When the player presses "S" 3 times
    And I advance the game by 0 seconds
    Then the Rock at 10,11 should no longer exist
    And the player's inventory should contain 8 Stone

  Scenario: Build a wood block
    And the player has at least 1 Wood in inventory
    And the player is facing "EAST"
    When the player presses "B"
    Then there should be a WoodBlock at 11,10
    And the player's Wood inventory should decrease by 1

  Scenario: Build a stone block
    And the player has at least 1 Stone in inventory
    And the player is facing "SOUTH"
    When the player presses "N"
    Then there should be a StoneBlock at 10,11
    And the player's Stone inventory should decrease by 1

  Scenario: Cannot build on water tile
    And the player starts at 12,5
    And a water Tile exists at 13,5
    And the player is facing "EAST"
    When the player presses "B"
    Then no block should be placed at 13,5
    And the player's Wood inventory should decrease by 0

  Scenario: Break a wood block with sword, no farming
    Given a WoodBlock exists at 11,10
    And the player is facing "EAST"
    When the player presses "S" 1 times
    And I advance the game by 0 seconds
    Then no block should exist at 11,10
    And the player's inventory should contain 5 Wood

  Scenario: Break a stone block with sword, no farming
    Given a StoneBlock exists at 11,10
    And the player is facing "EAST"
    When the player presses "S" 2 times
    And I advance the game by 0 seconds
    Then no block should exist at 11,10
    And the player's inventory should contain 5 Stone

  Scenario: Open and close the inventory panel
    # by default, inventory should start closed
    Given the inventory is closed
    When the player presses "E"
    Then the inventory is open
    When the player presses "E"
    Then the inventory is closed