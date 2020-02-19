package nl.management.finance.app.data.payment;

import java.util.List;

import javax.inject.Named;

import androidx.annotation.NonNull;
import nl.management.finance.app.data.common.TPPMessage;

public class PaymentStatusResponse {
    private String psuMessage;
    private String scaStatus;
    private List<TPPMessage> tppMessages;
    private String transactionStatus;

    public String getPsuMessage() {
        return psuMessage;
    }

    public void setPsuMessage(String psuMessage) {
        this.psuMessage = psuMessage;
    }

    public String getScaStatus() {
        return scaStatus;
    }

    public void setScaStatus(String scaStatus) {
        this.scaStatus = scaStatus;
    }

    public List<TPPMessage> getTppMessages() {
        return tppMessages;
    }

    public void setTppMessages(List<TPPMessage> tppMessages) {
        this.tppMessages = tppMessages;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    @Override
    @NonNull
    public String toString() {
        return String.format("[\npsuMessage=%s, \nscaStatus=%s, \ntppMessages=%s, \ntransactionStatus=%s]",
                psuMessage, scaStatus, tppMessages, transactionStatus);
    }
}
