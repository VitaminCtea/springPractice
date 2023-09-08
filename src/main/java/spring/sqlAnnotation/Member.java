package spring.sqlAnnotation;

@DBTable(name = "MEMBER")
@lombok.Getter
public class Member {
    @SQLString(30) private String firstName;
    @SQLString(30) private String lastName;
    @SQLInteger private int age;
    @SQLString(value = 30, constraints = @Constraints(primaryKey = true)) private String handle;
}
