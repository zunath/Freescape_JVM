package Entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ForcedSPResetDates")
public class ForcedSPResetEntity {

    @Id
    @Column(name = "ForcedSPResetDateID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int forcedSPResetDateID;

    @Column(name = "DateOfReset")
    private Date dateOfReset;

    public int getForcedSPResetDateID() {
        return forcedSPResetDateID;
    }

    public void setForcedSPResetDateID(int forcedSPResetDateID) {
        this.forcedSPResetDateID = forcedSPResetDateID;
    }

    public Date getDateOfReset() {
        return dateOfReset;
    }

    public void setDateOfReset(Date dateOfReset) {
        this.dateOfReset = dateOfReset;
    }
}
