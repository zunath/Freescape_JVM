package Entities;

import javax.persistence.*;

@Entity
@Table(name = "CraftLevels")
public class CreatureEntity {

    @Id
    @Column(name = "CreatureID")
    private int creatureID;

    @Column(name = "DifficultyRating")
    private float difficultyRating;

    @Column(name = "XPModifier")
    private float xpModifier;

    public int getCreatureID() {
        return creatureID;
    }

    public void setCreatureID(int creatureID) {
        this.creatureID = creatureID;
    }

    public float getDifficultyRating() {
        return difficultyRating;
    }

    public void setDifficultyRating(float difficultyRating) {
        this.difficultyRating = difficultyRating;
    }

    public float getXPModifier() {
        return xpModifier;
    }

    public void setXPModifier(float xpModifier) {
        this.xpModifier = xpModifier;
    }
}
