/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.brusamentoceruti.moviebookrest.boundary;



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
public class BookRequest {
    
    private static final String queryBook = "https://www.googleapis.com/books/v1/volumes?q=";
    
    /**
     * Given a title, searches for volumes that contain this text string using the Google Book APIs.
     * This method retrieves only the links to these books and creates a List to be returned.
     * @param name the string to search.
     * @return a list of String that are links.
     */
    public List<String> getBooks(String name){
        List<String> books = new ArrayList<>();
        try {
            JSONObject googleJson = JsonRequest.doQuery(queryBook + name);
            int size = googleJson.getInt("totalItems");
            JSONArray items = googleJson.getJSONArray("items");
            for(int i=0; i<size; i++){
                String selfLink = items.getJSONObject(i).getString("selfLink");
                JSONObject item = JsonRequest.doQuery(selfLink);
                books.add(item.getJSONObject("volumeInfo").getString("infoLink"));
            }
        } catch (JSONException ex) {
            return books;
        } catch (IOException ex) {
            Logger.getLogger(BookRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return books;
    }    
    
}
