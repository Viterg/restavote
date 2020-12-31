package ru.viterg.restavote;

import ru.viterg.restavote.model.Role;
import ru.viterg.restavote.model.User;
import ru.viterg.restavote.web.json.JsonUtil;

import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.viterg.restavote.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final TestMatcher<User> USER_MATCHER = TestMatcher.usingIgnoringFieldsComparator(User.class, "registered", "password");

    public static TestMatcher<User> USER_WITH_MEALS_MATCHER =
            TestMatcher.usingAssertions(User.class,
//     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("registered", "password").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static final int ADMIN_ID = START_SEQ ;
    public static final int USER_ID = START_SEQ+1;
    public static final int NOT_FOUND = 10;

    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin",  Role.ADMIN);
    public static final User user1 = new User(USER_ID, "User1", "user1@yandex.ru", "password", Role.USER);
    public static final User user2 = new User(USER_ID+1, "User2", "user2@yandex.ru", "wordpass", Role.USER);

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        User updated = new User(user1);
        updated.setName("UpdatedName");
        updated.setPassword("newPass");
        updated.setEnabled(false);
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
