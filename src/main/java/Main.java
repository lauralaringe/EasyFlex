import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PublicKey;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Objects;

public class Main {

    private static final AccountId OPERATOR_ID = AccountId.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_ID")));
    private static final Ed25519PrivateKey OPERATOR_KEY = Ed25519PrivateKey.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_KEY")));
    private static final Ed25519PublicKey OPERATOR_PUBLIC_KEY = Ed25519PublicKey.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_PUBLIC_KEY")));


    public static void main(String[] args) {

//create class of users
        //different files:
            //create account and generate keys + parameters of the user --> add to class

            //creation of TopicId + transaction
            //Account balance after transaction.
            // record data from user


        User laura= new User(OPERATOR_KEY, OPERATOR_PUBLIC_KEY, OPERATOR_ID);

        System.out.println( "USER laura ID: " + laura.accountId );
        System.out.println(" private key: " + laura.privateKey );
        System.out.println(" public key: " + laura.publicKey);


    }


}
