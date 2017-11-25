package com.codecool.krk.lucidmotors.queststore.models;

import java.util.ArrayList;
import java.util.List;

import com.codecool.krk.lucidmotors.queststore.dao.AchievedQuestDao;
import com.codecool.krk.lucidmotors.queststore.dao.ArtifactOwnersDao;
import com.codecool.krk.lucidmotors.queststore.dao.StudentDao;
import com.codecool.krk.lucidmotors.queststore.dao.ClassDao;
import com.codecool.krk.lucidmotors.queststore.exceptions.DaoException;

public class Student extends User {

    private final List<BoughtArtifact> ownedArtifacts;
    private final List<AchievedQuest> achievedQuests;
    private final SchoolClass class_;
    private final StudentDao studentDao = StudentDao.getDao();
    private Integer earnedCoins;
    private Integer possesedCoins;

    public Student(String name, String login, String password, String email, SchoolClass class_) throws DaoException {

        super(name, login, password, email);
        this.earnedCoins = 0;
        this.possesedCoins = 0;
        this.ownedArtifacts = new ArrayList<>();
        this.achievedQuests = new ArrayList<>();
        this.class_ = class_;
    }

	public Student(String name, String login, String password, String email, SchoolClass class_, 
                 Integer id, Integer earnedCoins, Integer possesedCoins) throws DaoException {
  
      super(name, login, password, email, id);
    	this.earnedCoins = earnedCoins;
	  	this.possesedCoins = possesedCoins;
	  	this.ownedArtifacts = ArtifactOwnersDao.getDao().getArtifacts(this);
	  	this.achievedQuests = AchievedQuestDao.getDao().getAllQuestsByStudent(this);
	  	this.class_ = class_;

    }



    public Integer getEarnedCoins() {
        return this.earnedCoins;
    }

    public void setPossesedCoins(Integer coins) {
        this.possesedCoins = coins;
    }

    public Integer getPossesedCoins() {
        return this.possesedCoins;
    }

    public List<BoughtArtifact> getOwnedArtifacts() {
        return this.ownedArtifacts;
    }

    public List<AchievedQuest> getAchievedQuests() {
        return this.achievedQuests;
    }

    public Integer getLevel(ExperienceLevels experienceLevels) {
        return experienceLevels.computeStudentLevel(this.earnedCoins);
    }


    public SchoolClass getClas() {
        return this.class_;
    }

    public String getStudentSaveString() {
        return String.format("%d|%s|%s|%s|%s|%d", this.getId(), this.getName(), this.getLogin(), this.getPassword(), this.getEmail(), this.getClas().getId());
    }

    public String toString() {
        return String.format("id: %d. %s", this.getId(), this.getName());
    }

    public void addCoins(Integer ammount) {
        this.earnedCoins += ammount;
        this.possesedCoins += ammount;
    }

    public void returnCoins(Integer amount) {
        this.possesedCoins += amount;
    }

    public void save() throws DaoException {
        studentDao.save(this);
    }

    public void update() throws DaoException {
        studentDao.update(this);
    }

}
