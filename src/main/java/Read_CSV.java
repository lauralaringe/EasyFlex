
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;

public class Read_CSV {

    public static void main(String[] args) {

            //set the excel file
        String csvFile = "mes.csv";

        CSVReader reader = null;
        try {
            //read the file
            reader = new CSVReader(new FileReader(csvFile));
            String[] line;
            //Read line by line
            while ((line = reader.readNext()) != null) {
                Data battery= new Data(line[0], line[1]);
                System.out.println("timestamp: " + battery.timestamp + " at power: " + battery.power  );

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

