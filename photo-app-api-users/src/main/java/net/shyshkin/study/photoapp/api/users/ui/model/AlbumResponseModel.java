package net.shyshkin.study.photoapp.api.users.ui.model;

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
