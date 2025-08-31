package Agent;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * StartStage represents the login/setup screen for an Agent.
 * It collects the agent's name and initial balance and initializes their account.
 */
public class StartStage extends VBox {
    Agent agent;

    /**
     * Constructs the startup screen and links the agent instance.
     * @param agent The agent to initialize with name and balance.
     */
    public StartStage(Agent agent) {
        this.agent = agent;
        startSetup();
    }

    /**
     * Sets up the UI layout for name and balance input and handles validation.
     */
    public void startSetup() {
        setSpacing(20);
        setStyle("-fx-padding: 40 60 40 60; -fx-background-color: #f5f5f5; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10,0,0,2);");

        Label prompt = new Label("Create an Account");
        prompt.setStyle("-fx-font-size: 20px; -fx-font-family: 'Futura'; -fx-font-weight: bold;");
        addToPane(prompt);

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        nameField.setPrefHeight(30);
        nameField.setFocusTraversable(false);

        TextField balanceField = new TextField();
        balanceField.setPromptText("Starting Balance");
        balanceField.setPrefHeight(30);
        balanceField.setFocusTraversable(false);

        Button submit = new Button("Submit");
        submit.setPrefSize(200, 30);
        submit.setStyle("-fx-background-color: #e07a55; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        submit.setDisable(true);

        // Form validation: enable submit only when both fields are non-empty
        nameField.textProperty().addListener((obs, oldText, newText) -> {
            submit.setDisable(balanceField.getText().isEmpty() || newText.trim().isEmpty());
        });

        balanceField.textProperty().addListener((obs, oldText, newText) -> {
            submit.setDisable(nameField.getText().trim().isEmpty() || newText.trim().isEmpty());
        });

        // On submit: save name and balance to agent and switch scenes
        submit.setOnAction(event -> {
            if (!balanceField.getText().isEmpty() && !nameField.getText().matches("^[0-9]+$")) {
                agent.setName(nameField.getText());
                agent.setTotalBalance(Double.parseDouble(balanceField.getText()));
                agent.setAvailableBalance(Double.parseDouble(balanceField.getText()));
                AgentMain.changeScenes();
            }
        });

        addToPane(nameField);
        addToPane(balanceField);
        addToPane(submit);
    }

    /**
     * Utility function to add a JavaFX node to the layout.
     * @param node The UI element to add to the vertical stack.
     */
    private void addToPane(Node node) {
        getChildren().add(node);
    }
}