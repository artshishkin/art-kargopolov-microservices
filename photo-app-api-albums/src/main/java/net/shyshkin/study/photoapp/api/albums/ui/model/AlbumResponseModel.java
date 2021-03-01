/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.shyshkin.study.photoapp.api.albums.ui.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlbumResponseModel {
    private String albumId;
    private String userId; 
    private String name;
    private String description;
}
