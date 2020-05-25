package Examples;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImportMeas {
   String file;
   List<String> content;
    public void setData(String file){
        this.file=file;
        List<String> content = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.content=content;
    }
    public List<String> getData(){
        return content;
    }
    public void printData(){
        System.out.println(getData());
    }
}

//content= content that has to be converted in byte and then stored