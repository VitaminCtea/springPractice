package spring.testRef.event;

import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

public class BlockedListNotifierAfterAgain implements ApplicationListener<BlockedListEvent>, Ordered {
    @Override
    public void onApplicationEvent(BlockedListEvent event) {
        System.out.println("看看BlockedListNotifierAfterAgain是否先运行");
    }

    @Override public int getOrder() { return -4; }
}

