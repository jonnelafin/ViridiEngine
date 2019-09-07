/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pointbreakdemo;
import PBEngine.*;
import static PBEngine.Main.k;
import java.awt.Color;
import java.net.URISyntaxException;
import java.util.LinkedList;
/**
 *
 * @author elias eskelinen
 */
public class PointBreakDemo {

    /**
     * @param args the command line arguments
     */
    static Main pbengine = new Main();
    public static void main(String[] args) throws URISyntaxException {
        // TODO code application logic here
        new game();
        
    }
    public void go(){
        
    }
}
class game{
    public game() throws URISyntaxException{
        String[] argss = new String[2];
        argss[0] = "nodemo";
        argss[1] = "calclights";
        k = new kick(0, false, new dVector(0, 0));
        Thread A = new Thread(k);
        A.start();
        while(!k.ready){
            
        }
        System.out.println("WOOOOOOOOOOOOOOOOOOOOOOOO");
//        try {
//            k.Logic.loadLevel("out.txt");
//        } catch (URISyntaxException ex) {
//            Logger.getLogger(game.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        k.rad.add(0, 0, 8, new Color(1,1,1), 0, false);
//       k.rad.recalculateParent();
        System.out.println("We'll take it form here!");
        k.Logic.loadLevel("out.txt");
        gameObject p = new Player(25, 5, 1, "player1", "█", 1F, Color.black, 1, k);
        AI ai = new AI(10, 10, 1, "ai1", "A", 1, Color.yellow, 2, k);
        k.objectManager.addObject(p);
        k.objectManager.addObject(ai);
        k.Logic.abright = true;
        System.out.println("Game Init Complete!");
    }
}
class AI extends gameObject{
    int timer = 0;
    LinkedList<dVector> path;
    boolean pathComplete = true;
    boolean PFinding = false;
    public AI(int xpos, int ypos, int size, String tag, String ap, double mass, Color color, int id, kick k){
        super(xpos, ypos, size, tag, ap, mass, color, id, k);
    }
    private void calcPath(){
        char[][] map = k.objectManager.getCollisionmap(new dVector(0, 0), new dVector(k.xd, k.yd), getTag().get(0));
        map[0][0] = 'X';
        System.out.println("Collision map ready: ");
        for(char[] lane : map){
            for(char i : lane){
                System.out.print(i+" ");
            }System.out.println("");
        }
        LinkedList<dVector> path = astar.pathToVector(astar.getPath(map, (int)this.x, (int)this.y));
        //System.out.println("Pathfinding Complete!");
        this.path = path;
        PFinding = false;
    }
    @Override
    public void update(int xd, int yd, objectManager oM){
        if(path == null && !PFinding){
            Thread pf = new Thread(){
                @Override
                public void run(){
                    calcPath();
                }
            };
            pf.run();
        }
        else{
            if(path.size() > 0){
                dVector step = path.getLast();
                setLocation(step);
                path.removeLast();
            }
            else{
                this.setLocation(new dVector(0, 0));
                Thread pf = new Thread(){
                    @Override
                    public void run(){
                        calcPath();
                    }
                    };
                pf.run();
            }
        }
    }
}