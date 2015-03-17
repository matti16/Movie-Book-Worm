/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.brusamentoceruti.moviebookrest.boundary;

import it.polimi.brusamentoceruti.moviebookrest.entity.MovieBook;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Mattia
 */
@Stateless
public class MovieRequest {
    
    private static final String queryMovie = "http://api.rottentomatoes.com/api/public/v1.0/movies";
    private static final String movieKey = "vtbgg3vj4g2cajr672uasjhb";
    private static final String opt = ".json?apikey=";
    /*http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=[your_api_key]&q=Jack&page_limit=1*/
    /*abridged_directors = direttori*/
    
    private String movieName;//parametro q dell'url
    private Integer pageLimit;//risultati per pagina
    private Integer page;//pagina selezionata
    
    public MovieRequest(){
        pageLimit=1;
        page=1;
    }
    
    public MovieBook search(String title) throws JSONException, IOException{
        String url = this.createQueryURL(title);
        MovieBook result = null;
        result = computeJson(JsonRequest.doQuery(url));

        return result;
    }
    
    private MovieBook computeJson(JSONObject jSO) throws JSONException, IOException{
        String adjustedUrl;
        
        adjustedUrl = adjustURL(jSO.getJSONArray("movies").getJSONObject(0).getString("id"));
        jSO = JsonRequest.doQuery(adjustedUrl);
        
        MovieBook movie = new MovieBook(jSO.getString("title"));
        
        movie.setYear(jSO.getString("year"));
        
        JSONArray directorsArray = jSO.getJSONArray("abridged_directors");
        List<String> dirs = new ArrayList<>();
        for(int i=0; i<directorsArray.length(); i++){
            dirs.add(directorsArray.getJSONObject(i).getString("name"));
        }
        movie.setDirectors(dirs);
        
        movie.setAudience_rating(jSO.getJSONObject("ratings").getInt("audience_score"));
        movie.setCritics_rating(jSO.getJSONObject("ratings").getInt("critics_score"));
        movie.setPoster(jSO.getJSONObject("posters").getString("thumbnail"));
        
        return movie;
    }
    
    private String createQueryURL(String qFieldValue){
        movieName = qFieldValue;
        return (queryMovie + opt + movieKey + "&q=" + qFieldValue + 
                "&page_limit=" + this.pageLimit + "&page=" + this.page);
    }
    
    private String adjustURL(String id){
        return (queryMovie + "/" + id + opt + movieKey + "&q=" + movieName + 
                "&page_limit=" + this.pageLimit + "&page=" + this.page);
    }
}
