package com.racasrpg.race;

import java.util.List;
import java.util.Locale;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * As racas jogaveis. Cada raca tem estagios; cada estagio tem bonus/penalidades (atributos)
 * e uma missao que precisa ser cumprida para evoluir para o proximo estagio.
 * O ultimo estagio de cada raca nao tem missao (forma maxima).
 */
public enum Race {

    HUMANO("humano", "Humano", List.of(
            new Stage("Humano", new Mission(MissionType.KILL_HOSTILE, 30), List.of()),
            new Stage("Humano Veterano", new Mission(MissionType.KILL_HOSTILE, 120), List.of(
                    Bonuses.maxHealth(4), Bonuses.speedPct(5))),
            new Stage("Heroi", null, List.of(
                    Bonuses.maxHealth(8), Bonuses.speedPct(8), Bonuses.attackDamage(1)))
    )),

    ELFO("elfo", "Elfo", List.of(
            new Stage("Elfo", new Mission(MissionType.KILL_RANGED, 25), List.of(
                    Bonuses.speedPct(10), Bonuses.maxHealth(-4))),
            new Stage("Elfo da Floresta", new Mission(MissionType.KILL_RANGED, 100), List.of(
                    Bonuses.speedPct(15), Bonuses.maxHealth(-2), Bonuses.luck(1))),
            new Stage("Alto Elfo", null, List.of(
                    Bonuses.speedPct(20), Bonuses.luck(2), Bonuses.reach(1)))
    )),

    ANAO("anao", "Anao", List.of(
            new Stage("Anao", new Mission(MissionType.MINE_ORE, 40), List.of(
                    Bonuses.breakSpeedPct(20), Bonuses.armor(2), Bonuses.speedPct(-8), Bonuses.maxHealth(-2))),
            new Stage("Anao de Ferro", new Mission(MissionType.MINE_ORE, 150), List.of(
                    Bonuses.breakSpeedPct(40), Bonuses.armor(4), Bonuses.speedPct(-5))),
            new Stage("Rei da Montanha", null, List.of(
                    Bonuses.breakSpeedPct(60), Bonuses.armor(6), Bonuses.toughness(2), Bonuses.speedPct(-5)))
    )),

    ORC("orc", "Orc", List.of(
            new Stage("Orc", new Mission(MissionType.KILL_MELEE, 30), List.of(
                    Bonuses.attackDamage(1), Bonuses.maxHealth(4), Bonuses.speedPct(-5))),
            new Stage("Orc Guerreiro", new Mission(MissionType.KILL_MELEE, 120), List.of(
                    Bonuses.attackDamage(2), Bonuses.maxHealth(6), Bonuses.speedPct(-3))),
            new Stage("Senhor da Guerra", null, List.of(
                    Bonuses.attackDamage(3), Bonuses.maxHealth(10), Bonuses.knockbackResistPct(30)))
    ));

    private final String id;
    private final String displayName;
    private final List<Stage> stages;

    Race(String id, String displayName, List<Stage> stages) {
        this.id = id;
        this.displayName = displayName;
        this.stages = stages;
    }

    public String id() {
        return id;
    }

    public String displayName() {
        return displayName;
    }

    public List<Stage> stages() {
        return stages;
    }

    /** Devolve o estagio pelo indice (limitado ao ultimo estagio). */
    public Stage stage(int index) {
        int i = Math.max(0, Math.min(index, stages.size() - 1));
        return stages.get(i);
    }

    public int lastStageIndex() {
        return stages.size() - 1;
    }

    @Nullable
    public static Race byId(String id) {
        if (id == null) return null;
        String wanted = id.toLowerCase(Locale.ROOT);
        for (Race r : values()) {
            if (r.id.equals(wanted)) return r;
        }
        return null;
    }

    // ---------------------------------------------------------------------------------------------

    /** O que o jogador precisa fazer para evoluir. */
    public enum MissionType {
        KILL_HOSTILE("abater monstros"),
        KILL_RANGED("abater monstros a distancia (arco, besta, tridente...)"),
        KILL_MELEE("abater monstros no corpo a corpo"),
        MINE_ORE("minerar minerios");

        private final String description;

        MissionType(String description) {
            this.description = description;
        }

        public String description() {
            return description;
        }
    }

    public record Mission(MissionType type, int target) {
        public String text() {
            return type.description() + " (" + target + ")";
        }
    }

    /** Um bonus/penalidade de atributo. {@code key} identifica o modificador (um por atributo). */
    public record Bonus(String key, Holder<Attribute> attribute, double amount,
                        AttributeModifier.Operation operation, String text) {
    }

    /** Um estagio da raca. {@code mission} e null no ultimo estagio. */
    public record Stage(String name, @Nullable Mission mission, List<Bonus> bonuses) {
    }

    /** Fabrica de bonus, so para deixar as definicoes acima legiveis. */
    static final class Bonuses {
        private Bonuses() {
        }

        private static String sign(double v) {
            return v >= 0 ? "+" : "";
        }

        private static String num(double v) {
            return v == Math.floor(v) ? String.valueOf((long) v) : String.valueOf(v);
        }

        static Bonus maxHealth(double v) {
            return new Bonus("max_health", Attributes.MAX_HEALTH, v,
                    AttributeModifier.Operation.ADD_VALUE, sign(v) + num(v) + " de vida maxima");
        }

        static Bonus speedPct(int pct) {
            return new Bonus("speed", Attributes.MOVEMENT_SPEED, pct / 100.0,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE, sign(pct) + pct + "% de velocidade");
        }

        static Bonus attackDamage(double v) {
            return new Bonus("attack_damage", Attributes.ATTACK_DAMAGE, v,
                    AttributeModifier.Operation.ADD_VALUE, sign(v) + num(v) + " de dano de ataque");
        }

        static Bonus armor(double v) {
            return new Bonus("armor", Attributes.ARMOR, v,
                    AttributeModifier.Operation.ADD_VALUE, sign(v) + num(v) + " de armadura");
        }

        static Bonus toughness(double v) {
            return new Bonus("toughness", Attributes.ARMOR_TOUGHNESS, v,
                    AttributeModifier.Operation.ADD_VALUE, sign(v) + num(v) + " de resistencia da armadura");
        }

        static Bonus luck(double v) {
            return new Bonus("luck", Attributes.LUCK, v,
                    AttributeModifier.Operation.ADD_VALUE, sign(v) + num(v) + " de sorte");
        }

        static Bonus reach(double v) {
            return new Bonus("reach", Attributes.ENTITY_INTERACTION_RANGE, v,
                    AttributeModifier.Operation.ADD_VALUE, sign(v) + num(v) + " de alcance de ataque");
        }

        static Bonus breakSpeedPct(int pct) {
            return new Bonus("break_speed", Attributes.BLOCK_BREAK_SPEED, pct / 100.0,
                    AttributeModifier.Operation.ADD_MULTIPLIED_BASE, sign(pct) + pct + "% de velocidade de mineracao");
        }

        static Bonus knockbackResistPct(int pct) {
            return new Bonus("knockback_resist", Attributes.KNOCKBACK_RESISTANCE, pct / 100.0,
                    AttributeModifier.Operation.ADD_VALUE, sign(pct) + pct + "% de resistencia a empurrao");
        }
    }
}
