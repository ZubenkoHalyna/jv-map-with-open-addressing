public class MapWithOpenAddressingImpl implements MapWithOpenAddressing {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.7f;
    private static final long DEFAULT_NULL_VALUE = -1;

    private long nullValue;
    private int size;
    private float loadFactor;
    private Node[] data;

    public MapWithOpenAddressingImpl() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_NULL_VALUE);
    }

    public MapWithOpenAddressingImpl(int initialCapacity, float loadFactor, long nullValue) {
        this.loadFactor = loadFactor;
        this.nullValue = nullValue;
        data = new Node[initialCapacity];
    }

    @Override
    public void put(int key, long value) {
        if (size >= data.length * loadFactor) {
            resize();
        }
        for (int index = getIndex(key); ; ) {
            if (data[index] == null) {
                data[index] = new Node(key, value);
                size++;
                break;
            }
            if (data[index].key == key) {
                data[index].value = value;
                break;
            }
            index++;
            if (index == data.length) {
                index = 0;
            }
        }
    }

    @Override
    public long get(int key) {
        for (int i = 0; i < data.length; i++) {
            Node node = data[getIndex(key + i)];
            if (node != null && node.key == key) {
                return node.value;
            }
        }
        return nullValue;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return data.length;
    }

    private int getIndex(int key) {
        return Math.abs(key % data.length);
    }

    private void resize() {
        Node[] oldData = data;
        data = new Node[data.length * 2];

        for (Node oldNode : oldData) {
            if (oldNode == null) {
                continue;
            }
            for (int newIndex = getIndex(oldNode.key); ; ) {
                if (data[newIndex] == null) {
                    data[newIndex] = oldNode;
                    break;
                }
                newIndex++;
                if (newIndex == data.length) {
                    newIndex = 0;
                }
            }
        }
    }

    private static class Node {
        private int key;
        private long value;

        private Node(int nodeKey, long nodeValue) {
            key = nodeKey;
            value = nodeValue;
        }
    }
}
