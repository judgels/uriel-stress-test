package org.iatoki.judgels.uriel.stresstest;

import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public final class UrielStressTestProperties {
    private static UrielStressTestProperties INSTANCE;

    private String urielStressTestSecret;

    private String urielBaseUrl;
    private String urielContestJid;
    private String urielProblemJid;
    private String urielProblemLanguage;
    private String urielProblemEngine;

    private String userJid;
    private String userSourceFilePath;

    public static UrielStressTestProperties getInstance() {
        if (INSTANCE == null) {
            buildInstance();
        }
        return INSTANCE;
    }

    public String getUrielStressTestSecret() {
        return urielStressTestSecret;
    }

    public String getUrielBaseUrl() {
        return urielBaseUrl;
    }

    public String getUrielContestJid() {
        return urielContestJid;
    }

    public String getUrielProblemJid() {
        return urielProblemJid;
    }

    public String getUrielProblemLanguage() {
        return urielProblemLanguage;
    }

    public String getUrielProblemEngine() {
        return urielProblemEngine;
    }

    public String getUserJid() {
        return userJid;
    }

    public String getUserSourceFilePath() {
        return userSourceFilePath;
    }

    private static void buildInstance() {
        INSTANCE = new UrielStressTestProperties();

        InputStream config = UrielStressTestProperties.class.getClassLoader().getResourceAsStream("conf/application.conf");

        if (config == null) {
            throw new RuntimeException("Missing src/main/resources/conf/application.conf file");
        }

        Properties properties = new Properties();

        try {
            properties.load(config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        verifyProperties(properties);

        INSTANCE.urielStressTestSecret = properties.getProperty("uriel.stressTestSecret").replaceAll("\"", "");

        INSTANCE.urielBaseUrl = properties.getProperty("uriel.baseUrl").replaceAll("\"", "");
        INSTANCE.urielContestJid = properties.getProperty("uriel.contestJid").replaceAll("\"", "");
        INSTANCE.urielProblemJid = properties.getProperty("uriel.problemJid").replaceAll("\"", "");
        INSTANCE.urielProblemLanguage = properties.getProperty("uriel.problemLanguage").replaceAll("\"", "");
        INSTANCE.urielProblemEngine = properties.getProperty("uriel.problemEngine").replaceAll("\"", "");

        INSTANCE.userJid = properties.getProperty("user.jid").replaceAll("\"", "");
        INSTANCE.userSourceFilePath = properties.getProperty("user.sourceFilePath").replaceAll("\"", "");
    }

    private static void verifyProperties(Properties properties) {
        List<String> requiredKeys = ImmutableList.of(
                "uriel.stressTestSecret",
                "uriel.baseUrl",
                "uriel.contestJid",
                "uriel.problemJid",
                "uriel.problemLanguage",
                "uriel.problemEngine",
                "user.jid",
                "user.sourceFilePath"
        );

        for (String key : requiredKeys) {
            if (properties.get(key) == null) {
                throw new RuntimeException("Missing " + key + " property in src/main/resources/conf/application.conf");
            }
        }
    }
}
