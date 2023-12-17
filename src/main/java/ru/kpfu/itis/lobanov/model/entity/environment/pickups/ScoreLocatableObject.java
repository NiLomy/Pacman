package ru.kpfu.itis.lobanov.model.entity.environment.pickups;

abstract class ScoreLocatableObject extends LocatableObject {
    protected int score;

    public ScoreLocatableObject(double x, double y) {
        super(x, y);
    }

    public ScoreLocatableObject(double x, double y, int score) {
        super(x, y);
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScoreLocatableObject that = (ScoreLocatableObject) o;

        return score == that.score;
    }

    @Override
    public int hashCode() {
        return score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
