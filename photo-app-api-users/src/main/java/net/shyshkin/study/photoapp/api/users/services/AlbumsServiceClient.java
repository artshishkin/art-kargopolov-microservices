package net.shyshkin.study.photoapp.api.users.services;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.photoapp.api.users.ui.model.AlbumResponseModel;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "albums-ws", fallbackFactory = AlbumsServiceFallbackFactory.class)
public interface AlbumsServiceClient {

    @GetMapping("/users/{userId}/albums404")
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

@Slf4j
@RequiredArgsConstructor
class DetailedAlbumsServiceFallback implements AlbumsServiceClient {

    private final Throwable cause;

    @Override
    public List<AlbumResponseModel> getUserAlbums(UUID userId) {
        log.error("404 error took place when getAlbums was called with userId `{}`. Error message: {}", userId, cause.getLocalizedMessage());
        return Collections.emptyList();
    }
}

@Slf4j
@Component
@RequiredArgsConstructor
class AlbumsServiceFallbackFactory implements FallbackFactory<AlbumsServiceClient> {

    private final AlbumsServiceFallback albumsServiceFallback;

    @Override
    public AlbumsServiceClient create(Throwable cause) {
        if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
            return new DetailedAlbumsServiceFallback(cause);
        } else {
            log.error("Other Error took place: `{}`", cause.getLocalizedMessage());
        }
        return albumsServiceFallback;
    }
}