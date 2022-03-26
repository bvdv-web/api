package com.bvdv.bvdvapi.models;

import com.bvdv.bvdvapi.representation.Mode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bvdv_requests")
@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class BvdvRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sessionId;
    private Long userId;

    private Mode mode;
    private String url;
    private String storage;
    private Integer totalRun = 1;
    private Double rs = 0.5;
    private Double r = 2.0;
    private Double rm = 0.0263073;
    private Double rmpi = 0.01;
    private Double bpw = 0.5;
    private Double btw = 0.03;
    private Double bpb = 0.1;
    private Double btb = 0.0;
    private Double bpbn = 0.03;

    private Date createdAt = new Date();

    public ConsoleResult toConsoleResult() {
        return new ConsoleResult()
                .withMode(mode)
                .withUrl(url)
                .withStorage(storage)
                .withTotalRun(totalRun)
                .withRs(rs)
                .withR(r)
                .withRm(rm)
                .withRmpi(rmpi)
                .withBpw(bpw)
                .withBtw(btw)
                .withBpb(bpb)
                .withBtb(btb)
                .withBpbn(bpbn);
    }

    @JsonIgnore
    public String getInputDataFolder() {
        return Mode.DEMO.equals(mode) ? "INPUT_DAT/" : "UPLOAD_INPUT_DAT/";
    }

    @JsonIgnore
    public String getInputCoorFolder() {
        return Mode.DEMO.equals(mode) ? "INPUT_COOR/" : "UPLOAD_INPUT_COOR/";
    }

    @JsonIgnore
    public String getOutputFolder() {
        return Mode.DEMO.equals(mode) ? "OUTPUT/" : "UPLOAD_OUTPUT/";
    }
}
