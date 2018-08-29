package com.pierceecom.blog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * TODO, Consider it part of the test to replace HttpURLConnection with better
 * APIs (for example Jersey-client, JSON-P etc-) to call REST-service
 */

public class BlogTestIntegr {

    private static final String POST_1 = "{\"content\":\"First content\",\"id\":\"1\",\"title\":\"First title\"}";
    private static final String POST_2 = "{\"content\":\"Second content\",\"id\":\"2\",\"title\":\"Second title\"}";
    private static final String INVALID_POST_1 = "{\"something\":\"blurg\"}";
    private static final String POSTS_URI = "http://localhost:8080/blog-web/posts/";


    public BlogTestIntegr() {
    }

    @Test
    public void addAPost() {
        try {
            String location = POST(POSTS_URI, POST_1, 201);
            assertEquals(POSTS_URI + "1", location);
            String actualPost = GET(POSTS_URI + "1", 200);
            assertEquals(POST_1, actualPost);
        } finally {
            DELETE(POSTS_URI + "1", 200);
        }
    }

    @Test
    public void deleteAPost() {
        try {
            POST(POSTS_URI, POST_1, 201);
            String actualPost = GET(POSTS_URI + "1", 200);
            assertEquals(POST_1, actualPost);
        } finally {
            DELETE(POSTS_URI + "1", 200);
            GET(POSTS_URI + "1", 204);
        }
    }

    @Test
    public void getAllPosts() {
        try {
            POST(POSTS_URI, POST_1, 201);
            POST(POSTS_URI, POST_2, 201);
            String actualPosts = GET(POSTS_URI, 200);
            assertEquals("[" + POST_1 + "," + POST_2 + "]", actualPosts);
        } finally {
            DELETE(POSTS_URI + "1", 200);
            DELETE(POSTS_URI + "2", 200);
        }
    }

    @Test
    public void blogWithoutPosts() {
        String output = GET(POSTS_URI, 200);
        assertEquals("[]", output);
    }

    @Test
    public void addInvalidPost() {
        try {
            POST(POSTS_URI, INVALID_POST_1, 405);
            GET(POSTS_URI + "1", 204);
        } finally {
            DELETE(POSTS_URI + "1", 404);
        }
    }

    @Test
    public void deleteNonexistentPost() {
        DELETE(POSTS_URI + "1", 404);
    }

    @Test
    public void getNonexistentPost() {
        GET(POSTS_URI + "1", 204);
    }

    /* Helper methods */
    private String GET(String uri, int expectedResponseCode) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            assertEquals(expectedResponseCode, conn.getResponseCode());
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            conn.disconnect();
        } catch (MalformedURLException ex) {
            Logger.getLogger(BlogTestIntegr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BlogTestIntegr.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }

    private String POST(String uri, String json, int expectedHTTPCode) {
        String location = "";
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            assertEquals(expectedHTTPCode, conn.getResponseCode());

            location = conn.getHeaderField("Location");
            conn.disconnect();

        } catch (MalformedURLException ex) {
            Logger.getLogger(BlogTestIntegr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BlogTestIntegr.class.getName()).log(Level.SEVERE, null, ex);
        }
        return location;
    }

    private void DELETE(String uri, int expectedResponseCode) {
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept", "application/json");
            assertEquals(expectedResponseCode, conn.getResponseCode());
        } catch (MalformedURLException ex) {
            Logger.getLogger(BlogTestIntegr.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BlogTestIntegr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
