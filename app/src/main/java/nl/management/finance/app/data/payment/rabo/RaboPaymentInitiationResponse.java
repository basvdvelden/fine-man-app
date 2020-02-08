package nl.management.finance.app.data.payment.rabo;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

public class RaboPaymentInitiationResponse {
    private RaboPaymentInitiationLinks _links;
    private String paymentId;
    private String psuMessage;
    private List<RaboTPPMessage> tppMessages;
    private String transactionStatus;

    public RaboPaymentInitiationLinks get_links() {
        return _links;
    }

    public void set_links(RaboPaymentInitiationLinks _links) {
        this._links = _links;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPsuMessage() {
        return psuMessage;
    }

    public void setPsuMessage(String psuMessage) {
        this.psuMessage = psuMessage;
    }

    public List<RaboTPPMessage> getTppMessages() {
        return tppMessages;
    }

    public void setTppMessages(List<RaboTPPMessage> tppMessages) {
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
        RaboTPPMessage[] arr;
        if (tppMessages == null) {
            arr = new RaboTPPMessage[]{};
        } else {
            arr = (RaboTPPMessage[]) tppMessages.toArray();
        }
        return String.format("[_links=%s, paymentId=%s, psuMessage=%s, tppMessages=%s, transactionStatus=%s]",
                _links.toString(), paymentId, psuMessage, Arrays.toString(arr), transactionStatus);
    }
}
