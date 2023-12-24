package gl;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.*;


import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;

public class Main extends GLCanvas
        implements GLEventListener, KeyListener
{
    private ArrayList<GraphicalObject> ennemi;
    private Cube vaisseau;
    private ArrayList<GraphicalObject> ennemiRemove;
    private ArrayList<GraphicalObject> enemyBulletList;
    private ArrayList<GraphicalObject> bulletList;
    private ArrayList<GraphicalObject> removeBullet;
    private boolean leftArrow, rightArrow, shootBullet;
    float limitRight = 8.0f;  
    float limitLeft = -10.0f;  

    private boolean moveRight = true;
 

    public static void main(String[] args)
    {
        GLCanvas canvas = new Main();
        canvas.setPreferredSize(new Dimension(800, 600));
        final JFrame frame = new JFrame();
        frame.getContentPane().add(canvas);
        frame.setTitle("Space invaders cube ");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Animator animator = new Animator(canvas);
        animator.start();
        frame.requestFocusInWindow();
    }

    public Main() {
        this.addGLEventListener(this);
        this.addKeyListener(this);
        this.ennemi = new ArrayList<GraphicalObject>();
        this.ennemiRemove = new ArrayList<GraphicalObject>();
        this.bulletList = new ArrayList<GraphicalObject>();
        this.removeBullet = new ArrayList<GraphicalObject>();
        this.enemyBulletList = new ArrayList<>();

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, -15.0f);
        gl.glPushMatrix();

     // Affiche les ennemis
        for (GraphicalObject cube : ennemi) {
            cube.display(gl);
        }

        gl.glPopMatrix();

     // Affiche le vaisseau 
        this.vaisseau.display(gl);

     // Déplace le vaisseau spatial en fonction des touches directionnelles
        if (leftArrow) {
            float moveShipX = -0.1f;
            vaisseau.translate(moveShipX, 0, 0);
        }
        if (rightArrow) {
            float moveShipX = 0.1f;
            vaisseau.translate(moveShipX, 0, 0);
        }

     // Tire des projectiles
        if (shootBullet) {
            Cube bullets = new Cube(
                    vaisseau.getPosX(), vaisseau.getPosY(), vaisseau.getPosZ(),
                    0, 0, 0,  
                    0.1f, 0f, 1f, 1f,  
                    true, true);  
            bulletList.add(bullets);
            shootBullet = false;
        }

     // Affiche les projectiles
        for (GraphicalObject bullets : bulletList) {
            bullets.translate(0f, 0.1f, 0f);
            bullets.display(gl);
        }

        // Détection des collisions
        for (GraphicalObject bullet : bulletList) {
            for (GraphicalObject enemy : ennemi) {
                float distance = enemy.distance(enemy, bullet);
                if (distance >= 0 && distance <= 1) {
                    ennemiRemove.add(enemy);
                    removeBullet.add(bullet);
                }
            }
        }

      
        if (ennemiRemove != null) {
            ennemi.removeAll(ennemiRemove);
        }
        if (removeBullet != null) {
            bulletList.removeAll(removeBullet);
        }

     // Enemy bullet collision with spaceship
        for (GraphicalObject enemyBullet : enemyBulletList) {
            float distance = vaisseau.distance(vaisseau, enemyBullet);
            if (distance >= 0 && distance <= 1) {
                drawable.destroy();
                JFrame endGame = new JFrame("END");
                JLabel congrat = new JLabel("YOU LOSE");
                congrat.setHorizontalAlignment(SwingConstants.CENTER);
                endGame.add(congrat);
                endGame.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
                endGame.setSize(250, 100);
                endGame.setLocationRelativeTo(null);
                endGame.setVisible(true);
            }
        }

        if (ennemi.isEmpty()) {
            drawable.destroy();
            JFrame endGame = new JFrame("END");
            JLabel congrat = new JLabel("CONGRATULATION");
            congrat.setHorizontalAlignment(SwingConstants.CENTER);
            endGame.add(congrat);
            endGame.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
            endGame.setSize(250, 100);
            endGame.setLocationRelativeTo(null);
            endGame.setVisible(true);
        }

        gl.glPopMatrix();
        
        
     // Déplace les ennemis horizontalement
        for (GraphicalObject cube : ennemi) {
            if (moveRight) {
                cube.translate(0.01f, 0, 0);
            } else {
                cube.translate(-0.01f, 0, 0);
            }
            
            if (cube.getPosX() > limitRight || cube.getPosX() < limitLeft) {
                moveRight = !moveRight; 
            }
        }
        
        
    }

    
    
   
    

    
    
    

    
    
    @Override
    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        // Coleur de fond
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
      
        float positionX, positionY;
        positionX = -10;
        positionY = 7;
        
        // Creation du vaissseau en vert 
        this.vaisseau = new Cube(0, -7, -10, 0, 0, 0, 1f, 0f, 1f, 0f, true, true);
        
        // Creation des ennemis en rose
        for (int i = 17; i >= 0; i--) {
            boolean canShoot = Math.random() < 0.5;  // 50% de chance que l'ennemi puisse tirer
            Cube createdCube = new Cube(positionX, positionY, -10, 0, 0, 0, 1f, 1.0f, 0.5f, 0.5f, false, canShoot);
            this.ennemi.add(createdCube);
            positionX += 4;
            if (i % 6 == 0) {
                positionX = -10;
                positionY -= 3;
            }
        }
        
        
   
      
        
        
        
    }

    @Override
    public void reshape(GLAutoDrawable drawable,
                        int x, int y, int width, int height) {
        // TODO Auto-generated method stub
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU glu = new GLU();
        glu.gluPerspective(45.0, (float)width/height,
                0.1, 100.0);
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                leftArrow = true;
                break;
            case KeyEvent.VK_RIGHT:
                rightArrow = true;
                break;
            case KeyEvent.VK_SPACE:
                shootBullet = true;
                break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                leftArrow = false;
                break;
            case KeyEvent.VK_RIGHT:
                rightArrow = false;
                break;
            case KeyEvent.VK_SPACE:
                shootBullet = false;

                break;

        }
    }
}