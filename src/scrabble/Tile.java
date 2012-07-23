package scrabble;

public enum Tile {

	BLANK('#', 0), A('a', 1), E('e', 1), I('i', 1), L('l', 1), N('n', 1), O(
			'o', 1), R('r', 1), S('s', 1), T('t', 1), U('u', 1), D('d', 2), G(
			'g', 2), B('b', 3), C('c', 3), M('m', 3), P('p', 3), F('f', 4), H(
			'h', 4), V('v', 4), W('w', 4), Y('y', 4), K('k', 5), J('j', 8), X(
			'x', 8), Q('q', 10), Z('z', 10), NULL('*', 0);

	public final char character;
	public final int score;

	private Tile(char character, int score) {
		this.character = character;
		this.score = score;
	}

	public static Tile valueOf(Character c) {
		for (Tile t : Tile.values()) {
			if (t.character == c) {
				return t;
			}
		}
		return Tile.NULL;
	}
}
