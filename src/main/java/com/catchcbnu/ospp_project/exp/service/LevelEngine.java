package com.catchcbnu.ospp_project.exp.service;

import com.catchcbnu.ospp_project.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class LevelEngine {

    public int requiredExpForNextLevel(int currentLevel) {
        return 100 + (currentLevel - 1) * 50;
    }

    public int minExpForLevel(int level) {
        if (level <= 1) {
            return 0;
        }

        int totalRequiredExp = 0;

        for (int currentLevel = 1; currentLevel < level; currentLevel++) {
            totalRequiredExp += requiredExpForNextLevel(currentLevel);
        }

        return totalRequiredExp;
    }

    public int nextLevelExp(int currentLevel) {
        return minExpForLevel(currentLevel + 1);
    }

    public int calculateLevelByTotalExp(int totalExp) {
        int level = 1;

        while (totalExp >= nextLevelExp(level)) {
            level++;
        }

        return level;
    }

    public boolean applyExp(User user, int gainedExp) {
        if (gainedExp <= 0) {
            throw new IllegalArgumentException("경험치는 1 이상이어야 합니다.");
        }

        int beforeLevel = user.getLevel();

        user.addExp(gainedExp);

        int calculatedLevel = calculateLevelByTotalExp(user.getExp());
        user.updateLevel(calculatedLevel);

        return calculatedLevel > beforeLevel;
    }

    public int requiredExpToNextLevel(int level, int totalExp) {
        return Math.max(nextLevelExp(level) - totalExp, 0);
    }

    public double progressRate(int level, int totalExp) {
        int currentLevelMinExp = minExpForLevel(level);
        int nextLevelExp = nextLevelExp(level);

        int levelRange = nextLevelExp - currentLevelMinExp;
        int currentProgress = totalExp - currentLevelMinExp;

        if (levelRange <= 0) {
            return 0.0;
        }

        return ((double) currentProgress / levelRange) * 100.0;
    }
}