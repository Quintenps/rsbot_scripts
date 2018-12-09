package bluedog.barblootcook;
import bluedog.barblootcook.tasks.*;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Script.Manifest(name = "bluedog's barb loot n cook", description = "", properties = "author=bluedog; topic=118146; client=4;")

public class Main extends PollingScript<ClientContext> implements PaintListener {
    List<Task> tasks = new ArrayList<Task>();

    private int COOKINGLVL_START = ctx.skills.level(Constants.SKILLS_COOKING);
    private int COOKINGEXP_START = ctx.skills.experience(Constants.SKILLS_COOKING);


    private String getRunningTime() {
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public void start() {
        Looter loot = new Looter(ctx);
        Cooker cook = new Cooker(ctx);
        Dropper drop = new Dropper(ctx);
        AntiBan ab = new AntiBan(ctx);
        tasks.add(cook);
        tasks.add(drop);
        tasks.add(loot);
        tasks.add(ab);
    }

    @Override
    public void poll() {
        for (Task currentTask : tasks) {
            System.out.println("tasks: " + currentTask.toString());
            if (ctx.controller.isStopping()) {
                break;
            }
            if (currentTask.activate()) {
                System.out.println("Executing: " + currentTask.toString());
                currentTask.execute();
                break;
            }
        }
    }

    @Override
    public void repaint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics; //get 2d graphics

        g.drawString("bluedog's barb loot n cook", 20, 80);
        g.drawString("Time : " + getRunningTime(), 20, 100);
        g.drawString("Cooking level: "+ ctx.skills.level(Constants.SKILLS_COOKING) + " (+"+(ctx.skills.level(Constants.SKILLS_COOKING) - COOKINGLVL_START)+")", 20, 120);
        g.drawString("Cooking exp/h: "+ ((ctx.skills.experience(Constants.SKILLS_COOKING) - COOKINGEXP_START) /  (this.getTotalRuntime() / 1000) * 3600), 20, 140);
    }
}
