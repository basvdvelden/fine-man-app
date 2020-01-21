package nl.management.finance.app;

public class AppOptional<T> {
    private T object;

    public AppOptional(T object) {
        this.object = object;
    }

    public T get() {
        return object;
    }
}
