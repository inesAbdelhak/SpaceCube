package gl;

import com.jogamp.opengl.GL2;

import java.util.ArrayList;

public class Cube extends GraphicalObject {

    private ArrayList<Square> faces;
    private boolean canShoot;
    public Cube(float pX, float pY, float pZ,
            float angX, float angY, float angZ,
            float scale,
            float r, float g, float b, boolean isShip, boolean canShoot) {
    super(pX, pY, pZ, angX, angY, angZ, scale, r, g, b);
    this.canShoot = canShoot;
    faces = new ArrayList<Square>();

        // couleur vaisseau
        float shipColorR = 0.0f;
        float shipColorG = 1.0f;
        float shipColorB = 0.0f;

     // couleur ennemi
        float enemyColorR = 1.0f;
        float enemyColorG = 0.5f;
        float enemyColorB = 0.5f;

        // Front face
        faces.add(new Square(0, 0, 1, 0, 0, 0, 1, isShip ? shipColorR : enemyColorR, isShip ? shipColorG : enemyColorG, isShip ? shipColorB : enemyColorB));

        // Back face
        faces.add(new Square(0, 0, -1, 0, 0, 0, 1, isShip ? shipColorR : enemyColorR, isShip ? shipColorG : enemyColorG, isShip ? shipColorB : enemyColorB));

        // Right face
        faces.add(new Square(1, 0, 0, 0, 90, 0, 1, isShip ? shipColorR : enemyColorR, isShip ? shipColorG : enemyColorG, isShip ? shipColorB : enemyColorB));

        // Left face
        faces.add(new Square(-1, 0, 0, 0, -90, 0, 1, isShip ? shipColorR : enemyColorR, isShip ? shipColorG : enemyColorG, isShip ? shipColorB : enemyColorB));

        // Top face
        faces.add(new Square(0, 1, 0, 90, 0, 0, 1, isShip ? shipColorR : enemyColorR, isShip ? shipColorG : enemyColorG, isShip ? shipColorB : enemyColorB));

        // Bottom face
        faces.add(new Square(0, -1, 0, 90, 0, 0, 1, isShip ? shipColorR : enemyColorR, isShip ? shipColorG : enemyColorG, isShip ? shipColorB : enemyColorB));
    }

    
 // Parcours toutes les faces de l'objet et les affiche une par une
    public void display_normalized(GL2 gl) {
        for (Square face : faces)
            face.display(gl);
    }
    
    
    
 // Méthode pour vérifier si l'objet peut tirer
    public boolean canShoot() {
        return canShoot;
    }
    
}