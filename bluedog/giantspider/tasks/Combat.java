package bluedog.giantspider.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

public class Combat extends Task {

    private final static Tile combatZone = new Tile(2132, 5263, 0);

    private Npc getNearestAvailableNpc() {
        return ctx.npcs.select().name("Giant spider").select(npc -> !npc.inCombat()).nearest().poll();
    }

    private Npc npcAttackingYou() {
        return ctx.npcs.select().name("Giant spider").select(npc -> npc.interacting().equals(ctx.players.local())).poll();
    }

    private void attackNpc(Npc npcToKill) {
        ctx.camera.turnTo(npcToKill);
        npcToKill.interact("Attack", npcToKill.name());
        System.out.println("Found NPC " + npcToKill.name() + " #" + npcToKill.id());
        Condition.wait(() -> !ctx.players.local().inCombat(), 800, 10);
    }

    public Combat(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        if (ctx.players.local().interacting().valid() || npcAttackingYou().valid() || ctx.players.local().interacting().interacting().equals(ctx.players.local()) || ctx.players.local().tile().distanceTo(combatZone) > 20) {
            if (npcAttackingYou().valid() && !ctx.players.local().interacting().interacting().equals(ctx.players.local())) {
                attackNpc(npcAttackingYou());
            }
            return false;
        }
        return true;
    }

    @Override
    public void execute() {
        toggleRun();
        attackNpc(getNearestAvailableNpc());
    }
}
