package bfmaker;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.LinkedList;

public class OutputPlan {
    public static class Table {
        public static class Course {
            private String subjName;
            public String getSubjName() {
                return subjName;
            }
            public void setSubjName(String subjName) {
                this.subjName = subjName;
            }
            private String professor;
            public String getProfessor() {
                return professor;
            }
            public void setProfessor(String professor) {
                this.professor = professor;
            }
            private LinkedList<Cord> courseTimes;
            public LinkedList<Cord> getCourseTimes() {
                return courseTimes;
            }
            public void setCourseTimes(LinkedList<Cord> courseTimes) {
                this.courseTimes = courseTimes;
            }
            public Course() {
                this.subjName = null;
                this.professor = null;
                courseTimes = new LinkedList<Cord>();
            }
            public Course(String subjName, String professor) {
                this.subjName = subjName;
                this.professor = professor;
                courseTimes = new LinkedList<Cord>();
            }
            public Course(String subjName, String professor, LinkedList<Cord> courseTimes) {
                this.subjName = subjName;
                this.professor = professor;
                this.courseTimes = courseTimes;
            }
        }
        private LinkedList<Course> courses;
        public LinkedList<Course> getCourses() {
            return courses;
        }
        public void setCourses(LinkedList<Course> courses) {
            this.courses = courses;
        }
        private boolean marked;
        @JsonGetter("marked")
        public boolean isMarked() {
            return marked;
        }
        @JsonSetter("marked")
        public void setMarked(boolean marked) {
            this.marked = marked;
        }
        public Table() {
            courses = new LinkedList<Course>();
        }
        public Table(LinkedList<Course> courses) {
            this.courses = courses;
        }
        public Table(LinkedList<Course> courses, boolean marked) {
            this.courses = courses;
            this.marked = marked;
        }
    }
    public final int MAX_DAY;
    public final int MAX_TIME;
    private LinkedList<Table> tables;
    public LinkedList<Table> getTables() {
        return tables;
    }
    public void setTables(LinkedList<Table> tables) {
        this.tables = tables;
    }
    public void clearAll() {
        tables.clear();
    }
    public OutputPlan(int MAX_DAY, int MAX_TIME) {
        this.MAX_DAY = MAX_DAY;
        this.MAX_TIME = MAX_TIME;
        tables = new LinkedList<Table>();
    }
    public OutputPlan(int MAX_DAY, int MAX_TIME, LinkedList<Table> tables) {
        this.MAX_DAY = MAX_DAY;
        this.MAX_TIME = MAX_TIME;
        this.tables = tables;
    }
}
