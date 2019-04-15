/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fpcamera;

/**
 *
 * @author Scott
 */
public class Block {
    private boolean active;
    private BlockType type;
    public enum BlockType{
        BlockType_Grass(0),
        BlockType_Sand(1),
        BlockType_Water(2),
        BlockType_Dirt(3),
        BlockType_Stone(4),
        BlockType_Bedrock(5);
        private int blockID;
        //method: BlockType
        //purpose: constructor
        BlockType(int i) {
            blockID=i;
        }
        //method: getID
        //purpose: getter for ID
        public int getID(){
            return blockID;
        }
        //method: setID
        //purpose: setter for ID
        public void setID(int i){
            blockID = i;
        }
    }
    //method: BlockLoader
    //purpose: constructor
    public Block(BlockType type){
        this.type= type;
    }
    //method: active
    //purpose: getter for active
    public boolean active() {
        return active;
    }
    //method: setActive
    //purpose: setter for active
    public void setActive(boolean active){
        this.active=active;
    }
    //method: getID
    //purpose: getter for blockID
    public int getID(){
        return type.getID();
    }
    //method: getType
    //purpose: getter for type
    public BlockType getType(){
        return type;
    }
    //method: setType
    //purpose: setter for Type
    public void setType(BlockType type){
        this.type = type;
    }
}