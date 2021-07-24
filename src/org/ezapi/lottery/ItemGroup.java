package org.ezapi.lottery;

import java.util.*;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

public class ItemGroup {

    private final int level;

    private double probability;

    private final Map<Double,Item> items = new HashMap<>();

    private double totalProbability = 0.0;

    public ItemGroup(int level, double probability) {
        this.level = level;
        this.probability = probability;
    }

    public void set(Item... items) {
        if (items.length == 0) return;
        for (Item item : items) {
            if (item.getProbability() <= 0.0) return;
            if (totalProbability > 100.0) return;
            if (totalProbability + item.getProbability() > 100.0) {
                item.setProbability(100.0 - totalProbability);
                totalProbability = 100.0;
            } else {
                totalProbability += item.getProbability();
            }
            this.items.put(totalProbability, item);
            List<Entry<Double,Item>> list = new ArrayList<>();
            for (Entry<Double,Item> entry : this.items.entrySet()) {
                list.add(new SimpleEntry<>(entry.getValue().getProbability(), entry.getValue()));
            }
            list.sort((o1, o2) -> -(o1.getKey().compareTo(o2.getKey())));
            System.out.println(list);
            this.items.clear();
            for (int i = 0; i < list.size(); i++) {
                Entry<Double,Item> entry = list.get(i);
                if (i == 0) {
                    this.items.put(entry.getKey(), entry.getValue());
                    continue;
                }
                this.items.put(entry.getKey() + list.get(i - 1).getKey(), entry.getValue());
            }
            System.out.println(this.items);
        }
    }

    public String newRound() {
        double d = new Random().nextInt(100);
        double position = (d / 100);
        for (double prob : items.keySet()) {
            if (position <= prob) {
                return items.get(prob).getId();
            }
        }
        return "unknown";
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }

    public int getLevel() {
        return level;
    }

    public String toString() {
        return items.toString();
    }

}
