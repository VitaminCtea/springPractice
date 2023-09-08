package spring;

import java.util.Objects;

public class RedBlackTreeOriginalVersion<Key extends Comparable<Key>, Value> {
    private enum RedBlackColor { RED, BLACK }

    private final RedBlackNode nilNode = new RedBlackNode(null, null);
    private RedBlackNode root;
    private int size;

    public class RedBlackNode implements Cloneable {
        public Key key;
        public Value value;
        public RedBlackColor color;
        public RedBlackNode parent;
        public RedBlackNode left;
        public RedBlackNode right;

        public RedBlackNode(Key key, Value value) { this(key, value, RedBlackColor.BLACK, null, null, null); }
        public RedBlackNode(Key key, Value value, RedBlackColor color, RedBlackNode parent, RedBlackNode left, RedBlackNode right) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        @Override public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            RedBlackNode other = (RedBlackNode) obj;
            if (value == null) return other.value == null;
            return value.equals(other.value);
        }

        @Override protected RedBlackNode clone() {
            try {
                RedBlackNode newNode = (RedBlackNode) super.clone();
                newNode.color = color;
                return newNode;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            return new RedBlackNode(null, null);
        }
    }

    public void put(Key key, Value value) { put(this.root, key, value); }

    private void put(RedBlackNode node, Key key, Value value) {
        size++;
        if (node == null) {
            root = createRedBlackNode(key, value, nilNode, createLeafNode(), createLeafNode());
            return;
        }

        RedBlackNode insertParentNode = findInsertParentNode(node, key);
        RedBlackNode newNode = createRedBlackNode(key, value, insertParentNode, createLeafNode(), createLeafNode());
        setChildNode(insertParentNode, newNode);

        node.parent = nilNode;

        insertRBFixup(newNode); // 修复操作
    }

    private RedBlackNode createRedBlackNode(Key key, Value value, RedBlackNode parent, RedBlackNode left, RedBlackNode right) {
        return new RedBlackNode(key, value, RedBlackColor.RED, parent, left, right);
    }

    private RedBlackNode createLeafNode() { return nilNode.clone(); }

    private RedBlackNode findInsertParentNode(RedBlackNode node, Key key) {
        RedBlackNode insertParentNode = null;
        for (RedBlackNode searchInsertNode = node; !isLeaf(searchInsertNode);) {
            insertParentNode = searchInsertNode;
            int cmp = key.compareTo(searchInsertNode.key);
            if (cmp < 0) searchInsertNode = searchInsertNode.left;
            else searchInsertNode = searchInsertNode.right;
        }

        return insertParentNode;
    }

    private void setChildNode(RedBlackNode parent, RedBlackNode newNode) {
        if (isGreater(parent, newNode)) parent.left = newNode;
        else parent.right = newNode;
    }

    private boolean isGreater(RedBlackNode parent, RedBlackNode newNode) { return parent.key.compareTo(newNode.key) > 0; }

    private void insertRBFixup(RedBlackNode currentNode) {
        while (currentNode.parent != root && isRedNode(currentNode.parent)) {   // 只有当新插入的父节点是红色节点时，进行修复操作
            RedBlackNode parent = currentNode.parent;
            RedBlackNode grandParent = parent.parent;
            RedBlackNode uncle = parent == grandParent.left ? grandParent.right : grandParent.left;

            // 当叔叔节点是红色时，此时只需将新节点的父亲和叔叔节点涂黑，爷爷节点涂红，以便于下一次以爷爷节点开始，直到根节点为止
            if (isRedNode(uncle)) currentNode = flipColors(parent, uncle);
            else {
                // 当叔叔节点是黑色时，此时就需要进行旋转操作，又因为可能会出现父亲相较于爷爷以及儿子相较于父亲子节点方向不一致情况
                // 所以先预先变为右右或左左情况，然后在进行左旋或右旋
                if (parent == grandParent.left && currentNode == parent.right) {    // 父亲是爷爷的左子节点并且儿子节点是父亲的右子节点
                    currentNode = parent;   // 和原父亲节点交换位置，旋转后，位置会调换
                    parent = rotateLeft(currentNode);   // 旋转后currentNode还是孙节点，而parent指向应该修正为旋转后的新的父亲节点
                } else if (parent == grandParent.right && currentNode == parent.left) {     // 父亲是爷爷的右子节点并且儿子节点是父亲的左子节点
                    currentNode = parent;
                    parent = rotateRight(currentNode);
                }

                // 下一步旋转完之后，循环会终止，因为currentNode的父亲节点已经是黑色了，所以不要在进行修复了
                if (currentNode == parent.right && parent == grandParent.right) rotateLeft(grandParent);
                else if (currentNode == parent.left && parent == grandParent.left) rotateRight(grandParent);

                // 爷爷和父亲的颜色进行互换，这样以父节点成为这棵子树的根节点，爷爷变为儿子，孙子不变
                RedBlackColor color = parent.color;
                parent.color = grandParent.color;
                grandParent.color = color;
            }
        }

        root.color = RedBlackColor.BLACK;   // 坚守第一原则，根节点永远是黑色
    }

