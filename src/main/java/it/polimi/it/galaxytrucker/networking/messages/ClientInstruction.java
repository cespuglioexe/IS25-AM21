package it.polimi.it.galaxytrucker.networking.messages;

import java.io.Serializable;

public class ClientInstruction implements Serializable {
    private final InstructionType instructionType;

    public ClientInstruction(InstructionType instructionType) {
        this.instructionType = instructionType;
    }

    public InstructionType getInstructionType() {
        return instructionType;
    }
}
