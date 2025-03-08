package it.polimi.it.galaxytrucker.componenttiles;

/**
 * Represents the possible edge types of a tile in the game, defining their compatibility rules.
 * Each edge type implements {@code isCompatible} to determine if it can connect with another edge.
 *
 * @author Giacomo Amaducci
 * @version 1.0
 */
public enum TileEdge {
    /**
     * An edge with a single connector on it.
     */
    SINGLE {
        /**
         * Determines if this edge is compatible with another {@code TileEdge}.
         *
         * @param other the {@code TileEdge} to compare with
         * @return {@code true} if {@code other} is {@code SINGLE}, {@code UNIVERSAL}, or {@code SMOOTH}; otherwise, {@code false}
         */
        @Override
        public boolean isCompatible(TileEdge other) {
            return (other == SINGLE || other == UNIVERSAL || other == SMOOTH);
        }
    },

    /**
     * An edge with a double connector on it.
     */
    DOUBLE {
        /**
         * Determines if this edge is compatible with another {@code TileEdge}.
         *
         * @param other the {@code TileEdge} to compare with
         * @return {@code true} if {@code other} is {@code DOUBLE}, {@code UNIVERSAL}, or {@code SMOOTH}; otherwise, {@code false}
         */
        @Override
        public boolean isCompatible(TileEdge other) {
            return (other == DOUBLE || other == UNIVERSAL || other == SMOOTH);
        }
    },

    /**
     * An edge with a universal connector that can connect with any other edge type.
     */
    UNIVERSAL {
        /**
         * Determines if this edge is compatible with another {@code TileEdge}.
         *
         * @param other the {@code TileEdge} to compare with
         * @return Always {@code true}, as universal edges are compatible with all types.
         */
        @Override
        public boolean isCompatible(TileEdge other) {
            return true;
        }
    },

    /**
     * An edge with no connectors on it.
     */
    SMOOTH {
        /**
         * Determines if this edge is compatible with another {@code TileEdge}.
         *
         * @param other the {@code TileEdge} to compare with
         * @return Always {@code true}, functionally equivalent to {@code TileEdge.UNIVERSAL}.
         */
        @Override
        public boolean isCompatible(TileEdge other) {
            return true;
        }
    },

    /**
     * Represents an incompatible edge type used for cannon fronts and engine backs, which require an empty cell.
     */
    INCOMPATIBLE {
        /**
         * Determines if this edge is compatible with another {@code TileEdge}.
         *
         * @param other the {@code TileEdge} to compare with
         * @return Always {@code false}, as incompatible edges cannot connect with any other edge.
         */
        @Override
        public boolean isCompatible(TileEdge other) {
            return false;
        }
    };

    /**
     * Determines if this edge is compatible with another {@code TileEdge}.
     * Each edge type defines its own compatibility rules.
     *
     * @param other the {@code TileEdge} to compare with this edge
     * @return {@code true} if the edges are compatible, otherwise {@code false}
     */
    public abstract boolean isCompatible(TileEdge other);
}
