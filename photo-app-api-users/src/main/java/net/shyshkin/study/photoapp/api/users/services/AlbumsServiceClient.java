package net.shyshkin.study.photoapp.api.users.services;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.photoapp.api.users.ui.model.AlbumResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "albums-ws", fallback = AlbumsServiceFallback.class)
public interface AlbumsServiceClient {

    @GetMapping("/users/{userId}/albums")
    List<AlbumResponseModel> getUserAlbums(@PathVariable UUID userId);

}

@Slf4j
@Component
class AlbumsServiceFallback implements AlbumsServiceClient {
    @Override
    public List<AlbumResponseModel> getUserAlbums(UUID userId) {
        log.debug("AlbumsFallback service called for user with id `{}`", userId);
        return Collections.emptyList();
    }
}
