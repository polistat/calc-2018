package bigmood;

import dataholder.District;

/**
 * Calculates the national shift from the democratic percent of the vote on the national generic ballot.
 */
public interface NationalShiftCalculator {

    /**
     * Get a national shift function.
     *
     * @param districts         A list of every district, with the fundamental dem win percent filled in.
     * @return a class that calculates the national shift
     */
    NationalShiftFunction getFunction(District[] districts);
}
