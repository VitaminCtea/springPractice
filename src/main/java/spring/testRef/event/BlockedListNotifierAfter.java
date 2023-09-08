package spring.testRef.event;

import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

public class BlockedListNotifierAfter implements ApplicationListener<BlockedListEvent>, Ordered {
    @Override
    public void onApplicationEvent(BlockedListEvent event) {
        System.out.println("通过实现Ordered接口来要求ApplicationContext发布事件时按照指定顺序进行调用");
    }

    @Override public int getOrder() { return -2; }
}
