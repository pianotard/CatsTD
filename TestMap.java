import java.awt.Point;

public class TestMap extends AbstractMap {

    public TestMap() {
        super();
        this.setBackground(java.awt.Color.BLACK);
        this.setOpaque(true);
        this.mobPath = MobPath
            .start(new Point(-20, 300))
            .add(new Point(250, 500))
            .add(new Point(380, 300))
            .add(new Point(550, 500))
            .add(new Point(550, 200))
            .add(new Point(-20, 150));
        this.levelPacks.add(new LevelPackage(1)
                .addMob(new MouseMob(), 0, 100, 10)
                .addMob(new MouseMob(), 50));
        this.levelPacks.add(new LevelPackage(2)
                .addMob(new MouseMob(), 0, 50, 10));
        this.levelPacks.add(new LevelPackage(3)
                .addMob(new MouseMob(), 0, 40, 15));
        this.levelPacks.add(new LevelPackage(4)
                .addMob(new MouseMob(), 0, 35, 20));
        this.levelPacks.add(new LevelPackage(5)
                .addMob(new MouseMob(), 0, 30, 25));
        this.levelPacks.add(new LevelPackage(6)
                .addMob(new MouseMob(), 0, 25, 30));
        this.levelPacks.add(new LevelPackage(7)
                .addMob(new MouseMob(), 0, 20, 35));
        this.levelPacks.add(new LevelPackage(8)
                .addMob(new MouseMob(), 0, 12, 50));
        this.applyPathToLevelPacks();
    }
}
