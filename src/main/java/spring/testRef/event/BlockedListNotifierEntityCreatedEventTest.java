package spring.testRef.event;

import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

public class BlockedListNotifierEntityCreatedEventTest implements ApplicationListener<EntityCreatedEvent<BlockedListEvent>>, Ordered {
    private String notificationAddress;

    @Override public void onApplicationEvent(EntityCreatedEvent<BlockedListEvent> event) {
        BlockedListEvent e = ((BlockedListEvent) event.getSource());
        System.out.println(
                "使用通用结构来创建事件Event，本质是包装事件Event的一个类！ -> 在黑名单中已经包含了" +
                        e.getAddress() + ", 因此，已阻止进一步操作！address: "
                        + e.getAddress() + ", content: "
                        + e.getContent()
        );
    }

    public void setNotificationAddress(String notificationAddress) { this.notificationAddress = notificationAddress; }

    @Override public int getOrder() { return -5; }
}
