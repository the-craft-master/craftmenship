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

public class craftmenship {
    private FpCamera fp;
    private DisplayMode displayMode;
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
            fp = new FpCamera(0f,0f,0f);
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
    }
}
