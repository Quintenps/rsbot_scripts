package bluedog.hillgiants.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

public class Combat extends Task {
    private final int npcsToKill[] = {2098, 2099, 2100, 2101, 2102, 2103};
    Npc targetedNpc;

    public Combat(ClientContext ctx) {
        super(ctx);
    }

    private void getNearestAvailableNpc() {
        targetedNpc = ctx.npcs.select().id(npcsToKill).select(npc -> !npc.inCombat()).nearest().poll();
    }

    private void attackNpc(Npc npcToKill) {
        ctx.camera.turnTo(npcToKill);
        npcToKill.interact("Attack");
        System.out.println("Found NPC "+npcToKill.name()+" #"+npcToKill.id());
        Condition.wait(() -> ctx.players.local().inCombat(), 200, 10);
    }

    @Override
    public boolean activate() {

        if (ctx.players.local().interacting().valid() || ctx.players.local().interacting().interacting().equals (ctx.players.local()) ) {
            return false;
        }
        return true;
    }

    @Override
    public void execute() {
        toggleRun();
        getNearestAvailableNpc();
        attackNpc(targetedNpc);
    }


}
