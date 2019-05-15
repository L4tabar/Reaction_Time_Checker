package controller;

import com.google.inject.Guice;
import com.google.inject.Injector;
import guice.PersistenceModule;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Shapes;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import model.TimerClass;
import model.Users;
import dao.UsersDao;

import javax.persistence.EntityManager;


public class Game {

    public static String felhasznalo;
    public static double[] time = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    public static double[] time_tmp = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    public static StackPane root = new StackPane();
    public static Scene scene = new Scene(root, 1200, 600);
    public static boolean same;

    public static EntityManager em;


    public void start(Stage primaryStage) throws InterruptedException {

        AtomicBoolean same = new AtomicBoolean(false);
        Random rand = new Random();


        TimerClass tc = new TimerClass();
        tc.startTimer();
        AtomicInteger i = new AtomicInteger(0);


        Text username = new Text();
        username.setTranslateX(-500);
        username.setTranslateY(-280);
        username.setStyle("-fx-font: 24 arial;");
        root.getChildren().add(username);
        username.setText(felhasznalo);


        Shapes shapes = new Shapes();
        ImageView iv1 = new ImageView();
        ImageView iv2 = new ImageView();
        root.getChildren().add(iv1);
        root.getChildren().add(iv2);


        Thread.sleep(2000);


        same.set(getScene(iv1, iv2, shapes, random(), random()));
        scene.setOnKeyPressed(event -> {

            if (i.get() == 10) {

                double reactT = (time[0] + time[1] + time[2] + time[3] + time[4] + time[5] + time[6] + time[7] + time[8] + time[9]) / 10;

                Text reactTime = new Text();
                reactTime.setTranslateX(-400);
                reactTime.setTranslateY(-250);
                reactTime.setStyle("-fx-font: 24 arial;");
                root.getChildren().add(reactTime);
                reactTime.setText(String.valueOf(reactT));

                addPlayer(username.getText(), reactT);
                System.out.println("A te reakcióidőd: " + reactT);
                i.incrementAndGet();

            }
            if(event.getCode() == KeyCode.F) {

                if(!same.get()) {
                    if (i.get() == 0) {

                        time[i.get()] = tc.getSecondsPassed();
                        time_tmp[i.get()] = tc.getSecondsPassed();
                        System.out.println("Az első nem volt egyforma: ");

                    }

                    else {

                        time[i.get()] = tc.getSecondsPassed() - time_tmp[i.get()-1];
                        time_tmp[i.get()] = tc.getSecondsPassed();
                        System.out.println("Nem volt egyforma: ");

                    }
                }

                if(same.get()) {

                    if (i.get() == 0) {
                        time[i.get()] = tc.getSecondsPassed() + 2;
                        time_tmp[i.get()] = tc.getSecondsPassed();
                        System.out.println("Az első helytelen: ");

                    }
                    else {

                        time[i.get()] = tc.getSecondsPassed() - time_tmp[i.get()-1] + 2;
                        time_tmp[i.get()] = tc.getSecondsPassed();
                        System.out.println("Helytelen: ");

                    }
                }
            }
            if(event.getCode() == KeyCode.J) {

                if(same.get()) {

                    if (i.get() == 0) {

                        time[i.get()] = tc.getSecondsPassed();
                        time_tmp[i.get()] = tc.getSecondsPassed();
                        System.out.println("Az első egyforma: ");

                    }
                    else {

                        time[i.get()] = tc.getSecondsPassed() - time_tmp[i.get()-1];
                        time_tmp[i.get()] = tc.getSecondsPassed();
                        System.out.println("Egyforma: ");

                    }

                }

                if(!same.get()) {

                    if (i.get() == 0) {

                        time[i.get()] = tc.getSecondsPassed() + 2;
                        time_tmp[i.get()] = tc.getSecondsPassed();
                        System.out.println("Az első helytelen válasz: ");

                    }
                    else {

                        time[i.get()] = tc.getSecondsPassed() - time_tmp[i.get()-1] + 2;
                        time_tmp[i.get()] = tc.getSecondsPassed();
                        System.out.println("Helytelen válasz: ");

                    }
                }
            }

            if(event.getCode() == KeyCode.Q) {
                root.getChildren().clear();
                primaryStage.close();
            }

            same.set(getScene(iv1, iv2, shapes, random(), random()));
            i.incrementAndGet();

        });

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static boolean getScene(ImageView iv1, ImageView iv2, Shapes shapes, int rand1, int rand2) {

        iv1.setImage(shapes.getImage(rand1));
        iv2.setImage(shapes.getImage(rand2));

        iv1.setFitHeight(300);
        iv1.setFitWidth(300);
        iv1.setTranslateX(-300);

        iv2.setFitHeight(300);
        iv2.setFitWidth(300);
        iv2.setTranslateX(300);

        same = iv1.getImage().equals(iv2.getImage());
        return same;
    }

    static int random(){
        Random random = new Random();
        return random.nextInt(3);
    }

    public static void addPlayer(String name, double score) {

        Injector injector = Guice.createInjector(new PersistenceModule("jpa-persistence-unit-1"));
        UsersDao usersDao = injector.getInstance(UsersDao.class);
        Users player = new Users(name, score);
        usersDao.persist(player);

    }


}
