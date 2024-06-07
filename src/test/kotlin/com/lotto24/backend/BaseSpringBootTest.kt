package com.lotto24.backend

import com.fasterxml.jackson.databind.ObjectMapper
import com.lotto24.backend.support.factory.BalanceFactoryConfig
import com.lotto24.backend.support.factory.PersistedBalanceFactory
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureEmbeddedDatabase(
    type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
    provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.DOCKER,
    refresh = AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD
)
@AutoConfigureMockMvc
@Import(BalanceFactoryConfig::class)
abstract class BaseSpringBootTest {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var persistedBalanceFactory: PersistedBalanceFactory
}