    private boolean isRedNode(RedBlackNode node) { return node.color == RedBlackColor.RED; }

    private RedBlackNode flipColors(RedBlackNode parent, RedBlackNode uncle) {
        // 只需把父亲节点和叔叔节点涂黑，爷爷节点涂成红色，因为下一个要修复的节点是爷爷这个节点
        RedBlackNode grandParent = parent.parent;
        parent.color = RedBlackColor.BLACK;
        uncle.color = RedBlackColor.BLACK;
        grandParent.color = RedBlackColor.RED;
        return grandParent;
    }

    private RedBlackNode rotateLeft(RedBlackNode node) {
        RedBlackNode nodeOrientation = determineNodeOrientation(node);    // 查看node所在的位置，以便后续将x的parent指向修正
        RedBlackNode x = node.right;
        x.parent = node.parent;
        node.right = x.left;

        if (!isLeaf(node.right)) node.right.parent = node;    // 修正x原左子节点指向node，因为此时node已经成为x原左子节点的父节点了，右旋同理

        x.left = node;
        node.parent = x;

        rotateAfterSetChild(x, nodeOrientation); // 修正新的节点应该是原爷爷节点在爷爷的父亲节点所在的位置
        return x;
    }

    private RedBlackNode rotateRight(RedBlackNode node) {
        RedBlackNode nodeOrientation = determineNodeOrientation(node);
        RedBlackNode x = node.left;
        x.parent = node.parent;
        node.left = x.right;

        if (!isLeaf(node.left)) node.left.parent = node;

        x.right = node;
        node.parent = x;

        rotateAfterSetChild(x, nodeOrientation);
        return x;
    }

    private void rotateAfterSetChild(RedBlackNode x, RedBlackNode nodeOrientation) {
        if (!isLeaf(x.parent)) {
            if (nodeOrientation == x.parent.left) x.parent.left = x;
            else x.parent.right = x;
        } else root = x;
    }

    // 确定旋转节点的方向
    private RedBlackNode determineNodeOrientation(RedBlackNode node) {
        if (node == node.parent.left) return node.parent.left;
        return node.parent.right;
    }

    public int getSize() { return size; }

    public Value getValue(Key key) {
        RedBlackNode node = getNode(root, key);
        if (node == null) return null;
        return node.value;
    }

    private RedBlackNode getNode(RedBlackNode node, Key key) {
        for (RedBlackNode n = node; !isLeaf(n);) {
            int cmp = key.compareTo(n.key);
            if (cmp < 0) n = n.left;
            else if (cmp > 0) n = n.right;
            else return n;
        }

        return null;
    }

    public RedBlackNode delete(Key key) { return delete(root, key); }

