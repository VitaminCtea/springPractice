package spring;

public class RedBlackTree<Key extends Comparable<Key>, Value> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;

    private class Node {
        private Key key;
        private Value value;
        private int N;
        private Node left;
        private Node right;

        private boolean color = RED;

        public Node(Key key, Value value, int N) {
            this.key = key;
            this.value = value;
            this.N = N;
        }
    }

    private boolean isRed(Node node) { return node != null && node.color == RED; }

    public void put(Key key, Value value) {
        this.root = put(this.root, key, value);
        this.root.color = BLACK;
    }

    public Value get(Key key) { return get(this.root, key); }

    public Key min() { return min(this.root).key; }

    public Key floor(Key key) {
        Node node = floor(this.root, key);
        if (node == null) return null;
        return node.key;
    }

    private Node put(Node node, Key key, Value value) {
        if (node == null) return new Node(key, value, 1);

        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = put(node.left, key, value);
        else if (cmp > 0) node.right = put(node.right, key, value);
        else node.value = value;

        if (isRed(node.right) && isRed(node.right.right)) node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left)) node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right)) flipColors(node);

        node.N = size(node.left) + size(node.right) + 1;

        return node;
    }

    private Node rotateLeft(Node node) {
        Node x = node.right;
        node.right = x.left;
        x.left = node;
        return collateNodesAfterRotation(x, node);
    }

    private Node rotateRight(Node node) {
        Node x = node.left;
        node.left = x.right;
        x.right = node;
        return collateNodesAfterRotation(x, node);
    }

    private Node collateNodesAfterRotation(Node x, Node node) {
        x.color = node.color;
        node.color = RED;
        x.N = node.N;
        node.N = 1 + size(node.left) + size(node.right);
        return x;
    }

    private int size(Node node) { return node == null ? 0 : node.N; }

    private void flipColors(Node node) {
        node.color = RED;
        node.left.color = BLACK;
        node.right.color = BLACK;
    }

    private Value get(Node node, Key key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);

        if (cmp < 0) return get(node.left, key);
        else if (cmp > 0) return get(node.right, key);
        return node.value;
    }

    private Node min(Node node) { return node.left == null ? node : min(node.left); }

    private Node floor(Node node, Key key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp == 0) return node;

        if (cmp < 0) return floor(node.left, key);

        Node t = floor(node.right, key);
        return t != null ? t : node;
    }
}
