import java.util.Scanner;

public class Main {

    public static void main(String[] args) { 
        
        CatsTD td = new CatsTD();
        AbstractMap map = new TestMap();

        Scanner sc = new Scanner(System.in);

        td.add(map);

        while (sc.hasNext()) {
            String comd = sc.next();
            if (comd.equals("add")) {
                String tower = sc.next();
                int x = sc.nextInt();
                int y = sc.nextInt();
                td.add(new BasicCatTower(x, y));
            }
            if (comd.equals("start")) {
                td.startLevel();
            }
        }
    }
}
