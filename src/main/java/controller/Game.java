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


        Text username = new Text();
        username.setTranslateX(-570);
        username.setTranslateY(-280);
        username.setStyle("-fx-font: 24 arial;");
        root.getChildren().add(username);
        username.setText(felhasznalo);

        AtomicBoolean same = new AtomicBoolean(false);
        Random rand = new Random();


        Shapes shapes = new Shapes();
        ImageView iv1 = new ImageView();
        ImageView iv2 = new ImageView();
        root.getChildren().add(iv1);
        root.getChildren().add(iv2);
        TimerClass tc = new TimerClass();
        tc.startTimer();
        AtomicInteger i = new AtomicInteger(0);

        Thread.sleep(2000);

        same.set(getScene(iv1, iv2, shapes, rand));

        scene.setOnKeyPressed(event -> {
            if (i.get() == 10) {
                addPlayer(username.getText(), (time[0] + time[1] + time[2] + time[3] + time[4] + time[5] + time[6] + time[7] + time[8] + time[9]) / 10);

                System.out.println("A te reakcióidőd: " + (time[0] + time[1] + time[2] + time[3] + time[4] + time[5] + time[6] + time[7] + time[8] + time[9]) / 10);
                System.exit(0);
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


            same.set(getScene(iv1, iv2, shapes, rand));

            i.incrementAndGet();



        });




        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static boolean getScene(ImageView iv1, ImageView iv2, Shapes shapes, Random rand) {

        iv1.setImage(shapes.getImage(rand.nextInt(3)));
        iv2.setImage(shapes.getImage(rand.nextInt(3)));

        iv1.setFitHeight(300);
        iv1.setFitWidth(300);
        iv1.setTranslateX(-300);


        iv2.setFitHeight(300);
        iv2.setFitWidth(300);
        iv2.setTranslateX(300);

        same = iv1.getImage().equals(iv2.getImage());

        return same;
    }

    public static void addPlayer(String name, double score) {
        Injector injector = Guice.createInjector(new PersistenceModule("jpa-persistence-unit-1"));
        UsersDao usersDao = injector.getInstance(UsersDao.class);
        Users player = new Users(name, score);
        usersDao.persist(player);
    }

/*
    public static void moveOnKeyPress (int i, TimerClass tc) {

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case F :
                        if(same)
                            time[i] = tc.getSecondsPassed() + 2;
                        else
                            time[i] = tc.getSecondsPassed();
                        break;
                    case J :
                        if(same)
                            time[i] = tc.getSecondsPassed();
                    else
                            time[i] = tc.getSecondsPassed() + 2;
                        break;

                }
            }
        });
    }*/

}
