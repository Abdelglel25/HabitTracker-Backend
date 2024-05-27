package com.sa.habitTrackerBackend.utils;

import com.sa.habitTrackerBackend.HabitTrackerBackendApplication;
import com.sa.habitTrackerBackend.utils.testcontainers.mariadb.MariaDBTestContainer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = HabitTrackerBackendApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
@TestExecutionListeners(mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS, value = {WithSecurityContextTestExecutionListener.class})
public abstract class AbstractIntegrationIT {
    private final static MariaDBTestContainer mariaDBTestContainer;

    static {
        mariaDBTestContainer = MariaDBTestContainer.getInstance();
        mariaDBTestContainer.start();
    }
}
