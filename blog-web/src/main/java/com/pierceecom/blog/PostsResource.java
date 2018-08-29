package com.pierceecom.blog;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

@Path("posts")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class PostsResource {

    private static final String POSTS_URI = "http://localhost:8080/blog-web/posts/";
    private PostDataService posts = PostDataService.getInstance();

    @GET
    public Response getAllPosts() {
        return Response.ok(posts.getPostList()).build();
    }

    @POST
    public Response postAPost(Post post) throws URISyntaxException {

        if (post.getTitle() == null || post.getContent() == null) {
            return Response.status(405).build();
        }
        Response.ResponseBuilder builder = Response.ok(post);
        builder.status(Response.Status.CREATED);
        posts.addPost(post);
        builder.location(new URI(POSTS_URI + post.getId()));
        return builder.build();
    }

    @PUT
    public Response putThePost(Post post) {
        if (post.getTitle() == null || post.getContent() == null) {
            return Response.status(405).build();
        }
        Response.ResponseBuilder builder = Response.ok(post);
        builder.status(Response.Status.CREATED);
        posts.replacePost(post);
        return builder.build();
    }

    @Path("/{postId}")
    @GET
    public Post getPostByID(@PathParam("postId") String postId) {
        return posts.getPostById(postId);
    }

    @Path("/{postId}")
    @DELETE
    public Response deletePostByID(@PathParam("postId") String postId) {
        Post deletedPost = posts.deletePostById(postId);
        if (deletedPost != null) {
            return Response.ok().build();
        } else {
            return Response.status(404).build();
        }
    }
}
