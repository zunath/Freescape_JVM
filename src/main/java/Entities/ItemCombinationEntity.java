package Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="ItemCombinations")
public class ItemCombinationEntity {

    @Id
    @Column(name="ItemCombinationID")
    private int itemCombinationID;
    @Column(name="ItemA")
    private String itemA;
    @Column(name="ItemB")
    private String itemB;
    @Column(name="ItemAInfinite")
    private boolean itemAInfinite;
    @Column(name="ItemBInfinite")
    private boolean itemBInfinite;
    @Column(name="Output")
    private String output;
    @Column(name="Quantity")
    private int quantity;
    @Column(name="HQOutput")
    private String hqOutput;
    @Column(name="HQQuantity")
    private int hqQuantity;
    @Column(name="MixingRequired")
    private int mixingRequired;
    @Column(name="IgnoreOrder")
    private boolean ignoreOrder;

    public int getItemCombinationID() {
        return itemCombinationID;
    }

    public void setItemCombinationID(int itemCombinationID) {
        this.itemCombinationID = itemCombinationID;
    }

    public String getItemA() {
        return itemA;
    }

    public void setItemA(String itemA) {
        this.itemA = itemA;
    }

    public String getItemB() {
        return itemB;
    }

    public void setItemB(String itemB) {
        this.itemB = itemB;
    }

    public boolean isItemAInfinite() {
        return itemAInfinite;
    }

    public void setItemAInfinite(boolean itemAInfinite) {
        this.itemAInfinite = itemAInfinite;
    }

    public boolean isItemBInfinite() {
        return itemBInfinite;
    }

    public void setItemBInfinite(boolean itemBInfinite) {
        this.itemBInfinite = itemBInfinite;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getHqOutput() {
        return hqOutput;
    }

    public void setHqOutput(String hqOutput) {
        this.hqOutput = hqOutput;
    }

    public int getHqQuantity() {
        return hqQuantity;
    }

    public void setHqQuantity(int hqQuantity) {
        this.hqQuantity = hqQuantity;
    }

    public int getMixingRequired() {
        return mixingRequired;
    }

    public void setMixingRequired(int mixingRequired) {
        this.mixingRequired = mixingRequired;
    }

    public boolean isIgnoreOrder() {
        return ignoreOrder;
    }

    public void setIgnoreOrder(boolean ignoreOrder) {
        this.ignoreOrder = ignoreOrder;
    }
}
