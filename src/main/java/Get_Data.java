import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.file.FileContentsQuery;
import com.hedera.hashgraph.sdk.file.FileId;

public class Get_Data {
Client client;
FileId fileId;
byte[] contents;

    public void get_data(Client client, FileId fileId) throws HederaStatusException {
        this.client=client;
        this.fileId=fileId;


        // Get file contents
        byte[] contents = new FileContentsQuery()
                .setFileId(fileId)
                .execute(client);

        this.contents=contents;


    }

    public String get_contents(){
        return new String(contents);

    }
}