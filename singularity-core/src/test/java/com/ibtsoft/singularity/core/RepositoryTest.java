package com.ibtsoft.singularity.core;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

public class RepositoryTest {

    private static class TestEntity {

        private String stringField;
        private int intField;

        public TestEntity() {
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
    }

    private static class TestEntityRepository extends Repository<TestEntity> {

        private TestEntityRepository() {
            super(repositoryClass);
        }

        public EntityValue<TestEntity> create() {
            return add(TestEntity.class);
        }

        @Override
        public void init() {

        }

        @Override
        protected UUID persist(TestEntity item) {
            return null;
        }

        @Override
        protected UUID persist(EntityValue<TestEntity> item) {
            return null;
        }

        @Override
        protected void daoDelete(EntityValue<TestEntity> item) {

        }
    }

    @Test
    public void testAdd() {
        TestEntityRepository repo = new TestEntityRepository();

        TestEntity testEntity = new TestEntity();

        EntityValue<TestEntity> entity= repo.create();

        Assert.assertNotNull(entity.getId());
        Assert.assertNotNull(entity.getValue());
        Assert.assertNull(entity.getValue().getStringField());
    }

}
