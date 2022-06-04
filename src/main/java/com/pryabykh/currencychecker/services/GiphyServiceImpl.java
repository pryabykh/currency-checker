package com.pryabykh.currencychecker.services;

import com.pryabykh.currencychecker.dto.GifObjectDto;
import com.pryabykh.currencychecker.dto.GifResponseDto;
import com.pryabykh.currencychecker.exceptions.GifDataIsNullException;
import com.pryabykh.currencychecker.exceptions.RandomGifIdIsNullException;
import com.pryabykh.currencychecker.feign.GiphyApiClient;
import com.pryabykh.currencychecker.feign.GiphyDownloadClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GiphyServiceImpl implements GiphyService {
    private final GiphyApiClient giphyApiClient;
    private final GiphyDownloadClient giphyDownloadClient;
    @Value("${giphy.tag.rich}")
    private String richTag;
    @Value("${giphy.tag.broke}")
    private String brokeTag;
    @Value("${giphy.api.key}")
    private String apiKey;

    public GiphyServiceImpl(GiphyApiClient giphyApiClient, GiphyDownloadClient giphyDownloadClient) {
        this.giphyApiClient = giphyApiClient;
        this.giphyDownloadClient = giphyDownloadClient;
    }

    @Override
    public byte[] fetchRichGif() throws RandomGifIdIsNullException, GifDataIsNullException {
        String richGifId = fetchRandomGifId(richTag);
        return giphyDownloadClient.downloadGif(richGifId);
    }

    @Override
    public byte[] fetchBrokeGif() throws RandomGifIdIsNullException, GifDataIsNullException {
        String richGifId = fetchRandomGifId(brokeTag);
        return giphyDownloadClient.downloadGif(richGifId);
    }

    private String fetchRandomGifId(String tag) throws GifDataIsNullException, RandomGifIdIsNullException {
        GifResponseDto gifResponseDto = giphyApiClient.fetchRandom(apiKey, tag);
        GifObjectDto data = gifResponseDto.getData();
        if (data == null) {
            throw new GifDataIsNullException();
        }
        String randomGifId = data.getId();
        if (randomGifId == null) {
            throw new RandomGifIdIsNullException();
        }
        return randomGifId;
    }
}