    private RedBlackNode delete(RedBlackNode node, Key key) {
        // 删除情况实际是把看似删除的节点转换为删除其他节点，比如删除一个节点，而这个节点有两个节点时，如果这两个子节点都没有子节点，那么就变成了删除节点的左子节点(从左子树中找最大)
        // 第一种情况，待删除的节点只有一个子节点(不分左右)，此时待删除的节点被删除后，这个唯一的子节点就会子承父业
        // 第二种情况是有两个孩子，所以要在子树(左子树或右子树)中找到一个能够子承父业的节点，然后该删除删除，该把链接修正的修正
        RedBlackNode deleteNode = getNode(node, key);
        if (deleteNode == null) return null;

        // 实际删除的节点
        RedBlackNode actuallyDeleteNode = isLeaf(deleteNode.left) ? deleteNode.right : isLeaf(deleteNode.right) ? deleteNode.left : null;
        RedBlackNode replaceNode = deleteNode;
        RedBlackColor replaceNodeColor = replaceNode.color;

        // 有两种情况，情况1：确实只有一个子节点(不分左右)，情况2：有两个子节点
        if (actuallyDeleteNode != null) deleteNode(deleteNode, actuallyDeleteNode);    // 假设按照情况1进行处理，子节点直接顶替
        else {
            // 有两个子节点时
            replaceNode = findDeleteNodeLeftChildMaximumNode(deleteNode); // 从左子树中找到最大的节点(亦或从右子树中找到最小的节点)对deleteNode进行顶替
            replaceNodeColor = replaceNode.color;

            actuallyDeleteNode = replaceNode.left;  // 取左子节点对replaceNode替换

            if (replaceNode.parent == deleteNode) {
                actuallyDeleteNode.parent = replaceNode;    // 所有的leaf节点没有对parent进行指定，所以这里需要进行修正，以便于后续的修复操作正常的往上进行
            } else {
                // 说明左子树不止一个节点，所以需要找到正确删除的位置(即右侧)
                // 情况1：可能replaceNode的兄弟节点是红色，但replaceNode的兄弟节点没有任何子节点
                deleteNode(replaceNode, actuallyDeleteNode);    // 让actuallyDeleteNode节点替换删除的节点
                replaceNode.left = deleteNode.left;   // 把删除节点的左子树给替换节点
                replaceNode.left.parent = replaceNode;
            }

            // 将替换上的节点指向修正
            deleteNode(deleteNode, replaceNode);    // 将替换的节点放到deleteNode的位置上

            // 把删除节点的右子树给替换节点
            replaceNode.right = deleteNode.right;
            replaceNode.right.parent = replaceNode;
            replaceNode.color = deleteNode.color; // 保留原deleteNode的节点颜色
        }

        size--;

        if (size == 0) {
            clear();
            return null;
        }

        if (replaceNodeColor == RedBlackColor.BLACK) deleteRBFixup(actuallyDeleteNode); // 只有删除黑节点才进行删除操作，红色不需要修复

        return deleteNode;
    }

    private RedBlackNode findDeleteNodeLeftChildMaximumNode(RedBlackNode deleteNode) {
        RedBlackNode maximumNode = deleteNode.left;
        for (; !isLeaf(maximumNode) && !isLeaf(maximumNode.right); maximumNode = maximumNode.right);
        return maximumNode;
    }

    private void deleteNode(RedBlackNode deleteNode, RedBlackNode newNode) {
        if (isLeaf(deleteNode.parent)) root = newNode;
        else if (deleteNode == deleteNode.parent.left) deleteNode.parent.left = newNode;
        else deleteNode.parent.right = newNode;

        newNode.parent = deleteNode.parent;
    }

