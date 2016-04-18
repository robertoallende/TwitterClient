package com.robertoallende.twitterclient.dao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

import java.io.IOException;
public class TwitterDaoGenerator extends DaoGenerator {

    private static final String PROJECT_DIR = System.getProperty("user.dir");
    private static final String OUT_DIR = PROJECT_DIR + "/app/src/main/java";

    public TwitterDaoGenerator() throws IOException {
    }

    public static void main(String[] args) {
        Schema schema = new Schema(3, "com.robertoallende.twitterclient.entities");
        schema.setDefaultJavaPackageTest("com.robertoallende.twitterclient.test");
        schema.setDefaultJavaPackageDao("com.robertoallende.twitterclient.dao");
        schema.enableKeepSectionsByDefault();
        Entity tweet = schema.addEntity("Tweet");
        tweet.addLongProperty("localId").primaryKey().autoincrement();
        tweet.addLongProperty("serverId").unique();
        tweet.addStringProperty("text");
        tweet.addLongProperty("userId");
        tweet.addBooleanProperty("isLocal");
        tweet.addDateProperty("createdAt");
        try {
            new DaoGenerator().generateAll(schema, OUT_DIR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
