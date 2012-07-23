package scrabble;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Move implements Iterable<Play> {

	Set<Play> plays;
	int score;

	public Move() {
		plays = new HashSet<Play>();

	}

	public Move(Move other) {
		this.plays = new HashSet<Play>(other.plays);
	}

	public void addPlay(Character c, int i, int j) {
		plays.add(new Play(i, j, c));
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public int size() {
		return plays.size();
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		for (Play p : plays) {
			out.append(p.tile + " " + p.i + " " + p.j + "\n");
		}
		return out.toString();
	}

	@Override
	public Iterator<Play> iterator() {
		return plays.iterator();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Move) {
			return plays.equals(((Move) obj).plays);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return plays.hashCode();
	}
}

class Play {

	public final int i;
	public final int j;
	public final Tile tile;

	public Play(int i, int j, Character tile) {
		super();
		this.i = i;
		this.j = j;
		this.tile = Tile.valueOf(tile);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Play) {
			Play that = (Play) obj;
			return this.i == that.i && this.j == that.j
					&& this.tile == that.tile;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (i + " " + j + " " + tile).hashCode();
	}
}
