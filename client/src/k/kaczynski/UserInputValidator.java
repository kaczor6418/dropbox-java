package k.kaczynski;

import java.util.Scanner;
import java.util.regex.Pattern;

class UserInputValidator {

    UserInputValidator() {}

    private  String isValidInput(Pattern validationRule) {
        String userInput;
        Scanner inputScanner = new Scanner(System.in);
        userInput = inputScanner.nextLine();
        while (!validationRule.matcher(userInput).matches()) {
            System.out.println("Wrong input. Try again: ");
            userInput = inputScanner.nextLine();
        }
        return userInput;
    }

    String isValidUserName(String userName) {
        Pattern isUserName = Pattern.compile("[a-zA-Z0-9]+");
        if (!isUserName.matcher(userName).matches()) {
            return this.getUserName();
        }
        return userName;
    }

    String isValidDirectoryPath(String dirPath) {
        Pattern isDirPath = Pattern.compile("^([a-zA-Z0-9]+//)([a-zA-Z0-9/]*)");
        if (!isDirPath.matcher(dirPath).matches()) {
            return this.getDirectoryPath();
        }
        return dirPath;
    }

    String getUserName() {
        Pattern isUserName = Pattern.compile("[a-zA-Z0-9]+");
        System.out.println("Enter your user name: ");
        return this.isValidInput(isUserName);
    }

    String getDirectoryPath() {
        Pattern isDirPath = Pattern.compile("^([a-zA-Z0-9]+//)([a-zA-Z0-9/]*)");
        System.out.println("Enter directory path: ");
        return this.isValidInput(isDirPath);
    }
}
