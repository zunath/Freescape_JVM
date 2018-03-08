package Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="DMRoleDomain")
public class DMRoleEntity {

    @Id
    @Column(name ="DMRoleDomainID")
    private int _dmRoleID;

    @Column(name="Description")
    private String _description;

    public DMRoleEntity(int dmRoleID, String name)
    {
        _dmRoleID = dmRoleID;
        _description = name;
    }

    public DMRoleEntity()
    {

    }

    public int getDMRoleID()
    {
        return _dmRoleID;
    }

    public void setDMRoleID(int roleID)
    {
        _dmRoleID = roleID;
    }

    public String getDescription()
    {
        return _description;
    }

    public void setDescription(String description)
    {
        _description = description;
    }

}
