package com.codecool.krk.lucidmotors.queststore.dao;

import com.codecool.krk.lucidmotors.queststore.exceptions.DaoException;
import com.codecool.krk.lucidmotors.queststore.models.QuestCategory;
import com.codecool.krk.lucidmotors.queststore.models.AvailableQuest;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AvailableQuestDao {

    private static AvailableQuestDao dao;
    private final Connection connection;
    private PreparedStatement stmt = null;
    private QuestCategoryDao questCategoryDao = QuestCategoryDao.getDao();

    private AvailableQuestDao() throws DaoException {

        this.connection = DatabaseConnection.getConnection();
    }

    public static AvailableQuestDao getDao() throws DaoException {
        if (dao == null) {

            synchronized (AvailableQuestDao.class) {

                if(dao == null) {
                    dao = new AvailableQuestDao();
                }
            }
        }

        return dao;
    }

    public AvailableQuest getQuest(Integer id) throws DaoException {

        AvailableQuest availableQuest = null;
        String sqlQuery = "SELECT * FROM available_quests WHERE id = ?;";

        try {
            stmt = connection.prepareStatement(sqlQuery);
            stmt.setInt(1, id);

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                String name = result.getString("name");
                BigInteger value = new BigInteger(result.getString("value"));
                Integer categoryId = result.getInt("category_id");
                String description = result.getString("description");

                QuestCategory questCategory = questCategoryDao.getQuestCategory(categoryId);

                availableQuest = new AvailableQuest(name, questCategory, description, value, id);
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            throw new DaoException(this.getClass().getName() + " class caused a problem!");
        }

        return availableQuest;
    }

    public void updateQuest(AvailableQuest availableQuest) throws DaoException {

        String name = availableQuest.getName();
        BigInteger value = availableQuest.getValue();
        Integer categoryId = availableQuest.getQuestCategory().getId();
        String description = availableQuest.getDescription();
        Integer questId = availableQuest.getId();

        String sqlQuery = "UPDATE available_quests "
                + "SET name = ?, value = ?, category_id = ?, description = ? "
                + "WHERE id = ?;";

        try {
            stmt = connection.prepareStatement(sqlQuery);

            stmt.setString(1, name);
            stmt.setString(2, value.toString());
            stmt.setInt(3, categoryId);
            stmt.setString(4, description);
            stmt.setInt(5, questId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(this.getClass().getName() + " class caused a problem!");
        }

    }

    public List<AvailableQuest> getAllQuests() throws DaoException {

        List<AvailableQuest> availableQuests = new ArrayList<>();
        String sqlQuery = "SELECT * FROM available_quests;";

        try {
            stmt = connection.prepareStatement(sqlQuery);

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                String name = result.getString("name");
                BigInteger value = new BigInteger(result.getString("value"));
                Integer categoryId = result.getInt("category_id");
                String description = result.getString("description");
                Integer questId = result.getInt("id");

                QuestCategory questCategory = questCategoryDao.getQuestCategory(categoryId);

                AvailableQuest availableQuest = new AvailableQuest(name, questCategory, description, value, questId);
                availableQuests.add(availableQuest);
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            throw new DaoException(this.getClass().getName() + " class caused a problem!");
        }

        return availableQuests;
    }

    public void save(AvailableQuest availableQuest) throws DaoException {

        String name = availableQuest.getName();
        BigInteger value = availableQuest.getValue();
        Integer categoryId = availableQuest.getQuestCategory().getId();
        String description = availableQuest.getDescription();
        Integer questId = availableQuest.getId();

        String sqlQuery = "INSERT INTO available_quests "
                + "(name, description, value, category_id) "
                + "VALUES (?, ?, ?, ?);";

        try {
            stmt = connection.prepareStatement(sqlQuery);

            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setString(3, value.toString());
            stmt.setInt(4, categoryId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(this.getClass().getName() + " class caused a problem!");
        }

    }

    public AvailableQuest getAvailableQuestByName(String name) throws DaoException {
        String sqlQuery = "SELECT * FROM available_quests WHERE name LIKE ?;";

        try {
            stmt = connection.prepareStatement(sqlQuery);
            stmt.setString(1, name);

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                String foundName = result.getString("name");
                BigInteger value = new BigInteger(result.getString("value"));
                Integer categoryId = result.getInt("category_id");
                String description = result.getString("description");
                Integer questId = result.getInt("id");

                QuestCategory questCategory = questCategoryDao.getQuestCategory(categoryId);

                return new AvailableQuest(foundName, questCategory, description, value, questId);
            }

            result.close();
            stmt.close();
        } catch (SQLException e) {
            throw new DaoException(this.getClass().getName() + " class caused a problem!");
        }

        return null;
    }

}
