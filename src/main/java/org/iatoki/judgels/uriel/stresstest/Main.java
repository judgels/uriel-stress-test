package org.iatoki.judgels.uriel.stresstest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Main {
    public static void main(String[] args) {
        init();

        if (args.length == 0) {
            System.out.println("Wrong usage!");
            System.exit(1);
        }

        if (args[0].equals("single")) {
            submitSingle();
        } else if (args[0].equals("simultaneous")) {

            if (args.length != 3) {
                System.out.println("Usage: simultaeous <threads> <submissionsInEachThread>");
                System.exit(1);
            }

            int threads = Integer.parseInt(args[1]);
            int submissionsInEachThread = Integer.parseInt(args[2]);

            submitSimultaneous(threads, submissionsInEachThread);
        }
    }

    private static void init() {
        UrielStressTestProperties.getInstance();
    }

    private static void submitSingle() {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = getUrielSubmissionRequest();

        HttpResponse response;

        try {
            response = client.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new RuntimeException("Response is " + response.getStatusLine().getStatusCode());
        }
    }

    private static void submitSimultaneous(int threads, int submissionsInEachThread) {
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        for (int t = 0; t < threads; t++) {
            executor.execute(() -> {
                for (int s = 0; s < submissionsInEachThread; s++) {
                    submitSingle();
                    System.out.println("Submitted!");
                }
            });
        }

        executor.shutdown();

        while (!executor.isTerminated());
    }

    private static HttpPost getUrielSubmissionRequest() {
        UrielStressTestProperties prop = UrielStressTestProperties.getInstance();

        HttpPost post = new HttpPost(prop.getUrielBaseUrl() + "/apis/testing/singleFileBlackBoxSubmit");

        HttpEntity entity = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addTextBody("stressTestSecret", prop.getUrielStressTestSecret())
                .addTextBody("contestJid", prop.getUrielContestJid())
                .addTextBody("problemJid", prop.getUrielProblemJid())
                .addTextBody("problemLanguage", prop.getUrielProblemLanguage())
                .addTextBody("problemEngine", prop.getUrielProblemEngine())
                .addTextBody("userJid", prop.getUserJid())
                .addPart("source", new FileBody(new File(prop.getUserSourceFilePath())))
                .build();

        post.setEntity(entity);

        return post;
    }
}
