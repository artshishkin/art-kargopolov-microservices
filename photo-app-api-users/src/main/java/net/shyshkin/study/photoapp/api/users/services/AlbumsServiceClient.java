package net.shyshkin.study.photoapp.api.users.services;

import net.shyshkin.study.photoapp.api.users.ui.model.AlbumResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "albums-ws")
public interface AlbumsServiceClient {

    @GetMapping("/users/{userId}/albums")
    List<AlbumResponseModel> getUserAlbums(@PathVariable UUID userId);

}
