package Entities;

import javax.persistence.*;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="PCAuthorizedCDKeys")
public class PCAuthorizedCDKeyEntity {

    @Id
    @Column(name = "AccountID")
    private String accountID;
    @Column(name = "IsAddingKey")
    private boolean isAddingKey;
    @Column(name = "CDKey1")
    private String cdKey1;
    @Column(name = "CDKey2")
    private String cdKey2;
    @Column(name = "CDKey3")
    private String cdKey3;
    @Column(name = "CDKey4")
    private String cdKey4;
    @Column(name = "CDKey5")
    private String cdKey5;
    @Column(name = "CDKey6")
    private String cdKey6;
    @Column(name = "CDKey7")
    private String cdKey7;
    @Column(name = "CDKey8")
    private String cdKey8;
    @Column(name = "CDKey9")
    private String cdKey9;
    @Column(name = "CDKey10")
    private String cdKey10;

    public PCAuthorizedCDKeyEntity()
    {
        this.accountID = "";
        this.cdKey1 = "";
        this.cdKey2 = "";
        this.cdKey3 = "";
        this.cdKey4 = "";
        this.cdKey5 = "";
        this.cdKey6 = "";
        this.cdKey7 = "";
        this.cdKey8 = "";
        this.cdKey9 = "";
        this.cdKey10 = "";

    }


    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getCdKey1() {
        return cdKey1;
    }

    public void setCdKey1(String cdKey1) {
        this.cdKey1 = cdKey1;
    }

    public String getCdKey2() {
        return cdKey2;
    }

    public void setCdKey2(String cdKey2) {
        this.cdKey2 = cdKey2;
    }

    public String getCdKey3() {
        return cdKey3;
    }

    public void setCdKey3(String cdKey3) {
        this.cdKey3 = cdKey3;
    }

    public String getCdKey4() {
        return cdKey4;
    }

    public void setCdKey4(String cdKey4) {
        this.cdKey4 = cdKey4;
    }

    public String getCdKey5() {
        return cdKey5;
    }

    public void setCdKey5(String cdKey5) {
        this.cdKey5 = cdKey5;
    }

    public String getCdKey6() {
        return cdKey6;
    }

    public void setCdKey6(String cdKey6) {
        this.cdKey6 = cdKey6;
    }

    public String getCdKey7() {
        return cdKey7;
    }

    public void setCdKey7(String cdKey7) {
        this.cdKey7 = cdKey7;
    }

    public String getCdKey8() {
        return cdKey8;
    }

    public void setCdKey8(String cdKey8) {
        this.cdKey8 = cdKey8;
    }

    public String getCdKey9() {
        return cdKey9;
    }

    public void setCdKey9(String cdKey9) {
        this.cdKey9 = cdKey9;
    }

    public String getCdKey10() {
        return cdKey10;
    }

    public void setCdKey10(String cdKey10) {
        this.cdKey10 = cdKey10;
    }

    public boolean isAddingKey() {
        return isAddingKey;
    }

    public void setIsAddingKey(boolean isAddingKey) {
        this.isAddingKey = isAddingKey;
    }
}
