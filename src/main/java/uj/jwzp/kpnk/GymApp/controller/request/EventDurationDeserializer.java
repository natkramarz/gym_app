package uj.jwzp.kpnk.GymApp.controller.request;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.DurationDeserializer;
import uj.jwzp.kpnk.GymApp.exception.EventDurationParsingException;

import java.io.IOException;
import java.time.Duration;
import java.time.format.DateTimeParseException;

public class EventDurationDeserializer extends DurationDeserializer {

    public EventDurationDeserializer() {
        super();
    }

    @Override
    public Duration deserialize(JsonParser parser, DeserializationContext ctxt) {
        String[] duration = null;
        try {
            String d = parser.getText();
            duration = d.split(":");
        } catch (IOException e) {
            throw new EventDurationParsingException();
        }
        if (duration.length != 2) throw new EventDurationParsingException();
        Duration result = null;
        try {
            result = Duration.parse("PT" + duration[0] + "H" + duration[1] + "M");
        } catch (DateTimeParseException e) {
            throw new EventDurationParsingException();
        }

        return result;
    }
}
