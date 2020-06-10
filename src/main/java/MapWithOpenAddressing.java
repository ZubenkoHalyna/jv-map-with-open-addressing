public interface MapWithOpenAddressing {
    void put(int key, long value);

    long get(int key);

    int getSize();
}
