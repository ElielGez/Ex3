package gameClient;

public class ExpectedResults {
	/**
	 * This class holds the expected moves and grade on each stage
	 * In addition , I manage from here the modulo and sleep time for every stage
	 */
	public static final int[] moves = { 290/* 0 */, 580/* 1 */, Integer.MAX_VALUE/* 2 */, 580/* 3 */,
			Integer.MAX_VALUE/* 4 */, 500/* 5 */, Integer.MAX_VALUE/* 6 */, Integer.MAX_VALUE/* 7 */,
			Integer.MAX_VALUE/* 8 */, 580/* 9 */, Integer.MAX_VALUE/* 10 */, 580/* 11 */, Integer.MAX_VALUE/* 12 */,
			580/* 13 */, Integer.MAX_VALUE/* 14 */, Integer.MAX_VALUE/* 15 */, 290/* 16 */, Integer.MAX_VALUE/* 17 */,
			Integer.MAX_VALUE/* 18 */, 580/* 19 */, 290/* 20 */, Integer.MAX_VALUE/* 21 */, Integer.MAX_VALUE/* 22 */,
			1140/* 23 */ };
	public static final int[] grade = { 125/* 0 */, 436/* 1 */, Integer.MIN_VALUE/* 2 */, 713/* 3 */,
			Integer.MIN_VALUE/* 4 */, 570/* 5 */, Integer.MIN_VALUE/* 6 */, Integer.MIN_VALUE/* 7 */,
			Integer.MIN_VALUE/* 8 */, 480/* 9 */, Integer.MIN_VALUE/* 10 */, 1050/* 11 */, Integer.MIN_VALUE/* 12 */,
			310/* 13 */, Integer.MIN_VALUE/* 14 */, Integer.MIN_VALUE/* 15 */, 235/* 16 */, Integer.MIN_VALUE/* 17 */,
			Integer.MIN_VALUE/* 18 */, 250/* 19 */, 200/* 20 */, Integer.MIN_VALUE/* 21 */, Integer.MIN_VALUE/* 22 */,
			1000/* 23 */ };
	public static final int[] sleep = { 10/* 0 */, 10/* 1 */, 10/* 2 */, 10/* 3 */, 10/* 4 */, 10/* 5 */, 10/* 6 */,
			10/* 7 */, 10/* 8 */, 10/* 9 */, 10/* 10 */, 50/* 11 */, 10/* 12 */, 52/* 13 */, 10/* 14 */, 10/* 15 */,
			50/* 16 */, 10/* 17 */, 10/* 18 */, 50/* 19 */, 20/* 20 */, 10/* 21 */, 10/* 22 */, 10/* 23 */ };

	public static final int[] mod = { 10/* 0 */, 10/* 1 */, 2/* 2 */, 6/* 3 */, 2/* 4 */, 6/* 5 */, 2/* 6 */, 2/* 7 */,
			2/* 8 */, 9/* 9 */, 2/* 10 */, 2/* 11 */, 2/* 12 */, 2/* 13 */, 2/* 14 */, 2/* 15 */, 2/* 16 */, 2/* 17 */,
			2/* 18 */, 2/* 19 */, 4/* 20 */, 2/* 21 */, 2/* 22 */, 6/* 23 */ };
}
