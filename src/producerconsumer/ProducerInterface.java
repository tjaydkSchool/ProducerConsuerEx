package producerconsumer;

import producerconsumer.ProducerConsumer.Consumer;

public interface ProducerInterface {
    public void registerConsumer(Consumer c);
    public void unregisterConsumer(Consumer c);
    public void notifyConsumers();
}
