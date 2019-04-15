package Craft;

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
    private Vector3f lposition = null;
    private Vector3f lookDirection = null;
    final private Chunks chunk = new Chunks(0,0,0);

    //method: CameraController
    //purpose: Constructor
    public fpCamera(float x, float y, float z){ 
        position = new Vector3f(x, y, z);
        lookDirection = new Vector3f(x,y,z);
        lposition = new Vector3f(x,y,z);
        lposition.x = 0f;
        lposition.y = 0f;
        lposition.z = 0f;
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
        
//        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
//        lightPosition.put(lposition.x-xOffset).put(lposition.y).put(lposition.z+zOffset).put(1.0f).flip();
//        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        
        position.x -= xOffset;
        position.z += zOffset;
    }

    //method: backwards
    //purpose: moves backward by a set distance
    public void walkBackwards(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        
//        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
//        lightPosition.put(lposition.x+xOffset).put(lposition.y).put(lposition.z-zOffset).put(1.0f).flip();
//        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        
        position.x += xOffset;
        position.z -= zOffset;
    }

    //method: left
    //purpose: moves left by a set amount
    public void strafeLeft(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw-90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw-90));
        
//        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
//        lightPosition.put(lposition.x-xOffset).put(lposition.y).put(lposition.z+zOffset).put(1.0f).flip();
//        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        
        position.x -= xOffset;
        position.z += zOffset;
    }

    //method: right
    //purpose: moves right by a set amount
    public void strafeRight(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw+90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw+90));
        
//        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
//        lightPosition.put(lposition.x-xOffset).put(lposition.y).put(lposition.z+zOffset).put(1.0f).flip();
//        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        
        position.x -= xOffset;
        position.z += zOffset;
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
      
//      FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
//      lightPosition.put(lposition.x).put(lposition.y).put(lposition.z).put(1.0f).flip();
//      glLight(GL_LIGHT0, GL_POSITION, lightPosition);
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