package it.polimi.it.galaxytrucker.componenttiles;

enum TileEdge {
    SINGLE {
        @Override
        public boolean isCompatible(TileEdge other) {
            return other == SINGLE || other == UNIVERSAL || other == SMOOTH;
        }
    },
    DOUBLE {
        @Override
        public boolean isCompatible(TileEdge other) {
            return other == DOUBLE || other == UNIVERSAL || other == SMOOTH;
        }
    },
    UNIVERSAL {
        @Override
        public boolean isCompatible(TileEdge other) {
            return true;
        }
    },
    // and edge with no connectors on it. Functionally equivalent to a UNIVERSAL connector but keeps
    // other connectors exposed
    SMOOTH {
        @Override
        public boolean isCompatible(TileEdge other) {
            return true;
        }
    },
    // INCOMPATIBLE is the edge type used for cannon fronts and engine backs, which require an empty cell
    INCOMPATIBLE {
        @Override
        public boolean isCompatible(TileEdge other) {
            return false;
        }
    };

    // returns, for each edge type, whether it's compatible with the type passed as a parameter
    public abstract boolean isCompatible(TileEdge other);
}
