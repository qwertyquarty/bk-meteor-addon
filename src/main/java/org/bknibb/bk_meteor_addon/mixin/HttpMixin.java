package org.bknibb.bk_meteor_addon.mixin;

import meteordevelopment.meteorclient.utils.network.Http;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.http.HttpClient;

@Mixin(Http.class)
public class HttpMixin {
    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Ljava/net/http/HttpClient;newBuilder()Ljava/net/http/HttpClient$Builder;"))
    private static HttpClient.Builder newHttpClient() {
        return java.net.http.HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL);
    }
}
