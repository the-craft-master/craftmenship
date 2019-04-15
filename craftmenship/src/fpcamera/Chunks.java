/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpcamera;

/**
 *
 * @author Scott
 */
import java.io.File;
import java.net.URL;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Random;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Chunks{
    static final int SIZE = 30;
    static final int CUBE_LENGTH = 2;
    static final float PMIN = 0.03f;
    static final float PMAX = 0.06f;
    private Random random = new Random();
    private final Block[][][] Blocks;
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private int VBOTextureHandle;
    private Texture texture;
    
    //method: ChunkLoader
    //purpose: constructor
    public Chunks(int startX, int startY, int startZ) {
        try{
            URL path = Chunks.class.getResource("terrain.png");
            File f = new File(path.getFile());
            texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("fpcamera/terrain.png"));
        }
        catch(Exception e){
            System.out.print("Terrain File not found");
        }
        random= new Random();
        Blocks = new
        Block[SIZE][SIZE][SIZE];
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                for (int z = 0; z < SIZE; z++) {
                    if(random.nextFloat()>0.8f){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Grass);
                    }else if(random.nextFloat()>0.6f){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Dirt);
                    }else if(random.nextFloat()>0.4f){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Water);
                    }else if(random.nextFloat()>0.2f){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Stone);
                    }else if(random.nextFloat()>0.1f){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Bedrock);
                    }else if(random.nextFloat()>0.0f){
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Sand);
                    }else{
                        Blocks[x][y][z] = new
                        Block(Block.BlockType.BlockType_Stone);
                    }
                }
            }
        }
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        rebuild(startX, startY, startZ);
    }
    //method: render
    //purpose: render the chunk
    public void render(){
        glPushMatrix();
        glBindBuffer(GL_ARRAY_BUFFER,VBOVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER,VBOColorHandle);
        glColorPointer(3,GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBindTexture(GL_TEXTURE_2D, 1);
        glTexCoordPointer(2,GL_FLOAT,0,0L);
        glDrawArrays(GL_QUADS, 0,SIZE *SIZE*SIZE * 24);
        glPopMatrix();
    }
    //method: rebuild
    //purpose: rebuild world
    private void rebuild(float startX, float startY, float startZ) {  
        float persistance = 0;
        while (persistance < PMIN) {
            persistance = (PMAX)*random.nextFloat();
        }
        int seed = (int)(50 * random.nextFloat());
        SimplexNoise noise = new SimplexNoise(SIZE, persistance, seed);
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers(); 
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer((SIZE * SIZE * SIZE) * 6 * 12);
        FloatBuffer VertexColorData = BufferUtils.createFloatBuffer((SIZE * SIZE * SIZE) * 6 * 12);
        FloatBuffer VertexTextureData = BufferUtils.createFloatBuffer((SIZE * SIZE * SIZE) * 6 * 12);
        float height;
        for (float x = 0; x < SIZE; x++) {
            for (float z = 0; z < SIZE; z++) {
                // Height randomized
                int i = (int) (startX + x * ((300 - startX) / 640));
                int j = (int) (startZ + z * ((300 - startZ) / 480));        
                height = 19+Math.abs((startY + (int) (190 * noise.getNoise(i, j))* CUBE_LENGTH/2));
                         persistance = 0;
                for (float y = 0; y < height; y++) {
                    if (height >= SIZE)
                        break;
                    Blocks[(int) x][(int) y][(int) z].setActive(true);
                    Blocks[(int) x][(int) y][(int) z].setType(Block.BlockType.BlockType_Stone);
                    while (persistance < PMIN) {
                        persistance = (PMAX) * random.nextFloat();
                    }  
                }
            }
        }
        dirtLayer();
        renderSandWater();
        bedrockLayer();
        bedrock();
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                for (int y = 0; y < SIZE; y++) {
                    if (Blocks[x][y][z].active() && blockExposed(x,y,z)){
                        VertexPositionData.put(createCube((startX + x * CUBE_LENGTH), (y * CUBE_LENGTH + (float) (SIZE * -1.0)),(startZ + z * CUBE_LENGTH) + (float) (SIZE * 1.5)));
                        float[] temp = new float[] { 1, 1, 1 };
                        VertexColorData.put(temp);
                        VertexTextureData.put(createTexture(0, 0, Blocks[x][y][z]));
                        
                    }
                } 
            }
        }
        VertexTextureData.flip();
        VertexColorData.flip();
        VertexPositionData.flip();
        glBindBuffer(GL_ARRAY_BUFFER,VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER,VertexPositionData,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER,VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER,VertexColorData,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    //method: blockExposed
    //purpose: this tells whether or not a block is exposed
    public boolean blockExposed(int x, int y, int z){
        try{
            if (!Blocks[x][y][z].active())
                return false;
            if (!Blocks[x+1][y][z].active())
                return true;
            if (!Blocks[x-1][y][z].active())
                return true;
            if (!Blocks[x][y+1][z].active())
                return true;
            if (!Blocks[x][y-1][z].active())
                return true;
            if (!Blocks[x][y][z+1].active())
                return true;
            if (!Blocks[x][y][z-1].active())
                return true;
        }
        catch(IndexOutOfBoundsException e){
            return true;
        }
        return false;
    }
    //method: createBlock
    //purpose: this tell whether or not a block is exposed.
    public void createBlock(int x, int y, int z, Block.BlockType type){
        try{
            Blocks[x][y][z].setActive(true);
            Blocks[x][y][z].setType(type);
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("Out of Bounds");
        }
    }
    //method: renderSandWater
    //purpose: this method creates a water and sand patch to be rendered
    public void renderSandWater(){
        Vector3f start = startWater();
        int x = (int)start.x;
        int y = (int)start.y;
        int z = (int)start.z;
        makeWater(x,y,z);
        makeSand(y);
    }
    //method: makeSand
    //purpose: this extends the sand patch
    public void makeSand(int yStart){
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                try{
                    if(Blocks[x][yStart][z].getType() == Block.BlockType.BlockType_Water){         
                        try{
                        if(Blocks[x+1][yStart][z].getType() != Block.BlockType.BlockType_Water)
                            Blocks[x+1][yStart][z].setType(Block.BlockType.BlockType_Sand);
                        }
                        catch(IndexOutOfBoundsException e){    
                        }
                        try{
                        if(Blocks[x-1][yStart][z].getType() != Block.BlockType.BlockType_Water)
                            Blocks[x-1][yStart][z].setType(Block.BlockType.BlockType_Sand);
                        }
                        catch(IndexOutOfBoundsException e){   
                        }
                        try{
                        if(Blocks[x][yStart][z+1].getType() != Block.BlockType.BlockType_Water)
                            Blocks[x][yStart][z+1].setType(Block.BlockType.BlockType_Sand);
                        }
                        catch(IndexOutOfBoundsException e){ 
                        }
                        try{
                        if(Blocks[x][yStart][z-1].getType() != Block.BlockType.BlockType_Water)
                            Blocks[x][yStart][z-1].setType(Block.BlockType.BlockType_Sand);
                        }
                        catch(IndexOutOfBoundsException e){ 
                        }
                        try{
                        if(!Blocks[x+1][yStart+2][z].active())
                            Blocks[x+1][yStart+1][z].setActive(false);
                        }
                        catch(IndexOutOfBoundsException e){
                        }
                        try{
                        if(!Blocks[x-1][yStart+1][z].active())
                            Blocks[x-1][yStart+1][z].setActive(false);
                        }
                        catch(IndexOutOfBoundsException e){
                        }
                        try{
                        if(!Blocks[x][yStart+1][z+1].active())
                            Blocks[x][yStart+1][z+1].setActive(false);
                        }
                        catch(IndexOutOfBoundsException e){ 
                        }
                        try{
                        if(!Blocks[x][yStart+1][z-1].active())
                            Blocks[x][yStart+1][z-1].setActive(false);
                        }
                        catch(IndexOutOfBoundsException e){
                        }
                        Blocks[x][yStart-1][z].setType(Block.BlockType.BlockType_Sand);
                    }
                }
                catch(IndexOutOfBoundsException e){  
                }
            }
        }
    }
    //method: startWater
    //purpose: this method returns a value where the water will start
    public Vector3f startWater(){
        LinkedList<Vector3f> positions = new LinkedList<>();
        int minY = SIZE - 1;
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                int y = 0;
                while(Blocks[x][y][z].getType() != Block.BlockType.BlockType_Grass && y < SIZE-1)
                    y ++;
                if (y < minY){
                    minY = y;
                    positions.clear();
                    positions.add(new Vector3f(x,y,z));
                }
                else if (y == minY){
                    positions.add(new Vector3f(x,y,z));
                }
            }
        }
        int rand = (int) (Math.random() * positions.size());
        return positions.get(rand);
    }
    //method: makeWater
    //purpose: this creates a water patch to be rendered
    public void makeWater(int x, int y, int z){
        if (x < 0 || y < 0 || z < 0 || x >= SIZE || y >= SIZE || z >= SIZE)
            return;
        if (Blocks[x][y][z].getType() == Block.BlockType.BlockType_Grass){
            Blocks[x][y][z].setType(Block.BlockType.BlockType_Water);
            makeWater(x+1,y,z);
            makeWater(x-1,y,z);
            makeWater(x,y,z+1);
            makeWater(x,y,z-1);
        }
    }
    //method: bedrock
    //purpose: creates the bedrock blocks
    public void bedrock(){
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                for (int y = 1; y < 4; y++){
                    if (Math.random() < 1-(double)(y)/4){
                        Blocks[x][y][z].setType(Block.BlockType.BlockType_Bedrock);
                    }
                }
            }
        }
    }
    //method: dirtLayer
    //purpose: this ensures that the top layer is grass/dirt
    public void dirtLayer(){
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                int y = 0;
                while (y < SIZE - 1 && Blocks[x][y][z].active()){
                    y ++;
                }
                y --;
                if (y > 2){
                Blocks[x][y][z].setType(Block.BlockType.BlockType_Grass);
                Blocks[x][y-1][z].setType(Block.BlockType.BlockType_Dirt);
                Blocks[x][y-2][z].setType(Block.BlockType.BlockType_Dirt);
                }
            }
        }
    }
    //method: bedrockLayer
    //purpose:this initializes the floor of bedrock
    public void bedrockLayer(){
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                Blocks[x][0][z].setType(Block.BlockType.BlockType_Bedrock);
            }
        }
    }
    //method: createCube
    //purpose: creates a cube
    public static float[] createCube(float x, float y, float z) {
        int oS = CUBE_LENGTH / 2;
        return new float[] {
            x + oS, y + oS, z,
            x - oS, y + oS, z,
            x - oS, y + oS, z - CUBE_LENGTH,
            x + oS, y + oS, z - CUBE_LENGTH,
            x + oS, y - oS, z - CUBE_LENGTH,
            x - oS, y - oS, z - CUBE_LENGTH,
            x - oS, y - oS, z,
            x + oS, y - oS, z,
            x + oS, y + oS, z - CUBE_LENGTH,
            x - oS, y + oS, z - CUBE_LENGTH,
            x - oS, y - oS, z - CUBE_LENGTH,
            x + oS, y - oS, z - CUBE_LENGTH,
            x + oS, y - oS, z,
            x - oS, y - oS, z,
            x - oS, y + oS, z,
            x + oS, y + oS, z,
            x - oS, y + oS, z - CUBE_LENGTH,
            x - oS, y + oS, z,
            x - oS, y - oS, z,
            x - oS, y - oS, z - CUBE_LENGTH,
            x + oS, y + oS, z,
            x + oS, y + oS, z - CUBE_LENGTH,
            x + oS, y - oS, z - CUBE_LENGTH,
            x + oS, y - oS, z };
    }
    //method: createTexture
    //purpose: tells how to texure each type of block
    public static float[] createTexture(float x, float y, Block block) {
        float oS = (1024f/16)/1024f;
        switch (block.getID()) {
            case 0:
                return texureBlock(x,y,oS,3,10,4,1,3,1);
            case 1:
                return texureBlock(x,y,oS,3,2,3,2,3,2);
            case 2:
                return texureBlock(x,y,oS,15,13,15,13,15,13);
            case 3:
                return texureBlock(x,y,oS,3,1,3,1,3,1);
            case 4:
                return texureBlock(x,y,oS,2,1,2,1,2,1);
            case 5:
                return texureBlock(x,y,oS,2,2,2,2,2,2);
            default:
                System.out.println("not found");
                return null;
        }
    }
    //method: texureBlock
    //purpose: this method gibes the block a texture
    public static float[] texureBlock(float x, float y, float oS, int xTop, int yTop, int xSide, int ySide, int xBottom, int yBottom){
        return new float[] {
            x + oS*xTop, y + oS*yTop,
            x + oS*(xTop-1), y + oS*yTop,
            x + oS*(xTop-1), y + oS*(yTop-1),
            x + oS*xTop, y + oS*(yTop-1),
            x + oS*xBottom, y + oS*yBottom,
            x + oS*(xBottom-1), y + oS*yBottom,
            x + oS*(xBottom-1), y + oS*(yBottom-1),
            x + oS*xBottom, y + oS*(yBottom-1),
            x + oS*xSide, y + oS*(ySide-1),
            x + oS*(xSide-1), y + oS*(ySide-1),
            x + oS*(xSide-1), y + oS*ySide,
            x + oS*xSide, y + oS*ySide,
            x + oS*xSide, y + oS*ySide,
            x + oS*(xSide-1), y + oS*ySide,
            x + oS*(xSide-1), y + oS*(ySide-1),
            x + oS*xSide, y + oS*(ySide-1),
            x + oS*xSide, y + oS*(ySide-1),
            x + oS*(xSide-1), y + oS*(ySide-1),
            x + oS*(xSide-1), y + oS*ySide,
            x + oS*xSide, y + oS*ySide,
            x + oS*xSide, y + oS*(ySide-1),
            x + oS*(xSide-1), y + oS*(ySide-1),
            x + oS*(xSide-1), y + oS*ySide,
            x + oS*xSide, y + oS*ySide};
    }
}