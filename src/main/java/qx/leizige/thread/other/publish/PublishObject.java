package qx.leizige.thread.other.publish;


import java.util.HashSet;
import java.util.Set;

/**
 * 发布(publish):发布一个对象是指,是对象能够在当前作用域之外的代码中访问
 * 逸出:当某个不该发布的对象被发布时就称为逸出! 比如下面的 states
 */
public class PublishObject {

    /**
     * 发布对象最简单的方法是将对象的引用保存到一个公有的静态变量中,以便任何类和线程都能够看见该对象
     */
    public static Set<Secret> knownStrings;

    public void initialize() {
        knownStrings = new HashSet<>();
    }

    /* --- -- -- 逸出 -- -- ---  */

    //😠 不能这样做(使内部的状态逸出),这样做会超出它的作用域。
    private String[] states = new String[]{"A", "B"};

    public String[] getStates() {
        return states;
    }
}
