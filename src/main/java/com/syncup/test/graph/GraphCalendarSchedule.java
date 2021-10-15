package com.syncup.test.graph;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonSerialize
public class GraphCalendarSchedule implements Serializable {
    @JsonProperty("Subject")
    private String subject;
    @JsonProperty("Body")
    private Body body;
    @JsonProperty("Start")
    private Date start;
    @JsonProperty("End")
    private Date end;
    @JsonProperty("Attendees")
    private List<Attendee> attendees;
    @JsonProperty("isOnlineMeeting")
    private boolean isOnlineMeeting;
    @JsonProperty("onlineMeetingProvider")
    private String onlineMeetingProvider="teamsForBusiness";


}






