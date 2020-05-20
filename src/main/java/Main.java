import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PublicKey;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.util.Objects;

public class Main {

    private static final AccountId OPERATOR_ID = AccountId.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_ID")));
    private static final Ed25519PrivateKey OPERATOR_KEY = Ed25519PrivateKey.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_KEY")));
    private static final Ed25519PublicKey OPERATOR_PUBLIC_KEY = Ed25519PublicKey.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_PUBLIC_KEY")));

    private static final AccountId OPERATOR_ID_EMILIA = AccountId.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_ID_EMILIA")));
    private static final Ed25519PrivateKey OPERATOR_KEY_EMILIA = Ed25519PrivateKey.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_KEY_EMILIA")));
    private static final Ed25519PublicKey OPERATOR_PUBLIC_KEY_EMILIA = Ed25519PublicKey.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_PUBLIC_KEY_EMILIA")));



    public static void main(String[] args) throws IOException, HederaStatusException {
        // Configure a client for Hedera testnet access
        Client client = Client.forTestnet();
        // Defaults the operator account ID and key such that all generated transactions will be paid for
        // by this account and be signed by this key

        User laura= new User(OPERATOR_KEY, OPERATOR_PUBLIC_KEY, OPERATOR_ID);
        User emilia = new User(OPERATOR_KEY_EMILIA, OPERATOR_PUBLIC_KEY_EMILIA, OPERATOR_ID_EMILIA);

        System.out.println( "USER laura ID: " + laura.accountId );
        System.out.println(" private key: " + laura.privateKey );
        System.out.println(" public key: " + laura.publicKey);

        // Emilia is the sender, Laura is DSO
        // 1. Emilia sends the transactions with messages and topic to the testnet/mainnet nodes
        // 2. Blockchain network puts messages in consensus order by topic
        // 3. Mirror node receive all infro from the testnet/mainnet


        // PUT CODE HERE


//        // Get the data from API api.testnet.kabuto.sh
//        MirrorNodeData myData = new MirrorNodeData();
//        myData.setOperator(Objects.requireNonNull(Dotenv.load().get("OPERATOR_ID")));
//        myData.importTransaction();

        // Mirror node sends messages for the topic for our application
        // The application sends the message to Laura to see if the the resource is giving the energy committed

        




    }


}
