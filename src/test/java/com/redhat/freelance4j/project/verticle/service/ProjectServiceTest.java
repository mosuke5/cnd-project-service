package com.redhat.freelance4j.project.verticle.service;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.redhat.freelance4j.project.model.Project;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class ProjectServiceTest extends MongoTestBase {
	
    private Vertx vertx;

    @Before
    public void setup(TestContext context) throws Exception {
        vertx = Vertx.vertx();
        vertx.exceptionHandler(context.exceptionHandler());
        JsonObject config = getConfig();
        mongoClient = MongoClient.createNonShared(vertx, config);
        Async async = context.async();
        dropCollection(mongoClient, "projects", async, context);
        async.await(10000);
    }

    @After
    public void tearDown() throws Exception {
        mongoClient.close();
        vertx.close();
    }
    
    @Test
    public void testGetProjects(TestContext context) throws Exception {
        Async saveAsync = context.async(2);
        String projectId1 = "111111";
        JsonObject json1 = new JsonObject()
                .put("projectId", projectId1)
                .put("ownerFirstName", "Masao")
                .put("ownerLastName", "Kato")
                .put("ownerEmail", "masao.kato@example.com")
                .put("projectTitle", "project-1")
                .put("projectDescription", "project-1-desc")
                .put("projectStatus", "open");

        mongoClient.save("projects", json1, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        String projectId2 = "222222";
        JsonObject json2 = new JsonObject()
                .put("projectId", projectId2)
                .put("ownerFirstName", "Kana")
                .put("ownerLastName", "Komiya")
                .put("ownerEmail", "kana.komiya@example.com")
                .put("projectTitle", "project-2")
                .put("projectDescription", "project-2-desc")
                .put("projectStatus", "open");

        mongoClient.save("projects", json2, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        saveAsync.await();

        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient);

        Async async = context.async();

        service.getProjects(ar -> {
            if (ar.failed()) {
                context.fail(ar.cause().getMessage());
            } else {
                assertThat(ar.result(), notNullValue());
                assertThat(ar.result().size(), equalTo(2));
                Set<String> projectIds = ar.result().stream().map(p -> p.getProjectId()).collect(Collectors.toSet());
                assertThat(projectIds.size(), equalTo(2));
                assertThat(projectIds, allOf(hasItem(projectId1),hasItem(projectId2)));
                async.complete();
            }
        });
    }
    
    	
    @Test
    public void testGetProject(TestContext context) throws Exception {
        Async saveAsync = context.async(2);
        String projectId1 = "111111";
        JsonObject json1 = new JsonObject()
                .put("projectId", projectId1)
                .put("ownerFirstName", "Masao")
                .put("ownerLastName", "Kato")
                .put("ownerEmail", "masao.kato@example.com")
                .put("projectTitle", "project-1")
                .put("projectDescription", "project-1-desc")
                .put("projectStatus", "open");

        mongoClient.save("projects", json1, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        String projectId2 = "222222";
        JsonObject json2 = new JsonObject()
                .put("projectId", projectId2)
                .put("ownerFirstName", "Kana")
                .put("ownerLastName", "Komiya")
                .put("ownerEmail", "kana.komiya@example.com")
                .put("projectTitle", "project-2")
                .put("projectDescription", "project-2-desc")
                .put("projectStatus", "open");

        mongoClient.save("projects", json2, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        saveAsync.await();

        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient);

        Async async = context.async();

        service.getProject("111111", ar -> {
            if (ar.failed()) {
                context.fail(ar.cause().getMessage());
            } else {
                assertThat(ar.result(), notNullValue());
                assertThat(ar.result().getProjectId(), equalTo("111111"));
                assertThat(ar.result().getOwnerFirstName(), equalTo("Masao"));
                async.complete();
            }
        });
    }
    
    @Test
    public void testGetProjectsByStatus(TestContext context) throws Exception {
        Async saveAsync = context.async(2);
        String projectId1 = "111111";
        JsonObject json1 = new JsonObject()
                .put("projectId", projectId1)
                .put("ownerFirstName", "Masao")
                .put("ownerLastName", "Kato")
                .put("ownerEmail", "masao.kato@example.com")
                .put("projectTitle", "project-1")
                .put("projectDescription", "project-1-desc")
                .put("projectStatus", "open");

        mongoClient.save("projects", json1, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        String projectId2 = "222222";
        JsonObject json2 = new JsonObject()
                .put("projectId", projectId2)
                .put("ownerFirstName", "Kana")
                .put("ownerLastName", "Komiya")
                .put("ownerEmail", "kana.komiya@example.com")
                .put("projectTitle", "project-2")
                .put("projectDescription", "project-2-desc")
                .put("projectStatus", "open");

        mongoClient.save("projects", json2, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });
        
        String projectId3 = "333333";
        JsonObject json3 = new JsonObject()
                .put("projectId", projectId3)
                .put("ownerFirstName", "Kohei")
                .put("ownerLastName", "Oda")
                .put("ownerEmail", "kohei.oda@example.com")
                .put("projectTitle", "project-3")
                .put("projectDescription", "project-3-desc")
                .put("projectStatus", "completed");

        mongoClient.save("projects", json3, ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        saveAsync.await();

        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient);

        Async async = context.async();

        service.getProjectsByStatus("open", ar -> {
            if (ar.failed()) {
                context.fail(ar.cause().getMessage());
            } else {
                assertThat(ar.result(), notNullValue());
                assertThat(ar.result().size(), equalTo(2));
                Set<String> projectIds = ar.result().stream().map(p -> p.getProjectId()).collect(Collectors.toSet());
                assertThat(projectIds.size(), equalTo(2));
                assertThat(projectIds, allOf(hasItem(projectId1),hasItem(projectId2)));
                async.complete();
            }
        });
    }
    
    @Test
    public void testPing(TestContext context) throws Exception {
        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient);

        Async async = context.async();
        service.ping(ar -> {
            assertThat(ar.succeeded(), equalTo(true));
            async.complete();
        });
    }

}
