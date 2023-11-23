package bstu.akudrenko.utils;

public class Tuple<T, U> {
    private T first;
    private U second;

    public Tuple(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public static <T, U> Tuple<T, U> of(T fst, U snd) {
        return new Tuple<>(fst, snd);
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }
}
