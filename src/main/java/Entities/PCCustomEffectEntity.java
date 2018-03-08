package Entities;

import javax.persistence.*;

@Entity
@Table(name = "PCCustomEffects")
public class PCCustomEffectEntity {

    @Id
    @Column(name = "PCCustomEffectID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pcCustomEffectID;

    @Column(name = "PlayerID")
    private String playerID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CustomEffectID")
    private CustomEffectEntity customEffect;

    @Column(name = "Ticks")
    private int ticks;

    public int getPcCustomEffectID() {
        return pcCustomEffectID;
    }

    public void setPcCustomEffectID(int pcCustomEffectID) {
        this.pcCustomEffectID = pcCustomEffectID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public CustomEffectEntity getCustomEffect() {
        return customEffect;
    }

    public void setCustomEffect(CustomEffectEntity customEffect) {
        this.customEffect = customEffect;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }
}
