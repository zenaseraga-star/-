package ru.itmo.ArsikAndEva.ui.dialog;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.ui.alert.AlertService;
import ru.itmo.ArsikAndEva.users.SessionManager;
import ru.itmo.ArsikAndEva.users.User;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ru.itmo.ArsikAndEva.users.UserManager;

public class UserDialog extends Stage {
    private final UserManager userManager;
    private final SessionManager sessionManager;

    public UserDialog(UserManager userManager, SessionManager sessionManager) {
        this.userManager = userManager;
        this.sessionManager = sessionManager;
        setTitle("Регистрация/вход");
        Scene scene = new Scene(create(), 350, 350);
        setScene(scene);
    }

    private VBox create() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        Label titleLabel = new Label("Вход в систему");
        TextField loginField = new TextField();
        loginField.setPromptText("Логин");
        loginField.setPrefWidth(200);
        TextField passwordField = new TextField();
        passwordField.setPrefWidth(200);
        passwordField.setPromptText("Пароль");
        TextField passwordField1 = new TextField();
        passwordField1.setPrefWidth(200);
        passwordField1.setPromptText("Пароль");
        passwordField1.setVisible(false);
        Button actionButton = new Button("Войти");
        Button switchButton = new Button("Зарегистрироваться");
        boolean[] isLoginMode = {true};
        switchButton.setOnAction(e -> {
            isLoginMode[0] = !isLoginMode[0];
            if (isLoginMode[0]) {
                titleLabel.setText("Вход в систему");
                actionButton.setText("Войти");
                switchButton.setText("Зарегистрироваться");
                passwordField1.setVisible(false);
                passwordField1.clear();

            } else {
                titleLabel.setText("Регистрация нового пользователя");
                actionButton.setText("Зарегистрироваться");
                switchButton.setText("Уже есть аккаунт? Войти");
                passwordField1.setVisible(true);
            }
            loginField.clear();
            passwordField.clear();
            passwordField1.clear();
        });
        Label messageLabel = new Label();
        actionButton.setOnAction(e -> {
            String login = loginField.getText().trim();
            String password = passwordField.getText();


            if (login.isEmpty() || password.isEmpty()) {
                AlertService.showError("Ошибка", "Заполните все поля");
                return;
            }
            try {
                if (isLoginMode[0]) {
                    User user = userManager.login(login, password);
                    sessionManager.setCurrentUser(user);
                    close();
                } else {
                    String confirm = passwordField1.getText();
                    if (!password.equals(confirm)) {
                        AlertService.showError("Ошибка", "Пароли не совпадают");
                        return;
                    } else {
                        User user = userManager.register(login, password);
                        sessionManager.setCurrentUser(user);
                        close();
                    }
                }
            } catch (ValidationException ex) {
                AlertService.showError("Ошибка", ex.getMessage());
            }
        });
        root.getChildren().addAll(
                titleLabel,
                loginField,
                passwordField,
                passwordField1,
                actionButton,
                switchButton,
                messageLabel
        );

        return root; }
}