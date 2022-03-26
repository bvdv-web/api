package com.bvdv.bvdvapi.workers;

import com.bvdv.bvdvapi.config.RabbitMqConfig;
import com.bvdv.bvdvapi.models.ConsoleResult;
import com.bvdv.bvdvapi.models.PtResult;
import com.bvdv.bvdvapi.models.RResult;
import com.bvdv.bvdvapi.repository.ConsoleResultRepository;
import com.bvdv.bvdvapi.repository.PtResultRepository;
import com.bvdv.bvdvapi.repository.RResultRepository;
import com.bvdv.bvdvapi.models.BvdvRequest;
import com.bvdv.bvdvapi.representation.Mode;
import com.bvdv.bvdvapi.service.GoogleDriveFileService;
import com.bvdv.bvdvapi.service.GoogleDriveFolderService;
import com.bvdv.bvdvapi.utils.GoogleDriveUtil;
import com.google.api.services.drive.model.File;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class BvdvRunWorker {
    @Autowired
    GoogleDriveFileService googleDriveFileService;

    @Autowired
    GoogleDriveFolderService googleDriveFolderService;

    @Autowired
    ConsoleResultRepository consoleResultRepository;

    @Autowired
    PtResultRepository ptResultRepository;

    @Autowired
    RResultRepository rResultRepository;

    private static final String FOLDER = "UPLOAD_INPUT_DAT";

    private void downloadFiles(String url) throws IOException, GeneralSecurityException {
        FileUtils.cleanDirectory(new java.io.File(FOLDER));

        String id = GoogleDriveUtil.getIdFromUrl(url);
        if (id == null) return;

        if (GoogleDriveUtil.isFile(url)) {
            OutputStream outputStream = new FileOutputStream(FOLDER + "/Detention_Dariy_Mix_05_full.csv");
            googleDriveFileService.downloadFile(GoogleDriveUtil.getIdFromUrl(id), outputStream);
        } else if (GoogleDriveUtil.isFolder(url)) {
            List<File> children = googleDriveFolderService.listFolderContent(id);
            children.parallelStream().forEach((child) -> {
                OutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(FOLDER + "/" + child.getName());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    googleDriveFileService.downloadFile(child.getId(), outputStream);
                } catch (IOException | GeneralSecurityException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    @RabbitListener(queues = RabbitMqConfig.BVDV_QUEUE)
    public void receiveMessage(final BvdvRequest request) throws IOException, GeneralSecurityException {
        log.info("Got new message " + request.getSessionId());
        if (StringUtils.isBlank(request.getSessionId())) {
            return;
        }

        ConsoleResult consoleResult = getConsoleResult(request.getSessionId());

        if (Mode.UPLOAD.equals(request.getMode())) {
            downloadFiles(request.getUrl());
        }

        Runtime rt = Runtime.getRuntime();
        String command = String.format("./BVDV_BetweenHerd_ForCloudVersion %s %s %s %s %s %s %s %s %s %s %s %s %s",
                request.getTotalRun(), request.getRs(), request.getR(), request.getRm(), request.getRmpi(),
                request.getBpw(), request.getBtw(), request.getBpb(), request.getBtb(), request.getBpbn(),
                request.getInputDataFolder(), request.getInputCoorFolder(), request.getOutputFolder()
        );

        Process proc = rt.exec(command);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

        System.out.println("Here is the standard output of the command:\n");
        String line;
        while ((line = stdInput.readLine()) != null) {
            addConsoleResult(consoleResult, line);
            log.info(line);
        }

        System.out.println("Here is the standard error of the command (if any):");
        while ((line = stdError.readLine()) != null) {
            addConsoleResult(consoleResult, line);
            log.error(line);
        }

        setFinished(consoleResult);
        insertPTResults(request);
        insertRResults(request);
    }

    private void insertPTResults(BvdvRequest request) {
        java.io.File[] files = new java.io.File(request.getOutputFolder()).listFiles();
        if (files == null) return;
        Arrays.stream(files).filter(f -> f.getName().startsWith("AllHerdInSystemPT")).findFirst().ifPresent(
                file -> {
                    Path filePath = Paths.get(request.getOutputFolder(), file.getName());
                    Map<String, List<String>> csvOutput = readCsv(filePath.toString());

                    List<Integer> animalSum = IntStream.range(0, csvOutput.entrySet().iterator().next().getValue().size()).mapToObj(timestep ->
                            csvOutput.values().stream().map(v -> v.get(timestep)).map(Integer::parseInt).mapToInt(Integer::intValue).sum()
                    ).collect(Collectors.toList());

                    List<Integer> herdSum = IntStream.range(0, csvOutput.entrySet().iterator().next().getValue().size()).mapToObj(timestep ->
                            csvOutput.values().stream().map(v -> v.get(timestep)).map(Integer::parseInt).filter(r -> r > 0).count()
                    ).map(Long::intValue).collect(Collectors.toList());


                    PtResult animalResult = PtResult.from(request)
                            .withType(PtResult.Type.ANIMAL)
                            .withResults(animalSum);

                    PtResult herdResult = PtResult.from(request)
                            .withType(PtResult.Type.HERD)
                            .withResults(herdSum);

                    ptResultRepository.save(animalResult);
                    ptResultRepository.save(herdResult);
                }
        );
    }

    private void insertRResults(BvdvRequest request) {
        java.io.File[] files = new java.io.File(request.getOutputFolder()).listFiles();
        if (files == null) return;
        Arrays.stream(files).filter(f -> f.getName().startsWith("AllHerdInSystemPropR")).findFirst().ifPresent(
                file -> {
                    Path filePath = Paths.get(request.getOutputFolder(), file.getName());
                    Map<String, List<String>> csvOutput = readCsv(filePath.toString());

//                    List<Double> rSum = IntStream.range(0, csvOutput.entrySet().iterator().next().getValue().size()).mapToObj(timestep ->
//                            csvOutput.values().stream().map(v -> v.get(timestep)).map(Double::parseDouble).mapToDouble(Double::doubleValue).sum()
//                    ).collect(Collectors.toList());
                    List<RResult> rResults = csvOutput.entrySet().stream().map(entry -> {
                        List<Double> results = entry.getValue().stream().map(v ->
                                NumberUtils.isCreatable(v) ? Double.parseDouble(v) : 0).collect(Collectors.toList());
                        return new RResult()
                                .withSessionId(request.getSessionId())
                                .withHerdId(entry.getKey())
                                .withResults(results);
                    }).filter(rResult -> rResult.getResults().stream().anyMatch(r -> r > 0))
                            .collect(Collectors.toList());

                    List<List<RResult>> chunks = Lists.partition(rResults, 1000);
                    for (int i = 0; i < chunks.size(); i++) {
                        List<RResult> chunk = chunks.get(i);
                        log.info("Saving immune results {} - {} / {}", i * 1000 + 1, (i + 1) * 1000, rResults.size());
                        rResultRepository.saveAll(chunk);
                    }
                }
        );
    }

    private Map<String, List<String>> readCsv(String path) {
        Map<String, List<String>> output = new HashMap<>();
        try (
                Reader reader = Files.newBufferedReader(Paths.get(path));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.TDF);
        ) {
            for (CSVRecord csvRecord : csvParser) {
                String herdId = csvRecord.get(0);
                List<String> results = IntStream.range(1, csvRecord.size()).mapToObj(csvRecord::get).collect(Collectors.toList());
                if (StringUtils.isBlank(results.get(results.size() - 1))) results.remove(results.size() - 1);
                output.put(herdId, results);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }

    void addConsoleResult(ConsoleResult consoleResult, String line) {
        if (consoleResult == null) return;
        try {
//            ConsoleResult consoleResult = consoleResultRepository.get(sessionId);
            consoleResult.getResults().add(line);
            consoleResultRepository.save(consoleResult.getId(), consoleResult);
        } catch (Exception ignored) {

        }
    }

    ConsoleResult getConsoleResult(String sessionId) {
        try {
            ConsoleResult consoleResult = consoleResultRepository.get(sessionId);
            if (consoleResult == null) {
                log.info("Cannot connect to firestore");
            } else {
                log.info("Connected to firestore");
            }
            return consoleResult;
        } catch (Exception e) {
            log.info("Cannot connect to firestore");
            return null;
        }
    }

    void setFinished(ConsoleResult consoleResult) {
        if (consoleResult == null) return;
        consoleResult.setIsFinished(true);
        consoleResultRepository.save(consoleResult.getId(), consoleResult);
    }
}
