import java.util.ArrayList;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import com.hedera.hashgraph.sdk.file.FileContentsQuery;
import com.hedera.hashgraph.sdk.file.FileCreateTransaction;
import com.hedera.hashgraph.sdk.file.FileId;
import com.opencsv.CSVReader;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class Read_CSV {
    private static final AccountId OPERATOR_ID = AccountId.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_ID")));
    private static final Ed25519PrivateKey OPERATOR_KEY = Ed25519PrivateKey.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_KEY")));


    public static void main(String[] args) {

            //set the excel file
        String csvFile = "mes.csv";
        // `Client.forMainnet()` is provided for connecting to Hedera mainnet
        Client client = Client.forTestnet();

        // Defaults the operator account ID and key such that all generated transactions will be paid for
        // by this account and be signed by this key
        client.setOperator(OPERATOR_ID, OPERATOR_KEY);

        CSVReader reader = null;
        try {
            //read the file
            reader = new CSVReader(new FileReader(csvFile));
            String[] line;


            String fileContents= "";
            //Read line by line
            while ((line = reader.readNext()) != null) {
                Data battery= new Data(line[0], line[1]);
                fileContents = fileContents +  battery.timestamp  + ", " + battery.power + "\n";

            }
            //convert the file content in bytes
            byte[] fileContents_Bytes = fileContents.getBytes();

            // Create the new file and set its properties
            TransactionId newFileTxId = new FileCreateTransaction()
                    .addKey(OPERATOR_KEY.publicKey) // The public key of the owner of the file
                    .setContents(fileContents) // Contents of the file
                    .setMaxTransactionFee(new Hbar(2))
                    .execute(client);
            FileId newFileId = newFileTxId.getReceipt(client).getFileId();

            // Get file contents
            byte[] contents = new FileContentsQuery()
                    .setFileId(newFileId)
                    .execute(client);

            // Prints query results to console
            System.out.println("File content query results: " + new String(contents));


        } catch (IOException | HederaStatusException e) {
            e.printStackTrace();}



    }

}

