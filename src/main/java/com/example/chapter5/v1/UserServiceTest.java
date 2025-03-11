package com.example.chapter5.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserServiceTest {

    private UserService userService;
    private UserDao userDao;
    private List<User> users = new ArrayList<>();

    @BeforeEach
    void setup() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(DaoFactory.class);
        userService = ac.getBean("userService", UserService.class);
        userDao = ac.getBean("userDao", UserDao.class);
        userDao.deleteAll();

        users.add(new User("userA", "유저A", "passwordA", Level.BASIC, 49, 0));
        users.add(new User("userB", "유저B", "passwordB", Level.BASIC, 50, 0));
        users.add(new User("userC", "유저C", "passwordC", Level.SILVER, 60, 29));
        users.add(new User("userD", "유저D", "passwordD", Level.SILVER, 60, 30));
        users.add(new User("userE", "유저E", "passwordE", Level.GOLD,  100, 100));
    }

    @Test
    @DisplayName("유저 업그레이드 테스트")
    void testUserLevelUpgrade() {
        // given
        users.forEach(user -> userDao.add(user));

        // when
        userService.upgradeLevels();

        // then
        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }

    private void checkLevel(User user, Level level) {
        User findUser = userDao.get(user.getId());
        assertThat(findUser.getLevel()).isEqualByComparingTo(level);
    }

    @Test
    @DisplayName("사용자 가입 시 BASIC 레벨 부여")
    void testInitialBasic() {
        // given
        User notInitialUser = users.get(4);
        User initialUser = users.get(0);
        initialUser.setLevel(null);

        // when
        userService.add(notInitialUser);
        userService.add(initialUser);

        User findNotInitialUser = userDao.get(notInitialUser.getId());
        User findInitialUser = userDao.get(initialUser.getId());

        // then
        checkLevel(findNotInitialUser, notInitialUser.getLevel());
        checkLevel(findInitialUser, Level.BASIC);
    }

}
