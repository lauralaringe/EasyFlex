import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.consensus.ConsensusMessageSubmitTransaction;
import com.hedera.hashgraph.sdk.consensus.ConsensusTopicId;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SubmitMeasFreq {
    String file;
    String content;
    public void setData(String file, Client client, ConsensusTopicId topicId){
        this.file=file;
        String content;
        String line;
        int count_content;
        content = "";
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                //content.add(line);
                content = content + line + "\n";
                count_content = 1;
                while ((count_content <= 148) & ((line = br.readLine()) != null)){
                    //content.add(line);
                    content = content + line + "\n";
                    count_content = count_content + 1;
                }
                content = content + line + "\n";
                new ConsensusMessageSubmitTransaction()
                        .setTopicId(topicId)
                        .setMessage(content)
                        .execute(client);
                Thread.sleep(6000);
                content = "";
            }
        } catch (IOException | InterruptedException | HederaStatusException e) {
            e.printStackTrace();
        }
        this.content=content;
    }
    public String getData(){
        return content;
    }
    public void printData(){
        System.out.println(getData());
    }
}

