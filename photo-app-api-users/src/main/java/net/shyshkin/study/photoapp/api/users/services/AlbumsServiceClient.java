package net.shyshkin.study.photoapp.api.users.services;

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

    @GetMapping("/users/{userId}/albums404")
    List<AlbumResponseModel> getUserAlbums(@PathVariable UUID userId);

}

@Component
class AlbumsServiceFallback implements AlbumsServiceClient {
    @Override
    public List<AlbumResponseModel> getUserAlbums(UUID userId) {
        return Collections.emptyList();
    }
}
