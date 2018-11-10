package bg.uni.sofia.fmi.mjt.dungeon;

import bg.uni.sofia.fmi.mjt.dungeon.actor.Enemy;
import bg.uni.sofia.fmi.mjt.dungeon.actor.Hero;
import bg.uni.sofia.fmi.mjt.dungeon.actor.Position;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Treasure;

public class GameEngine {
    private char[][] map;
    private Hero hero;
    private Enemy[] enemies;
    private Treasure[] treasures;
    private int currTreasure;
    private int currEnemy;

    public GameEngine(char[][] map, Hero hero, Enemy[] enemies, Treasure[] treasures) {
        this.map = map;
        this.hero = new Hero(hero);
        this.enemies = enemies;
        this.treasures = treasures;
        currEnemy = 0;
        currTreasure = 0;
        Position position = findStartPosition();
        this.hero.setPosition(position.getX(), position.getY());
    }

    public char[][] getMap() {
        return map;
    }

    public Position getHeroPosition() {
        return hero.getPosition();
    }

    public String makeMove(Direction direction) {
        if (!hero.isAlive()) {
            return "Hero is dead! Game over!";
        }

        String message = null;

        Position nextPosition;
        int currX = hero.getPosition().getX();
        int currY = hero.getPosition().getY();

        switch (direction) {
            case UP:
                if (currX - 1 >= 0) {
                    nextPosition = new Position(currX - 1, currY);
                    message = move(hero.getPosition(), nextPosition);
                }
                break;

            case DOWN:
                if (currX + 1 < map.length) {
                    nextPosition = new Position(currX + 1, currY);
                    message = move(hero.getPosition(), nextPosition);
                }
                break;

            case LEFT:
                if (currY - 1 >= 0) {
                    nextPosition = new Position(currX, currY - 1);
                    message = move(hero.getPosition(), nextPosition);
                }
                break;

            case RIGHT:
                if (currY + 1 < map[0].length) {
                    nextPosition = new Position(currX, currY + 1);
                    message = move(hero.getPosition(), nextPosition);
                }
                break;
            default:
                message = "Unknown command entered.";
        }

        if (message == null) {
            message = "Wrong move. There is an obstacle and you cannot bypass it.";
        }

        return message;
    }

    public String move(Position currPosition, Position nextPosition) {
        char nextChar = map[nextPosition.getX()][nextPosition.getY()];
        String message;

        switch (nextChar) {
            case '#':
                message = "Wrong move. There is an obstacle and you cannot bypass it.";
                break;

            case '.':
                changeThePosition(currPosition, nextPosition);
                message = "You moved successfully to the next position.";
                break;

            case 'T':
                changeThePosition(currPosition, nextPosition);
                message = treasures[currTreasure++].collect(hero);
                break;

            case 'E':
                Enemy enemy = enemies[currEnemy++];
                while (hero.isAlive()) {
                    enemy.takeDamage(hero.attack());
                    if (enemy.isAlive()) {
                        hero.takeDamage(enemy.attack());
                    } else {
                        break;
                    }
                }

                if (hero.isAlive()) {
                    changeThePosition(currPosition, nextPosition);
                    message = "Enemy died.";

                } else {
                    message = "Hero is dead! Game over!";
                }
                break;
            case 'G':
                message = "You have successfully passed through the dungeon. Congrats!";
                break;
            default:
                message = "Wrong move. There is an obstacle and you cannot bypass it.";
        }

        return message;
    }

    public void changeThePosition(Position currPosition, Position nextPosition) {
        map[nextPosition.getX()][nextPosition.getY()] = 'H';
        map[currPosition.getX()][currPosition.getY()] = '.';
        hero.setPosition(nextPosition.getX(), nextPosition.getY());
    }

    public Hero getHero() {
        return hero;
    }

    private Position findStartPosition() {
        Position position = new Position(0, 0);

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 'S') {
                    position.setX(i);
                    position.setY(j);
                    return position;
                }
            }
        }
        return position;
    }
}
