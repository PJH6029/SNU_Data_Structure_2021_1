package HW5;

// 수업 pdf 참고함
public class AVLTree<K extends Comparable<K>, P extends Comparable<P>> {

    public int numItems;
    private AVLNode<K, P> root;
    final static AVLNode NIL = new AVLNode(null, null, null, null, 0);

    public AVLTree() {
        root = NIL;
        numItems = 0;
    }

    public void insert(K key, P pair) {
        root = insertItem(root, key, pair);
        numItems++;

    }

    private AVLNode<K, P> insertItem(AVLNode<K, P> tNode, K key, P pair) {
        int type;
        if (tNode == NIL) {
            tNode = new AVLNode<>(key, new LinkedList<P>());
            tNode.insertPair(pair);
        } else if (key.compareTo(tNode.key) == 0) {
            // 이미 있던 AVLNode와 일치
            tNode.insertPair(pair);
        } else if (key.compareTo(tNode.key) < 0) {
            // branch left
            tNode.left = insertItem(tNode.left, key, pair);
            tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);

            type = needBalance(tNode);

            if (type != NO_NEED) {
                tNode = balanceAVL(tNode, type);
            }
        } else {
            // branch right
            tNode.right = insertItem(tNode.right, key, pair);
            tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);

            type = needBalance(tNode);

            if (type != NO_NEED) {
                tNode = balanceAVL(tNode, type);
            }
        }
        return tNode;
    }

    public LinkedList<P> search(K key) {
        AVLNode<K, P> resultNode = searchItem(root, key);
        if (resultNode == NIL) {
            return null;
        } else {
            return resultNode.item;
        }
    }

    private AVLNode<K, P> searchItem(AVLNode<K, P> tNode, K key) {
        if (tNode == NIL) return NIL;
        else if (key.compareTo(tNode.key) == 0) return tNode;
        else if (key.compareTo(tNode.key) < 0) return searchItem(tNode.left, key);
        else return searchItem(tNode.right, key);
    }



    private final int LL = 1, LR = 2, RR = 3, RL = 4, NO_NEED = 0, ILLEGAL = -1;
    private int needBalance(AVLNode<K, P> node) {
        int type = ILLEGAL;
        if (node.left.height + 2 <= node.right.height) {
            // type R
            if (node.right.left.height <= node.right.right.height) type = RR;
            else type = RL;
        } else  if (node.left.height >= node.right.height + 2) {
            // type L
            if (node.left.left.height >= node.left.right.height) type = LL;
            else type = LR;
        } else type = NO_NEED;

        return type;
    }

    public String preOrderAsString() {
        return preOrderHelper(root, new StringBuilder()).toString();
    }

    public StringBuilder preOrderHelper(AVLNode<K, P> node, StringBuilder sb) {
        if (node == NIL) return sb;

        sb.append(node.key.toString());
        sb.append(" ");
        preOrderHelper(node.left, sb);
        preOrderHelper(node.right, sb);

        return sb;
    }


    private AVLNode<K, P> balanceAVL(AVLNode<K, P> tNode, int type) {
        AVLNode<K, P> returnNode = NIL;
        switch(type) {
            case LL:
                returnNode = rightRotate(tNode);
                break;
            case LR:
                tNode.left = leftRotate(tNode.left);
                returnNode = rightRotate(tNode);
                break;
            case RR:
                returnNode = leftRotate(tNode);
                break;
            case RL:
                tNode.right = rightRotate(tNode.right);
                returnNode = leftRotate(tNode);
                break;
            default:
                System.out.println("impossible type. should be one of LL, LR, RR, RL");
                break;
        }

        return returnNode;
    }

    private AVLNode<K, P> leftRotate(AVLNode<K, P> node) {
        AVLNode<K, P> rChild = node.right;
        if (rChild == NIL) {
            System.out.println("node's rChild shouldn't be NIL");
        } else {
            AVLNode<K, P> rlChild = rChild.left;
            rChild.left = node;
            node.right = rlChild;
            node.height = 1 + Math.max(node.left.height, node.right.height);
            rChild.height = 1 + Math.max(rChild.left.height, rChild.right.height);
        }
        return rChild;
    }

    private AVLNode<K, P> rightRotate(AVLNode<K, P> node) {
        AVLNode<K, P> lChild = node.left;
        if (lChild == NIL) {
            System.out.println("node's lChild shouldn't be NIL");
        } else {
            AVLNode<K, P> lrChild = lChild.right;
            lChild.right = node;
            node.left = lrChild;
            node.height = 1 + Math.max(node.left.height, node.right.height);
            lChild.height = 1 + Math.max(lChild.left.height, lChild.right.height);
        }
        return lChild;
    }
}
