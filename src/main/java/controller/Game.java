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
import lombok.extern.slf4j.Slf4j;
import javax.persistence.EntityManager;


/**
 * A játék kotrollere, ahol az egész játék zajlik.
 *
 */
@Slf4j
public class Game {

    public static String felhasznalo;
    public static double[] time = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    public static double[] time_tmp = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    public static StackPane root = new StackPane();
    public static Scene scene = new Scene(root, 1200, 600);
    public static boolean same;

    public static EntityManager em;


    public void start(Stage primaryStage) throws InterruptedException {

        AtomicBoolean same = new AtomicBoolean(false);;

        TimerClass tc = new TimerClass();
        tc.startTimer();
        AtomicInteger i = new AtomicInteger(0);


        Text username = new Text();
        username.setTranslateX(-500);
        username.setTranslateY(-280);
        username.setStyle("-fx-font: 24 arial;");
        root.getChildren().add(username);
        username.setText(felhasznalo);
        log.info("Elmentette a felhasználónevet");


        Shapes shapes = new Shapes();
        ImageView iv1 = new ImageView();
        ImageView iv2 = new ImageView();
        root.getChildren().add(iv1);
        root.getChildren().add(iv2);
        log.info("Az alakzatok lekérése és az imageview-ek létrehozása");


        Thread.sleep(2000);

        /**
         * Gombokra való reagálás.
         * Ha 10 teszt megtörtént akkor megkapjuk a reakcióidőt.
         * A Q gomb megnyomásával kilép a játékból.
         * Az F gomb megnyomásával azt mondja a játékos, hogy nem egyforma és ha nem az akkor nincs büntetési idő.
         * A J gomb megnyomásával azt mondja a játékos, hogy egyforma és ha az akkor nincs büntetési idő.
         *
         */
        same.set(getScene(iv1, iv2, shapes, random(), random()));
        scene.setOnKeyPressed(event -> {

            if(i.get()>10) {
                log.info("Véget ért a játék");
            }
            else{
                if (i.get() == 10) {

                    double reactT = (time[0] + time[1] + time[2] + time[3] + time[4] + time[5] + time[6] + time[7] + time[8] + time[9]) / 10;

                    Text reactTime = new Text();
                    reactTime.setTranslateX(-400);
                    reactTime.setTranslateY(-250);
                    reactTime.setStyle("-fx-font: 24 arial;");
                    root.getChildren().add(reactTime);
                    reactTime.setText(String.valueOf(reactT));

                    addPlayer(username.getText(), reactT);
                    log.info("A játékos neve és pontjának mentése");
                    i.incrementAndGet();

                }
                if(event.getCode() == KeyCode.F) {

                    if(!same.get()) {
                        if (i.get() == 0) {

                            time[i.get()] = tc.getSecondsPassed();
                            time_tmp[i.get()] = tc.getSecondsPassed();
                            log.info("Helyes válasz, az első nem volt egyforma");

                        }

                        else {

                            time[i.get()] = tc.getSecondsPassed() - time_tmp[i.get()-1];
                            time_tmp[i.get()] = tc.getSecondsPassed();
                            log.info("Helyes válasz, nem volt egyforma");

                        }
                    }

                    if(same.get()) {

                        if (i.get() == 0) {
                            time[i.get()] = tc.getSecondsPassed() + 2;
                            time_tmp[i.get()] = tc.getSecondsPassed();
                            log.info("Helytelen válasz, egyforma volt az első");

                        }
                        else {

                            time[i.get()] = tc.getSecondsPassed() - time_tmp[i.get()-1] + 2;
                            time_tmp[i.get()] = tc.getSecondsPassed();
                            log.info("Helytelen válasz, egyforma volt");

                        }
                    }
                }
                if(event.getCode() == KeyCode.J) {

                    if(same.get()) {

                        if (i.get() == 0) {

                            time[i.get()] = tc.getSecondsPassed();
                            time_tmp[i.get()] = tc.getSecondsPassed();
                            log.info("Helyes válasz, az első egyforma volt");

                        }
                        else {

                            time[i.get()] = tc.getSecondsPassed() - time_tmp[i.get()-1];
                            time_tmp[i.get()] = tc.getSecondsPassed();
                            log.info("Helyes válasz, egyforma volt");

                        }

                    }

                    if(!same.get()) {

                        if (i.get() == 0) {

                            time[i.get()] = tc.getSecondsPassed() + 2;
                            time_tmp[i.get()] = tc.getSecondsPassed();
                            log.info("Helytelen válasz, az első nem volt egyforma");

                        }
                        else {

                            time[i.get()] = tc.getSecondsPassed() - time_tmp[i.get()-1] + 2;
                            time_tmp[i.get()] = tc.getSecondsPassed();
                            log.info("Helytelen válasz, nem volt egyforma");

                        }
                    }
                }

                if(event.getCode() == KeyCode.Q) {
                    root.getChildren().clear();
                    primaryStage.close();
                    log.info("Kiléptél a játékból");
                }

                same.set(getScene(iv1, iv2, shapes, random(), random()));
                i.incrementAndGet();
                log.info("Új képek következnek");
            }

        });

        primaryStage.setScene(scene);
        primaryStage.show();

    }
    /**
     * Egy új "scene" kérése ami feldob két új képet és visszaad egy boolean-t ami true ha a két kép egyforma és false ha nem egyformák.
     * @param iv1 imageView
     * @param iv2 imageView
     * @param rand1 egy random szám
     * @param rand2 még egy random szám
     * @param shapes az alakzatok (képek)
     * @return egyforma vagy sem
     *
     */
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
        log.info("Be lett állítva 2 kép és visszaadta, hogy egyforma vagy sem");
        return same;
    }

    static int random(){
        Random random = new Random();
        return random.nextInt(3);
    }
    /**
     * Játékos hozzáadása az adatbázishoz
     *
     */
    public static void addPlayer(String name, double score) {

        Injector injector = Guice.createInjector(new PersistenceModule("jpa-persistence-unit-1"));
        UsersDao usersDao = injector.getInstance(UsersDao.class);
        Users player = new Users(name, score);
        usersDao.persist(player);
        log.info("Hozzá lett adva egy játékos");

    }


}
