package bluedog.giantspider.tasks;

import java.awt.Point;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;

public class AntiBan extends Task {
    public AntiBan(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        if (ctx.players.local().inCombat()) {
            return getRand(-50, 1) > 0;
        }
        return false;
    }

    @Override
    public void execute() {
        int randNum = Random.nextInt(1, 11);
        if (randNum == 1) checkXP();
        if (randNum >= 2 && randNum <= 5) randomCameraTurn();
        if (randNum >= 6 && randNum <= 8) moveMouseOffScreen();
        if (randNum >= 9 && randNum <= 11) randomMouseMovement(100, 300);
    }

    /**
     * turns camera once to random sport
     * or 2-6 times to random places
     * or turns camera to nearest player
     * <p>
     * all rolled randomly
     */
    public void randomCameraTurn() {
//        int randNum = Random.nextInt(1, 3);
//        if (randNum == 1)
//            ctx.camera.angle(ctx.camera.yaw() + Random.nextInt(-100, 100));
//
//        if (randNum == 2) {
//            randNum = Random.nextInt(2, 6);
//            for (int i = 0; i < randNum; i++) {
//                ctx.camera.angle(ctx.camera.yaw() + Random.nextInt(-100, 100));
//                Condition.sleep(Random.nextInt(250, 500));
//            }
//        }
//
//        if (randNum == 3)
//            ctx.camera.turnTo(ctx.players.select().nearest().poll());

    }

    public void moveMouseOffScreen() {
        int x = Random.nextInt(-1000, -100);
        int y = Random.nextInt(-1000, -100);
        ctx.input.move(x, y);
        ctx.input.defocus();

        Condition.sleep(Random.nextInt(5000, 13000));

        ctx.input.focus();
        x = Random.nextInt(16, 512);
        y = Random.nextInt(45, 334);
        ctx.input.move(x, y);
    }

    /**
     * Author - Enfilade Moves the mouse a random distance between minDistance
     * and maxDistance from the current position of the mouse by generating
     * random vector and then multiplying it by a random number between
     * minDistance and maxDistance. The maximum distance is cut short if the
     * mouse would go off screen in the direction it chose.
     *
     * @param minDistance The minimum distance the cursor will move
     * @param maxDistance The maximum distance the cursor will move (exclusive)
     */
    public void randomMouseMovement(int minDistance, int maxDistance) {
        double xvec = Math.random();
        if (Random.nextInt(0, 2) == 1) {
            xvec = -xvec;
        }
        double yvec = Math.sqrt(1 - xvec * xvec);
        if (Random.nextInt(0, 2) == 1) {
            yvec = -yvec;
        }
        double distance = maxDistance;
        Point p = ctx.input.getLocation();
        int maxX = (int) Math.round(xvec * distance + p.x);
        distance -= Math.abs((maxX - Math.max(0,
                Math.min(ctx.game.dimensions().getWidth(), maxX)))
                / xvec);
        int maxY = (int) Math.round(yvec * distance + p.y);
        distance -= Math.abs((maxY - Math.max(0,
                Math.min(ctx.game.dimensions().getHeight(), maxY)))
                / yvec);
        if (distance < minDistance) {
            return;
        }
        distance = Random.nextInt(minDistance, (int) distance);
        ctx.input.move((int) (xvec * distance) + p.x, (int) (yvec * distance) + p.y);
    }

    public void checkXP() {
        ctx.widgets.component(161, 52).click();
        ctx.widgets.component(320, 1).hover();
        Condition.sleep(Random.nextInt(1000, 4000));
        ctx.widgets.component(161, 54).click();
    }

}
