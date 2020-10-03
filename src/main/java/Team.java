public class Team {
    private int totalBugs = 0, BlockerBugsCount = 0,
            CriticalBugs = 0, MajorBugs = 0, MediumBugs = 0, MinorBugs = 0, NormalBugs = 0, UnresolvedBugs = 0;
    private Team.teamType type;

    public enum teamType {
        TEAM_BEAUJOLAIS,
        TEAM_REGSERV,
        TEAM_LOIRE,
        TEAM_RHONE,
        TEAM_TECH,
        TEAM_ALSACE,
        MISC
    }

    public void setType(teamType type) {
        this.type = type;
    }

    public teamType getType() {
        return type;
    }

    public int getTotalBugs() {
        return totalBugs;
    }

    public void increaseTotalBugs() {
        this.totalBugs++;
    }

    public int getBlockerBugsCount() {
        return BlockerBugsCount;
    }

    public void increaseBlockerBugsCount() {
        BlockerBugsCount++;
    }

    public int getCriticalBugs() {
        return CriticalBugs;
    }

    public void increaseCriticalBugs() {
        CriticalBugs++;
    }

    public int getMajorBugs() {
        return MajorBugs;
    }

    public void increaseMajorBugs() {
        MajorBugs++;
    }

    public int getMediumBugs() {
        return MediumBugs;
    }

    public void increaseMediumBugs() {
        MediumBugs++;
    }

    public int getMinorBugs() {
        return MinorBugs;
    }

    public void increaseMinorBugs() {
        MinorBugs++;
    }

    public int getNormalBugs() {
        return NormalBugs;
    }

    public void increaseNormalBugs() {
        NormalBugs++;
    }

    public int getUnresolvedBugs() {
        return UnresolvedBugs;
    }

    public void increaseUnresolvedBugs() {
        UnresolvedBugs++;
    }
}