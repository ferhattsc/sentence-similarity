package com.yazlab.yazlabproject.entity;

import lombok.Data;

import java.util.List;

@Data
public class MetinlerDto {
    private String id;
    private List<String> eklenenMetinler;
}
