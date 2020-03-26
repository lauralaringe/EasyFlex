import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PublicKey;

import com.hedera.hashgraph.sdk.file.FileId;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.util.Objects;

public class Main {

    private static final AccountId OPERATOR_ID = AccountId.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_ID")));
    private static final Ed25519PrivateKey OPERATOR_KEY = Ed25519PrivateKey.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_KEY")));
    private static final Ed25519PublicKey OPERATOR_PUBLIC_KEY = Ed25519PublicKey.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_PUBLIC_KEY")));


    public static void main(String[] args) throws IOException, HederaStatusException {
        //Configure a client for Hedera testnet access
        Client client = Client.forTestnet();
        // Defaults the operator account ID and key such that all generated transactions will be paid for
        // by this account and be signed by this key

        User laura= new User(OPERATOR_KEY, OPERATOR_PUBLIC_KEY, OPERATOR_ID);

        System.out.println( "USER laura ID: " + laura.accountId );
        System.out.println(" private key: " + laura.privateKey );
        System.out.println(" public key: " + laura.publicKey);



        Store_Data data_to_store= new Store_Data();
        data_to_store.store_data(laura.accountId, laura.privateKey, client);

        Get_Data data = new Get_Data();
        data.get_data(client, data_to_store.get_fileId());


        // Prints query results to console
        System.out.println("File content query results: " + data.get_contents());


    }


}
