package bg.uni.sofia.fmi.mjt.dungeon.test;

import bg.uni.sofia.fmi.mjt.dungeon.Direction;
import bg.uni.sofia.fmi.mjt.dungeon.GameEngine;
import bg.uni.sofia.fmi.mjt.dungeon.actor.Enemy;
import bg.uni.sofia.fmi.mjt.dungeon.actor.Hero;
import bg.uni.sofia.fmi.mjt.dungeon.actor.Position;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameEngineTest {
    private Hero hero;
    private char[][] map;
    private Enemy[] enemies;
    private Treasure[] treasures;
    private GameEngine gameEngine;
    private Weapon weapon;
    private Spell spell;
    private HealthPotion healthPotion;
    private ManaPotion manaPotion;

    @Before
    public void setup() {
        hero = new Hero("hero", 100, 100);
        map = new char[][]{ "###".toCharArray(),
                "TS.".toCharArray(),
                "#EG".toCharArray() };
        enemies = new Enemy[] {new Enemy("enemy", 100, 0, new Weapon("enemy weapon", 30), null)};
        treasures = new Treasure[] {new Weapon("strong weapon", 50)};
        gameEngine = new GameEngine(map, hero, enemies, treasures);
    }

    @Test
    public void testMoveToEmptyBlock() {
        String moveMessage = gameEngine.makeMove(Direction.RIGHT);

        assertEquals("You moved successfully to the next position.", moveMessage);
        assertEquals('.', gameEngine.getMap()[1][1]);
        assertEquals('H', gameEngine.getMap()[1][2]);
    }

    @Test
    public void testMoveToObstacle() {
        gameEngine.makeMove(Direction.RIGHT);
        String moveMessage = gameEngine.makeMove(Direction.UP);

        assertEquals("Wrong move. There is an obstacle and you cannot bypass it.", moveMessage);
        assertEquals('H', gameEngine.getMap()[1][2]);
    }

    @Test
    public void testMoveToTreasure() {
        String moveMessage = gameEngine.makeMove(Direction.LEFT);

        assertEquals("Weapon found! Damage points: 50", moveMessage);
        assertEquals('.', gameEngine.getMap()[1][1]);
        assertEquals('H', gameEngine.getMap()[1][0]);

        assertEquals("strong weapon", gameEngine.getHero().getWeapon().getName());
        assertEquals(50, gameEngine.getHero().getWeapon().getDamage());
    }

    @Test
    public void testFindPosition() {
        assertEquals(1, gameEngine.getHero().getPosition().getX());
        assertEquals(1, gameEngine.getHero().getPosition().getY());
    }

    @Test
    public void testHeroDead() {
        gameEngine.getHero().takeDamage(80);
        String message = gameEngine.makeMove(Direction.DOWN);
        assertEquals("Hero is dead! Game over!", message);
    }

    @Test
    public void testSuccesfullyPassed() {
        String message = gameEngine.makeMove(Direction.RIGHT);
        assertEquals("You moved successfully to the next position.", message);

        message = gameEngine.makeMove(Direction.DOWN);
        assertEquals("You have successfully passed through the dungeon. Congrats!", message);
    }

    @Test
    public void testWinFightWithEnemy() {
        String moveMessage = gameEngine.makeMove(Direction.LEFT);
        assertEquals("Weapon found! Damage points: " + 50, moveMessage);

        moveMessage = gameEngine.makeMove(Direction.RIGHT);
        assertEquals("You moved successfully to the next position.", moveMessage);

        moveMessage = gameEngine.makeMove(Direction.DOWN);
        assertEquals("Enemy died.", moveMessage);

    }

    @Test
    public void testTakeHealingTakeManaTakeDamage() {
        assertEquals(100, gameEngine.getHero().getHealth());
        gameEngine.getHero().takeDamage(70);

        assertEquals(30, gameEngine.getHero().getHealth());
        gameEngine.getHero().takeHealing(50);

        assertEquals(80, gameEngine.getHero().getHealth());

        assertEquals(100, gameEngine.getHero().getMana());
        gameEngine.getHero().takeMana(60);
        assertEquals(100, gameEngine.getHero().getMana());
    }

    @Test
    public void testEquipLearn() {
        weapon = new Weapon("strong weapon", 60);
        spell = new Spell("strong spell", 65, 30);

        gameEngine.getHero().equip(weapon);
        gameEngine.getHero().learn(spell);
        assertEquals(weapon, gameEngine.getHero().getWeapon());
        assertEquals(spell, gameEngine.getHero().getSpell());
    }

    @Test
    public void testHealtPotionManaPoition() {
        healthPotion = new HealthPotion(35);
        manaPotion = new ManaPotion(60);

        String message = healthPotion.collect(hero);
        assertEquals(message, "Health potion found! " + 35 + " health points added to your hero!");

        message = manaPotion.collect(hero);
        assertEquals(message, "Mana potion found! " + 60 +" mana points added to your hero!");

        assertEquals(35, healthPotion.heal());
        assertEquals(60, manaPotion.heal());
    }
}