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
         * @return {@code true} if {@code other} is {@code SINGLE}, {@code UNIVERSAL}; otherwise, {@code false}
         */
        @Override
        public boolean isCompatible(TileEdge other) {
            return (other == SINGLE || other == UNIVERSAL);
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
         * @return {@code true} if {@code other} is {@code DOUBLE}, {@code UNIVERSAL}; otherwise, {@code false}
         */
        @Override
        public boolean isCompatible(TileEdge other) {
            return (other == DOUBLE || other == UNIVERSAL);
        }
    },

    /**
     * An edge with a universal connector that can connect with any other connector type.
     */
    UNIVERSAL {
        /**
         * Determines if this edge is compatible with another {@code TileEdge}.
         *
         * @param other the {@code TileEdge} to compare with
         * @return {@code true} if {@code other} is not {@code SMOOTH}; otherwise, {@code false}
         */
        @Override
        public boolean isCompatible(TileEdge other) {
            return (other != SMOOTH);
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
         * @return Always {@code true}, as smooth edges do not restrict connections.
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
