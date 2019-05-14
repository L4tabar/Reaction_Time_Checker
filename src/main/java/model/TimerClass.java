package model;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.text.Text;

/**
 * Used to control the timer.
 *
 */
public class TimerClass {

    private double secondsPassed = 0;

    private static Logger logger = LoggerFactory.getLogger(TimerClass.class);

    public Timer timer = new Timer();
    public Text timerText = new Text();
    public TimerTask task = new TimerTask() {
        public void run() {
            secondsPassed+=0.001;
            timerText.setText(Double.toString(secondsPassed));
            logger.trace("secondsPassed: {}", secondsPassed);
        }
    };

    /**
     * Schedules the task for repeated execution
     */
    public void startTimer() {
        timer.scheduleAtFixedRate(task, 0, 1);
    }

    /**
     * Returns the seconds passed since the start of the timer.
     *
     * @return seconds passed in int
     */
    public double getSecondsPassed() {
        return this.secondsPassed;
    }
}
