package SuperScrabble;

/**
 * Created by Gary on 14/06/2016.
 */
public class PlayerTest {
       public static void main(String[] args) {
               Pool p = new Pool();
               Player player = new Player();
               Frame f = player.getFrame();
               System.out.println(f.isEmpty());
               f.refillFrame(p);
               System.out.println(f.isEmpty());
               System.out.println(f);

       }
}
