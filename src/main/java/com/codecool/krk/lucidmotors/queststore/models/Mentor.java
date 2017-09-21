package com.codecool.krk.lucidmotors.queststore.models;

import com.codecool.krk.lucidmotors.queststore.dao.MentorDao;
import com.codecool.krk.lucidmotors.queststore.dao.ClassDao;

public class Mentor extends User {
    private SchoolClass class_;
    private MentorDao mentorDao = new MentorDao(new ClassDao());

    public Mentor(String name, String login, String password, String email, SchoolClass class_) {
        super(name, login, password, email);
        this.class_ = class_;
        mentorDao.save(this);
    }

    public Mentor(String name, String login, String password, String email,SchoolClass class_, Integer id) {
        super(name, login, password, email, id);
        this.class_ = class_;
    }

    public SchoolClass getClas() {
        return this.class_;
    }

    public String getMentorSaveString() {
        return String.format("%d|%s|%s|%s|%s|%d%n", this.getId(), this.getName(), this.getLogin(), this.getPassword(), this.getEmail(), this.getClas().getId());
    }

    public String getMentorData() {
        return String.format("id: %d. %s %s class name: %s%n",
                             this.getId(),
                             this.getName(),
                             this.getEmail(),
                             this.getClas().getName());
    }

    public String toString() {
        return String.format("id: %d. %s%n", this.getId(), this.getName());
    }
}
