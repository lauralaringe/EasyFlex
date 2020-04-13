import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MirrorNodeData {

    private String operatorId;
    private String myURL;


    public void setOperator(String operatorId){
        this.operatorId = operatorId;
    }

    public void importTransaction(){
        this.myURL = ("https://api.testnet.kabuto.sh/v1/account/" + operatorId + "/transaction");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(myURL)).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(MirrorNodeData::parse)
                .join();

    }

    // Parsing the JSON string to retrieve specific information
    public static String parse(String responseBody){
        JSONObject transactions = new JSONObject(responseBody);
        JSONArray transaction = transactions.getJSONArray("transactions");
        for (int i = 0; i < transaction.length(); i++){
            JSONObject transactionInfo = transaction.getJSONObject(i);
            String transactionId = transactionInfo.getString("id");
            String transactionHash = transactionInfo.getString("hash");
            System.out.println(transactionId + "\n" + transactionHash + "\n");
            System.out.println("---------------------------");
        }
        return null;
    }
}
