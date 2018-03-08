package Entities;

import javax.persistence.*;

@Entity
@Table(name = "ChatLog")
public class ChatLogEntity {

    @Id
    @Column(name = "ChatLogID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int chatLogID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ChatChannelID")
    private ChatChannelEntity chatChannel;

    @Column(name = "SenderPlayerID")
    private String senderPlayerID;

    @Column(name = "SenderAccountName")
    private String senderAccountName;

    @Column(name = "SenderCDKey")
    private String senderCDKey;

    @Column(name = "ReceiverPlayerID")
    private String receiverPlayerID;

    @Column(name = "ReceiverAccountName")
    private String receiverAccountName;

    @Column(name = "ReceiverCDKey")
    private String receiverCDKey;

    @Column(name = "Message")
    private String message;

    @Column(name = "SenderDMName")
    private String senderDMName;

    @Column(name = "ReceiverDMName")
    private String receiverDMName;

    public int getChatLogID() {
        return chatLogID;
    }

    public void setChatLogID(int chatLogID) {
        this.chatLogID = chatLogID;
    }

    public ChatChannelEntity getChatChannel() {
        return chatChannel;
    }

    public void setChatChannel(ChatChannelEntity chatChannel) {
        this.chatChannel = chatChannel;
    }

    public String getSenderPlayerID() {
        return senderPlayerID;
    }

    public void setSenderPlayerID(String senderPlayerID) {
        this.senderPlayerID = senderPlayerID;
    }

    public String getSenderAccountName() {
        return senderAccountName;
    }

    public void setSenderAccountName(String senderAccountName) {
        this.senderAccountName = senderAccountName;
    }

    public String getSenderCDKey() {
        return senderCDKey;
    }

    public void setSenderCDKey(String senderCDKey) {
        this.senderCDKey = senderCDKey;
    }

    public String getReceiverPlayerID() {
        return receiverPlayerID;
    }

    public void setReceiverPlayerID(String receiverPlayerID) {
        this.receiverPlayerID = receiverPlayerID;
    }

    public String getReceiverAccountName() {
        return receiverAccountName;
    }

    public void setReceiverAccountName(String receiverAccountName) {
        this.receiverAccountName = receiverAccountName;
    }

    public String getReceiverCDKey() {
        return receiverCDKey;
    }

    public void setReceiverCDKey(String receiverCDKey) {
        this.receiverCDKey = receiverCDKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderDMName() {
        return senderDMName;
    }

    public void setSenderDMName(String senderDMName) {
        this.senderDMName = senderDMName;
    }

    public String getReceiverDMName() {
        return receiverDMName;
    }

    public void setReceiverDMName(String receiverDMName) {
        this.receiverDMName = receiverDMName;
    }
}
