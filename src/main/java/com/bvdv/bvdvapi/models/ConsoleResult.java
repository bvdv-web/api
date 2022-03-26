package com.bvdv.bvdvapi.models;

import com.bvdv.bvdvapi.representation.Mode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class ConsoleResult {
    private String id;
    private List<String> results = new ArrayList<>();
    private Date createdAt = new Date();
    private Mode mode;
    private String url;
    private String storage;
    private Integer totalRun;
    private Double rs;
    private Double r;
    private Double rm;
    private Double rmpi;
    private Double bpw;
    private Double btw;
    private Double bpb;
    private Double btb;
    private Double bpbn;

    private Boolean isFinished = false;
}
