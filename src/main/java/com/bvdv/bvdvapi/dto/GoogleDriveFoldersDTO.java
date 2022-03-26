package com.bvdv.bvdvapi.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Setter
public class GoogleDriveFoldersDTO implements Serializable {
    private String id;
    private String name;
    private String link;
}