package com.syncup.test.graph;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.awt.*;
import java.io.Serializable;

@Data
public class EmailAddress implements Serializable {
    @JsonProperty("Address")
    private String address;
    @JsonProperty("Name")
    private String name;
}
