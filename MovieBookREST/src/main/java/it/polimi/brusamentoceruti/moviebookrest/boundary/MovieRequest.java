/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.brusamentoceruti.moviebookrest.boundary;

import it.polimi.brusamentoceruti.moviebookrest.entity.MovieBook;
import it.polimi.brusamentoceruti.moviebookrest.entity.MoviesResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    
    public MovieRequest(){
    }
    
    public MoviesResult search(String title, int pageLimit) throws JSONException, IOException{
        String url = this.createQueryURL(title, pageLimit);
        MoviesResult result;
        result = computeJson(JsonRequest.doQuery(url), pageLimit);

        return result;
    }
    
    private MoviesResult computeJson(JSONObject jSO, int pageLimit) throws JSONException, IOException{
        String adjustedUrl;
        int total = jSO.getInt("total");
        MoviesResult result = new MoviesResult();
        
        result.setSize( (total<pageLimit) ? total : pageLimit);
        JSONObject partialJSON;
        
        for(int i=0; i<result.getSize() ; i++){
            adjustedUrl = adjustURL(jSO.getJSONArray("movies").getJSONObject(i).getString("id"));
            partialJSON = JsonRequest.doQuery(adjustedUrl);

            MovieBook movie = new MovieBook(partialJSON.getString("title"));

            movie.setYear(partialJSON.getString("year"));
            
            try{
                JSONArray directorsArray = partialJSON.getJSONArray("abridged_directors");
                List<String> dirs = new ArrayList<>();
                for(int j=0; j<directorsArray.length(); ){
                    dirs.add(directorsArray.getJSONObject(j).getString("name"));
                    j++;
                }
                movie.setDirectors(dirs);
            }catch(Exception e){}

            movie.setAudience_rating(partialJSON.getJSONObject("ratings").getInt("audience_score"));
            movie.setCritics_rating(partialJSON.getJSONObject("ratings").getInt("critics_score"));
            movie.setPoster(partialJSON.getJSONObject("posters").getString("thumbnail"));
            
            result.addMovie(movie);
        }
        
        return result;
    }
    
    private String createQueryURL(String qFieldValue, int pageLimit){
        movieName = qFieldValue;
        return (queryMovie + opt + movieKey + "&q=" + qFieldValue + 
                "&page_limit=" + pageLimit);
    }
    
    private String adjustURL(String id){
        return (queryMovie + "/" + id + opt + movieKey + "&q=" + movieName);
    }
}
