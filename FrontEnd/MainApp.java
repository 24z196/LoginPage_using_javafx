import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {
    TextField loginEmail;
    PasswordField loginPass;
    TextField signupUser;
    PasswordField signupPass;
    @Override
    public void start(Stage st){
        st.setScene(login(st));
        st.setWidth(1150);
        st.setHeight(700);
        st.setTitle("Login Page");
        st.show();
    }
    private Scene login(Stage st){
        VBox c = card();
        Label t = title("Login");
        loginEmail = field("Email");
        loginPass = pass("Password");
        Button b = primary("Login");
        b.setOnAction(e -> {
            if(!loginEmail.getText().contains("@")){
                new Alert(Alert.AlertType.ERROR,"Invalid Username. Must contain '@'").showAndWait();
                return;
            }
            new Alert(Alert.AlertType.INFORMATION,"Login Successful").showAndWait();
            loginEmail.clear();
            loginPass.clear();
        });
        c.getChildren().add(new VBox(15, t, loginEmail, loginPass, b));
        return new Scene(base(st, c));
    }
    private Scene signup(Stage st){
        VBox c = card();
        Label t = title("Create Account");
        signupUser = field("Username");
        signupPass = pass("Choose Password");
        ProgressBar bar = new ProgressBar(0);
        Label s = new Label("Strength: Weak");
        Button b = primary("Sign Up");
        b.setDisable(true);
        signupPass.textProperty().addListener((a, o, x) -> {
            int sc = strength(x);
            bar.setProgress(sc / 4.0);
            if(sc >= 3){
                s.setText("Strength: Strong");
                if(signupUser.getText().contains("@")) b.setDisable(false);
            } else {
                s.setText("Strength: Weak");
                b.setDisable(true);
            }
        });
        signupUser.textProperty().addListener((a, o, x) -> {
            if(!x.contains("@")) b.setDisable(true);
            else if(strength(signupPass.getText()) >= 3) b.setDisable(false);
        });
        b.setOnAction(e -> {
            if(!signupUser.getText().contains("@")){
                new Alert(Alert.AlertType.ERROR,"Invalid Username. Must contain '@'").showAndWait();
                return;
            }
            new Alert(Alert.AlertType.INFORMATION,"Account Created Successfully").showAndWait();
            signupUser.clear();
            signupPass.clear();
            bar.setProgress(0);
            s.setText("Strength: Weak");
            b.setDisable(true);
        });
        c.getChildren().add(new VBox(15, t, signupUser, signupPass, bar, s, b));
        return new Scene(base(st, c));
    }
    private Scene help(Stage st){
        VBox c = card();
        Label t = title("Help & Instructions");
        Label tx = new Label(
            "LOGIN\n" +
            "• Username must contain '@'.\n\n" +
            "SIGNUP\n" +
            "• Username must contain '@'.\n" +
            "• Password must be STRONG.\n\n" +
            "Rules to set Strong Password\n" +
            "• 8+ characters\n" +
            "• At least one uppercase character\n" +
            "• At least one number\n" +
            "• At least one symbol\n"
        );
        tx.setWrapText(true);
        tx.setStyle("-fx-font-size:16px;-fx-font-weight:500;");
        c.getChildren().add(new VBox(12, t, tx));
        return new Scene(base(st, c));
    }
    private BorderPane base(Stage st, VBox card){
        VBox side = new VBox(18);
        side.setPadding(new Insets(30));
        side.setPrefWidth(180);
        side.setStyle("-fx-background-color:#1E4BFF;");
        side.getChildren().addAll(
            menu("Login", e -> st.setScene(login(st))),
            menu("Signup", e -> st.setScene(signup(st))),
            menu("Help", e -> st.setScene(help(st))),
            menu("Exit", e -> System.exit(0))
        );
        VBox center = new VBox(card);
        center.setAlignment(Pos.CENTER);
        center.setStyle("-fx-background-color:#F3F4F6;");
        center.setPadding(new Insets(20));
        BorderPane bp = new BorderPane();
        bp.setLeft(side);
        bp.setCenter(center);
        return bp;
    }
    private VBox card(){
        VBox v = new VBox(20);
        v.setPadding(new Insets(30));
        v.setMaxWidth(430);
        v.setMinHeight(450);
        v.setStyle("-fx-background:white;-fx-background-radius:14;-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.18),20,0,0,6);");
        return v;
    }
    private Label title(String t){
        Label l = new Label(t);
        l.setStyle("-fx-font-size:24px;-fx-font-weight:bold;");
        return l;
    }
    private TextField field(String p){
        TextField t = new TextField();
        t.setPromptText(p);
        return t;
    }
    private PasswordField pass(String p){
        PasswordField t = new PasswordField();
        t.setPromptText(p);
        return t;
    }
    private Button primary(String t){
        Button b = new Button(t);
        b.setStyle("-fx-background-color:#1E4BFF;-fx-text-fill:white;-fx-font-size:16px;-fx-padding:10 20;-fx-background-radius:10;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color:#1538C4;-fx-text-fill:white;-fx-font-size:16px;-fx-padding:10 20;-fx-background-radius:10;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color:#1E4BFF;-fx-text-fill:white;-fx-font-size:16px;-fx-padding:10 20;-fx-background-radius:10;"));
        return b;
    }
    private Button menu(String t, javafx.event.EventHandler<?> h){
        Button b = new Button(t);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setStyle("-fx-background-color:transparent;-fx-text-fill:white;-fx-font-size:16px;-fx-padding:10 10;");
        b.setOnMouseEntered(v -> b.setStyle("-fx-background-color:#3C6BFF;-fx-text-fill:white;-fx-font-size:16px;-fx-padding:10 10;"));
        b.setOnMouseExited(v -> b.setStyle("-fx-background-color:transparent;-fx-text-fill:white;-fx-font-size:16px;-fx-padding:10 10;"));
        b.setOnAction((javafx.event.EventHandler<javafx.event.ActionEvent>)h);
        return b;
    }
    private int strength(String p){
        int s = 0;
        if(p.length() >= 8) s++;
        if(p.matches(".*[A-Z].*")) s++;
        if(p.matches(".*[0-9].*")) s++;
        if(p.matches(".*[@#$%^&+=!].*")) s++;
        return s;
    }
    public static void main(String[] a){
        launch();
    }
}
