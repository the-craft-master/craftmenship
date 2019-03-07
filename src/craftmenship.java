import org.lwjgl.opengl.Display; 
import org.lwjgl.opengl.DisplayMode; 
import static org.lwjgl.opengl.GL11.*; 
import org.lwjgl.util.glu.GLU;
/**
 *
 * @author nathaniel
 */
public class craftmenship {
    private fpCamera fp = new fpCamera(0f,0f,0f); 
    private DisplayMode displayMode;
    
    public static void main(String[] args) {
        craftmenship basic = new craftmenship();
        basic.start();
    }
    
    public void start()
    {
        try{
            createWindow();
            initGL();
            fp.gameLoop();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void createWindow() throws Exception{
        Display.setFullscreen(false);
        
        DisplayMode d[] = Display.getAvailableDisplayModes();
        for(int i = 0; i < d.length; i++)
        {
            if(d[i].getWidth() == 640 && d[i].getHeight() == 480 && d[i].getBitsPerPixel() == 32)
            {
                displayMode = d[i];
                break;
            }
        }
        Display.setDisplayMode(displayMode);
        Display.setTitle(("Final Project - Craftmenship"));
        Display.create();
    }
    
    private void initGL()
    {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(100.0f, (float)displayMode.getWidth()/(float) displayMode.getHeight(), 0.1f, 300.0f);
        
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        
    }
}
