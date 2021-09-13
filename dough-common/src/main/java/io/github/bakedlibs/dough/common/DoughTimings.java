package io.github.bakedlibs.dough.common;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DoughTimings {

    private static final DecimalFormat FORMAT = new DecimalFormat("0.########");

    private final String name;
    private final List<Long> steps = new ArrayList<>();
    private final Logger logger;

    public DoughTimings(@Nonnull Plugin plugin, @Nonnull String name) {
        Validate.notNull(name, "Name cannot be null");
        Validate.notNull(plugin, "Plugin cannot be null");
        this.name = name;
        this.logger = plugin.getLogger();
    }

    public void step() {
        this.steps.add(System.nanoTime());
    }

    public void printTimings() {
        this.printTimings(true);
    }

    public void printTimings(boolean clearTimings) {
        final StringBuilder sb = new StringBuilder("-- Timings " + this.name
                + " (" + this.steps.size() + ") --");

        int step = 1;
        long lastStep = 0;
        if (this.steps.size() >= 2) {
            for (long l : steps) {
                if (step != 1) {
                    sb.append("\n  Step ").append(step).append(". ").append(l - lastStep)
                            .append("ns (").append(FORMAT.format((l - lastStep) / 1e6)).append("ms)");
                }
                lastStep = l;
                step++;
            }
        }
        final long totalNs = this.steps.get(this.steps.size() - 1) - this.steps.get(0);
        sb.append("\n  Total: ").append(totalNs)
                .append("ns (").append(FORMAT.format(totalNs / 1e6)).append("ms)");

        this.logger.info(sb.toString());

        if (clearTimings) {
            this.steps.clear();
        }
    }
}