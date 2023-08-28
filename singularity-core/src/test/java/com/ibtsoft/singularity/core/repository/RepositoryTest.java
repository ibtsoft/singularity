package com.ibtsoft.singularity.core.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;
import com.ibtsoft.singularity.core.SingularityConfiguration.EntityTypeConfiguration;
import com.ibtsoft.singularity.core.repository.entity.EntityValue;
import com.ibtsoft.singularity.core.repository.reflection.entitystructure.EntityStructureCache;
import com.ibtsoft.singularity.core.repository.transaction.TransactionManager;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RepositoryTest {

    @Test
    public void testAdd() {
        TestEntityRepository repo = new TestEntityRepository();

        EntityValue<TestEntity> entity = repo.create();

        assertNotNull(entity.getId());
        assertNotNull(entity.getValue());
        assertNull(entity.getValue().getStringField());
    }

    public static class TestEntity {

        private boolean booleanField;
        private String stringField;
        private int intField;

        private ObjectField objectField;

        private List<ObjectField> listField;

        private Set<ObjectField> setField;

        private Map<ObjectFieldKey, ObjectField> mapField;

        private AnotherTestEntity anotherTestEntityField;

        private List<AnotherTestEntity> listAnotherTestEntityField;

        private Set<AnotherTestEntity> setAnotherTestEntityField;

        private Map<ObjectFieldKey, ObjectField> mapAnotherTestEntityField;

        public TestEntity() {
        }

        public boolean isBooleanField() {
            return booleanField;
        }

        public void setBooleanField(boolean booleanField) {
            this.booleanField = booleanField;
        }

        public String getStringField() {
            return stringField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }

        public int getIntField() {
            return intField;
        }

        public void setIntField(int intField) {
            this.intField = intField;
        }

        public ObjectField getObjectField() {
            return objectField;
        }

        public void setObjectField(ObjectField objectField) {
            this.objectField = objectField;
        }

        public static class ObjectField {

            private String extraField;

            public String getExtraField() {
                return extraField;
            }

            public void setExtraField(String extraField) {
                this.extraField = extraField;
            }
        }
    }

    public static class ObjectFieldKey {

        private Integer keyFieldOne;
        private String keyFieldTwo;

        public Integer getKeyFieldOne() {
            return keyFieldOne;
        }

        public void setKeyFieldOne(Integer keyFieldOne) {
            this.keyFieldOne = keyFieldOne;
        }

        public String getKeyFieldTwo() {
            return keyFieldTwo;
        }

        public void setKeyFieldTwo(String keyFieldTwo) {
            this.keyFieldTwo = keyFieldTwo;
        }
    }

    public static class AnotherTestEntity {

        public String getStringField() {
            return stringField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }

        private String stringField;
    }

    private static class TestEntityRepository extends Repository<TestEntity> {

        private TestEntityRepository() {
            super(TestEntity.class,
                new EntityStructureCache(
                    ImmutableList.of(TestEntity.class, AnotherTestEntity.class).stream()
                        .map(aClass -> new EntityTypeConfiguration(aClass))
                        .collect(toList())),
                new TransactionManager());
        }

        public EntityValue<TestEntity> create() {
            return save(new TestEntity());
        }

        @Override
        public void init() {
        }
    }
}
