package skiplist;


/**
 * 跳表的一种实现方法。
 * 跳表中存储的是正整数，并且存储的是不重复的。
 *
 * Author：ZHENG
 */
public class SkipList {

  private static final float SKIPLIST_P = 0.5f;
  private static final int MAX_LEVEL = 16;

  private int levelCount = 1;

  private Node head = new Node();  // 带头链表

  public Node find(int value) {
    Node p = head;
    for (int i = levelCount - 1; i >= 0; --i) {
      while (p.forwards[i] != null && p.forwards[i].data < value) {
        p = p.forwards[i];
      }
    }

    if (p.forwards[0] != null && p.forwards[0].data == value) {
      return p.forwards[0];
    } else {
      return null;
    }
  }
// 结合黄铮讲的，基本上懂了
  public void insert(int value) {
    int level = randomLevel();
    Node newNode = new Node();
    newNode.data = value;
    newNode.maxLevel = level;//一共有多少层
    Node update[] = new Node[level];//创建level层链表
//       private Node head = new Node();  //每一层创建一个带头链表
    for (int i = 0; i < level; ++i) {
      update[i] = head;
    }

    // record every level largest value which smaller than insert value in update[]
    //找出每层p.forward[]相当于p.next
    Node p = head;
    for (int i = level - 1; i >= 0; --i) {
      while (p.forwards[i] != null && p.forwards[i].data < value) {
        p = p.forwards[i];
      }
      //保存每一层中要更新的位置
      update[i] = p;// use update save node in search path
    }

    // in search path node next node become new node forwords(next)
    //    Node newNode = new Node();
//     newNode.data = value;
    //在每层中<level的每层都插入都插入最简单的插入方法
    for (int i = 0; i < level; ++i) {
      newNode.forwards[i] = update[i].forwards[i];
      update[i].forwards[i] = newNode;
    }

    // update node hight
    if (levelCount < level) levelCount = level;
  }

  public void delete(int value) {
    Node[] update = new Node[levelCount];
    Node p = head;
    for (int i = levelCount - 1; i >= 0; --i) {
      while (p.forwards[i] != null && p.forwards[i].data < value) {
        p = p.forwards[i];
      }
      update[i] = p;
    }
//  判断该链表中是否存在value
    if (p.forwards[0] != null && p.forwards[0].data == value) {
      for (int i = levelCount - 1; i >= 0; --i) {
        if (update[i].forwards[i] != null && update[i].forwards[i].data == value) {
          update[i].forwards[i] = update[i].forwards[i].forwards[i];
        }
      }
    }
//最高层一个节点也没有，减掉一层
    while (levelCount>1&&head.forwards[levelCount]==null){
      levelCount--;
    }

  }

  // 理论来讲，一级索引中元素个数应该占原始数据的 50%，二级索引中元素个数占 25%，三级索引12.5% ，一直到最顶层。
  // 因为这里每一层的晋升概率是 50%。对于每一个新插入的节点，都需要调用 randomLevel 生成一个合理的层数。
  // 该 randomLevel 方法会随机生成 1~MAX_LEVEL 之间的数，且 ：
  //        50%的概率返回 1
  //        25%的概率返回 2
  //      12.5%的概率返回 3 ...
  //这个random函数太牛了
  private int randomLevel() {
    int level = 1;

    while (Math.random() < SKIPLIST_P && level < MAX_LEVEL)
      level += 1;
    return level;
  }
//打印所有链表
  public void printAll() {
    Node p = head;
    while (p.forwards[0] != null) {
      System.out.print(p.forwards[0] + " ");
      p = p.forwards[0];
    }
    System.out.println();
  }

  public class Node {
    private int data = -1;
    private Node forwards[] = new Node[MAX_LEVEL];
    private int maxLevel = 0;

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("{ data: ");
      builder.append(data);
      builder.append("; levels: ");
      builder.append(maxLevel);
      builder.append(" }");

      return builder.toString();
    }
  }

}
