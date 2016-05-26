package messagesystem;

import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("InstanceVariableNamingConvention")
public class Address {
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger();
    private final int id;

    public Address(){
        id = ID_GENERATOR.getAndIncrement();
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Address
                && this.hashCode() == other.hashCode();
    }
}
