import com.hedera.hashgraph.sdk.*;
import com.hedera.hashgraph.sdk.account.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.account.CryptoTransferTransaction;

public class Transaction {
    AccountId senderId;
    AccountId recipientId;
    Client client;
    long amount; //100_000_000 tiny Hbars equals to 1 Hbar
    String memo;

    public void sendHbar(AccountId senderId, AccountId recipientId, Client client, long amount, String memo) throws HederaStatusException {
        this.senderId=senderId;
        this.recipientId=recipientId;
        this.amount=amount;
        this.client=client;
        this.memo=memo;

        TransactionId transactionId = new CryptoTransferTransaction()
                // .addSender and .addRecipient can be called as many times as you want as long as the total sum from
                // both sides is equivalent
                .addSender(senderId, amount)
                .addRecipient(recipientId, amount)
                .setTransactionMemo(memo)
                .execute(client);

        System.out.println("transaction ID: " + transactionId);

        TransactionRecord record = transactionId.getRecord(client);

        System.out.println("transferred " + amount + "...");

        Hbar senderBalanceAfter = new AccountBalanceQuery()
                .setAccountId(senderId)
                .execute(client);

        Hbar receiptBalanceAfter = new AccountBalanceQuery()
                .setAccountId(recipientId)
                .execute(client);

        System.out.println("" + senderId + " balance = " + senderBalanceAfter);
        System.out.println("" + recipientId + " balance = " + receiptBalanceAfter);
        System.out.println("Transfer memo: " + record.transactionMemo);

    }
}
