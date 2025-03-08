package it.polimi.it.galaxytrucker.componenttiles;

public enum TileEdge {
    SINGLE {
        /*
         *  An edge with a single connector on it
         *
         *  @return {@code true} or {@code false} based on the parameter {@code other}
         */
        @Override
        public boolean isCompatible(TileEdge other) {
            return (other == SINGLE || other == UNIVERSAL || other == SMOOTH);
        }
    },
    DOUBLE {
        /*
         *  An edge with a double connector on it
         *
         *  @return {@code true} or {@code false} based on the parameter {@code other}
         */
        @Override
        public boolean isCompatible(TileEdge other) {
            return (other == DOUBLE || other == UNIVERSAL || other == SMOOTH);
        }
    },
    UNIVERSAL {
        /*
         *  An edge a universal connector
         *
         *  @return Always {@code true}
         */
        @Override
        public boolean isCompatible(TileEdge other) {
            return true;
        }
    },
    SMOOTH {
        /*
         *  An edge with no connectors on it
         *
         *  @return Always {@code true}, functionally equivalent to {@code TileEdge.UNIVERSAL}
         */
        @Override
        public boolean isCompatible(TileEdge other) {
            return true;
        }
    },
    INCOMPATIBLE {
        /*
         *  INCOMPATIBLE is the edge type used for cannon fronts and engine backs, which require an empty cell
         *
         *  @return Always {@code false}
         */
        @Override
        public boolean isCompatible(TileEdge other) {
            return false;
        }
    };

    /*
     *  Each edge type defines the function based on specific requirements
     *
     *  @param  A {@code TileEdge} to be compared with {@code this} to determine compatibility
     *  @return {@code true} or {@code false} based on the parameter {@code other}
     */
    public abstract boolean isCompatible(TileEdge other);
}
