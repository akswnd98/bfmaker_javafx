package bfmaker;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class OutputPlanDeserializer extends JsonDeserializer<OutputPlan> {
    public static class TableDeserializer extends JsonDeserializer<OutputPlan.Table> {
        public static class CourseDeserializer extends JsonDeserializer<OutputPlan.Table.Course> {
            private ObjectMapper mapper;
            public CourseDeserializer() {
                mapper = new ObjectMapper();
                SimpleModule module = new SimpleModule();
                module.addDeserializer(Cord.class, new CordDeserializer());
                mapper.registerModule(module);
            }
            @Override
            public OutputPlan.Table.Course deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                ObjectCodec codec = jsonParser.getCodec();
                JsonNode node = codec.readTree(jsonParser);
                String subjName = node.get("subjName").asText();
                String professor = node.get("professor").asText();
                Cord[] courseTimesAr = mapper.readValue(node.get("courseTimes").asText(), Cord[].class);
                LinkedList<Cord> courseTimes = new LinkedList<Cord>();
                courseTimes.addAll(Arrays.asList(courseTimesAr));

                return new OutputPlan.Table.Course(subjName, professor, courseTimes);
            }
        }
        private ObjectMapper mapper;
        public TableDeserializer() {
            mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(OutputPlan.Table.Course.class, new CourseDeserializer());
            mapper.registerModule(module);
        }
        @Override
        public OutputPlan.Table deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            ObjectCodec codec = jsonParser.getCodec();
            JsonNode node = codec.readTree(jsonParser);
            OutputPlan.Table.Course[] coursesAr = mapper.readValue(node.get("courses").asText(), OutputPlan.Table.Course[].class);
            LinkedList<OutputPlan.Table.Course> courses = new LinkedList<OutputPlan.Table.Course>();
            courses.addAll(Arrays.asList(coursesAr));
            boolean marked = node.get("marked").asBoolean();

            return new OutputPlan.Table(courses, marked);
        }
    }
    private ObjectMapper mapper;
    public OutputPlanDeserializer() {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(OutputPlan.Table.class, new TableDeserializer());
        mapper.registerModule(module);
    }
    @Override
    public OutputPlan deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);
        int MAX_DAY = node.get("MAX_DAY").asInt();
        int MAX_TIME = node.get("MAX_TIME").asInt();
        OutputPlan.Table[] tablesAr = mapper.readValue(node.get("tables").asText(), OutputPlan.Table[].class);
        LinkedList<OutputPlan.Table> tables = new LinkedList<OutputPlan.Table>();
        tables.addAll(Arrays.asList(tablesAr));

        return new OutputPlan(MAX_DAY, MAX_TIME, tables);
    }
}
