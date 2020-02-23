package Examples;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.TransactionRecord;
import com.hedera.hashgraph.sdk.account.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.account.CryptoTransferTransaction;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;

import java.util.Objects;

import io.github.cdimascio.dotenv.Dotenv;

    public final class TransferCrypto {

        // see `.env.sample` in the repository root for how to specify these values
        // or set environment variables with the same names
        private static final AccountId OPERATOR_ID = AccountId.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_ID")));
        private static final Ed25519PrivateKey OPERATOR_KEY = Ed25519PrivateKey.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_KEY")));

        private TransferCrypto() { }

        public static void main(String[] args) throws HederaStatusException {
            // `Client.forMainnet()` is provided for connecting to Hedera mainnet
            Client client = Client.forTestnet();

            // Defaults the operator account ID and key such that all generated transactions will be paid for
            // by this account and be signed by this key
            client.setOperator(OPERATOR_ID, OPERATOR_KEY);

            //recipientId is a AccountId type and it takes the Id from the string
            //In this case the Id of the recipient is 0.0.3
            AccountId recipientId = AccountId.fromString("0.0.3");

            //hbar is the currency. 1 hbar=100_000_000 bars
            Hbar amount = Hbar.fromTinybar(10_000);

            // in Hbar class
            Hbar senderBalanceBefore = new AccountBalanceQuery()
                    .setAccountId(OPERATOR_ID)
                    .execute(client);

            Hbar receiptBalanceBefore = new AccountBalanceQuery()
                    .setAccountId(recipientId)
                    .execute(client);

            System.out.println("" + OPERATOR_ID + " balance = " + senderBalanceBefore);
            System.out.println("" + recipientId + " balance = " + receiptBalanceBefore);

            TransactionId transactionId = new CryptoTransferTransaction()
                    // .addSender and .addRecipient can be called as many times as you want as long as the total sum from
                    // both sides is equivalent
                    .addSender(OPERATOR_ID, amount)
                    .addRecipient(recipientId, amount)
                    .setTransactionMemo("transfer test")
                    .execute(client);

            System.out.println("transaction ID: " + transactionId);

            TransactionRecord record = transactionId.getRecord(client);

            System.out.println("transferred " + amount + "...");

            Hbar senderBalanceAfter = new AccountBalanceQuery()
                    .setAccountId(OPERATOR_ID)
                    .execute(client);

            Hbar receiptBalanceAfter = new AccountBalanceQuery()
                    .setAccountId(recipientId)
                    .execute(client);

            System.out.println("" + OPERATOR_ID + " balance = " + senderBalanceAfter);
            System.out.println("" + recipientId + " balance = " + receiptBalanceAfter);
            System.out.println("Transfer memo: " + record.transactionMemo);
        }
    }

