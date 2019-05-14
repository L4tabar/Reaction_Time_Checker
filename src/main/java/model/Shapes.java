package model;

import javafx.scene.image.Image;


public class Shapes {
     private Image image1 = new Image("/Pic/1.png");
     private Image image2 = new Image("/Pic/2.png");
     private Image image3 = new Image("/Pic/3.png");
     private Image[] images = {image1, image2, image3};

    public Image getImage(int a) {
        return images[a];
    }
}
