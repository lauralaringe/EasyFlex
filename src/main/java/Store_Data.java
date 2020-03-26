import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PublicKey;
import com.hedera.hashgraph.sdk.file.FileCreateTransaction;
import com.hedera.hashgraph.sdk.file.FileId;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Store_Data {
    AccountId OPERATOR_ID;
    Ed25519PrivateKey OPERATOR_KEY;
    Client client;
    FileId fileId;

    public void store_data( AccountId OPERATOR_ID, Ed25519PrivateKey OPERATOR_KEY, Client client) throws IOException, HederaStatusException {
        // Defaults the operator account ID and key such that all generated transactions will be paid for
        // by this account and be signed by this key
        client.setOperator(OPERATOR_ID,OPERATOR_KEY); //now this is the operator

        //Import the measurement from .csv
        String fileName = "mes.csv";
        ImportMeas myMeas = new ImportMeas();
        myMeas.setData(fileName);

        // write to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        for (String element : myMeas.getData()) {
            out.writeUTF(element);
        }

        // write the byte of the measurement to the file's content
        byte[] fileContents = baos.toByteArray();

        //File service
        TransactionId txId = new FileCreateTransaction()
                // Use the same key as the operator to "own" this file
                .addKey(OPERATOR_KEY.publicKey)
                .setContents(fileContents)
                // The default max fee of 1 HBAR is not enough to make a file ( starts around 1.1 HBAR )
                .setMaxTransactionFee(1_000_000_000) // 10 HBAR
                .setTransactionMemo("Measurement file transaction")
                .execute(client);

        TransactionReceipt receipt = txId.getReceipt(client);

        FileId newFileId = receipt.getFileId();
        fileId = newFileId;}

        public FileId get_fileId(){
            return fileId;
        }






    }




