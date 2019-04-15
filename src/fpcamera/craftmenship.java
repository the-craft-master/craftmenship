/*
 * Nathaniel Dao
 * Scott Ha
 * Andrew Lee
 * Jeffrey Nguyen
 * 
 */

package fpcamera;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

public class craftmenship {
    private fpCamera fp;
    private DisplayMode displayMode;
    
    private FloatBuffer lightPosition1;
    private FloatBuffer whiteLight1;
    private FloatBuffer lightPosition2;
    private FloatBuffer whiteLight2;
    
    //method: main
    //purpose: The main method for the project
    public static void main(String[] args){
        craftmenship basic = new craftmenship();
        basic.start(); 
    }    
    
    //method: start
    //purpose: creates window
    public void start() {
        try{
            createWindow();
            initGL();
            fp = new fpCamera(0f,0f,0f);
            fp.gameLoop();
        } 
        catch (Exception e) {
        }
    }
    
    //method: createWindow
    //purpose: sets the window parameters
    private void createWindow() throws Exception{
        Display.setFullscreen(false);
        DisplayMode d[] =
        Display.getAvailableDisplayModes();
        for (DisplayMode d1 : d) {
            if (d1.getWidth() == 640 && d1.getHeight() == 480 && d1.getBitsPerPixel() == 32) {
                displayMode = d1;
                break;
            }
        }
        Display.setDisplayMode(displayMode);
        Display.setTitle("Minecraft 2.0");
        Display.create();
    }
    
    //method: initGL
    //purpose: initiates the window
    private void initGL() {
        glEnable(GL_TEXTURE_2D);
        glEnableClientState (GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnable(GL_DEPTH_TEST);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(100.0f, displayMode.getWidth()/(float)
        displayMode.getHeight(), 0.1f, 300.0f);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        
        initLightArrays();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition1); //sets our light’s position
        glLight(GL_LIGHT0, GL_SPECULAR, whiteLight1);    //sets our specular light
        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight1);     //sets our diffuse light
        glLight(GL_LIGHT0, GL_AMBIENT, whiteLight1);     //sets our ambient light
        glLight(GL_LIGHT1, GL_POSITION, lightPosition2); //sets our light’s position
        glLight(GL_LIGHT1, GL_SPECULAR, whiteLight2);    //sets our specular light
        glLight(GL_LIGHT1, GL_DIFFUSE, whiteLight2);     //sets our diffuse light
        glLight(GL_LIGHT1, GL_AMBIENT, whiteLight2);     //sets our ambient light
        glEnable(GL_LIGHTING);                          //enables our lighting
        glEnable(GL_LIGHT0);                            //enables light0
        glEnable(GL_LIGHT1);                            //enables light0
    }
    
    private void initLightArrays(){
        //"the first three values for lightPosition are what we would expect
        // the x,y,z values of where we want to place our source. 
        // the last number 1.0f tells OpenGL the designated coords are the 
        // position of the light source"
        lightPosition1 = BufferUtils.createFloatBuffer(4);
        lightPosition1.put(0.0f).put(0.0f).put(0.0f).put(1.0f).flip();
        whiteLight1 = BufferUtils.createFloatBuffer(4);
        whiteLight1.put(0.0f).put(0.0f).put(0.0f).put(0.0f).flip();
        
        lightPosition2 = BufferUtils.createFloatBuffer(4);
        lightPosition2.put(1.0f).put(0.0f).put(0.0f).put(1.0f).flip();
        whiteLight2 = BufferUtils.createFloatBuffer(4);
        whiteLight2.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
    }
}
