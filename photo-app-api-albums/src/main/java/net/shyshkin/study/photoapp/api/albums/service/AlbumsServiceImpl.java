/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.shyshkin.study.photoapp.api.albums.service;

import net.shyshkin.study.photoapp.api.albums.data.AlbumEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static java.lang.String.format;

@Service
public class AlbumsServiceImpl implements AlbumsService {

    private List<AlbumEntity> entityRepo;

    @PostConstruct
    void initData() {
        entityRepo = LongStream.rangeClosed(1, 3)
                .mapToObj(this::createFakeEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlbumEntity> getAlbums(String userId) {
        return entityRepo.stream()
                .peek(albumEntity -> albumEntity.setUserId(userId))
                .collect(Collectors.toList());
    }

    private AlbumEntity createFakeEntity(long id) {
        return AlbumEntity.builder()
                .id(id)
                .albumId(format("album%02dId", id))
                .description(format("album %d description", id))
                .name(format("album %d name", id))
                .build();
    }
}
