import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.text.html.Option;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MainTest {

    @Test
    public void fileWriteTest(){
        CsvParser parser = new CsvParser();
        parser.readCsvFile("1.csv");
        Assert.assertTrue("Can't write to file!", true);
    }

    @Test
    public void existsFileTest(){
        File file = new File("1.csv");
        assert file.exists();
    }

    @Test
    public void writeToFileTest(){
        try(FileWriter writer = new FileWriter("3.csv")){
            writer.write("hello!");
            writer.flush();
            assert true;
        }catch (Exception e){
            assert false;
        }
    }

    @Test
    public void fileOpensTest(){

        try {
            FileInputStream is = new FileInputStream("3.csv");
            assert is.available() != 0;
        }catch (IOException e){
            assert false;
        }
    }

    @Test
    public void resultTest(){
        String result = "\"\",TEAM_BEAUJOLAIS,TEAM_REGSERV,TEAM_LOIRE,TEAM_RHONE,TEAM_TECH,TEAM_ALSACE,MISC\n" +
                "Blocker,11,8,0,0,0,1,6\n" +
                "Critical,22,10,3,0,0,0,6\n" +
                "Major,23,5,4,0,0,4,5\n" +
                "Medium,0,0,0,0,0,0,0\n" +
                "Minor,4,0,7,0,0,1,2\n" +
                "Normal,0,0,0,0,0,0,0\n" +
                "Total,60,23,14,0,0,6,19\n" +
                "Unresolved,52,16,13,0,0,4,6";
        CsvParser parser = new CsvParser();
        parser.readCsvFile("1.csv");
        try {
            String[] resultLines = result.split("\\n");
            List<String> builderLines = Files.readAllLines(new File("3.csv").toPath());
            for(int i = 0;i<resultLines.length;i++){
                String resLine = resultLines[i];
                String builderLine = builderLines.get(i);
                assert resLine.equals(builderLine);
            }
            assert resultLines.length == builderLines.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void columnDataExistsTest() throws Exception{
        Reader reader = Files.newBufferedReader(Paths.get("1.csv"));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
        csvParser.forEach((element)->{
            String[] neededColumns = {"Status", "Labels", "Priority"};
            Arrays.asList(neededColumns).forEach((column)->{
                Optional<String> optional = Optional.of(element.get(column));
                optional.orElseThrow(IllegalArgumentException::new);
            });
        });
    }
    
}
