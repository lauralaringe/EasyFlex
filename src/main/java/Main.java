import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.account.AccountId;
import com.hedera.hashgraph.sdk.consensus.ConsensusMessageSubmitTransaction;
import com.hedera.hashgraph.sdk.consensus.ConsensusTopicCreateTransaction;
import com.hedera.hashgraph.sdk.consensus.ConsensusTopicId;
import com.hedera.hashgraph.sdk.consensus.ConsensusTopicUpdateTransaction;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PublicKey;
import com.hedera.hashgraph.sdk.file.FileCreateTransaction;
import com.hedera.hashgraph.sdk.file.FileId;
import com.hedera.hashgraph.sdk.file.FileInfo;
import com.hedera.hashgraph.sdk.file.FileInfoQuery;
import com.hedera.hashgraph.sdk.mirror.MirrorClient;
import com.hedera.hashgraph.sdk.mirror.MirrorConsensusTopicQuery;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

    private static final AccountId OPERATOR_ID = AccountId.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_ID")));
    private static final Ed25519PrivateKey OPERATOR_KEY = Ed25519PrivateKey.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_KEY")));
    private static final Ed25519PublicKey OPERATOR_PUBLIC_KEY = Ed25519PublicKey.fromString(Objects.requireNonNull(Dotenv.load().get("OPERATOR_PUBLIC_KEY")));
    private static final String MIRROR_NODE_ADDRESS = Objects.requireNonNull(Dotenv.load().get("MIRROR_NODE_ADDRESS"));

    public static void main(String[] args) throws HederaStatusException, IOException, InterruptedException {
        float power = 5; //5 kW
        float tolerance = (float) 0.05; // 5%
        Commitment power_committed = new Commitment(power, tolerance);

        System.out.println("Your account: "+OPERATOR_ID);

        //Configure a client for Hedera testnet access
        Client client = Client.forTestnet();

        // Defaults the operator account ID and key such that all generated transactions will be paid for
        // by this account and be signed by this key
        client.setOperator(OPERATOR_ID,OPERATOR_KEY); //now this is the operator

        // Build the mirror node client
        // final MirrorClient mirrorClient = new MirrorClient(MIRROR_NODE_ADDRESS);

        AtomicBoolean flag = new AtomicBoolean(true);
        //initializing the menu
        int menu = 1; //to get into the while loop
        // object to give input in the options
        Scanner inputStr = new Scanner(System.in);
        Scanner inputOption = new Scanner(System.in);
//        try{
        while(menu != 0) {
//            if (flag.get()== false) {
//                System.out.println("The commitment is not valid");
//                        }

            System.out.println("Choose the service (type the number)");
            System.out.println("(1) Cryptocurrency (HBar transfer)");
            System.out.println("(2) File service (sending the measurement file to the network)");
            System.out.println("(3) Consensus service");
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
                System.out.println("Sending measurement_invalid.csv...");
                String fileName = "src/measurement_invalid.csv";
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
                        .addKey(OPERATOR_PUBLIC_KEY)
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
                String temp = inputOption.nextLine();
                if(temp.equals("y")) {
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
                if(temp.equals("y")) {
                    System.out.println(fileContents);
                }
                else{
                    System.out.println("Not shown");
                }
                //View the file info
                System.out.println("Do you want to view the file info? (y/n)");
                temp = inputOption.nextLine();
                if(temp.equals("y")) {
                    FileInfo info = new FileInfoQuery()
                            .setFileId(newFileId)
                            .execute(client);
                    System.out.println(info.size);
                }
                else{
                    System.out.println("Not shown");
                }
            }
            else if (menu == 3) {
                int tmenu = -1;
                while(tmenu != 0) {
                    System.out.println("What action would you like to do?\n" +
                            "(1) Create a topic\n" +
                            "(2) Update a topic\n" +
                            "(3) Submit a message\n" +
                            "(4) Delete a topic\n" +
                            "(5) Get topic info\n" +
                            "(6) Automated message\n"+
                            "(7) Listen to the mirror node\n"+
                            "(0) Back to main menu");
                    tmenu = inputOption.nextInt();
                    ConsensusTopicId myTopicId;
                    switch(tmenu) {
                        case 0:
                            break;
                        case 1:
                            // Create a new topic
                            final TransactionId transactionId = new ConsensusTopicCreateTransaction()
                                    // Set the creator as the admin
                                    .setAdminKey(OPERATOR_PUBLIC_KEY)
                                    .execute(client);
                            // Grab the newly generated topic ID
                            ConsensusTopicId topicId = transactionId.getReceipt(client).getConsensusTopicId();
                            System.out.println("Your topic ID is: " + topicId);
                            break;
                        case 2:
                            System.out.print("Write the topic ID: ");
                            String topicId_ = inputStr.nextLine();
                            myTopicId = ConsensusTopicId.fromString(topicId_);
                            System.out.println("Choose what to update:\n" +
                                    "(1) Set topic memo\n" +
                                    "(2) Clear topic memo\n" +
                                    "(3) Set submit key\n" +
                                    "(4) Clear submit key");
                            int temp = inputMenu.nextInt();
                            switch(temp){
                                case 1:
                                    System.out.print("Set the topic memo: ");
                                    String tmemo = inputStr.nextLine();
                                    new ConsensusTopicUpdateTransaction()
                                            .setTopicId(myTopicId)
                                            .setTopicMemo(tmemo)
                                            .execute(client);
                                    break;
                                case 2:
                                    System.out.println("Clearing the topic memo...");
                                    new ConsensusTopicUpdateTransaction()
                                            .setTopicId(myTopicId)
                                            .clearTopicMemo()
                                            .execute(client);
                                    break;
                                case 3:
                                    System.out.print("Set the submit key: ");
                                    String submitKey = inputStr.nextLine();
                                    Ed25519PublicKey addSubmitKey = Ed25519PublicKey.fromString(submitKey);
                                    new ConsensusTopicUpdateTransaction()
                                            .setTopicId(myTopicId)
                                            .setSubmitKey(addSubmitKey)
                                            .execute(client);
                                    break;
                                case 4:
                                    System.out.print("Clearing the submit key...");
                                    new ConsensusTopicUpdateTransaction()
                                            .setTopicId(myTopicId)
                                            .clearSubmitKey()
                                            .execute(client);
                                    break;
                                default:
                                    System.out.println("Invalid request");
                                    break;
                            }
                            break;
                        case 3:
                            System.out.print("Insert the topic ID: ");
                            topicId_ = inputStr.nextLine();
                            myTopicId = ConsensusTopicId.fromString(topicId_);
                            System.out.println("Insert your message below:");
                            String myMessage = inputStr.nextLine();

                            new ConsensusMessageSubmitTransaction()
                                    .setTopicId(myTopicId)
                                    .setMessage(myMessage)
                                    .execute(client);
                            break;
                        case 6:
                            System.out.print("Insert the topic ID: ");
                            topicId_ = inputStr.nextLine();
                            myTopicId = ConsensusTopicId.fromString(topicId_);
                            System.out.println("Sending the data every ten minutes");
                            SubmitMeasFreq myMeasFreq = new SubmitMeasFreq();
                            String filename;
                            // Choose the measurment file
                            System.out.println("What file would you like to use?\n" +
                                    "(1) Invalid file\n" +
                                    "(2) Valid file");
                            tmenu = inputOption.nextInt();
                            switch(tmenu){
                                case 1:
                                    filename = "src/measurement_invalid.csv";
                                    myMeasFreq.setData(filename, client, myTopicId);
                                    break;

                                case 2:
                                    filename = "src/measurement_valid.csv";
                                    myMeasFreq.setData(filename, client, myTopicId);
                                    break;
                                            }
                            break;


                        case 7:
                            System.out.println("Obtaining message");
                            System.out.println("Insert the topic ID: ");
                            topicId_ = inputStr.nextLine();
                            myTopicId = ConsensusTopicId.fromString(topicId_);
                            MirrorClient consensusClient = new MirrorClient(MIRROR_NODE_ADDRESS);
                            List<String> listenedMessage = new ArrayList<>();

                            new MirrorConsensusTopicQuery()
                                    .setTopicId(myTopicId)
                                    .subscribe(consensusClient, resp -> {
                                                if (flag.get()== false) {
                                                    System.out.println("The commitment is not valid");
                                                }
                                                String messageAsString = new String(resp.message, StandardCharsets.UTF_8);
                                                listenedMessage.add(resp.consensusTimestamp + "\n" + messageAsString);
//                                                System.out.println( messageAsString + "end");
                                                String lines[] = messageAsString.split("\\r?\\n");
                                                for (int k=1; k<lines.length-1; k++) {
                                                    String[] power_string = lines[k].split(",");
//                                                    System.out.println(power_string[1]);
                                                    float real_power = Float.parseFloat(power_string[1].toString());
                                                 if (power >= (1+tolerance)*real_power ||
                                                        power <= (1-tolerance)*real_power) {
                                                    flag.set(false);
                                                    break;
                                                    }

                                                }


//
                                            },
                                            // On gRPC error, print the stack trace
                                            Throwable::printStackTrace);
                            Thread.sleep(30000);
                            if (flag.get() == true) {
                                System.out.println("The commitment is valid!");

                            };
                            System.out.println("Do you want to show the data listened?\n" +
                                    "(1) Yes\n" +
                                    "(2) No\n" );
                            int men = inputMenu.nextInt();
                            switch(men) {
                                case 1:
                                    System.out.println(listenedMessage);

                                case 2:
                                    break;
                            }



                            break;
                        default:
                            System.out.println("Invalid request");
                            break;
                    }
                }
            }
            else if (menu != 0) {
                System.out.println("Not valid. Please choose one of the available services.");

            }
        }
        //} catch (Exception e) {
        //    System.out.println("Something is not right");
        //}
        System.out.println("EXIT");
    }
}
