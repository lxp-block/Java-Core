package generic;
//2018/10/25  20:36

/*
具有泛型的栈结构
 */
public class LinkedStack<T> {
    private static class Node<U> {
        U item;
        Node<U> next;

        public Node() { }
        public Node(U item, Node<U> next) {
            this.item = item;
            this.next = next;
        }
        boolean end() {
            return item == null && next == null;
        }
    }

    //末端哨兵 top
    //调用push方法时。将当前栈顶与下一个节点Node关联起来
    private Node<T> top = new Node<T>();

    public void push(T item) {
        top = new Node<T>(item, top);
    }

    public T pop() {
        T result = top.item;
        if (!top.end())
            top = top.next;
        return result;
    }

    public static void main(String[] args) {
        LinkedStack<String> stack= new LinkedStack<String>();
        for(String s:"aaa bbb ccc".split(" "))
            stack.push(s);
        String s;
        while ((s=stack.pop())!=null)
            System.out.println(s);
    }
}
