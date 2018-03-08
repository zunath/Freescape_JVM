package Entities;

import javax.persistence.*;

@Entity
@Table(name = "BuildPrivacyDomain")
public class BuildPrivacyEntity {

    @Id
    @Column(name = "BuildPrivacyTypeID")
    private int buildPrivacyTypeID;

    @Column(name = "Name")
    private String name;

    public int getBuildPrivacyTypeID() {
        return buildPrivacyTypeID;
    }

    public void setBuildPrivacyTypeID(int buildPrivacyTypeID) {
        this.buildPrivacyTypeID = buildPrivacyTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
