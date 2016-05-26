package messagesystem;

import game.gamemanagement.gamemessages.GameMessageProcessor;


public final class AddressService {
    private Address inputMessageProcessor;
    private Address outputMessageProcessor;

    public void registerInputProcessor(GameMessageProcessor input) {
        this.inputMessageProcessor = input.getAddress();
    }

    public void registerOutputProcessor(GameMessageProcessor output) {
        this.outputMessageProcessor = output.getAddress();
    }

    public Address getInputProcessorAddress() {
        return inputMessageProcessor;
    }

    public Address getOutputProcessorAddress() {
        return outputMessageProcessor;
    }

}
