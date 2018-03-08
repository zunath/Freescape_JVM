package Entities;

import javax.persistence.*;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="AuthorizedDMs")
public class AuthorizedDMEntity {

    @Id
    @Column(name="AuthorizedDMID")
    private int authorizedDMId;

    @Column(name="Name")
    private String name;

    @Column(name="CDKey")
    private String cdKey;

    @Column(name="DMRole")
    private int dmRole;

    @Column(name="IsActive")
    private boolean isActive;


    public AuthorizedDMEntity(int authorizedDMId,
                              String name,
                              String cdKey,
                              int dmRole,
                              boolean isActive)
    {
        this.authorizedDMId = authorizedDMId;
        this.name = name;
        this.cdKey = cdKey;
        this.dmRole = dmRole;
        this.isActive = isActive;
    }

    public AuthorizedDMEntity()
    {

    }


    public int getDMRole() {
        return this.dmRole;
    }

    public void setDMRole(int _dmRole) {
        this.dmRole = _dmRole;
    }

    public int getAuthorizedDMId() {
        return authorizedDMId;
    }

    public void setAuthorizedDMID(int _authorizedDMId) {
        this.authorizedDMId = _authorizedDMId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCDKey() {
        return this.cdKey;
    }

    public void setCDKey(String cdKey) {
        this.cdKey = cdKey;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
