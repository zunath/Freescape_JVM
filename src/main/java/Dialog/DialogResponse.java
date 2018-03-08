package Dialog;

@SuppressWarnings("UnusedDeclaration")
public class DialogResponse {
    private String text;
    private boolean isActive;
    private Object customData;

    public DialogResponse(String text)
    {
        this.text = text;
        this.isActive = true;
    }

    public DialogResponse(String text, boolean isVisible)
    {
        this.text = text;
        this.isActive = isVisible;
    }

    public DialogResponse(String text, boolean isVisible, Object customData)
    {
        this.text = text;
        this.isActive = isVisible;
        this.customData = customData;
    }

    public DialogResponse(String text, Object customData)
    {
        this.text = text;
        this.isActive = true;
        this.customData = customData;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getCustomData()
    {
        return customData;
    }

    public void setCustomData(Object customData)
    {
        this.customData = customData;
    }

}
