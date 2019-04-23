package fpcamera;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;

public class fpCamera {
    private float yaw = 0.0f; 
    private float pitch = 0.0f;
    private Vector3f position = null;
    private Vector3f lookDirection = null;
    private Chunks chunk = new Chunks(0,0,0, false);
    
    private boolean gp = false;
    private final float fogDensity = 0.1f;
    float fogColor1[]= {0.5f, 0.5f, 0.5f, 1.0f};
    private float fogColor[];
    /*
        GL_EXP - Basic rendered fog which fogs out all of the screen. 
            It doesn't give much of a fog effect, but gets the job done on
            older PC's.
        GL_EXP2 - Is the next step up from GL_EXP. This will fog out all 
            of the screen, however it will give more depth to the scene.
        GL_LINEAR - This is the best fog rendering mode. Objects fade in 
            and out of the fog much better.*/
    private final int fogMode[] = {GL_EXP, GL_EXP2, GL_LINEAR};
    private int fogfilter = 0;
    private boolean fogEnable = false;
    
    public boolean chunkMode = false;

    //method: CameraController
    //purpose: Constructor
    public fpCamera(float x, float y, float z){ 
        position = new Vector3f(x, y, z);
        lookDirection = new Vector3f(x,y,z);
    }
    //method: yaw
    //purpose: increments yaw by a set amount
    public void yaw(float amount){
        yaw += amount;
    }

    //method: pitch
    //purpose: increments pitch by a set amount
    public void pitch(float amount){
        pitch -= amount;
    }
    //method: forard
    //purpose: moves forward by a set distance
    public void walkForward(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(position.x-=xOffset).put(position.y).put(position.z+=zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        
        //position.x -= xOffset;
        //position.z += zOffset;
    }

    //method: backwards
    //purpose: moves backward by a set distance
    public void walkBackwards(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(position.x+= xOffset).put(position.y).put(position.z-=zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        
        //position.x += xOffset;
        //position.z -= zOffset;
    }

    //method: left
    //purpose: moves left by a set amount
    public void strafeLeft(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw-90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw-90));
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(position.x-=xOffset).put(position.y).put(position.z+=zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        
        //position.x -= xOffset;
        //position.z += zOffset;
    }

    //method: right
    //purpose: moves right by a set amount
    public void strafeRight(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw+90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw+90));
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(position.x-=xOffset).put(position.y).put(position.z+=zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        
        //position.x -= xOffset;
        //position.z += zOffset;
    }

    //method: up
    //purpose: moves up by a set amount
    public void moveUp(float distance){
        position.y -= distance;
    }

    //method: down
    //purpose: moves the camera down by an increment
    public void moveDown(float distance){
     position.y += distance;
    }

    //method: lookAround
    //purpose: moves the camera around
    public void lookThrough(){
      glRotatef(pitch, 1f, 0.0f, 0.0f);
      glRotatef(yaw, 0.0f, 1.0f, 0.0f);
      glTranslatef(position.x, position.y, position.z); 
      FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
      lightPosition.put(position.x).put(position.y).put(position.z).put(1.0f).flip();
      glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }
    
    private void fogInit(){
        //fogColor = BufferUtils.createFloatBuffer(4);
        //fogColor.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();
        //fogColor = {0.5f, 0.5f,0.5f, 1.0f};
        glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        glFogi(GL_FOG_MODE, fogMode[fogfilter]);        // Fog Mode
        //glFogfv(GL_FOG_COLOR, fogColor1);            // Set Fog Color
        glFogf(GL_FOG_DENSITY, fogDensity);              // How Dense Will The Fog Be
        glHint(GL_FOG_HINT, GL_DONT_CARE);          // Fog Hint Value
        glFogf(GL_FOG_START, 1.0f);             // Fog Start Depth
        glFogf(GL_FOG_END, 5.0f);               // Fog End Depth
        glEnable(GL_FOG);                   // Enables GL_FOG
    }
    
    private void fogDisable(){
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glDisable(GL_FOG);                   // Disable GL_FOG
    }
    
    //method: renderLoop
    //purpose: loops the view render
    public void gameLoop(){
        fpCamera camera = new fpCamera(-35,-10,-90);
        float dx;
        float dy;
        float mouseSensitivity = 0.2f;
        float movementSpeed = .35f;
        Mouse.setGrabbed(true); 
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            dx = Mouse.getDX();
            dy = Mouse.getDY();
            camera.yaw(dx * mouseSensitivity);  
            camera.pitch(dy * mouseSensitivity); 
            if (Keyboard.isKeyDown(Keyboard.KEY_W))
            {
                camera.walkForward(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A))
            {
                camera.strafeLeft(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S))
            {
                camera.walkBackwards(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D))
            {
                camera.strafeRight(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) 
            {
                camera.moveUp(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) 
            {
                camera.moveDown(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_R)) 
            {
                if (!chunkMode)
                {
                    chunkMode = true;
                    chunk = new Chunks(0,0,0, chunkMode);
                }
                else
                {
                    chunkMode = false;
                    chunk = new Chunks(0,0,0, chunkMode);
                }
                chunk.render();
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_F)) 
            {
                if(!fogEnable)
                {
                    fogInit();
                    fogEnable = true;
                }
                else
                {
                    fogDisable();
                    fogEnable = false;
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_G)) 
            {
                gp = true;
                fogfilter += 1;
                if(fogfilter > 2)
                    fogfilter = 0;
                glFogi(GL_FOG_MODE, fogMode[fogfilter]);
            }
            if (!Keyboard.isKeyDown(Keyboard.KEY_G)) 
            {
                gp = false;
            }
            glLoadIdentity();
            camera.lookThrough(); 
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            chunk.render();
            Display.update();
            
            Display.sync(60);
        }
        Display.destroy();
    }
}