package Conversation.ViewModels;

public class ItemViewModel {
    private int version;
    private String resref;
    private String tag;
    private String nameFormat;
    private boolean replace;
    private int quantity;

    public ItemViewModel() {
        this.setNameFormat("");
        this.setQuantity(0);
        this.setReplace(false);
        this.setResref("");
        this.setVersion(0);
        this.setTag("");
    }


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getResref() {
        return resref;
    }

    public void setResref(String resref) {
        this.resref = resref;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getNameFormat() {
        return nameFormat;
    }

    public void setNameFormat(String nameFormat) {
        this.nameFormat = nameFormat;
    }

    public boolean isReplace() {
        return replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
