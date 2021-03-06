package ru.digitaluniversity.entity;

import javax.persistence.*;
import java.util.List;

/**
 * The type Timetable.
 */
@Entity
@Table(name = "timetables")
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    private Teacher timetableTeacher;
    @ManyToOne
    private Group timetableGroup;
    @ManyToOne
    private Subject timetableSubject;
    @ManyToOne
    private Day timetableDay;
    @ManyToOne
    private Pair timetablePair;
    @OneToMany(mappedBy = "journalTimetable", fetch = FetchType.EAGER)
    private List<Journal> timetableJournal;

    public Timetable() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Teacher getTimetableTeacher() {
        return timetableTeacher;
    }

    public void setTimetableTeacher(Teacher timetableTeacher) {
        this.timetableTeacher = timetableTeacher;
    }

    public Group getTimetableGroup() {
        return timetableGroup;
    }

    public void setTimetableGroup(Group timetableGroup) {
        this.timetableGroup = timetableGroup;
    }

    public Subject getTimetableSubject() {
        return timetableSubject;
    }

    public void setTimetableSubject(Subject timetableSubject) {
        this.timetableSubject = timetableSubject;
    }

    public Day getTimetableDay() {
        return timetableDay;
    }

    public void setTimetableDay(Day timetableDay) {
        this.timetableDay = timetableDay;
    }

    public Pair getTimetablePair() {
        return timetablePair;
    }

    public void setTimetablePair(Pair timetablePair) {
        this.timetablePair = timetablePair;
    }

    public List<Journal> getTimetableJournal() {
        return timetableJournal;
    }

    public void setTimetableJournal(List<Journal> timetableJournal) {
        this.timetableJournal = timetableJournal;
    }
}
