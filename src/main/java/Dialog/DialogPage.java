package Dialog;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
public class DialogPage {

    private String header;
    private List<DialogResponse> responses;
    private Object customData;

    public DialogPage()
    {
        responses = new ArrayList<>();
        header = "";
    }

    public DialogPage(String header, String... responseOptions)
    {
        responses = new ArrayList<>();
        this.header = header;

        for(String response : responseOptions)
        {
            this.responses.add(new DialogResponse(response));
        }
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<DialogResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<DialogResponse> responses) {
        this.responses = responses;
    }

    public int getNumberOfResponses()
    {
        return responses.size();
    }

    public Object getCustomData() {
        return customData;
    }

    public void setCustomData(Object customData) {
        this.customData = customData;
    }

    public DialogResponse getDialogResponseByID(int responseID)
    {
        return responses.get(responseID - 1);
    }

    public void addResponse(String text, boolean isVisible)
    {
        responses.add(new DialogResponse(text, isVisible));
    }

    public void addResponse(String text, boolean isVisible, Object customData)
    {
        responses.add(new DialogResponse(text, isVisible, customData));
    }


}
