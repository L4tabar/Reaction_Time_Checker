import controller.Game;
import javafx.scene.image.ImageView;
import model.Shapes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class sameShapeTesting {
    @Test
    void testShapes (){
        Game tester = new Game();

        Shapes shapes = new Shapes();
        ImageView iv1 = new ImageView();
        ImageView iv2 = new ImageView();

        assertTrue(tester.getScene(iv1,iv2,shapes,1,1));
        assertTrue(tester.getScene(iv1,iv2,shapes,2,2));
        assertTrue(tester.getScene(iv1,iv2,shapes,3,3));
        assertFalse(tester.getScene(iv1,iv2,shapes,1,2));
        assertFalse(tester.getScene(iv1,iv2,shapes,1,3));
        assertFalse(tester.getScene(iv1,iv2,shapes,2,3));
        assertFalse(tester.getScene(iv1,iv2,shapes,2,1));
        assertFalse(tester.getScene(iv1,iv2,shapes,3,1));
        assertFalse(tester.getScene(iv1,iv2,shapes,3,2));
    }
}
