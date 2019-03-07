import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author nathaniel
 */
public class craftmenship {
    
    public void startDraw()
    {
        while(!Display.isCloseRequested())
        {
            pollInput();
            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }
    
    public void start()
    {
        try{
            createWindow();
            initGL();
            pollInput();
        }
        catch(Exception e)
        {
        }
    }
    
    public void pollInput()
    {
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
        {
            System.exit(0);
        }
    }
    
    private void createWindow() throws Exception{
        Display.setFullscreen(false);
        
        Display.setDisplayMode(new DisplayMode(640, 480));
        Display.setTitle(("Final Project - Craftmenship"));
        Display.create();
    }
    
    private void initGL()
    {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        glOrtho(-640, 640, -480, 480, 1, -1);
        
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        
        startDraw();
    }
}
