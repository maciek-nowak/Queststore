package queststore.controllers;

import java.util.ArrayList;

import queststore.models.User;
import queststore.models.School;
import queststore.models.Class;
import queststore.models.Student;
import queststore.models.Mentor;
import queststore.views.UserInterface;
import queststore.exceptions.LoginInUseException;


public class MentorController {
    private UserInterface userInterface = new UserInterface();
    private Mentor user;
    private School school;

    public void startController(User user, School school) {

        this.user = (Mentor) user;
        this.school = school;
        String userChoice = "";

        while (!userChoice.equals("0")) {
            this.userInterface.printMentorMenu();
            userChoice = userInterface.inputs.getInput("What do you want to do: ");
            handleUserRequest(userChoice);
            userInterface.lockActualState();
            school.save();
        }
    }

    private void handleUserRequest(String userChoice) {

        switch(userChoice) {
            case "1":
                addStudent();
                break;

            case "2":
                addQuest();
                break;

            case "3":
                addQuestCategory();
                break;

            case "4":
                updateQuest();
                break;

            case "5":
                markBoughtArtifactsAsUsed();
                break;

            case "6":
                runMentorStoreController();

            case "0":
                break;

            default:
                handleNoSuchCommand();
        }
    }

    private void addStudent() {
        String[] questions = {"Name: ", "Login: ", "Password: ", "Email: "};
        String[] expectedTypes = {"String", "String", "String", "String"};

        ArrayList<String> basicUserData = userInterface.inputs.getValidatedInputs(questions, expectedTypes);
        String name = basicUserData.get(0);
        String login = basicUserData.get(1);
        String password = basicUserData.get(2);
        String email = basicUserData.get(3);
        Class choosenClass = chooseProperClass();

        try {
            this.school.addUser(new Student(name, login, password, email, choosenClass));
        } catch (LoginInUseException e) {
            userInterface.println(e.getMessage());
        }
    }

    private Class chooseProperClass() {
        ArrayList<Class> allClasses = this.school.getAllClasses();
        int userChoice;

        do {
            showAvailableClasses(allClasses);
            userChoice = getUserChoice() - 1;

        } while (userChoice > (allClasses.size() - 1) || userChoice < 0);

        return allClasses.get(userChoice);
    }

    private Integer getUserChoice() {
        String[] questions = {"Please choose class"};
        String[] expectedTypes = {"integer"};
        ArrayList<String> userInput = userInterface.inputs.getValidatedInputs(questions, expectedTypes);

        return Integer.parseInt(userInput.get(0));
    }

    private void showAvailableClasses(ArrayList<Class> allClasses) {
        userInterface.println("");

        for (int i = 0; i < allClasses.size(); i++) {
            String index = Integer.toString(i+1);
            System.out.println(index + ". " + allClasses.get(i));
        }

        userInterface.println("");
    }

    private void addQuest() {
        userInterface.println("Here you will create new quests");
    }

    private void addQuestCategory() {
        userInterface.println("Here you will create new quest category");
    }

    private void updateQuest() {
        userInterface.println("Here you will change quest details");
    }

    private void markBoughtArtifactsAsUsed() {
        userInterface.println("Here you will mark students artifacts as used");
    }

    private void runMentorStoreController() {
        new MentorStoreController().startController(this.user, this.school);
    }

    private void handleNoSuchCommand() {
        userInterface.println("Wrong command!");
    }

}
