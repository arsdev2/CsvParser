import jdk.nashorn.internal.runtime.options.Option;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class CsvParser {
    private ArrayList<Team> teamArrayList = new ArrayList<>();
    private ArrayList<String> headers = new ArrayList<>(), blockerData = new ArrayList<>(), criticalData = new ArrayList<>(), MajorData = new ArrayList<>(), MediumData = new ArrayList<>(),MinorData = new ArrayList<>(), NormalData = new ArrayList<>(), TotalData = new ArrayList<>(), UnresolvedData = new ArrayList<>();
    private ArrayList<ArrayList<String>> data = new ArrayList<>();

    private void createResultCsvFile(String[] headers,final ArrayList<ArrayList<String>> data){
        try {
            File file = new File("3.csv");
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.getName()));
                 CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                         .withHeader(headers))
            ) {
                data.forEach((record)->{
                    try {
                        csvPrinter.printRecord(record);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
                csvPrinter.flush();
            }
        }catch (IOException e){
            System.out.println("Can't write to file!");
            System.exit(1);
        }
    }

    public void readCsvFile(String csvFilename){
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilename));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            csvParser.forEach((this::processCsvFile));
            parseResultCsvFile();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void processCsvFile(CSVRecord csvRecord){
        Team team = new Team();
        Arrays.asList(Team.teamType.values()).forEach((type)->{
            Optional<String> optional = Optional.of(csvRecord.get("Labels"));
            if(optional.isPresent()) {
                if (optional.get().contains(type.toString())) {
                    team.setType(type);
                }
            }
        });
        final AtomicBoolean toAdd = new AtomicBoolean(true);
        teamArrayList.forEach((teamCheck)->{
            Optional<String> optional = Optional.of(csvRecord.get("Labels"));
            if(optional.isPresent()) {
                if (optional.get().contains(teamCheck.getType().toString())) {
                    increase(teamCheck, csvRecord);
                    toAdd.set(false);
                }
            }
        });
        if(toAdd.get()) {
            increase(team, csvRecord);
            teamArrayList.add(team);
        }
    }
    private void parseResultCsvFile(){
        final Integer[] index = {3};
        Arrays.asList(Team.teamType.values()).forEach((type)->{
            AtomicBoolean found = new AtomicBoolean(false);
            teamArrayList.forEach((team)->{
                if(type.toString().equals(team.getType().toString())){
                    found.set(true);
                }
            });
            if(!found.get()){
                Team team = new Team();
                team.setType(type);
                teamArrayList.add(index[0],team);
                index[0] = 4;
            }
        });

        initBugsData();
        fillBugsData();
        String[] headersStringArray = headers.toArray(new String[0]);
        fillResultData();
        createResultCsvFile(headersStringArray, data);
    }

    private void initBugsData(){
        headers.add("");
        blockerData.add("Blocker");
        criticalData.add("Critical");
        MajorData.add("Major");
        MediumData.add("Medium");
        MinorData.add("Minor");
        NormalData.add("Normal");
        TotalData.add("Total");
        UnresolvedData.add("Unresolved");
    }

    private void fillBugsData(){
        teamArrayList.forEach((team)->{
            headers.add(team.getType().toString());
            blockerData.add(String.valueOf(team.getBlockerBugsCount()));
            criticalData.add(String.valueOf(team.getCriticalBugs()));
            MajorData.add(String.valueOf(team.getMajorBugs()));
            MediumData.add(String.valueOf(team.getMediumBugs()));
            MinorData.add(String.valueOf(team.getMinorBugs()));
            NormalData.add(String.valueOf(team.getNormalBugs()));
            TotalData.add(String.valueOf(team.getTotalBugs()));
            UnresolvedData.add(String.valueOf(team.getUnresolvedBugs()));
        });
    }

    private void fillResultData(){
        data.add(blockerData);
        data.add(criticalData);
        data.add(MajorData);
        data.add(MediumData);
        data.add(MinorData);
        data.add(NormalData);
        data.add(TotalData);
        data.add(UnresolvedData);
    }



    private void increase(Team teamCheck,CSVRecord csvRecord){
        String prior = csvRecord.get("Priority");
        try {
            Class c = teamCheck.getClass();
            Method[] m = c.getDeclaredMethods();
            Arrays.asList(m).forEach(method -> {
                if(method.getName().contains(prior)){
                    try {
                        method.invoke(teamCheck);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if(!csvRecord.get("Status").contains("Complete")){
            teamCheck.increaseUnresolvedBugs();
        }
        teamCheck.increaseTotalBugs();
    }
}
