package bfmaker;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.LinkedList;

public class InputPlan {
    public static class Subj {
        public static class Course {
            @JsonIgnore
            public static final int DEFAULT_COURSE_TIMES_CAPACITY = 100;

            private String professor;
            @JsonGetter("professor")
            public String getProfessor() {
                return professor;
            }
            @JsonSetter("professor")
            public void setProfessor(String professor) {
                this.professor = professor;
            }


            private ArrayList<Cord> courseTimes;
            @JsonGetter("courseTimes")
            public ArrayList<Cord> getCourseTimes() {
                return courseTimes;
            }
            @JsonSetter("courseTimes")
            public void setCourseTimes(ArrayList<Cord> courseTimes) {
                this.courseTimes = courseTimes;
            }

            private boolean enabled;
            @JsonGetter("enabled")
            public boolean isEnabled() {
                return enabled;
            }
            @JsonSetter("enabled")
            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public Course(String professor) {
                this.professor = professor;
                courseTimes = new ArrayList<Cord>(DEFAULT_COURSE_TIMES_CAPACITY);
                enabled = true;
            }
            public Course(String professor, ArrayList<Cord> courseTimes) {
                this.professor = professor;
                this.courseTimes = courseTimes;
                enabled = true;
            }
            public Course(String professor, ArrayList<Cord> courseTimes, boolean enabled) {
                this.professor = professor;
                this.courseTimes = courseTimes;
                this.enabled = enabled;
            }
        }
        @JsonIgnore
        public static final int DEFAULT_COURSES_CAPACITY = 100;

        private String subjName;
        @JsonGetter("subjName")
        public String getSubjName() {
            return subjName;
        }
        @JsonSetter("subjName")
        public void setSubjName(String subjName) {
            this.subjName = subjName;
        }

        private ArrayList<Course> courses;
        @JsonGetter("courses")
        public ArrayList<Course> getCourses() {
            return courses;
        }
        @JsonSetter("courses")
        public void setCourses(ArrayList<Course> courses) {
            this.courses = courses;
        }

        private boolean enabled;
        @JsonGetter("enabled")
        public boolean isEnabled() {
            return enabled;
        }
        @JsonSetter("enabled")
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        public Subj(String subjName) {
            this.subjName = subjName;
            courses = new ArrayList<Course>(DEFAULT_COURSES_CAPACITY);
            enabled = true;
        }

        public Subj(String subjName, ArrayList<Course> courses) {
            this.subjName = subjName;
            this.courses = courses;
            enabled = true;
        }
        public Subj(String subjName, ArrayList<Course> courses, boolean enabled) {
            this.subjName = subjName;
            this.courses = courses;
            this.enabled = enabled;
        }
    }
    @JsonIgnore
    public static final int DEFAULT_MAX_DAY = 5;
    @JsonIgnore
    public static final int DEFAULT_MAX_TIME = 12;
    @JsonIgnore
    public static final int MAX_MAX_DAY = 31;
    @JsonIgnore
    public static final int MAX_MAX_TIME = 48;
    @JsonIgnore
    public static final int DEFAULT_SUBJS_CAPACITY = 100;

    public final int MAX_DAY;
    public final int MAX_TIME;

    private ArrayList<Subj> subjs;
    @JsonGetter("subjs")
    public ArrayList<Subj> getSubjs() {
        return subjs;
    }
    @JsonSetter("subjs")
    public void setSubjs(ArrayList<Subj> subjs) {
        this.subjs = subjs;
    }

    public InputPlan() {
        this.MAX_DAY = DEFAULT_MAX_DAY;
        this.MAX_TIME = DEFAULT_MAX_TIME;
        subjs = new ArrayList<Subj>(DEFAULT_SUBJS_CAPACITY);
    }
    public InputPlan(int MAX_DAY, int MAX_TIME) {
        if(MAX_DAY <= 0 || MAX_TIME <= 0 || MAX_DAY > MAX_MAX_DAY || MAX_TIME > MAX_MAX_TIME) {
            MAX_DAY = DEFAULT_MAX_DAY;
            MAX_TIME = DEFAULT_MAX_TIME;
        }
        this.MAX_DAY = MAX_DAY;
        this.MAX_TIME = MAX_TIME;
        subjs = new ArrayList<Subj>(DEFAULT_SUBJS_CAPACITY);
    }
    public InputPlan(int MAX_DAY, int MAX_TIME, ArrayList<Subj> subjs) {
        if(MAX_DAY <= 0 || MAX_TIME <= 0 || MAX_DAY > MAX_MAX_DAY || MAX_TIME > MAX_MAX_TIME) {
            MAX_DAY = DEFAULT_MAX_DAY;
            MAX_TIME = DEFAULT_MAX_TIME;
        }
        this.MAX_DAY = MAX_DAY;
        this.MAX_TIME = MAX_TIME;
        this.subjs = subjs;
    }
}