    private void deleteRBFixup(RedBlackNode node) {
        // 情况1：兄弟红，兄弟两个孩子黑
        // 情况2：兄弟黑，无子节点
        // 情况3：兄弟黑，兄弟左子节点红
        // 情况4：兄弟黑，兄弟右子节点红
        // 情况5：兄弟黑，兄弟两个孩子红
        // 意外情况：node和node.parent都为红节点，出现连续节点情况，此时只需把node涂黑即可
        // 为啥是node涂黑，而不是node.parent涂黑？很简单，node是一个红节点，那么被替换上去的父亲节点必定是黑色
        // 假设左侧的叔叔节点没有孩子，那么叔叔节点也必然是黑色节点，如果是叔叔是红色的，则违反红黑树性质5，所以只要把node涂黑就行了
        // node.parent此时可黑可红，要根据其他节点情况进行判断，简单情况就是可黑可红

        while (node != root && !isRedNode(node)) {
            if (node == node.parent.left) {
                RedBlackNode brotherNode = node.parent.right;

                // 兄红，父必为黑
                if (isRedNode(brotherNode)) {   // 如果兄弟节点是红色的话，则只会出现一种情况，即：有两个黑色的子节点
                    rotateLeft(node.parent);
                    /*
                    *                    黑(node parent)                                      红(brother node)                              黑
                    *              /           \                                     /               \                               /            \
                    *      黑(deleted node)   红(brother node)     ----->           黑(node parent)    黑         ----->           红                黑
                    *                           / \                           /         \                                    /         \
                    *                          黑  黑                  黑(deleted node)   黑                         黑(deleted node)    黑(New brother node)
                    * */
                    brotherNode.color = RedBlackColor.BLACK;
                    node.parent.color = RedBlackColor.RED;
                    brotherNode = node.parent.right;    // 兄弟节点变为了原兄弟节点的左侄子节点
                }

                // 黑色兄弟节点，有两种情况，情况1：要么没有节点，情况2：要么都是红节点
                // 这里主要是针对情况1，情况2的出现是上述把兄弟节点转换成黑色节点后所产生的这种结果，这两种结果解决办法都是一个，即：兄弟变为红色，变色红可以确定父亲必然是黑色
                if (!isRedNode(brotherNode.left) && !isRedNode(brotherNode.right)) {
                    brotherNode.color = RedBlackColor.RED;
                    node = node.parent;
                } else if (!isLeaf(brotherNode)) {
                    if (isRedNode(brotherNode.left) && isLeaf(brotherNode.right)) { // 情况3：兄弟节点有左子红节点
                        rotateRight(brotherNode);
                        brotherNode.left.color = RedBlackColor.BLACK;
                        brotherNode.color = RedBlackColor.RED;
                        brotherNode = node.parent.right;
                    }

                    // 两种情况都是一个操作，情况4：兄弟节点有红右子节点，情况5：兄弟节点有两个红子节点
                    rotateLeft(brotherNode.parent);
                    brotherNode.color = node.parent.color;  // 此时的brotherNode可黑可红，需要保持原来的颜色
                    brotherNode.left.color = RedBlackColor.BLACK;
                    brotherNode.right.color = RedBlackColor.BLACK;

                    node = root;
                }
            } else {
                RedBlackNode brotherNode = node.parent.left;

                if (isRedNode(brotherNode)) {
                    brotherNode.color = RedBlackColor.BLACK;
                    node.parent.color = RedBlackColor.RED;
                    rotateRight(node.parent);
                    brotherNode = node.parent.left;
                }

                if (!isRedNode(brotherNode.left) && !isRedNode(brotherNode.right)) {
                    brotherNode.color = RedBlackColor.RED;
                    node = node.parent;
                } else if (!isLeaf(brotherNode)) {
                    if (isRedNode(brotherNode.right) && isLeaf(brotherNode.left)) {
                        rotateLeft(brotherNode);
                        brotherNode.right.color = RedBlackColor.BLACK;
                        brotherNode.color = RedBlackColor.RED;
                        brotherNode = node.parent.left;
                    }
                    rotateRight(brotherNode.parent);
                    brotherNode.color = node.parent.color;
                    brotherNode.left.color = RedBlackColor.BLACK;
                    brotherNode.right.color = RedBlackColor.BLACK;
                    node = root;
                }
            }
        }
        node.color = RedBlackColor.BLACK;   // 保证根节点是黑色的，并有可能node和node.parent都是红色的情况
    }

    private boolean isLeaf(RedBlackNode node) { return Objects.equals(node, nilNode) && node.value == null; }

    public boolean isRBTree() { return !isRedNode(root) && checkBalance(root) && checkBalance(root, 0, -1); }

    private boolean checkBalance(RedBlackNode node) {
        if (isLeaf(node)) return true;
        if (isRedNode(node) && !isLeaf(node.parent) && isRedNode(node.parent)) {
            System.out.println("存在连续的红色节点, 出错的节点为: " + node.parent.value);
            return false;
        }

        return checkBalance(node.left) && checkBalance(node.right);
    }

    private boolean checkBalance(RedBlackNode node, int count, int num) {
        if (node == null) return true;
        if (!isRedNode(node)) count++;
        if (isLeaf(node)) { // 在一个子树中，其中的一个路径已经走完，则需要对比另一条路径上的值
            if (num == -1) num = count; // 记录黑色节点的数量
            else {
                if (num != count) return false;
            }
        }

        // 相当于前序遍历(根左右)
        return checkBalance(node.left, count, num) && checkBalance(node.right, count, num);
    }

    public void inOrder() { inOrder(root); }

    private void inOrder(RedBlackNode node) {
        if (isLeaf(node)) return;

        inOrder(node.left);

        if (isRedNode(node)) System.out.print("\033[31m" + node.key + "\033[0m");
        else System.out.print("\033[36m" + node.key + "\033[0m");

        inOrder(node.right);
    }

    public void clear() { root = null; }
}
