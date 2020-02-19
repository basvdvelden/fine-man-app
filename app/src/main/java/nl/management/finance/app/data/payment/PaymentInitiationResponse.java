package nl.management.finance.app.data.payment;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import nl.management.finance.app.data.common.TPPMessage;

public class PaymentInitiationResponse {
    private PaymentInitiationLinks _links;
    private String paymentId;
    private String psuMessage;
    private List<TPPMessage> tppMessages;
    private String transactionStatus;

    public PaymentInitiationLinks get_links() {
        return _links;
    }

    public void set_links(PaymentInitiationLinks _links) {
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
        TPPMessage[] arr;
        if (tppMessages == null) {
            arr = new TPPMessage[]{};
        } else {
            arr = (TPPMessage[]) tppMessages.toArray();
        }
        return String.format("[_links=%s, paymentId=%s, psuMessage=%s, tppMessages=%s, transactionStatus=%s]",
                _links.toString(), paymentId, psuMessage, Arrays.toString(arr), transactionStatus);
    }
}
