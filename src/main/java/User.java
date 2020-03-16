import com.hedera.hashgraph.proto.AccountID;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PublicKey;



public class User<key> {
    //private key, public key, account ID
    Ed25519PrivateKey privateKey;
    Ed25519PublicKey publicKey;
    AccountId accountId;


    public User(Ed25519PrivateKey private_key, Ed25519PublicKey public_key, AccountId account_id  ) {
        privateKey= private_key;
        publicKey= public_key;
        accountId = account_id;

    }

}
