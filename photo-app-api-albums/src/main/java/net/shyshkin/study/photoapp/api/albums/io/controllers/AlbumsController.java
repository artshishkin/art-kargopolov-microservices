/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.shyshkin.study.photoapp.api.albums.io.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.photoapp.api.albums.data.AlbumEntity;
import net.shyshkin.study.photoapp.api.albums.service.AlbumsService;
import net.shyshkin.study.photoapp.api.albums.ui.model.AlbumResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping("/users/{id}/albums")
@Slf4j
@RequiredArgsConstructor
public class AlbumsController {

    private final AlbumsService albumsService;

    private static final Type LIST_TYPE = new TypeToken<List<AlbumResponseModel>>() {
    }.getType();

    @GetMapping(produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE,})
    public ResponseEntity<List<AlbumResponseModel>> userAlbums(@PathVariable String id) {

        List<AlbumEntity> albumsEntities = albumsService.getAlbums(id);

        if (albumsEntities == null || albumsEntities.isEmpty())
            return ResponseEntity.noContent().build();

        List<AlbumResponseModel> returnValue = new ModelMapper().map(albumsEntities, LIST_TYPE);
        log.debug("Returning {} albums", returnValue.size());
        return ResponseEntity.ok(returnValue);
    }
}
