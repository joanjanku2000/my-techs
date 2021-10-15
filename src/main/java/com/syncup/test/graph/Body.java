package com.syncup.test.graph;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Body implements Serializable {
    @JsonProperty("ContentType")
    public String contentType;
    @JsonProperty("Content")
    public String content;
}
