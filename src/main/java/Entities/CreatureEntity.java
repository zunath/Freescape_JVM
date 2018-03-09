package Entities;

import javax.persistence.*;

@Entity
@Table(name = "CraftLevels")
public class CreatureEntity {

    @Id
    @Column(name = "CreatureID")
    private int creatureID;

    @Column(name = "DifficultyRating")
    private int difficultyRating;

    public int getCreatureID() {
        return creatureID;
    }

    public void setCreatureID(int creatureID) {
        this.creatureID = creatureID;
    }

    public int getDifficultyRating() {
        return difficultyRating;
    }

    public void setDifficultyRating(int difficultyRating) {
        this.difficultyRating = difficultyRating;
    }
}
