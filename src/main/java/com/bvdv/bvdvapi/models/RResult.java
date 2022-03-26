package com.bvdv.bvdvapi.models;

import com.bvdv.bvdvapi.utils.DoubleListConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "r_results")
@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class RResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId;
    private String herdId;
    @Column(columnDefinition = "TEXT")
    @Convert(converter = DoubleListConverter.class)
    private List<Double> results;

//    public List<Integer> getResultList() {
//        return Optional.ofNullable(results)
//                .map(r -> r.replaceAll(",$", ""))
//                .map(r -> Arrays.stream(r.split(",")).map(Integer::parseInt)
//                        .collect(Collectors.toList()))
//                .orElse(List.of());
//    }

//    private Date createdAt = new Date();
//    private BvdvRequest.Mode mode;
//    private String url;
//    private String storage;
//    private Integer totalRun;
//    private Double rs;
//    private Double r;
//    private Double rm;
//    private Double rmpi;
//    private Double bpw;
//    private Double btw;
//    private Double bpb;
//    private Double btb;
//    private Double bpbn;
//
//    public static RResult from(BvdvRequest request) {
//        return new RResult()
//                .withId(request.getSessionId())
//                .withMode(request.getMode())
//                .withUrl(request.getUrl())
//                .withStorage(request.getStorage())
//                .withTotalRun(request.getTotalRun())
//                .withRs(request.getRs())
//                .withR(request.getR())
//                .withRm(request.getRm())
//                .withRmpi(request.getRmpi())
//                .withBpw(request.getBpw())
//                .withBtw(request.getBtw())
//                .withBpb(request.getBpb())
//                .withBtb(request.getBtb())
//                .withBpbn(request.getBpbn());
//    }
}
