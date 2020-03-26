import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PublicKey;
import com.hedera.hashgraph.sdk.file.FileCreateTransaction;
import com.hedera.hashgraph.sdk.file.FileId;
import com.hedera.hashgraph.sdk.file.FileInfo;
import com.hedera.hashgraph.sdk.file.FileInfoQuery;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class MainExSrc {

    private static final AccountId OPERATOR_ID = AccountId.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_ID")));
    private static final Ed25519PrivateKey OPERATOR_KEY = Ed25519PrivateKey.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_KEY")));
    private static final Ed25519PublicKey OPERATOR_PUBLIC_KEY = Ed25519PublicKey.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_PUBLIC_KEY")));
    private static final String MIRROR_NODE_ADDRESS = Objects.requireNonNull(Dotenv.load().get("MIRROR_NODE_ADDRESS"));

    public static void MainExSrc() throws HederaStatusException, IOException {
        System.out.println("Your account: "+OPERATOR_ID);

        //Configure a client for Hedera testnet access
        Client client = Client.forTestnet();

        // Defaults the operator account ID and key such that all generated transactions will be paid for
        // by this account and be signed by this key
        client.setOperator(OPERATOR_ID,OPERATOR_KEY); //now this is the operator

        // Build the mirror node client
        // final MirrorClient mirrorClient = new MirrorClient(MIRROR_NODE_ADDRESS);

        //initializing the menu
        int menu = 1; //to get into the while loop
        //try{
        while(menu != 0) {
            System.out.println("Choose the service (type the number)");
            System.out.println("(1) Cryptocurrency (HBar transfer)");
            System.out.println("(2) File service (sending the measurement file to the network)");
            System.out.println("(0) Exit");
            Scanner inputMenu = new Scanner(System.in);
            menu = inputMenu.nextInt();
            if (menu == 1) {
                // Do transaction
                Transaction mytransaction = new Transaction();

                //Insert the recipient account id
                System.out.println("Insert the recipient account:");
                Scanner inputRecipient = new Scanner(System.in);
                String recipientIdSt = inputRecipient.nextLine();
                AccountId recipientId = AccountId.fromString(recipientIdSt);
                //Insert the amount you want to transfer
                System.out.println("Insert the amount of Hbar (in tiny Hbar):");
                Scanner inputAmount = new Scanner(System.in);
                long amount = inputAmount.nextLong();
                //Insert the memo of your transaction
                System.out.println("Write the transaction memo:");
                Scanner inputMemo = new Scanner(System.in);
                String memo = inputMemo.nextLine();

                mytransaction.sendHbar(OPERATOR_ID, recipientId, client, amount, memo);
                System.out.println("You have just completed a transaction!");
            } else if (menu == 2) {
                //Import the measurement from .csv
                String fileName = "measurement.csv";
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
                //Get the file id
                FileId newFileId = receipt.getFileId();

                System.out.println("file: " + newFileId);

                //Print the file content (string)
                System.out.println("Do you want to print the file content (String)? (y/n)");
                Scanner inputOption = new Scanner(System.in);
                String temp = inputOption.nextLine();
                if(temp.equals("y")){
                    // read from byte array
                    ByteArrayInputStream bais = new ByteArrayInputStream(fileContents);
                    DataInputStream in = new DataInputStream(bais);
                    List<String> backToString = new ArrayList<>();
                    while (in.available() > 0) {
                        String element = in.readUTF();
                        backToString.add(element);
                    }
                    System.out.println(backToString);
                }
                else{
                    System.out.println("Not shown");
                }
                //Print the file content (byte)
                System.out.println("Do you want to print the file content (byte)? (y/n)");
                temp = inputOption.nextLine();
                if(temp.equals("y")){
                    System.out.println(fileContents);
                }
                else{
                    System.out.println("Not shown");
                }
                //View the file info
                System.out.println("Do you want to view the file info? (y/n)");
                temp = inputOption.nextLine();
                if(temp.equals("y")){
                    FileInfo info = new FileInfoQuery()
                            .setFileId(newFileId)
                            .execute(client);
                    System.out.println(info.size);
                }
                else{
                    System.out.println("Not shown");
                }
            } else if (menu != 0){
                System.out.println("Not valid. Please choose one of the available services.");

            }
        }
        //} catch (Exception e){
        //    System.out.println("Something is not right");
        //}
        System.out.println("EXIT");


    }
}
