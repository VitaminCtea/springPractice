package spring.testRef.util;

import java.util.List;

public class FavoriteFruit {
    private Fruit fruit;

    public void setMailboxList(List<String> mailboxList) {
        this.mailboxList = mailboxList;
    }

    private List<String> mailboxList;

    public void setFruit(Fruit fruit) {
        this.fruit = fruit;
    }

    @Override
    public String toString() {
        return "FavoriteFruit{" +
                "fruit=" + fruit +
                ", mailboxList=" + mailboxList +
                '}';
    }
}
