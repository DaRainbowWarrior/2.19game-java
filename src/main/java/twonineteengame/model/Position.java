package twonineteengame.model;

/**
 *  Stores a position on the two-dimensional board more conveniently.
 * @param row
 * @param col
 */
public record Position(int row, int col) {

    /**
     *  Creates a human-readable version of the record's data.
     * @return formatted version of record
     */
    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
