import java.util.Random;
import java.util.Scanner;

public class NumberMastermind {
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();

    public static void main(String[] args) {
    	System.out.println("Welcome to Mastermind!");
        System.out.println("In this game, your goal is to guess a passcode.");
        System.out.println("An X represents a correct digit in the correct position");
        System.out.println("An O represesnts a correct digit in an incorrect position\n");
    	boolean playAgain;
        do {
            // Get user inputs
            int attempts = getIntInput("Enter the number of attempts: ");
            int possibleNumbers = getIntInput("Enter the range of possible numbers (e.g., 10 for 0-9): ");
            int passwordLength = getIntInput("Enter the length of the password: ");

            // Generate the password
            int[] password = generatePassword(possibleNumbers, passwordLength);

            boolean guessedCorrectly = playGame(attempts, possibleNumbers, passwordLength, password);
            
            if (guessedCorrectly) {
                System.out.println("Congratulations! You've guessed the password!");
            } else {
                System.out.println("Sorry, you've used all your attempts. The password was: " + arrayToString(password));
            }

            // Ask if the user wants to play again
            playAgain = getYesNoInput("Do you want to play again? (yes/no): ");
        } while (playAgain);

        System.out.println("Thanks for playing!");
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. " + prompt);
            scanner.next(); // Clear the invalid input
        }
        return scanner.nextInt();
    }

    private static boolean getYesNoInput(String prompt) {
        System.out.print(prompt);
        String response = scanner.next().trim().toLowerCase();
        return response.equals("yes") || response.equals("y");
    }

    private static int[] generatePassword(int possibleNumbers, int length) {
        int[] password = new int[length];
        for (int i = 0; i < length; i++) {
            password[i] = random.nextInt(possibleNumbers);
        }
        return password;
    }

    private static boolean playGame(int attempts, int possibleNumbers, int passwordLength, int[] password) {
        for (int i = 0; i < attempts; i++) {
            System.out.print("Attempt " + (i + 1) + ": ");
            int[] guess = getGuess(passwordLength, possibleNumbers);
            if (isCorrectPassword(guess, password)) {
                return true;
            } else {
                provideFeedback(guess, password);
            }
        }
        return false;
    }

    private static int[] getGuess(int length, int possibleNumbers) {
        int[] guess = new int[length];
        System.out.print("Enter your guess (space-separated numbers): ");
        for (int i = 0; i < length; i++) {
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Enter your guess (space-separated numbers): ");
                scanner.next(); // Clear the invalid input
            }
            guess[i] = scanner.nextInt();
            if (guess[i] < 0 || guess[i] >= possibleNumbers) {
                System.out.println("Number out of range. Try again.");
                i--;
            }
        }
        return guess;
    }

    private static boolean isCorrectPassword(int[] guess, int[] password) {
        for (int i = 0; i < password.length; i++) {
            if (guess[i] != password[i]) {
                return false;
            }
        }
        return true;
    }

    private static void provideFeedback(int[] guess, int[] password) {
        int length = guess.length;
        char[] feedback = new char[length];
        boolean[] passwordChecked = new boolean[length];
        boolean[] guessChecked = new boolean[length];

        // Initialize feedback with '-'
        for (int i = 0; i < length; i++) {
            feedback[i] = '-';
        }

        // Check for correct positions
        for (int i = 0; i < length; i++) {
            if (guess[i] == password[i]) {
                feedback[i] = 'X';
                passwordChecked[i] = true;
                guessChecked[i] = true;
            }
        }

        // Check for correct numbers in wrong positions
        for (int i = 0; i < length; i++) {
            if (!guessChecked[i]) {
                for (int j = 0; j < length; j++) {
                    if (!passwordChecked[j] && guess[i] == password[j]) {
                        feedback[i] = 'O';
                        passwordChecked[j] = true;
                        break;
                    }
                }
            }
        }

        // Print feedback in order: X, O, -
        StringBuilder feedbackOutput = new StringBuilder();
        for (char f : feedback) {
            feedbackOutput.append(f).append(" ");
        }
        System.out.println("Feedback: " + feedbackOutput.toString().trim());
    }

    private static String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int num : array) {
            sb.append(num).append(" ");
        }
        return sb.toString().trim();
    }
}
