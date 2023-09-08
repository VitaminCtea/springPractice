package spring.testRef.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.List;

public class EmailService implements ApplicationEventPublisherAware {
    private List<String> blockedList;   // 阻止发送清单
    private ApplicationEventPublisher publisher;    // 由spring自动注入

    public void sendEmail(String address, String content) {
        if (blockedList.contains(address)) {
            BlockedListEvent event = new BlockedListEvent(this, address, content);
            publisher.publishEvent(event);
            publisher.publishEvent(new EntityCreatedEvent<>(event));
            return;
        }
        System.out.println("给" + address + "发送邮件成功！邮件内容为: " + content);
    }

    public void setBlockedList(List<String> blockedList) { this.blockedList = blockedList; }
    @Override public void setApplicationEventPublisher(ApplicationEventPublisher publisher) { this.publisher = publisher; }
}
