/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.brusamentoceruti.moviebookrest.boundary;

import it.polimi.brusamentoceruti.moviebookrest.entity.MovieBook;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.ws.soap.AddressingFeature.Responses;
import org.json.JSONException;

/**
 *
 * @author Mattia
 */
@Path("movie")
@Produces("application/json")
public class MovieResource {
    
    @EJB
    BookRequest books;
    @EJB
    MovieRequest movies;
    
    
    
    @GET
    public MovieBook getMovie(@Context UriInfo info){
        String p = info.getQueryParameters().getFirst("q");
        if( p==null ){
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        p = p.replaceAll(" ", "%20");
        MovieBook movie = null;
        try {
            movie = movies.search(p);
        } catch (IOException | JSONException ex) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } catch(NullPointerException | EJBException n){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        try{
            List<String> bookLinks = books.getBooks(p);
            movie.setBooks(bookLinks);
        }catch(Exception e){}
        System.out.println(movie.toString());
        return movie;
        
    }
}
