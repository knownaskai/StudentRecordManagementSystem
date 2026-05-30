package com.srms;

import javafx.beans.property.*;

/**
 * Student - Model class with JavaFX properties for TableView binding.
 * year_level is stored as INT in the database, so it is IntegerProperty here.
 */
public class Student {

    private IntegerProperty id;
    private StringProperty  name;
    private StringProperty  course;
    private IntegerProperty yearLevel;  // matches INT column in database

    public Student(int id, String name, String course, int yearLevel) {
        this.id        = new SimpleIntegerProperty(id);
        this.name      = new SimpleStringProperty(name);
        this.course    = new SimpleStringProperty(course);
        this.yearLevel = new SimpleIntegerProperty(yearLevel);
    }

    // ── Getters ───────────────────────────────────────────────────────────
    public int    getId()        { return id.get(); }
    public String getName()      { return name.get(); }
    public String getCourse()    { return course.get(); }
    public int    getYearLevel() { return yearLevel.get(); }

    // ── Property accessors (needed by TableView) ──────────────────────────
    public IntegerProperty idProperty()        { return id; }
    public StringProperty  nameProperty()      { return name; }
    public StringProperty  courseProperty()    { return course; }
    public IntegerProperty yearLevelProperty() { return yearLevel; }
}
