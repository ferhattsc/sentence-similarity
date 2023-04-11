package com.yazlab.yazlabproject.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Metinler {

    @Id
    private String id = UUID.randomUUID().toString();
    private List<String> eklenenMetinler;
    private String birlestirilmisAnlamliCumle;
    private long responseTime;
}
