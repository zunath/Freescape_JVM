package Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Portraits")
public class PortraitEntity {

    @Id
    @Column(name="PortraitID")
    private int portraitID;

    @Column(name = "Resref")
    private String resref;

    @Column(name = "2DAID")
    private int _2daID;


    public int getPortraitID() {
        return portraitID;
    }

    public void setPortraitID(int portraitID) {
        this.portraitID = portraitID;
    }

    public String getResref() {
        return resref;
    }

    public void setResref(String resref) {
        this.resref = resref;
    }

    public int get2DAID() {
        return _2daID;
    }

    public void set2DAID(int _2DAID) {
        this._2daID = _2DAID;
    }
}
