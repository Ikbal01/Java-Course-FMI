package bg.uni.sofia.fmi.mjt.dungeon.actor;

import bg.uni.sofia.fmi.mjt.dungeon.actor.Actor;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Spell;
import bg.uni.sofia.fmi.mjt.dungeon.treasure.Weapon;

public class Enemy implements Actor {
    private String name;
    private int health;
    private int mana;
    private Weapon weapon;
    private Spell spell;
    private boolean isAlive;

    public Enemy(String name, int health, int mana, Weapon weapon, Spell spell) {
        this.name = name;
        this.health = health;
        this.mana = mana;
        this.weapon = weapon;
        this.spell = spell;
        isAlive = true;
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
}
