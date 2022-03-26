package com.bvdv.bvdvapi.models;

import com.bvdv.bvdvapi.representation.Mode;
import com.bvdv.bvdvapi.utils.IntegerListConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "pt_results")
@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class PtResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId;
    private String herdId;

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

    @Column(columnDefinition = "TEXT")
    @Convert(converter = IntegerListConverter.class)
    private List<Integer> results;

    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        ANIMAL, HERD
    }

    public static PtResult from(BvdvRequest request) {
        return new PtResult()
                .withSessionId(request.getSessionId())
                .withMode(request.getMode())
                .withUrl(request.getUrl())
                .withStorage(request.getStorage())
                .withTotalRun(request.getTotalRun())
                .withRs(request.getRs())
                .withR(request.getR())
                .withRm(request.getRm())
                .withRmpi(request.getRmpi())
                .withBpw(request.getBpw())
                .withBtw(request.getBtw())
                .withBpb(request.getBpb())
                .withBtb(request.getBtb())
                .withBpbn(request.getBpbn());

    }
}
