package com.ibtsoft.singularity.persistence.mongodb;

import java.math.BigDecimal;
import java.net.UnknownHostException;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import com.ibtsoft.singularity.core.Singularity;
import com.ibtsoft.singularity.core.SingularityConfiguration;
import com.ibtsoft.singularity.core.repository.IRepository;
import com.ibtsoft.singularity.core.repository.RepositoryDescriptor;
import com.ibtsoft.singularity.core.repository.entity.Entity;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("checkstyle:MagicNumber")
public class MongoDbPersistenceTest {

    private static final String DB_NAME = "test";

    private final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    private DB db;

    private Singularity singularity;

    private IRepository<TestEntity> testEntityRepository;
    private IRepository<OtherTestEntity> otherTestEntityRepository;

    @BeforeEach
    public void setup() {
        mongoDBContainer.start();

        MongoClient mongoClient;
        try {
            mongoClient = new MongoClient(new MongoClientURI(mongoDBContainer.getReplicaSetUrl()));
        } catch (UnknownHostException e) {
            throw new RuntimeException("Failed to connect to MongoDB", e);
        }
        db = mongoClient.getDB(DB_NAME);

        MongoClientURI mongoClientURI = new MongoClientURI(mongoDBContainer.getReplicaSetUrl());

        singularity = new Singularity(SingularityConfiguration.builder()
            .withEntityType(TestEntity.class)
            .withEntityType(OtherTestEntity.class)
            .persistenceUnit(entityStructureCache ->
                new MongoDbPersistenceUnit(entityStructureCache, mongoDBContainer.getReplicaSetUrl(), mongoClientURI.getDatabase()))
            .build());

        testEntityRepository = singularity.getRepositoriesManager().getRepository(RepositoryDescriptor.forClass(TestEntity.class));
        otherTestEntityRepository = singularity.getRepositoriesManager().getRepository(RepositoryDescriptor.forClass(OtherTestEntity.class));
    }

    @Test
    @SuppressWarnings("checkstyle:MagicNumber")
    public void save() {
        Entity<OtherTestEntity> otherTestEntityEntity = otherTestEntityRepository.save(new OtherTestEntity());

        TestEntity test = new TestEntity();
        test.setStringField("Test string value");
        test.setIntField(123);
        test.setBigDecimalField(new BigDecimal("3.14"));
        test.setOtherField(otherTestEntityEntity);

        Entity<TestEntity> testEntity = testEntityRepository.save(test);

        DBCollection testEntityCollection = db.getCollection(TestEntity.class.getSimpleName());

        BasicDBObject basicDBObject = (BasicDBObject) testEntityCollection.findOne(
            new BasicDBObject("_id", new ObjectId(testEntity.getId().toString().substring(9).replace("-", ""))));

        assertEquals("TestEntity", basicDBObject.getString("_type"));
    }

    @Test
    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    public void loadAll() {
        Entity<OtherTestEntity> otherTestEntityEntity = otherTestEntityRepository.save(new OtherTestEntity());

        TestEntity test = new TestEntity();
        test.setStringField("Test string value");
        test.setIntField(123);
        test.setBigDecimalField(new BigDecimal("3.14"));
        test.setOtherField(otherTestEntityEntity);

        Entity<TestEntity> testEntity = testEntityRepository.save(test);

        MongoClientURI mongoClientURI = new MongoClientURI(mongoDBContainer.getReplicaSetUrl());

        singularity = new Singularity(SingularityConfiguration.builder()
            .withEntityType(TestEntity.class)
            .withEntityType(OtherTestEntity.class)
            .persistenceUnit(entityStructureCache ->
                new MongoDbPersistenceUnit(entityStructureCache, mongoDBContainer.getReplicaSetUrl(), mongoClientURI.getDatabase()))
            .build());

        testEntityRepository = singularity.getRepositoriesManager().getRepository(RepositoryDescriptor.forClass(TestEntity.class));
        otherTestEntityRepository = singularity.getRepositoriesManager().getRepository(RepositoryDescriptor.forClass(OtherTestEntity.class));

        assertThat(testEntityRepository.findById(testEntity.getId())).isEqualToComparingFieldByFieldRecursively(testEntity);
    }

    public static class TestEntity {

        private int intField;
        private String stringField;
        private BigDecimal bigDecimalField;
        private Entity<OtherTestEntity> otherField;

        public int getIntField() {
            return intField;
        }

        public void setIntField(final int intField) {
            this.intField = intField;
        }

        public String getStringField() {
            return stringField;
        }

        public void setStringField(final String stringField) {
            this.stringField = stringField;
        }

        public BigDecimal getBigDecimalField() {
            return bigDecimalField;
        }

        public void setBigDecimalField(final BigDecimal bigDecimalField) {
            this.bigDecimalField = bigDecimalField;
        }

        public Entity<OtherTestEntity> getOtherField() {
            return otherField;
        }

        public void setOtherField(final Entity<OtherTestEntity> otherField) {
            this.otherField = otherField;
        }
    }

    public static class OtherTestEntity {

        private int intField;

        public int getIntField() {
            return intField;
        }

        public void setIntField(final int intField) {
            this.intField = intField;
        }
    }
}
