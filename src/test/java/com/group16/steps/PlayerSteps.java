package com.group16.steps;

import com.group16.controller.GameLogic;
import com.group16.model.area.Island;
import com.group16.model.area.IslandMapGenerator;
import com.group16.model.area.Orientation;
import com.group16.model.area.tiles.TileType;
import com.group16.model.entity.GameEntity;
import com.group16.model.entity.actors.Actors;
import com.group16.model.entity.actors.Mobs;
import com.group16.model.entity.actors.Player;
import com.group16.model.entity.elements.Rock;
import com.group16.model.entity.elements.StoneBlock;
import com.group16.model.entity.elements.Tree;
import com.group16.model.entity.elements.WoodBlock;
import com.group16.model.items.resources.ResourceType;
import com.group16.model.items.resources.Resources;
import com.group16.model.items.weapons.Bullet;
import com.group16.model.utils.maths.Vector;
import io.cucumber.java.en.*;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerSteps {
    private GameLogic model;
    private Player player;
    private int initialWood;
    private int initialStone;
    private Island island;
    private Mobs mob;
    private int initialAmmo;
    private int initialMobHealth;
    private int initialPlayerHP;
    private Vector initialMobPos;

    @Given("a new game")
    public void a_new_game() throws Exception {

        island = new Island(new IslandMapGenerator("src/main/resources/MapDesign.txt").getMap());
        player = new Player(island, new Vector(10, 10));
        initialPlayerHP = player.getHealth();
        model = new GameLogic(island, player);
        island.addPlayer(player);


        initialWood = player.getInventory().getAmount(new Resources(ResourceType.WOOD, 5));
        initialStone = player.getInventory().getAmount(new Resources(ResourceType.STONE, 5));
        initialAmmo = player.getInventory().getAmount(new Resources(ResourceType.AMMO, 10));

        resetMoveTimers();
    }

    // we use reflection to set the player's position
    @Given("the player starts at {int},{int}")
    public void the_player_starts_at(Integer x, Integer y) {
        Vector newPos = new Vector(x, y);
        try {
            // 1) Get the Field object for "position" on GameEntity
            Field posField = GameEntity.class.getDeclaredField("position");
            // 2) Override Java access checks
            posField.setAccessible(true);
            // 3) Set the new Vector on our player instance
            posField.set(player, newPos);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Could not set player position via reflection", e);
        }
        // sanity check
        assertEquals(x.intValue(), (int)player.getPosition().x());
        assertEquals(y.intValue(), (int)player.getPosition().y());
    }

    private void resetMoveTimers() {
        try {
            Field lastMoveField = Actors.class.getDeclaredField("lastMoveTime");
            lastMoveField.setAccessible(true);
            lastMoveField.setFloat(player, -1f);

            Field movePressField = Player.class.getDeclaredField("movePressTime");
            movePressField.setAccessible(true);
            movePressField.setFloat(player, Float.MAX_VALUE);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Could not reset move timers via reflection", e);
        }
    }

    @When("the player presses {string}")
    public void the_player_presses(String keyName) {
        resetMoveTimers();
        int code;
        switch(keyName.toUpperCase()) {
            case "UP"->    code = KeyEvent.VK_UP;
            case "DOWN"->  code = KeyEvent.VK_DOWN;
            case "LEFT"->  code = KeyEvent.VK_LEFT;
            case "RIGHT"-> code = KeyEvent.VK_RIGHT;
            case "S"-> code = KeyEvent.VK_S;
            case "G"-> code = KeyEvent.VK_G;
            case "E"-> code = KeyEvent.VK_E;
            case "B"-> code = KeyEvent.VK_B;
            case "N"-> code = KeyEvent.VK_N;
            default-> throw new IllegalArgumentException("Unknown key");
        }
        // Simulate key press
        model.onKeyPressed(new java.awt.event.KeyEvent(
                new java.awt.Button(), KeyEvent.KEY_PRESSED, 0, 0, code, KeyEvent.CHAR_UNDEFINED));
    }

    @When("the player presses {string} {int} times")
    public void the_player_presses(String keyName, Integer times) {
        for (int i = 0; i < times; i++) {
            the_player_presses(keyName);
        }
    }

    @Then("the player's position should be {int},{int}")
    public void the_player_s_position_should_be(Integer expX, Integer expY) {
        int actualX = (int) player.getPosition().x();
        int actualY = (int) player.getPosition().y();
        assertEquals(expX.intValue(), actualX);
        assertEquals(expY.intValue(), actualY);
    }

    private void setOrientation(Actors actor, Orientation orientation) {
        try {
            Field orientationField = Actors.class.getDeclaredField("orientation");
            orientationField.setAccessible(true);
            orientationField.set(actor, orientation);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Could not set player orientation via reflection", e);
        }
    }

    @And("the player is facing {string}")
    public void thePlayerIsFacing(String orientation) {

        switch(orientation.toUpperCase()) {
            case "NORTH" -> setOrientation(player, Orientation.NORTH);
            case "SOUTH"-> setOrientation(player, Orientation.SOUTH);
            case "WEST"-> setOrientation(player, Orientation.WEST);
            case "EAST"-> setOrientation(player, Orientation.EAST);
            default -> throw new IllegalArgumentException("Unknown Orientation");
        }

    }

    @Given("a Tree exists at {int},{int}")
    public void a_tree_exists_at(int x, int y) {
        Vector pos = new Vector(x, y);
        island.addEntity(new Tree(pos));
        i_advance_the_game_by_seconds(0.1f);
    }

    @Given("a Rock exists at {int},{int}")
    public void a_rock_exists_at(int x, int y) {
        Vector pos = new Vector(x, y);
        island.addEntity(new Rock(pos));
        i_advance_the_game_by_seconds(0.1f);
    }

    @Given("a water Tile exists at {int},{int}")
    public void a_water_tile_exists_at(int x, int y) {
        assertSame(TileType.WATER, island.getTile(new Vector(x, y)).getType());
    }

    @Given("the player has at least {int} Wood in inventory")
    public void ensure_wood_in_inventory(int amt) {
        player.getInventory().add(new Resources(ResourceType.WOOD, amt));
        initialWood += amt;
    }

    @Given("the player has at least {int} Stone in inventory")
    public void ensure_stone_in_inventory(int amt) {
        player.getInventory().add(new Resources(ResourceType.STONE, amt));
        initialStone += amt;
    }

    @Then("the Tree at {int},{int} should no longer exist")
    public void tree_should_be_removed(int x, int y) {
        boolean hasTree = island.getTile(new Vector(x, y)).getEntity() instanceof Tree;

        assertFalse(hasTree);
    }

    @Then("the Rock at {int},{int} should no longer exist")
    public void rock_should_be_removed(int x, int y) {
        boolean hasRock = island.getTile(new Vector(x, y)).getEntity() instanceof Rock;

        assertFalse(hasRock);
    }

    @Then("the player's inventory should contain {int} Wood")
    public void inventory_should_contain_wood(int expected) {
        int actual = player.getInventory().getAmount(new Resources(ResourceType.WOOD, 0));
        assertEquals(expected, actual);
    }

    @Then("the player's inventory should contain {int} Stone")
    public void inventory_should_contain_stone(int expected) {
        int actual = player.getInventory().getAmount(new Resources(ResourceType.STONE, 0));
        assertEquals(expected, actual);
    }

    @Then("there should be a WoodBlock at {int},{int}")
    public void woodblock_should_exist(int x, int y) {
        boolean isWood = island.getTile(new Vector(x, y)).getEntity() instanceof WoodBlock;

        assertTrue(isWood);
    }

    @Then("there should be a StoneBlock at {int},{int}")
    public void stoneblock_should_exist(int x, int y) {
        boolean isStone = island.getTile(new Vector(x, y)).getEntity() instanceof StoneBlock;

        assertTrue(isStone);
    }

    @Then("the player's Wood inventory should decrease by {int}")
    public void wood_inventory_decreased(int delta) {
        assert(initialWood - delta == player.getInventory().getAmount(new Resources(ResourceType.WOOD, 0)));

    }

    @Then("the player's Stone inventory should decrease by {int}")
    public void stone_inventory_decreased(int delta) {
        assert(initialStone - delta == player.getInventory().getAmount(new Resources(ResourceType.STONE, 0)));
    }

    @Then("no block should be placed at {int},{int}")
    public void no_block_at(int x, int y) {
        boolean anyBlock = island.getTile(new Vector(x, y)).getEntity() instanceof StoneBlock ||
                island.getTile(new Vector(x, y)).getEntity() instanceof WoodBlock;
        assertFalse(anyBlock);
    }

    @And("a Tree still exists at {int},{int}")
    public void aTreeStillExistsAt(int x, int y) {
        boolean anyTree = island.getTile(new Vector(x,y)).getEntity() instanceof Tree;
        assertTrue(anyTree);
    }

    @And("I advance the game by {float} seconds")
    public void i_advance_the_game_by_seconds(float seconds) {
        float nanoseconds = seconds * com.group16.controller.config.GameConfig.ONE_SECOND_NS;
        model.update(nanoseconds);
        resetMoveTimers();
    }

    @Given("a WoodBlock exists at {int},{int}")
    public void woodblock_exists(int x, int y) {
        // create and inject a WoodBlock into the island
        var pos = new Vector(x, y);
        var block = new WoodBlock(pos);
        island.addEntity(block);
        i_advance_the_game_by_seconds(0.1f);
        // sanity
        var tile = island.getTile(pos);
        assertInstanceOf(WoodBlock.class, tile.getEntity());
    }

    @Given("a StoneBlock exists at {int},{int}")
    public void stoneblock_exists(int x, int y) {
        var pos = new Vector(x, y);
        var block = new StoneBlock(pos);
        island.addEntity(block);
        i_advance_the_game_by_seconds(0.1f);
        var tile = island.getTile(pos);
        assertInstanceOf(StoneBlock.class, tile.getEntity());
    }


    @Then("no block should exist at {int},{int}")
    public void no_block_should_exist(int x, int y) {
        var entity = island.getTile(new com.group16.model.utils.maths.Vector(x, y)).getEntity();
        assertNull(entity);
    }

    @Given("the inventory is closed")
    public void inventory_is_closed() {
        assertFalse(player.getInventory().isOpen());
    }

    @Then("the inventory is open")
    public void inventory_is_open() {
        assertTrue(player.getInventory().isOpen());
    }


    @Given("the player has at least {int} Ammo in inventory")
    public void ensure_ammo_in_inventory(int amt) {
        player.getInventory().add(new Resources(ResourceType.AMMO, amt));
        initialAmmo += amt;
    }

    @Given("a Mob exists at {int},{int}")
    public void a_mob_exists_at(int x, int y) {
        mob = new Mobs(island, new Vector(x, y), player);
        island.addEntity(mob);
        initialMobHealth = mob.getHealth();
        initialMobPos = mob.getPosition();
        i_advance_the_game_by_seconds(0.1f);
    }


    @Then("there should be a Bullet at {int},{int}")
    public void bullet_exists(int x, int y) {
        var entity = island.getTile(new Vector(x, y)).getEntity();
        System.out.println(entity);
        System.out.println(island.getTile(new Vector(x+1, y)));
        assertTrue(entity instanceof Bullet, "Expected a Bullet at " + x + "," + y);
    }

    @Then("the player's Ammo inventory should decrease by {int}")
    public void ammo_inventory_should_decrease(int delta) {
        int current = player.getInventory().getAmount(new Resources(ResourceType.AMMO, 0));
        assertEquals(initialAmmo - delta, current);
    }

    @Then("the mob's health should decrease by {int}")
    public void mob_health_decrease(int delta) {
        int curr = mob.getHealth();
        assertEquals(initialMobHealth - delta, curr);
    }

    @Then("the Bullet at {int},{int} should no longer exist")
    public void bullet_should_not_exist(int x, int y) {
        GameEntity entity = island.getTile(new Vector(x, y)).getEntity();
        assertFalse(entity instanceof Bullet, "Expected no Bullet at " + x + "," + y);
    }


    @Then("the player's health should decrease by {int}")
    public void thePlayerSHealthShouldDecreaseBy(int arg0) {
        int current = player.getHealth();
        assertEquals(initialPlayerHP - arg0, current);
    }

    @Then("the mob's position should be closer to the player")
    public void theMobSPositionShouldBeCloserToThePlayer() {
        // Check if the mob's position is closer to the player
        double initialDistance = computeL2Distance(initialMobPos, player.getPosition());
        double newDistance = computeL2Distance(mob.getPosition(), player.getPosition());
        assertTrue(newDistance < initialDistance, "Mob did not move closer to the player");
    }

    private double computeL2Distance(Vector v1, Vector v2) {
        return Math.sqrt(Math.pow(v1.x() - v2.x(), 2) + Math.pow(v1.y() - v2.y(), 2));
    }

    @Given("the player's health is {int}")
    public void thePlayerSHealthIs(int arg0) {
        try {
            Field health = GameEntity.class.getDeclaredField("health");
            health.setAccessible(true);
            health.set(player, arg0);
            initialPlayerHP = arg0;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Then("the mob should not move")
    public void theMobShouldNotMove() {
        // Check if the mob's position is the same as the initial position
        double initialDistance = computeL2Distance(initialMobPos, player.getPosition());
        double newDistance = computeL2Distance(mob.getPosition(), player.getPosition());
        assertEquals(initialDistance, newDistance, 0.1, "Mob moved when it shouldn't have");
    }

    @Then("no Bullet should exist at {int},{int}")
    public void noBulletShouldExistAt(int arg0, int arg1) {
        GameEntity entity = island.getTile(new Vector(arg0, arg1)).getEntity();
        assertNull(entity, "Expected no Bullet at " + arg0 + "," + arg1);
    }

    @And("the mob is facing {string}")
    public void theMobIsFacing(String arg0) {
        switch(arg0.toUpperCase()) {
            case "NORTH" -> setOrientation(mob, Orientation.NORTH);
            case "SOUTH"-> setOrientation(mob, Orientation.SOUTH);
            case "WEST"-> setOrientation(mob, Orientation.WEST);
            case "EAST"-> setOrientation(mob, Orientation.EAST);
            default -> throw new IllegalArgumentException("Unknown Orientation");
        }

    }
}