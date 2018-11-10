package bg.uni.sofia.fmi.mjt.dungeon.actor;

import bg.uni.sofia.fmi.mjt.dungeon.treasure.Spell;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Weapon;

public class Hero implements Actor {
    private String name;
    private int health;
    private int startingHealth;
    private int mana;
    private int startingMana;
    private boolean isAlive;
    private Weapon weapon;
    private Spell spell;
    private Position position;

    public Hero(String name, int health, int mana) {
        this.name = name;
        this.health = health;
        this.mana = mana;
        startingHealth = health;
        startingMana = mana;
        position = new Position(0, 0);
        isAlive = true;
    }

    public Hero(Hero hero) {
        this.name = hero.name;
        this.health = hero.health;
        this.startingHealth = hero.startingHealth;
        this.mana = hero.mana;
        this.startingMana = hero.startingMana;
        this.isAlive = hero.isAlive;
        this.weapon = hero.weapon;
        this.spell = hero.spell;
        this.position = new Position(hero.position);
    }

    public void takeHealing(int healingPoints) {
        if (isAlive) {
            health += healingPoints;
            if (health > startingHealth) {
                health = startingHealth;
            }
        }
    }

    public void takeMana(int manaPoints) {
        if (isAlive) {
            mana += manaPoints;
            if (mana > startingMana) {
                mana = startingMana;
            }
        }
    }

    public void equip(Weapon weapon) {
        if (this.weapon == null) {
            this.weapon = weapon;
        } else if (this.weapon.getDamage() <= weapon.getDamage()) {
            this.weapon = weapon;
        }
    }

    public void learn(Spell spell) {
        if (this.spell == null) {
            this.spell = spell;
        } else if (this.spell.getDamage() <= spell.getDamage()) {
            this.spell = spell;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getMana() {
        return mana;
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public Spell getSpell() {
        return spell;
    }

    @Override
    public void takeDamage(int damagePoints) {
        health -= damagePoints;
        if (health <= 0) {
            isAlive = false;
            if (health < 0) {
                health = 0;
            }
        }
    }

    @Override
    public int attack() {
        if (spell == null && weapon == null) {
            return 0;
        } else if (weapon == null) {
           if (mana >= spell.getManaCost()) {
               mana -= spell.getManaCost();
               return spell.getDamage();
           } else {
               return 0;
           }

        } else if (spell == null) {
            return weapon.getDamage();

        } else if (weapon.getDamage() <= spell.getDamage() && mana >= spell.getManaCost()) {
            mana -= spell.getManaCost();
            return spell.getDamage();

        } else {
            return weapon.getDamage();
        }
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        this.position.setX(x);
        this.position.setY(y);
    }
}
