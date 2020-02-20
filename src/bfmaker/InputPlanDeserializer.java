package bfmaker;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Arrays.asList;

public class InputPlanDeserializer extends JsonDeserializer<InputPlan> {
    public static class SubjDeserializer extends JsonDeserializer<InputPlan.Subj> {
        public static class CourseDeserializer extends JsonDeserializer<InputPlan.Subj.Course> {
            private ObjectMapper mapper;
            public CourseDeserializer() {
                mapper = new ObjectMapper();
                SimpleModule module = new SimpleModule();
                module.addDeserializer(Cord.class, new CordDeserializer());
                mapper.registerModule(module);
            }
            @Override
            public InputPlan.Subj.Course deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                ObjectCodec codec = jsonParser.getCodec();
                JsonNode node = codec.readTree(jsonParser);
                String professor = node.get("professor").asText();
                Cord[] courseTimesAr = mapper.readValue(node.get("courseTimes").toString(), Cord[].class);
                ArrayList<Cord> courseTimes = new ArrayList<Cord>(InputPlan.Subj.Course.DEFAULT_COURSE_TIMES_CAPACITY);
                courseTimes.addAll(Arrays.asList(courseTimesAr));
                boolean enabled = node.get("enabled").asBoolean();

                return new InputPlan.Subj.Course(professor, courseTimes, enabled);
            }
        }
        private ObjectMapper mapper;
        public SubjDeserializer() {
            mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(InputPlan.Subj.Course.class, new CourseDeserializer());
            mapper.registerModule(module);
        }
        @Override
        public InputPlan.Subj deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            ObjectCodec codec = jsonParser.getCodec();
            JsonNode node = codec.readTree(jsonParser);
            String subjName = node.get("subjName").asText();
            InputPlan.Subj.Course[] coursesAr = mapper.readValue(node.get("courses").toString(), InputPlan.Subj.Course[].class);
            ArrayList<InputPlan.Subj.Course> courses = new ArrayList<InputPlan.Subj.Course>(InputPlan.Subj.DEFAULT_COURSES_CAPACITY);
            courses.addAll(Arrays.asList(coursesAr));
            boolean enabled = node.get("enabled").asBoolean();

            return new InputPlan.Subj(subjName, courses, enabled);
        }
    }
    private ObjectMapper mapper;
    public InputPlanDeserializer() {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(InputPlan.Subj.class, new SubjDeserializer());
        mapper.registerModule(module);
    }
    @Override
    public InputPlan deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);
        int MAX_DAY = node.get("MAX_DAY").asInt();
        int MAX_TIME = node.get("MAX_TIME").asInt();
        InputPlan.Subj[] subjsAr = mapper.readValue(node.get("subjs").toString(), InputPlan.Subj[].class);
        ArrayList<InputPlan.Subj> subjs = new ArrayList<InputPlan.Subj>(InputPlan.DEFAULT_SUBJS_CAPACITY);
        subjs.addAll(Arrays.asList(subjsAr));

        return new InputPlan(MAX_DAY, MAX_TIME, subjs);
    }
}
