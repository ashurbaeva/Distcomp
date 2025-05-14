package by.symonik.issue_service.util;

import by.symonik.issue_service.dto.label.LabelRequestTo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.List;

public class LabelRequestToDeserializer extends JsonDeserializer<List<LabelRequestTo>> {

    @Override
    public List<LabelRequestTo> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        List<String> labelNames = jsonParser.readValueAs(List.class);
        return labelNames.stream()
                .map(name -> LabelRequestTo.builder().name(name).build())
                .toList();
    }
}
