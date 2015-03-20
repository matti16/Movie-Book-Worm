/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.polimi.brusamentoceruti.moviebookrest.gui;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Mattia
 */
@Named
@SessionScoped
@ManagedBean
public class SearchBean implements Serializable{
    
    private String title;
    
    private int limit = -1;
    
    private static final String baseURL = "http://localhost:8080/MovieBookREST/webresources/movie?q=";

    public SearchBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    
    public void search(){
        try {
            String queryURL = (this.limit <= 0) ? (baseURL+this.title) : baseURL+this.title + "&limit=" + this.limit;
            FacesContext.getCurrentInstance().getExternalContext().redirect(queryURL);
        } catch (IOException ex) {
            Logger.getLogger(SearchBean.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    
    
}
