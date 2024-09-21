package eu.happycoders.shop.adapter.out.persistence.jpa;

import eu.happycoders.shop.adapter.TestProfileWithMySQL;
import eu.happycoders.shop.adapter.out.persistence.AbstractCartRepositoryTest;
import eu.happycoders.shop.adapter.out.persistence.AbstractProductRepositoryTest;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(TestProfileWithMySQL.class)
public class JpaCartRepositoryTest extends AbstractCartRepositoryTest {}
