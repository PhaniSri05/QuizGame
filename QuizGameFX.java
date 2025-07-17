import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class QuizGameFX extends Application {

    String[] questions = {
        "What is the capital of India?",
        "Which planet is called the Red Planet?",
        "Who invented Java?",
        "Which is the smallest continent?",
        "What is 10 + 5?"
    };

    String[][] options = {
        {"Delhi", "Mumbai", "Chennai", "Kolkata"},
        {"Earth", "Mars", "Venus", "Jupiter"},
        {"James Gosling", "Bill Gates", "Elon Musk", "Mark Zuckerberg"},
        {"Asia", "Europe", "Australia", "Africa"},
        {"10", "12", "15", "20"}
    };

    String[] answers = {"Delhi", "Mars", "James Gosling", "Australia", "15"};

    int index = 0;
    int score = 0;
    String playerName = "";

    Label questionLabel = new Label();
    Label finalMessageLabel = new Label(); // ✅ NEW label for message near title
    RadioButton option1 = new RadioButton();
    RadioButton option2 = new RadioButton();
    RadioButton option3 = new RadioButton();
    RadioButton option4 = new RadioButton();
    ToggleGroup optionsGroup = new ToggleGroup();
    Button nextButton = new Button("Next");
    Label scoreLabel = new Label();
    Button restartButton = new Button("Restart");
    ProgressBar progressBar = new ProgressBar(0);

    Stage mainStage;

    @Override
    public void start(Stage primaryStage) {
        mainStage = primaryStage;
        askPlayerName();
    }

    private void askPlayerName() {
        Label nameLabel = new Label("Enter your name:");
        TextField nameField = new TextField();
        Button startButton = new Button("Start Quiz");

        VBox nameLayout = new VBox(10, nameLabel, nameField, startButton);
        nameLayout.setAlignment(Pos.CENTER);
        nameLayout.setPadding(new Insets(20));
        Scene nameScene = new Scene(nameLayout, 400, 200);

        startButton.setOnAction(e -> {
            playerName = nameField.getText().trim();
            if (!playerName.isEmpty()) {
                showQuizScene();
            }
        });

        mainStage.setScene(nameScene);
        mainStage.setTitle("Quiz Game");
        mainStage.show();
    }

    private void showQuizScene() {
        option1 = new RadioButton();
        option2 = new RadioButton();
        option3 = new RadioButton();
        option4 = new RadioButton();

        option1.setToggleGroup(optionsGroup);
        option2.setToggleGroup(optionsGroup);
        option3.setToggleGroup(optionsGroup);
        option4.setToggleGroup(optionsGroup);

        // Styling for visibility
        questionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        finalMessageLabel.setStyle("-fx-text-fill: yellow; -fx-font-size: 14px; -fx-font-weight: bold;");
        option1.setStyle("-fx-text-fill: white;");
        option2.setStyle("-fx-text-fill: white;");
        option3.setStyle("-fx-text-fill: white;");
        option4.setStyle("-fx-text-fill: white;");
        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        progressBar.setStyle("-fx-accent: green;");
        progressBar.setPrefWidth(400);

        VBox optionsBox = new VBox(10, option1, option2, option3, option4);
        optionsBox.setAlignment(Pos.CENTER_LEFT);

        nextButton.setOnAction(e -> checkAnswer());
        restartButton.setOnAction(e -> restartQuiz());
        restartButton.setVisible(false);

        VBox contentBox = new VBox(10, progressBar, questionLabel, finalMessageLabel, optionsBox, nextButton, scoreLabel, restartButton);
        contentBox.setPadding(new Insets(20));
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setStyle("-fx-background-color: rgba(0,0,0,0.6);");

        Image bgImage = new Image("file:quiz.png", 600, 400, false, true);
        BackgroundImage bgi = new BackgroundImage(bgImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(100, 100, true, true, true, false));

        StackPane root = new StackPane();
        root.setBackground(new Background(bgi));
        root.getChildren().add(contentBox);

        Scene scene = new Scene(root, 600, 400);
        mainStage.setScene(scene);
        mainStage.setTitle("Quiz Game");

        showQuestion();
    }

    private void showQuestion() {
        if (index < questions.length) {
            questionLabel.setText((index + 1) + ". " + questions[index]);
            finalMessageLabel.setText(""); // clear any old message
            option1.setText(options[index][0]);
            option2.setText(options[index][1]);
            option3.setText(options[index][2]);
            option4.setText(options[index][3]);
            optionsGroup.selectToggle(null);
            progressBar.setProgress((double) index / questions.length);
        } else {
            showFinalResult();
        }
    }

    private void checkAnswer() {
        RadioButton selected = (RadioButton) optionsGroup.getSelectedToggle();
        if (selected != null && selected.getText().equals(answers[index])) {
            score++;
        }
        index++;
        showQuestion();
    }

    private void showFinalResult() {
        questionLabel.setText("🎉 Quiz Finished!");
        option1.setVisible(false);
        option2.setVisible(false);
        option3.setVisible(false);
        option4.setVisible(false);
        nextButton.setDisable(true);
        restartButton.setVisible(true);
        progressBar.setProgress(1.0);

        String message;
        if (score == 5) {
            message = "🌟 Excellent! You got all answers right!";
        } else if (score >= 3) {
            message = "👍 Good job, " + playerName + "! Keep it up!";
        } else if (score >= 1) {
            message = "🙂 You tried, " + playerName + "! Keep practicing!";
        } else {
            message = "😅 Oops! " + playerName + ", better luck next time!";
        }

        finalMessageLabel.setText(message); // 👈 appears under title now
        scoreLabel.setText("Your Score: " + score + "/" + questions.length);
    }

    private void restartQuiz() {
        index = 0;
        score = 0;
        option1.setVisible(true);
        option2.setVisible(true);
        option3.setVisible(true);
        option4.setVisible(true);
        nextButton.setDisable(false);
        restartButton.setVisible(false);
        scoreLabel.setText("");
        finalMessageLabel.setText("");
        progressBar.setProgress(0);
        showQuestion();
    }

    public static void main(String[] args) {
        launch();
    }
}
