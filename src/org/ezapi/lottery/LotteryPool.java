package org.ezapi.lottery;

import org.ezapi.util.Loop;

import java.util.*;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

public class LotteryPool {

    private int times = 0;

    private final Map<Double,ItemGroup> itemGroups = new HashMap<>();

    private double totalProbability = 0.0;

    public LotteryPool(ItemGroup... itemGroups) {
        if (itemGroups.length == 0) throw new IllegalArgumentException("Need itemGroups!");
        List<Entry<Double,ItemGroup>> first = new ArrayList<>();
        for (ItemGroup itemGroup : itemGroups) {
            first.add(new SimpleEntry<>(itemGroup.getProbability(), itemGroup));
        }
        first.sort((o1, o2) -> -(o1.getKey().compareTo(o2.getKey())));
        List<Entry<Double,ItemGroup>> list = new ArrayList<>();
        for (Entry<Double,ItemGroup> entry : first) {
            if (entry.getValue().getProbability() <= 0.0) continue;
            if (totalProbability > 100.0) break;
            ItemGroup itemGroup = entry.getValue();
            if (totalProbability + itemGroup.getProbability() > 100.0) {
                itemGroup.setProbability(100.0 - totalProbability);
                totalProbability = 100.0;
            } else {
                totalProbability += itemGroup.getProbability();
            }
            list.add(new SimpleEntry<>(itemGroup.getProbability(), itemGroup));
        }
        for (int i = 0; i < list.size(); i++) {
            Entry<Double,ItemGroup> entry = list.get(i);
            if (i == 0) {
                this.itemGroups.put(entry.getKey(), entry.getValue());
                continue;
            }
            this.itemGroups.put(entry.getKey() + list.get(i - 1).getKey(), entry.getValue());
        }
        System.out.println(this.itemGroups);
    }

    public String newRound() {
        if ((times % 10) == 0) {
            ItemGroup best = new ArrayList<>(itemGroups.entrySet()).get(itemGroups.size() - 1).getValue();
            ItemGroup secondBest = new ArrayList<>(itemGroups.entrySet()).get(itemGroups.size() - 2).getValue();
            int position = new Random().nextInt(5);
            if (position == 0) {
                return best.newRound();
            } else {
                return secondBest.newRound();
            }
        }
        double d = new Random().nextInt(100) + 1;
        double position = (d / 100);
        System.out.println(position);
        for (double prob : itemGroups.keySet()) {
            if (position <= prob) {
                return itemGroups.get(prob).newRound();
            }
        }
        return "unknown";
    }

    public List<String> tenRounds() {
        List<String> list = new ArrayList<>();
        Loop.range(10, (integer -> list.add(newRound())));
        return list;
    }

}
