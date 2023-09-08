package spring.testRef.event;

import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

// 如果实现了Ordered接口，那么在发布事件时会根据order的值进行输出，值越小则越先输出
// 这里我觉得应该是一个最小堆的实现，在spring对bean进行初始化时就会根据这个order值进行排序，然后在发布事件时直接遍历即可
// 当然也可以在方法上使用@EventListener @Order两个注解，和实现ApplicationListener<T>, Ordered效果相同
public class BlockedListNotifier implements ApplicationListener<BlockedListEvent>, Ordered {
    private String notificationAddress;

    @Override public void onApplicationEvent(BlockedListEvent event) {
        System.out.println(
                "不使用通用结构来发布event -> 在黑名单中已经包含了" +
                        event.getAddress() + ", 因此，已阻止进一步操作！address: "
                        + event.getAddress() + ", content: "
                        + event.getContent()
        );
    }

    public void setNotificationAddress(String notificationAddress) { this.notificationAddress = notificationAddress; }

    @Override public int getOrder() { return -3; }
}
