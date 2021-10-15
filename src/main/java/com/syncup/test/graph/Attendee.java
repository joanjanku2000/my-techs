package com.syncup.test.graph;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Attendee implements Serializable {
    @JsonProperty("EmailAddress")
    public EmailAddress emailAddress;
    @JsonProperty("Type")
    public String type;
}
