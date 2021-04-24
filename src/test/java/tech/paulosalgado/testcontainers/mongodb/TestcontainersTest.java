package tech.paulosalgado.testcontainers.mongodb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class TestcontainersTest {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.5");

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private UserRepository repository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@BeforeEach
	void setup() {
		repository.save(paulo());
	}

	@Test
	public void shouldFindUser() {

		Query query = Query.query(Criteria.where("company").is("CI&T"));
		List<User> users = mongoTemplate.find(query, User.class);

		assertThat(users).isNotEmpty()
				.hasSize(1)
				.extracting("name")
				.containsExactly("Paulo");
	}

	@AfterEach
	void teardown() {
		mongoTemplate.dropCollection(User.class);
	}

	private User paulo() {
		return User.builder()
				.name("Paulo")
				.email("pjosalgado@gmail.com")
				.company("CI&T")
				.build();
	}

}
